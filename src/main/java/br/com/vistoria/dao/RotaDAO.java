package br.com.vistoria.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.model.Rota;
import br.com.vistoria.util.Conexao;

public class RotaDAO {

    public void cadastrar(Rota r) throws SQLException {
        String sql = "INSERT INTO rota (planta_id, pontos, origem, destino) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getPlantaId());
            stmt.setString(2, r.getPontos());
            stmt.setString(3, r.getOrigem());
            stmt.setString(4, r.getDestino());
            stmt.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM rota WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public boolean pertence(int id, int usuarioId) throws SQLException {
        return Conexao.existe("SELECT 1 FROM rota r JOIN planta p ON p.id = r.planta_id "
                + "JOIN edificacao e ON e.id = p.edificacao_id WHERE r.id = ? AND e.usuario_id = ?", id, usuarioId);
    }

    public List<Rota> listarPorPlanta(int plantaId) throws SQLException {
        String sql = "SELECT id, planta_id, pontos, origem, destino FROM rota WHERE planta_id = ?";
        List<Rota> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, plantaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rota r = new Rota();
                    r.setId(rs.getInt("id"));
                    r.setPlantaId(rs.getInt("planta_id"));
                    r.setPontos(rs.getString("pontos"));
                    r.setOrigem(rs.getString("origem"));
                    r.setDestino(rs.getString("destino"));
                    lista.add(r);
                }
            }
        }
        return lista;
    }
}
