<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Plantas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb"><b>Plantas</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head">
            <div>
                <h1>Plantas</h1>
                <div class="sub">Plantas baixas de todas as suas edificações, com os equipamentos e rotas de fuga marcados.</div>
            </div>
        </div>

        <c:if test="${not empty mensagemErro}"><div class="error-banner espaco-baixo-grande">${mensagemErro}</div></c:if>

        <div class="panel">
            <c:choose>
                <c:when test="${empty plantas}">
                    <p class="estado-vazio">
                        Nenhuma planta cadastrada. Abra uma <a href="${pageContext.request.contextPath}/edificacoes">edificação</a> e use "Adicionar planta".
                    </p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Planta</th><th>Pavimento</th><th>Edificação</th><th>Enviada em</th><th class="text-end">Ações</th></tr></thead>
                        <tbody>
                        <c:forEach var="p" items="${plantas}">
                            <tr>
                                <td><b>${p.nome}</b></td>
                                <td>${p.pavimento}</td>
                                <td><a class="link-action" href="${pageContext.request.contextPath}/edificacoes?acao=detalhe&id=${p.edificacaoId}">${p.edificacaoNome}</a></td>
                                <td>${p.dataUploadFormatada}</td>
                                <td>
                                    <a class="link-action" href="${pageContext.request.contextPath}/plantas?acao=visualizar&id=${p.id}">Visualizar</a>
                                    <a class="link-action danger" href="${pageContext.request.contextPath}/plantas?acao=excluir&id=${p.id}"
                                       onclick="return confirm('Excluir a planta ${p.nome} e todos os seus marcadores?');">Excluir</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

<%@ include file="/WEB-INF/views/_shell_close.jspf" %>
</body>
</html>
