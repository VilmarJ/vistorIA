package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.vistoria.model.Usuario;
import br.com.vistoria.util.Conexao;

public class UsuarioDAO {

    public void cadastrar(Usuario u) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha_hash, telefone, identificacao) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getSenhaHash());
            stmt.setString(4, u.getTelefone());
            stmt.setString(5, u.getIdentificacao());
            stmt.executeUpdate();
        }
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, email, senha_hash, telefone, identificacao FROM usuario WHERE email = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public boolean emailJaExiste(String email) throws SQLException {
        return buscarPorEmail(email) != null;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNome(rs.getString("nome"));
        u.setEmail(rs.getString("email"));
        u.setSenhaHash(rs.getString("senha_hash"));
        u.setTelefone(rs.getString("telefone"));
        u.setIdentificacao(rs.getString("identificacao"));
        return u;
    }
}
