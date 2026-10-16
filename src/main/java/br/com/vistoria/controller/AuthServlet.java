package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.UsuarioDAO;
import br.com.vistoria.model.Usuario;
import br.com.vistoria.util.SenhaUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        if (acao == null) acao = "login";

        switch (acao) {
            case "cadastro":
                request.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(request, response);
                break;
            case "logout":
                HttpSession session = request.getSession(false);
                if (session != null) session.invalidate();
                response.sendRedirect(request.getContextPath() + "/auth?acao=login");
                break;
            case "login":
            default:
                request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");

        try {
            if ("autenticar".equals(acao)) {
                tratarLogin(request, response);
            } else if ("cadastrar".equals(acao)) {
                tratarCadastro(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/auth?acao=login");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível comunicar com o banco de dados.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
        }
    }

    private void tratarLogin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String email = valor(request.getParameter("email"));
        String senha = valor(request.getParameter("senha"));

        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        boolean valido = usuario != null && usuario.getSenhaHash().equals(SenhaUtil.hash(senha));

        if (!valido) {
            request.setAttribute("erroLogin", "E-mail ou senha inválidos. Verifique e tente novamente.");
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNome", usuario.getNome());
        session.setAttribute("usuarioIniciais", usuario.getIniciais());

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void tratarCadastro(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String nome = valor(request.getParameter("nome"));
        String identificacao = valor(request.getParameter("identificacao"));
        String email = valor(request.getParameter("email"));
        String telefone = valor(request.getParameter("telefone"));
        String senha = valor(request.getParameter("senha"));
        String confirmarSenha = valor(request.getParameter("confirmarSenha"));

        String erro = validarCadastro(nome, email, senha, confirmarSenha);
        if (erro == null && usuarioDAO.emailJaExiste(email)) {
            erro = "Já existe uma conta cadastrada com este e-mail.";
        }

        if (erro != null) {
            request.setAttribute("erroCadastro", erro);
            request.getRequestDispatcher("/WEB-INF/views/auth/cadastro.jsp").forward(request, response);
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setIdentificacao(identificacao);
        usuario.setEmail(email);
        usuario.setTelefone(telefone);
        usuario.setSenhaHash(SenhaUtil.hash(senha));
        usuarioDAO.cadastrar(usuario);

        response.sendRedirect(request.getContextPath() + "/auth?acao=login");
    }

    private String validarCadastro(String nome, String email, String senha, String confirmarSenha) {
        if (nome.isBlank()) return "O nome completo é obrigatório.";
        if (email.isBlank()) return "O e-mail é obrigatório.";
        if (senha.length() < 6) return "A senha deve ter ao menos 6 caracteres.";
        if (!senha.equals(confirmarSenha)) return "As senhas não coincidem.";
        return null;
    }

    private String valor(String texto) {
        return (texto == null) ? "" : texto.trim();
    }
}
