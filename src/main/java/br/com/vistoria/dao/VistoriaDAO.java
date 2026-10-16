package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.model.Vistoria;
import br.com.vistoria.util.Conexao;

public class VistoriaDAO {

    private final ItemVistoriaDAO itemDAO = new ItemVistoriaDAO();

    public void cadastrar(Vistoria v) throws SQLException {
        String sql = "INSERT INTO vistoria (edificacao_id, usuario_id, planta_id, data_vistoria, tipo, status, observacoes) "
                + "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                int vistoriaId;
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, v.getEdificacaoId());
                    stmt.setInt(2, v.getUsuarioId());
                    if (v.getPlantaId() != null && v.getPlantaId() > 0) {
                        stmt.setInt(3, v.getPlantaId());
                    } else {
                        stmt.setNull(3, java.sql.Types.INTEGER);
                    }
                    stmt.setObject(4, v.getDataVistoria());
                    stmt.setString(5, v.getTipo());
                    stmt.setString(6, v.getStatus());
                    stmt.setString(7, v.getObservacoes());
                    stmt.executeUpdate();
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        rs.next();
                        vistoriaId = rs.getInt(1);
                    }
                }
                itemDAO.inserirItens(conn, vistoriaId, v.getItens());
                conn.commit();
                v.setId(vistoriaId);
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM item_vistoria WHERE vistoria_id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM foto WHERE vistoria_id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM vistoria WHERE id = ?")) {
                    stmt.setInt(1, id);
                    stmt.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Vistoria> listarPorUsuario(int usuarioId) throws SQLException {
        return listar("WHERE v.usuario_id = ?", usuarioId);
    }

    public List<Vistoria> listarPendentesPorUsuario(int usuarioId) throws SQLException {
        return listar("WHERE v.usuario_id = ? AND v.status IN ('PENDENTE','ANDAMENTO')", usuarioId);
    }

    public List<Vistoria> listarRecentes(int usuarioId, int limite) throws SQLException {
        String sql = base() + "WHERE v.usuario_id = ? ORDER BY v.data_vistoria DESC LIMIT " + limite;
        return executarLista(sql, usuarioId);
    }

    public List<Vistoria> listarPorEdificacao(int edificacaoId) throws SQLException {
        String sql = base() + "WHERE v.edificacao_id = ? ORDER BY v.data_vistoria DESC";
        return executarLista(sql, edificacaoId);
    }

    public Vistoria buscarPorId(int id) throws SQLException {
        String sql = base() + "WHERE v.id = ?";
        List<Vistoria> lista = executarLista(sql, id);
        if (lista.isEmpty()) return null;
        Vistoria vistoria = lista.get(0);
        vistoria.setItens(itemDAO.listarPorVistoria(id));
        return vistoria;
    }

    public boolean pertence(int id, int usuarioId) throws SQLException {
        return Conexao.existe("SELECT 1 FROM vistoria WHERE id = ? AND usuario_id = ?", id, usuarioId);
    }

    public int contarPorUsuario(int usuarioId) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM vistoria WHERE usuario_id = ?")) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public int contarPorStatus(int usuarioId, String status) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM vistoria WHERE usuario_id = ? AND status = ?")) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, status);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private String base() {
        return "SELECT v.id, v.edificacao_id, e.nome AS edificacao_nome, v.usuario_id, u.nome AS usuario_nome, "
                + "v.planta_id, v.data_vistoria, v.tipo, v.status, v.observacoes "
                + "FROM vistoria v "
                + "JOIN edificacao e ON e.id = v.edificacao_id "
                + "JOIN usuario u ON u.id = v.usuario_id ";
    }

    private List<Vistoria> listar(String whereClause, int param) throws SQLException {
        return executarLista(base() + whereClause + " ORDER BY v.data_vistoria DESC", param);
    }

    private List<Vistoria> executarLista(String sql, int param) throws SQLException {
        List<Vistoria> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, param);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    private Vistoria mapear(ResultSet rs) throws SQLException {
        Vistoria v = new Vistoria();
        v.setId(rs.getInt("id"));
        v.setEdificacaoId(rs.getInt("edificacao_id"));
        v.setEdificacaoNome(rs.getString("edificacao_nome"));
        v.setUsuarioId(rs.getInt("usuario_id"));
        v.setUsuarioNome(rs.getString("usuario_nome"));
        int plantaId = rs.getInt("planta_id");
        v.setPlantaId(rs.wasNull() ? null : plantaId);
        LocalDate data = rs.getObject("data_vistoria", LocalDate.class);
        v.setDataVistoria(data);
        v.setTipo(rs.getString("tipo"));
        v.setStatus(rs.getString("status"));
        v.setObservacoes(rs.getString("observacoes"));
        return v;
    }
}
