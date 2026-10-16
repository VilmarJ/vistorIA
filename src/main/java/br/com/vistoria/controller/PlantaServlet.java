package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.EdificacaoDAO;
import br.com.vistoria.dao.EquipamentoDAO;
import br.com.vistoria.dao.PlantaDAO;
import br.com.vistoria.dao.RotaDAO;
import br.com.vistoria.model.Equipamento;
import br.com.vistoria.model.Planta;
import br.com.vistoria.util.UploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/plantas")
@MultipartConfig(maxFileSize = 20 * 1024 * 1024)
public class PlantaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EdificacaoDAO edificacaoDAO = new EdificacaoDAO();
    private final PlantaDAO plantaDAO = new PlantaDAO();
    private final EquipamentoDAO equipamentoDAO = new EquipamentoDAO();
    private final RotaDAO rotaDAO = new RotaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "plantas");
        String acao = request.getParameter("acao");
        if (acao == null) acao = (request.getParameter("id") != null) ? "visualizar" : "listar";

        try {
            switch (acao) {
                case "listar":
                    listarTodas(request, response);
                    break;
                case "excluir":
                    tratarExcluir(request, response);
                    break;
                case "visualizar":
                default:
                    tratarVisualizar(request, response);
                    break;
            }
        } catch (SQLException e) {
            tratarErroBanco(request, response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            tratarUpload(request, response);
        } catch (SQLException e) {
            tratarErroBanco(request, response, e);
        }
    }

    private void listarTodas(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        request.setAttribute("plantas", plantaDAO.listarPorUsuario(usuarioLogado(request)));
        request.getRequestDispatcher("/WEB-INF/views/plantas/listar.jsp").forward(request, response);
    }

    private void tratarVisualizar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseInt(request.getParameter("id"));
        if (!plantaDAO.pertence(id, usuarioLogado(request))) {
            response.sendRedirect(request.getContextPath() + "/edificacoes");
            return;
        }
        Planta planta = plantaDAO.buscarPorId(id);

        request.setAttribute("planta", planta);
        request.setAttribute("equipamentos", equipamentoDAO.listarPorPlanta(id));
        request.setAttribute("rotas", rotaDAO.listarPorPlanta(id));

        int verId = parseInt(request.getParameter("verId"));
        if (verId > 0) {
            Equipamento selecionado = equipamentoDAO.buscarPorId(verId);
            if (selecionado != null && selecionado.getPlantaId() == id) {
                request.setAttribute("equipamentoSelecionado", selecionado);
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/plantas/visualizar.jsp").forward(request, response);
    }

    private void tratarUpload(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int edificacaoId = parseInt(request.getParameter("edificacaoId"));
        if (!edificacaoDAO.pertence(edificacaoId, usuarioLogado(request))) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String nome = valor(request.getParameter("nome"));
        String pavimento = valor(request.getParameter("pavimento"));
        Part arquivoPart = request.getPart("arquivo");

        String uploadDir = getServletContext().getRealPath("/uploads");
        String nomeArquivoSalvo = UploadUtil.salvar(arquivoPart, uploadDir);

        if (nome.isBlank() || pavimento.isBlank() || nomeArquivoSalvo == null) {
            request.setAttribute("mensagemErro", "Informe nome, pavimento e selecione um arquivo.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
            return;
        }

        Planta planta = new Planta();
        planta.setEdificacaoId(edificacaoId);
        planta.setNome(nome);
        planta.setPavimento(pavimento);
        planta.setArquivo(nomeArquivoSalvo);
        plantaDAO.cadastrar(planta);

        response.sendRedirect(request.getContextPath() + "/edificacoes?acao=detalhe&id=" + edificacaoId);
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseInt(request.getParameter("id"));
        if (!plantaDAO.pertence(id, usuarioLogado(request))) {
            response.sendRedirect(request.getContextPath() + "/edificacoes");
            return;
        }
        int edificacaoId = plantaDAO.buscarPorId(id).getEdificacaoId();
        plantaDAO.excluir(id);
        response.sendRedirect(request.getContextPath() + "/edificacoes?acao=detalhe&id=" + edificacaoId);
    }

    private int usuarioLogado(HttpServletRequest request) {
        return (int) request.getSession().getAttribute("usuarioId");
    }

    private int parseInt(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private String valor(String v) {
        return (v == null) ? "" : v.trim();
    }

    private void tratarErroBanco(HttpServletRequest request, HttpServletResponse response, SQLException e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("mensagemErro", "Não foi possível comunicar com o banco de dados.");
        request.setAttribute("detalheErro", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
    }
}
