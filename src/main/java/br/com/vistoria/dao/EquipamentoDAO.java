package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.model.Equipamento;
import br.com.vistoria.util.Conexao;

public class EquipamentoDAO {

    private static final String COLUNAS =
            "planta_id, tipo, pos_x, pos_y, identificacao, campo1_label, campo1_valor, "
            + "campo2_label, campo2_valor, localizacao, status, observacoes";

    public void cadastrar(Equipamento eq) throws SQLException {
        String sql = "INSERT INTO equipamento (" + COLUNAS + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preencher(stmt, eq, 1);
            stmt.executeUpdate();
        }
    }

    public void alterar(Equipamento eq) throws SQLException {
        String sql = "UPDATE equipamento SET planta_id=?, tipo=?, pos_x=?, pos_y=?, identificacao=?, "
                + "campo1_label=?, campo1_valor=?, campo2_label=?, campo2_valor=?, localizacao=?, "
                + "status=?, observacoes=? WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = preencher(stmt, eq, 1);
            stmt.setInt(i, eq.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM equipamento WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Equipamento> listarPorPlanta(int plantaId) throws SQLException {
        String sql = "SELECT id, " + COLUNAS + " FROM equipamento WHERE planta_id = ? ORDER BY id";
        List<Equipamento> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, plantaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Equipamento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, " + COLUNAS + " FROM equipamento WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public boolean pertence(int id, int usuarioId) throws SQLException {
        return Conexao.existe("SELECT 1 FROM equipamento q JOIN planta p ON p.id = q.planta_id "
                + "JOIN edificacao e ON e.id = p.edificacao_id WHERE q.id = ? AND e.usuario_id = ?", id, usuarioId);
    }

    private int preencher(PreparedStatement stmt, Equipamento eq, int i) throws SQLException {
        stmt.setInt(i++, eq.getPlantaId());
        stmt.setString(i++, eq.getTipo());
        stmt.setDouble(i++, eq.getPosX());
        stmt.setDouble(i++, eq.getPosY());
        stmt.setString(i++, eq.getIdentificacao());
        stmt.setString(i++, eq.getCampo1Label());
        stmt.setString(i++, eq.getCampo1Valor());
        stmt.setString(i++, eq.getCampo2Label());
        stmt.setString(i++, eq.getCampo2Valor());
        stmt.setString(i++, eq.getLocalizacao());
        stmt.setString(i++, eq.getStatus());
        stmt.setString(i++, eq.getObservacoes());
        return i;
    }

    private Equipamento mapear(ResultSet rs) throws SQLException {
        Equipamento eq = new Equipamento();
        eq.setId(rs.getInt("id"));
        eq.setPlantaId(rs.getInt("planta_id"));
        eq.setTipo(rs.getString("tipo"));
        eq.setPosX(rs.getDouble("pos_x"));
        eq.setPosY(rs.getDouble("pos_y"));
        eq.setIdentificacao(rs.getString("identificacao"));
        eq.setCampo1Label(rs.getString("campo1_label"));
        eq.setCampo1Valor(rs.getString("campo1_valor"));
        eq.setCampo2Label(rs.getString("campo2_label"));
        eq.setCampo2Valor(rs.getString("campo2_valor"));
        eq.setLocalizacao(rs.getString("localizacao"));
        eq.setStatus(rs.getString("status"));
        eq.setObservacoes(rs.getString("observacoes"));
        return eq;
    }
}
