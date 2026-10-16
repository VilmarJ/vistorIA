package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.model.ItemVistoria;
import br.com.vistoria.util.Conexao;

public class ItemVistoriaDAO {

    public void inserirItens(Connection conn, int vistoriaId, List<ItemVistoria> itens) throws SQLException {
        String sql = "INSERT INTO item_vistoria (vistoria_id, categoria, item, status, gravidade, observacao) "
                + "VALUES (?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (ItemVistoria item : itens) {
                stmt.setInt(1, vistoriaId);
                stmt.setString(2, item.getCategoria());
                stmt.setString(3, item.getItem());
                stmt.setString(4, item.getStatus());
                stmt.setString(5, item.getGravidade());
                stmt.setString(6, item.getObservacao());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public List<ItemVistoria> listarPorVistoria(int vistoriaId) throws SQLException {
        String sql = "SELECT id, vistoria_id, categoria, item, status, gravidade, observacao "
                + "FROM item_vistoria WHERE vistoria_id = ? ORDER BY id";
        List<ItemVistoria> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vistoriaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public List<ItemVistoria> listarNaoConformidades(int usuarioId) throws SQLException {
        String sql = "SELECT iv.id, iv.vistoria_id, iv.categoria, iv.item, iv.status, iv.gravidade, iv.observacao, "
                + "e.nome AS edificacao_nome, v.data_vistoria "
                + "FROM item_vistoria iv "
                + "JOIN vistoria v ON v.id = iv.vistoria_id "
                + "JOIN edificacao e ON e.id = v.edificacao_id "
                + "WHERE iv.status = 'NAO_CONFORME' AND v.usuario_id = ? "
                + "ORDER BY v.data_vistoria DESC";
        List<ItemVistoria> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemVistoria item = mapear(rs);
                    item.setEdificacaoNome(rs.getString("edificacao_nome"));
                    item.setDataVistoria(rs.getObject("data_vistoria", java.time.LocalDate.class));
                    lista.add(item);
                }
            }
        }
        return lista;
    }

    public void atualizarGravidadeStatus(int id, String gravidade, String status) throws SQLException {
        String sql = "UPDATE item_vistoria SET gravidade = ?, status = ? WHERE id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, gravidade);
            stmt.setString(2, status);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    public boolean pertence(int id, int usuarioId) throws SQLException {
        return Conexao.existe("SELECT 1 FROM item_vistoria iv JOIN vistoria v ON v.id = iv.vistoria_id "
                + "WHERE iv.id = ? AND v.usuario_id = ?", id, usuarioId);
    }

    public int contarNaoConformidades(int usuarioId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM item_vistoria iv JOIN vistoria v ON v.id = iv.vistoria_id "
                + "WHERE iv.status = 'NAO_CONFORME' AND v.usuario_id = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private ItemVistoria mapear(ResultSet rs) throws SQLException {
        ItemVistoria item = new ItemVistoria();
        item.setId(rs.getInt("id"));
        item.setVistoriaId(rs.getInt("vistoria_id"));
        item.setCategoria(rs.getString("categoria"));
        item.setItem(rs.getString("item"));
        item.setStatus(rs.getString("status"));
        item.setGravidade(rs.getString("gravidade"));
        item.setObservacao(rs.getString("observacao"));
        return item;
    }
}
