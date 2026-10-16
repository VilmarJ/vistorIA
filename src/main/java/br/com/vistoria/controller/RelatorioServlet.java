package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.EdificacaoDAO;
import br.com.vistoria.dao.FotoDAO;
import br.com.vistoria.dao.VistoriaDAO;
import br.com.vistoria.model.Edificacao;
import br.com.vistoria.model.Vistoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/relatorios")
public class RelatorioServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final VistoriaDAO vistoriaDAO = new VistoriaDAO();
    private final EdificacaoDAO edificacaoDAO = new EdificacaoDAO();
    private final FotoDAO fotoDAO = new FotoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int vistoriaId = parseInt(request.getParameter("vistoriaId"));

        int usuarioId = (int) request.getSession().getAttribute("usuarioId");

        try {
            if (!vistoriaDAO.pertence(vistoriaId, usuarioId)) {
                response.sendRedirect(request.getContextPath() + "/vistorias");
                return;
            }
            Vistoria vistoria = vistoriaDAO.buscarPorId(vistoriaId);
            Edificacao edificacao = edificacaoDAO.buscarPorId(vistoria.getEdificacaoId());

            request.setAttribute("vistoria", vistoria);
            request.setAttribute("edificacao", edificacao);
            request.setAttribute("fotos", fotoDAO.listarPorVistoria(vistoriaId));
            request.getRequestDispatcher("/WEB-INF/views/relatorios/relatorio.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível gerar o relatório.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
        }
    }

    private int parseInt(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
