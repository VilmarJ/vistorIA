package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.model.Foto;
import br.com.vistoria.util.Conexao;

public class FotoDAO {

    public void cadastrar(Foto f) throws SQLException {
        String sql = "INSERT INTO foto (vistoria_id, arquivo, descricao, localizacao) VALUES (?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, f.getVistoriaId());
            stmt.setString(2, f.getArquivo());
            stmt.setString(3, f.getDescricao());
            stmt.setString(4, f.getLocalizacao());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM foto WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean pertence(int id, int usuarioId) throws SQLException {
        return Conexao.existe("SELECT 1 FROM foto f JOIN vistoria v ON v.id = f.vistoria_id "
                + "WHERE f.id = ? AND v.usuario_id = ?", id, usuarioId);
    }

    public List<Foto> listarPorVistoria(int vistoriaId) throws SQLException {
        String sql = "SELECT id, vistoria_id, arquivo, descricao, localizacao FROM foto WHERE vistoria_id = ?";
        List<Foto> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vistoriaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Foto f = new Foto();
                    f.setId(rs.getInt("id"));
                    f.setVistoriaId(rs.getInt("vistoria_id"));
                    f.setArquivo(rs.getString("arquivo"));
                    f.setDescricao(rs.getString("descricao"));
                    f.setLocalizacao(rs.getString("localizacao"));
                    lista.add(f);
                }
            }
        }
        return lista;
    }
}
