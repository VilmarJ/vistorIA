package br.com.vistoria.controller;

import java.io.IOException;
import java.sql.SQLException;

import br.com.vistoria.dao.FotoDAO;
import br.com.vistoria.dao.VistoriaDAO;
import br.com.vistoria.model.Foto;
import br.com.vistoria.util.UploadUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/fotos")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024)
public class FotoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final FotoDAO fotoDAO = new FotoDAO();
    private final VistoriaDAO vistoriaDAO = new VistoriaDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int vistoriaId = parseInt(request.getParameter("vistoriaId"));
        String acao = request.getParameter("acao");
        int fotoId = parseInt(request.getParameter("id"));
        int usuarioId = (int) request.getSession().getAttribute("usuarioId");

        try {
            boolean autorizado = vistoriaDAO.pertence(vistoriaId, usuarioId)
                    && (!"excluir".equals(acao) || fotoDAO.pertence(fotoId, usuarioId));
            if (!autorizado) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if ("excluir".equals(acao)) {
                fotoDAO.excluir(fotoId);
            } else {
                Part part = request.getPart("arquivo");
                String uploadDir = getServletContext().getRealPath("/uploads");
                String nomeSalvo = UploadUtil.salvar(part, uploadDir);

                if (nomeSalvo != null) {
                    Foto foto = new Foto();
                    foto.setVistoriaId(vistoriaId);
                    foto.setArquivo(nomeSalvo);
                    foto.setDescricao(valor(request.getParameter("descricao")));
                    foto.setLocalizacao(valor(request.getParameter("localizacao")));
                    fotoDAO.cadastrar(foto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("mensagemErro", "Não foi possível salvar a foto.");
            request.getRequestDispatcher("/WEB-INF/views/erro.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/vistorias?acao=detalhe&id=" + vistoriaId);
    }

    private int parseInt(String v) {
        try { return (v == null || v.isBlank()) ? 0 : Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private String valor(String v) {
        return (v == null) ? "" : v.trim();
    }
}
