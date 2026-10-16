package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import br.com.vistoria.dao.EdificacaoDAO;
import br.com.vistoria.dao.PlantaDAO;
import br.com.vistoria.dao.VistoriaDAO;
import br.com.vistoria.model.Edificacao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/edificacoes")
public class EdificacaoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final EdificacaoDAO edificacaoDAO = new EdificacaoDAO();
    private final PlantaDAO plantaDAO = new PlantaDAO();
    private final VistoriaDAO vistoriaDAO = new VistoriaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "edificacoes");
        String acao = request.getParameter("acao");
        if (acao == null) acao = "listar";

        try {
            switch (acao) {
                case "novo":
                    request.setAttribute("edificacao", new Edificacao());
                    request.getRequestDispatcher("/WEB-INF/views/edificacoes/form.jsp").forward(request, response);
                    break;
                case "editar":
                    tratarEditar(request, response);
                    break;
                case "excluir":
                    tratarExcluir(request, response);
                    break;
                case "detalhe":
                    tratarDetalhe(request, response);
                    break;
                case "listar":
                default:
                    listarTodas(request, response);
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
            tratarSalvar(request, response);
        } catch (SQLException e) {
            tratarErroBanco(request, response, e);
        }
    }

    private void listarTodas(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int usuarioId = (int) request.getSession().getAttribute("usuarioId");
        List<Edificacao> lista = edificacaoDAO.listarPorUsuario(usuarioId);
        request.setAttribute("edificacoes", lista);
        request.getRequestDispatcher("/WEB-INF/views/edificacoes/listar.jsp").forward(request, response);
    }

    private void tratarDetalhe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseInt(request.getParameter("id"));
        if (!edificacaoDAO.pertence(id, usuarioLogado(request))) {
            response.sendRedirect(request.getContextPath() + "/edificacoes");
            return;
        }
        Edificacao edificacao = edificacaoDAO.buscarPorId(id);
        request.setAttribute("edificacao", edificacao);
        request.setAttribute("plantas", plantaDAO.listarPorEdificacao(id));
        request.setAttribute("vistorias", vistoriaDAO.listarPorEdificacao(id));
        request.getRequestDispatcher("/WEB-INF/views/edificacoes/detalhe.jsp").forward(request, response);
    }

    private void tratarEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseInt(request.getParameter("id"));
        if (!edificacaoDAO.pertence(id, usuarioLogado(request))) {
            response.sendRedirect(request.getContextPath() + "/edificacoes");
            return;
        }
        request.setAttribute("edificacao", edificacaoDAO.buscarPorId(id));
        request.getRequestDispatcher("/WEB-INF/views/edificacoes/form.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int usuarioId = usuarioLogado(request);
        int id = parseInt(request.getParameter("id"));

        if (id > 0 && !edificacaoDAO.pertence(id, usuarioId)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Edificacao e = new Edificacao();
        e.setId(id);
        e.setUsuarioId(usuarioId);
        e.setNome(valor(request.getParameter("nome")));
        e.setRazaoSocial(valor(request.getParameter("razaoSocial")));
        e.setCnpj(valor(request.getParameter("cnpj")));
        e.setTipoOcupacao(valor(request.getParameter("tipoOcupacao")));
        e.setEndereco(valor(request.getParameter("endereco")));
        e.setNumero(valor(request.getParameter("numero")));
        e.setBairro(valor(request.getParameter("bairro")));
        e.setCidade(valor(request.getParameter("cidade")));
        e.setEstado(valor(request.getParameter("estado")));
        e.setCep(valor(request.getParameter("cep")));
        e.setAreaConstruida(parseDouble(request.getParameter("areaConstruida")));
        e.setPavimentos(parseInt(request.getParameter("pavimentos")));
        e.setAltura(parseDouble(request.getParameter("altura")));
        e.setOcupantes(parseInt(request.getParameter("ocupantes")));
        e.setTipoEdificacao(valor(request.getParameter("tipoEdificacao")));
        e.setUsoEdificacao(valor(request.getParameter("usoEdificacao")));
        e.setAreaPavimento(parseDouble(request.getParameter("areaPavimento")));
        e.setQtdBlocos(Math.max(1, parseInt(request.getParameter("qtdBlocos"))));
        e.setTemSubsolo(request.getParameter("temSubsolo") != null);
        e.setTemMezanino(request.getParameter("temMezanino") != null);

        if (e.getNome().isBlank()) {
            request.setAttribute("mensagemErro", "O nome da edificação é obrigatório.");
            request.setAttribute("edificacao", e);
            request.getRequestDispatcher("/WEB-INF/views/edificacoes/form.jsp").forward(request, response);
            return;
        }

        if (id > 0) {
            edificacaoDAO.alterar(e);
        } else {
            edificacaoDAO.cadastrar(e);
        }

        response.sendRedirect(request.getContextPath() + "/edificacoes");
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        int id = parseInt(request.getParameter("id"));
        if (edificacaoDAO.pertence(id, usuarioLogado(request))) {
            edificacaoDAO.excluir(id);
        }
        response.sendRedirect(request.getContextPath() + "/edificacoes");
    }

    private int usuarioLogado(HttpServletRequest request) {
        return (int) request.getSession().getAttribute("usuarioId");
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

    private void tratarErroBanco(HttpServletRequest request, HttpServletResponse response, SQLException e)
            throws ServletException, IOException {
        e.printStackTrace();
        request.setAttribute("mensagemErro", "Não foi possível comunicar com o banco de dados.");
        request.setAttribute("detalheErro", e.getMessage());
        request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
    }
}
