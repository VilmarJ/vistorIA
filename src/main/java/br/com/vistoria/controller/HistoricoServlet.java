package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import br.com.vistoria.dao.EdificacaoDAO;
import br.com.vistoria.dao.VistoriaDAO;
import br.com.vistoria.model.Vistoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/historico")
public class HistoricoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final VistoriaDAO vistoriaDAO = new VistoriaDAO();
    private final EdificacaoDAO edificacaoDAO = new EdificacaoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "historico");
        int usuarioId = (int) request.getSession().getAttribute("usuarioId");

        int filtroEdificacaoId = parseInt(request.getParameter("edificacaoId"));
        String filtroStatus = request.getParameter("status");

        try {
            List<Vistoria> vistorias = vistoriaDAO.listarPorUsuario(usuarioId);

            if (filtroEdificacaoId > 0) {
                vistorias = vistorias.stream()
                        .filter(v -> v.getEdificacaoId() == filtroEdificacaoId)
                        .collect(Collectors.toList());
            }
            if (filtroStatus != null && !filtroStatus.isBlank()) {
                vistorias = vistorias.stream()
                        .filter(v -> filtroStatus.equals(v.getStatus()))
                        .collect(Collectors.toList());
            }

            request.setAttribute("vistorias", vistorias);
            request.setAttribute("edificacoes", edificacaoDAO.listarPorUsuario(usuarioId));
            request.setAttribute("filtroEdificacaoId", filtroEdificacaoId);
            request.setAttribute("filtroStatus", filtroStatus);

            request.getRequestDispatcher("/WEB-INF/views/historico/listar.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível carregar o histórico.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
        }
    }

    private int parseInt(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
