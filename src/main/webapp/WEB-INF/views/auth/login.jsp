<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Entrar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<div class="login-wrap">
    <div class="login-side">
        <div class="brand">
            <svg class="brand-mark" viewBox="0 0 40 40" fill="none">
                <path d="M20 3C20 3 10 12 10 21a10 10 0 0020 0c0-4-2-6-3.5-8.5.3 3-.8 4.8-2.3 5.6C25 15 24 10 20 3z" fill="#c22a2a"/>
            </svg>
            <span class="brand-name">VistorIA</span>
        </div>
        <div class="login-copy">
            <span class="eyebrow">VISTORIAS · SEGURANÇA CONTRA INCÊNDIO</span>
            <h1>Cada saída, extintor e rota registrados no lugar certo da planta.</h1>
            <p>Sistema inteligente para gerenciamento e análise de vistorias de segurança contra incêndio.</p>
        </div>
        <p class="login-foot">© 2026 VistorIA — uso restrito a profissionais habilitados.</p>
    </div>

    <div class="login-main">
        <div class="login-card">
            <h2>Acessar sua conta</h2>
            <span class="sub">Entre com suas credenciais de responsável técnico.</span>

            <c:if test="${not empty erroLogin}">
                <div class="error-banner">${erroLogin}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/auth">
                <input type="hidden" name="acao" value="autenticar">
                <div class="field">
                    <label>E-mail</label>
                    <input type="email" name="email" placeholder="voce@empresa.com.br" required autofocus>
                </div>
                <div class="field">
                    <label>Senha</label>
                    <input type="password" name="senha" placeholder="••••••••" required>
                </div>
                <div class="row-between">
                    <label><input type="checkbox" name="lembrar" checked class="largura-automatica"> Lembrar-me</label>
                    <a href="#">Esqueci minha senha</a>
                </div>
                <button type="submit" class="btn-primary">Entrar</button>
            </form>

            <p class="login-alt">
                Ainda não tem conta?
                <a href="${pageContext.request.contextPath}/auth?acao=cadastro">Criar uma conta</a>
            </p>
            <p class="login-alt login-alt-secundario">
                Demo: demo@vistoria-ia.com / 123456
            </p>
        </div>
    </div>
</div>
</body>
</html>
