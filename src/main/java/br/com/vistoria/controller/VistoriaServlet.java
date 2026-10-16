package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.vistoria.dao.EdificacaoDAO;
import br.com.vistoria.dao.PlantaDAO;
import br.com.vistoria.dao.VistoriaDAO;
import br.com.vistoria.model.ItemVistoria;
import br.com.vistoria.model.Planta;
import br.com.vistoria.model.Vistoria;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/vistorias")
public class VistoriaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final VistoriaDAO vistoriaDAO = new VistoriaDAO();
    private final EdificacaoDAO edificacaoDAO = new EdificacaoDAO();
    private final PlantaDAO plantaDAO = new PlantaDAO();

    /** Checklist fixo — categoria -> itens (Seção 13 da especificação). */
    public static final java.util.LinkedHashMap<String, String[]> CHECKLIST = new java.util.LinkedHashMap<>();
    static {
        CHECKLIST.put("Extintores", new String[]{
                "Quantidade adequada", "Localização adequada", "Sinalização",
                "Acesso desobstruído", "Altura adequada", "Validade", "Lacre",
                "Manômetro", "Estado físico"});
        CHECKLIST.put("Saídas de emergência", new String[]{
                "Sinalização fotoluminescente", "Largura adequada", "Desobstrução",
                "Sentido de abertura da porta", "Iluminação de emergência próxima"});
        CHECKLIST.put("Rotas de fuga", new String[]{
                "Sinalização ao longo do trajeto", "Largura mínima respeitada",
                "Ausência de obstáculos", "Piso antiderrapante"});
        CHECKLIST.put("Hidrantes", new String[]{
                "Mangueira em bom estado", "Pressão adequada", "Abrigo sinalizado",
                "Registro sem vazamentos"});
        CHECKLIST.put("Iluminação de emergência", new String[]{
                "Acionamento automático", "Autonomia da bateria", "Blocos sem danos"});
        CHECKLIST.put("Alarmes", new String[]{
                "Acionadores manuais acessíveis", "Central de alarme operante",
                "Sirenes audíveis em todo o pavimento"});
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("paginaAtiva", "vistorias");
        String acao = request.getParameter("acao");
        if (acao == null) acao = "listar";

        try {
            switch (acao) {
                case "novo":
                    exibirFormulario(request, response);
                    break;
                case "detalhe":
                    tratarDetalhe(request, response);
                    break;
                case "excluir":
                    tratarExcluir(request, response);
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
        boolean somentePendentes = "pendentes".equals(request.getParameter("status"));
        if (somentePendentes) {
            request.setAttribute("vistorias", vistoriaDAO.listarPendentesPorUsuario(usuarioId));
            request.setAttribute("filtroPendentes", true);
        } else {
            request.setAttribute("vistorias", vistoriaDAO.listarPorUsuario(usuarioId));
        }
        request.getRequestDispatcher("/WEB-INF/views/vistorias/listar.jsp").forward(request, response);
    }

    private void exibirFormulario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int usuarioId = (int) request.getSession().getAttribute("usuarioId");
        request.setAttribute("edificacoes", edificacaoDAO.listarPorUsuario(usuarioId));
        request.setAttribute("checklist", CHECKLIST);

        int edificacaoIdPre = parseInt(request.getParameter("edificacaoId"));
        if (edificacaoIdPre > 0 && edificacaoDAO.pertence(edificacaoIdPre, usuarioId)) {
            request.setAttribute("plantas", plantaDAO.listarPorEdificacao(edificacaoIdPre));
            request.setAttribute("edificacaoIdSelecionada", edificacaoIdPre);
        }

        request.getRequestDispatcher("/WEB-INF/views/vistorias/novo.jsp").forward(request, response);
    }

    private void tratarDetalhe(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = parseInt(request.getParameter("id"));
        int usuarioId = (int) request.getSession().getAttribute("usuarioId");
        if (!vistoriaDAO.pertence(id, usuarioId)) {
            response.sendRedirect(request.getContextPath() + "/vistorias");
            return;
        }
        request.setAttribute("vistoria", vistoriaDAO.buscarPorId(id));
        request.setAttribute("fotos", new br.com.vistoria.dao.FotoDAO().listarPorVistoria(id));
        request.getRequestDispatcher("/WEB-INF/views/vistorias/detalhe.jsp").forward(request, response);
    }

    private void tratarSalvar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int usuarioId = (int) request.getSession().getAttribute("usuarioId");
        int edificacaoId = parseInt(request.getParameter("edificacaoId"));
        int plantaId = parseInt(request.getParameter("plantaId"));
        String tipo = valor(request.getParameter("tipo"));
        LocalDate data = parseData(request.getParameter("dataVistoria"));
        String observacoesGerais = valor(request.getParameter("observacoesGerais"));

        List<ItemVistoria> itens = new ArrayList<>();
        List<String> categorias = new ArrayList<>(CHECKLIST.keySet());
        for (int indiceCategoria = 0; indiceCategoria < categorias.size(); indiceCategoria++) {
            String categoria = categorias.get(indiceCategoria);
            String[] itensCategoria = CHECKLIST.get(categoria);
            for (int indiceItem = 0; indiceItem < itensCategoria.length; indiceItem++) {
                String chave = indiceCategoria + "_" + indiceItem;
                String status = valor(request.getParameter("status_" + chave));
                if (status.isBlank()) continue;

                ItemVistoria item = new ItemVistoria();
                item.setCategoria(categoria);
                item.setItem(itensCategoria[indiceItem]);
                item.setStatus(status);
                item.setObservacao(valor(request.getParameter("obs_" + chave)));
                if ("NAO_CONFORME".equals(status)) {
                    String gravidade = valor(request.getParameter("gravidade_" + chave));
                    item.setGravidade(gravidade.isBlank() ? "MEDIA" : gravidade);
                }
                itens.add(item);
            }
        }

        if (edificacaoId <= 0 || data == null || itens.isEmpty()) {
            request.setAttribute("mensagemErro",
                    "Selecione a edificação, informe a data e responda ao menos um item do checklist.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
            return;
        }

        boolean plantaValida = plantaId <= 0 || plantaPertenceAEdificacao(plantaId, edificacaoId);
        if (!edificacaoDAO.pertence(edificacaoId, usuarioId) || !plantaValida) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Vistoria vistoria = new Vistoria();
        vistoria.setEdificacaoId(edificacaoId);
        vistoria.setUsuarioId(usuarioId);
        vistoria.setPlantaId(plantaId > 0 ? plantaId : null);
        vistoria.setDataVistoria(data);
        vistoria.setTipo(tipo.isBlank() ? "PERIODICA" : tipo);
        vistoria.setStatus("CONCLUIDA");
        vistoria.setObservacoes(observacoesGerais);
        vistoria.setItens(itens);

        vistoriaDAO.cadastrar(vistoria);

        response.sendRedirect(request.getContextPath() + "/vistorias?acao=detalhe&id=" + vistoria.getId());
    }

    private void tratarExcluir(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = parseInt(request.getParameter("id"));
        int usuarioId = (int) request.getSession().getAttribute("usuarioId");
        if (vistoriaDAO.pertence(id, usuarioId)) {
            vistoriaDAO.excluir(id);
        }
        response.sendRedirect(request.getContextPath() + "/vistorias");
    }

    private boolean plantaPertenceAEdificacao(int plantaId, int edificacaoId) throws SQLException {
        Planta planta = plantaDAO.buscarPorId(plantaId);
        return planta != null && planta.getEdificacaoId() == edificacaoId;
    }

    private LocalDate parseData(String texto) {
        try { return (texto == null || texto.isBlank()) ? null : LocalDate.parse(texto.trim()); }
        catch (DateTimeParseException e) { return null; }
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
