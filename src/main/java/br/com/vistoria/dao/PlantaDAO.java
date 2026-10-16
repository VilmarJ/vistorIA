package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.model.Planta;
import br.com.vistoria.util.Conexao;

public class PlantaDAO {

    public int cadastrar(Planta p) throws SQLException {
        String sql = "INSERT INTO planta (edificacao_id, nome, pavimento, arquivo) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, p.getEdificacaoId());
            stmt.setString(2, p.getNome());
            stmt.setString(3, p.getPavimento());
            stmt.setString(4, p.getArquivo());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM planta WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Planta> listarPorEdificacao(int edificacaoId) throws SQLException {
        String sql = "SELECT id, edificacao_id, nome, pavimento, arquivo, data_upload "
                + "FROM planta WHERE edificacao_id = ? ORDER BY id";
        List<Planta> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, edificacaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<Planta> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT p.id, p.edificacao_id, p.nome, p.pavimento, p.arquivo, p.data_upload, "
                + "e.nome AS edificacao_nome "
                + "FROM planta p JOIN edificacao e ON e.id = p.edificacao_id "
                + "WHERE e.usuario_id = ? ORDER BY e.nome, p.id";
        List<Planta> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Planta planta = mapear(rs);
                    planta.setEdificacaoNome(rs.getString("edificacao_nome"));
                    lista.add(planta);
                }
            }
        }
        return lista;
    }

    public Planta buscarPorId(int id) throws SQLException {
        String sql = "SELECT p.id, p.edificacao_id, p.nome, p.pavimento, p.arquivo, p.data_upload, "
                + "e.nome AS edificacao_nome "
                + "FROM planta p JOIN edificacao e ON e.id = p.edificacao_id WHERE p.id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Planta planta = mapear(rs);
                    planta.setEdificacaoNome(rs.getString("edificacao_nome"));
                    return planta;
                }
            }
        }
        return null;
    }

    public boolean pertence(int id, int usuarioId) throws SQLException {
        return Conexao.existe("SELECT 1 FROM planta p JOIN edificacao e ON e.id = p.edificacao_id "
                + "WHERE p.id = ? AND e.usuario_id = ?", id, usuarioId);
    }

    private Planta mapear(ResultSet rs) throws SQLException {
        Planta p = new Planta();
        p.setId(rs.getInt("id"));
        p.setEdificacaoId(rs.getInt("edificacao_id"));
        p.setNome(rs.getString("nome"));
        p.setPavimento(rs.getString("pavimento"));
        p.setArquivo(rs.getString("arquivo"));
        java.sql.Timestamp ts = rs.getTimestamp("data_upload");
        if (ts != null) p.setDataUpload(ts.toLocalDateTime());
        return p;
    }
}
