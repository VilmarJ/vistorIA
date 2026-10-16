package br.com.vistoria.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        boolean rotaPublica = path.startsWith("/auth")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/uploads")
                || path.equals("/index.jsp")
                || path.equals("/");

        HttpSession session = request.getSession(false);
        boolean autenticado = (session != null && session.getAttribute("usuarioId") != null);

        if (rotaPublica || autenticado) {
            chain.doFilter(req, resp);
        } else {
            response.sendRedirect(request.getContextPath() + "/auth?acao=login");
        }
    }

    @Override
    public void destroy() {
    }
}
