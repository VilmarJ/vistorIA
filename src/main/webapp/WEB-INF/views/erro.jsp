<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Erro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
    <div class="area-pagina-erro">
        <div class="form-card">
            <h2>Ops! Ocorreu um problema</h2>
            <div class="error-banner">${empty mensagemErro ? 'Recurso não encontrado.' : mensagemErro}</div>
            <c:if test="${not empty detalheErro}">
                <p class="detalhe-do-erro">
                    Detalhe técnico: ${detalheErro}
                </p>
            </c:if>
            <a class="btn-ghost" href="${pageContext.request.contextPath}/dashboard">Voltar ao início</a>
        </div>
    </div>
</body>
</html>
