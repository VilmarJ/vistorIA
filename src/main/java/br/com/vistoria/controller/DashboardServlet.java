package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.EdificacaoDAO;
import br.com.vistoria.dao.ItemVistoriaDAO;
import br.com.vistoria.dao.VistoriaDAO;
import br.com.vistoria.model.Vistoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EdificacaoDAO edificacaoDAO = new EdificacaoDAO();
    private final VistoriaDAO vistoriaDAO = new VistoriaDAO();
    private final ItemVistoriaDAO itemVistoriaDAO = new ItemVistoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int usuarioId = (int) request.getSession().getAttribute("usuarioId");
        request.setAttribute("paginaAtiva", "dashboard");

        try {
            int totalEdificacoes = edificacaoDAO.contarPorUsuario(usuarioId);
            int totalVistorias = vistoriaDAO.contarPorUsuario(usuarioId);
            int vistoriasPendentes = vistoriaDAO.contarPorStatus(usuarioId, Vistoria.STATUS_PENDENTE)
                    + vistoriaDAO.contarPorStatus(usuarioId, Vistoria.STATUS_ANDAMENTO);
            int naoConformidades = itemVistoriaDAO.contarNaoConformidades(usuarioId);

            request.setAttribute("totalEdificacoes", totalEdificacoes);
            request.setAttribute("totalVistorias", totalVistorias);
            request.setAttribute("vistoriasPendentes", vistoriasPendentes);
            request.setAttribute("naoConformidades", naoConformidades);
            request.setAttribute("vistoriasRecentes", vistoriaDAO.listarRecentes(usuarioId, 6));

            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível carregar o dashboard.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
        }
    }
}
