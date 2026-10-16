<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Criar conta</title>
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
            <span class="eyebrow">CADASTRO DE PROFISSIONAL</span>
            <h1>Crie sua conta e comece a gerenciar suas vistorias hoje mesmo.</h1>
            <p>Uso restrito a responsáveis técnicos habilitados para vistorias de segurança contra incêndio.</p>
        </div>
        <p class="login-foot">© 2026 VistorIA.</p>
    </div>

    <div class="login-main">
        <div class="login-card">
            <h2>Criar uma conta</h2>
            <span class="sub">Leva menos de um minuto.</span>

            <c:if test="${not empty erroCadastro}">
                <div class="error-banner">${erroCadastro}</div>
            </c:if>

            <form method="post" action="${pageContext.request.contextPath}/auth">
                <input type="hidden" name="acao" value="cadastrar">
                <div class="field">
                    <label>Nome completo</label>
                    <input type="text" name="nome" required autofocus>
                </div>
                <div class="field">
                    <label>CPF ou registro profissional (CREA/CRBM)</label>
                    <input type="text" name="identificacao" placeholder="CREA-SC 123456">
                </div>
                <div class="field">
                    <label>E-mail</label>
                    <input type="email" name="email" required>
                </div>
                <div class="field">
                    <label>Telefone</label>
                    <input type="text" name="telefone" placeholder="(47) 99999-0000">
                </div>
                <div class="field">
                    <label>Senha</label>
                    <input type="password" name="senha" required minlength="6">
                </div>
                <div class="field">
                    <label>Confirmar senha</label>
                    <input type="password" name="confirmarSenha" required minlength="6">
                </div>
                <div class="row-between">
                    <label><input type="checkbox" required class="largura-automatica"> Aceito os termos de uso</label>
                </div>
                <button type="submit" class="btn-primary">Criar conta</button>
            </form>

            <p class="login-alt">
                Já tem conta? <a href="${pageContext.request.contextPath}/auth?acao=login">Entrar</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>
