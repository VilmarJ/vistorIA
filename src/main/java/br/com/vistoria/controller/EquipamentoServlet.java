package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.EquipamentoDAO;
import br.com.vistoria.dao.PlantaDAO;
import br.com.vistoria.model.Equipamento;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/equipamentos")
public class EquipamentoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
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
                    && (id == 0 || equipamentoDAO.pertence(id, usuarioId));
            if (!autorizado) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if ("excluir".equals(acao)) {
                equipamentoDAO.excluir(id);
            } else {
                tratarSalvar(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível salvar o equipamento.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/plantas?acao=visualizar&id=" + plantaId);
    }

    private void tratarSalvar(HttpServletRequest request) throws SQLException {
        int id = parseInt(request.getParameter("id"));

        Equipamento eq = new Equipamento();
        eq.setId(id);
        eq.setPlantaId(parseInt(request.getParameter("plantaId")));
        eq.setTipo(valor(request.getParameter("tipo")));
        eq.setPosX(parseDouble(request.getParameter("posX")));
        eq.setPosY(parseDouble(request.getParameter("posY")));
        eq.setIdentificacao(valor(request.getParameter("identificacao")));
        eq.setCampo1Label(valor(request.getParameter("campo1Label")));
        eq.setCampo1Valor(valor(request.getParameter("campo1Valor")));
        eq.setCampo2Label(valor(request.getParameter("campo2Label")));
        eq.setCampo2Valor(valor(request.getParameter("campo2Valor")));
        eq.setLocalizacao(valor(request.getParameter("localizacao")));
        eq.setStatus(valor(request.getParameter("status")));
        eq.setObservacoes(valor(request.getParameter("observacoes")));

        if (id > 0) {
            equipamentoDAO.alterar(eq);
        } else {
            equipamentoDAO.cadastrar(eq);
        }
    }

    private int parseInt(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private double parseDouble(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Double.parseDouble(v.trim().replace(",", ".")); }
        catch (NumberFormatException e) { return 0; }
    }

    private String valor(String v) {
        return (v == null) ? "" : v.trim();
    }
}
