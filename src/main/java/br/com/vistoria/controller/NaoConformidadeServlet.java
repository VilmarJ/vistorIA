package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.ItemVistoriaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/naoconformidades")
public class NaoConformidadeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ItemVistoriaDAO itemVistoriaDAO = new ItemVistoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "naoconformidades");
        int usuarioId = (int) request.getSession().getAttribute("usuarioId");

        try {
            request.setAttribute("naoConformidades", itemVistoriaDAO.listarNaoConformidades(usuarioId));
            request.getRequestDispatcher("/WEB-INF/views/naoconformidades/listar.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível carregar as não conformidades.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = parseInt(request.getParameter("id"));
        String gravidade = request.getParameter("gravidade");
        String status = request.getParameter("status");
        int usuarioId = (int) request.getSession().getAttribute("usuarioId");

        try {
            if (!itemVistoriaDAO.pertence(id, usuarioId)) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            itemVistoriaDAO.atualizarGravidadeStatus(id, gravidade, status);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível atualizar o item.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/naoconformidades");
    }

    private int parseInt(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
