package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.model.Edificacao;
import br.com.vistoria.util.Conexao;

public class EdificacaoDAO {

    private static final String COLUNAS =
            "id, usuario_id, nome, razao_social, cnpj, tipo_ocupacao, endereco, numero, bairro, "
            + "cidade, estado, cep, area_construida, pavimentos, altura, ocupantes, tipo_edificacao, "
            + "uso_edificacao, area_pavimento, qtd_blocos, tem_subsolo, tem_mezanino";

    public void cadastrar(Edificacao e) throws SQLException {
        String sql = "INSERT INTO edificacao (" + COLUNAS.substring("id, ".length()) + ") "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            preencher(stmt, e, 1);
            stmt.executeUpdate();
        }
    }

    public void alterar(Edificacao e) throws SQLException {
        String sql = "UPDATE edificacao SET usuario_id=?, nome=?, razao_social=?, cnpj=?, tipo_ocupacao=?, "
                + "endereco=?, numero=?, bairro=?, cidade=?, estado=?, cep=?, area_construida=?, pavimentos=?, "
                + "altura=?, ocupantes=?, tipo_edificacao=?, uso_edificacao=?, area_pavimento=?, qtd_blocos=?, "
                + "tem_subsolo=?, tem_mezanino=? WHERE id=?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            int i = preencher(stmt, e, 1);
            stmt.setInt(i, e.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM edificacao WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Edificacao> listarPorUsuario(int usuarioId) throws SQLException {
        String sql = "SELECT " + COLUNAS + " FROM edificacao WHERE usuario_id = ? ORDER BY nome";
        List<Edificacao> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Edificacao buscarPorId(int id) throws SQLException {
        String sql = "SELECT " + COLUNAS + " FROM edificacao WHERE id = ?";
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
        return Conexao.existe("SELECT 1 FROM edificacao WHERE id = ? AND usuario_id = ?", id, usuarioId);
    }

    public int contarPorUsuario(int usuarioId) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT COUNT(*) FROM edificacao WHERE usuario_id = ?")) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private int preencher(PreparedStatement stmt, Edificacao e, int i) throws SQLException {
        stmt.setInt(i++, e.getUsuarioId());
        stmt.setString(i++, e.getNome());
        stmt.setString(i++, e.getRazaoSocial());
        stmt.setString(i++, e.getCnpj());
        stmt.setString(i++, e.getTipoOcupacao());
        stmt.setString(i++, e.getEndereco());
        stmt.setString(i++, e.getNumero());
        stmt.setString(i++, e.getBairro());
        stmt.setString(i++, e.getCidade());
        stmt.setString(i++, e.getEstado());
        stmt.setString(i++, e.getCep());
        stmt.setDouble(i++, e.getAreaConstruida());
        stmt.setInt(i++, e.getPavimentos());
        stmt.setDouble(i++, e.getAltura());
        stmt.setInt(i++, e.getOcupantes());
        stmt.setString(i++, e.getTipoEdificacao());
        stmt.setString(i++, e.getUsoEdificacao());
        stmt.setDouble(i++, e.getAreaPavimento());
        stmt.setInt(i++, e.getQtdBlocos());
        stmt.setBoolean(i++, e.isTemSubsolo());
        stmt.setBoolean(i++, e.isTemMezanino());
        return i;
    }

    private Edificacao mapear(ResultSet rs) throws SQLException {
        Edificacao e = new Edificacao();
        e.setId(rs.getInt("id"));
        e.setUsuarioId(rs.getInt("usuario_id"));
        e.setNome(rs.getString("nome"));
        e.setRazaoSocial(rs.getString("razao_social"));
        e.setCnpj(rs.getString("cnpj"));
        e.setTipoOcupacao(rs.getString("tipo_ocupacao"));
        e.setEndereco(rs.getString("endereco"));
        e.setNumero(rs.getString("numero"));
        e.setBairro(rs.getString("bairro"));
        e.setCidade(rs.getString("cidade"));
        e.setEstado(rs.getString("estado"));
        e.setCep(rs.getString("cep"));
        e.setAreaConstruida(rs.getDouble("area_construida"));
        e.setPavimentos(rs.getInt("pavimentos"));
        e.setAltura(rs.getDouble("altura"));
        e.setOcupantes(rs.getInt("ocupantes"));
        e.setTipoEdificacao(rs.getString("tipo_edificacao"));
        e.setUsoEdificacao(rs.getString("uso_edificacao"));
        e.setAreaPavimento(rs.getDouble("area_pavimento"));
        e.setQtdBlocos(rs.getInt("qtd_blocos"));
        e.setTemSubsolo(rs.getBoolean("tem_subsolo"));
        e.setTemMezanino(rs.getBoolean("tem_mezanino"));
        return e;
    }
}
