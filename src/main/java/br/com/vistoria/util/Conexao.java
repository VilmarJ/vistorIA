package br.com.vistoria.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/vistoria_ia"
            + "?useTimezone=true&serverTimezone=America/Sao_Paulo"
            + "&useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USUARIO = "root";
    private static final String SENHA = "root";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "Driver do MySQL não encontrado no classpath. Verifique o pom.xml.");
        }
    }

    private Conexao() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static boolean existe(String sql, int... parametros) throws SQLException {
        try (Connection conn = conectar();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                stmt.setInt(i + 1, parametros[i]);
            }
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
