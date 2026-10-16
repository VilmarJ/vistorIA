package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.PlantaDAO;
import br.com.vistoria.dao.RotaDAO;
import br.com.vistoria.model.Rota;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/rotas")
public class RotaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final RotaDAO rotaDAO = new RotaDAO();
    private final PlantaDAO plantaDAO = new PlantaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        int plantaId = parseInt(request.getParameter("plantaId"));
        int id = parseInt(request.getParameter("id"));
        int usuarioId = (int) request.getSession().getAttribute("usuarioId");

        try {
            boolean autorizado = plantaDAO.pertence(plantaId, usuarioId)
                    && (!"excluir".equals(acao) || rotaDAO.pertence(id, usuarioId));
            if (!autorizado) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if ("excluir".equals(acao)) {
                rotaDAO.excluir(id);
            } else {
                Rota rota = new Rota();
                rota.setPlantaId(plantaId);
                rota.setPontos(valor(request.getParameter("pontos")));
                rota.setOrigem(valor(request.getParameter("origem")));
                rota.setDestino(valor(request.getParameter("destino")));

                if (rota.getPontos().isBlank()) {
                    response.sendRedirect(request.getContextPath() + "/plantas?acao=visualizar&id=" + plantaId);
                    return;
                }
                rotaDAO.cadastrar(rota);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível salvar a rota de fuga.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/plantas?acao=visualizar&id=" + plantaId);
    }

    private int parseInt(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private String valor(String v) {
        return (v == null) ? "" : v.trim();
    }
}
