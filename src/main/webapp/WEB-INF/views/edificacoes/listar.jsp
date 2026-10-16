<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Edificações</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb"><b>Edificações</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head">
            <div>
                <h1>Edificações</h1>
                <div class="sub">Todas as edificações sob sua responsabilidade técnica.</div>
            </div>
            <a class="btn-accent" href="${pageContext.request.contextPath}/edificacoes?acao=novo">${ic_plus} Nova edificação</a>
        </div>

        <c:if test="${not empty mensagemErro}"><div class="error-banner espaco-baixo-grande">${mensagemErro}</div></c:if>

        <div class="panel">
            <c:choose>
                <c:when test="${empty edificacoes}">
                    <p class="estado-vazio">
                        Nenhuma edificação cadastrada. Clique em "Nova edificação" para começar.
                    </p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Nome</th><th>Cidade/UF</th><th>Tipo de ocupação</th><th>Pavimentos</th><th class="text-end">Ações</th></tr></thead>
                        <tbody>
                        <c:forEach var="e" items="${edificacoes}">
                            <tr>
                                <td><b>${e.nome}</b></td>
                                <td>${e.cidade}<c:if test="${not empty e.estado}">/${e.estado}</c:if></td>
                                <td>${e.tipoOcupacao}</td>
                                <td>${e.pavimentos}</td>
                                <td>
                                    <a class="link-action" href="${pageContext.request.contextPath}/edificacoes?acao=detalhe&id=${e.id}">Abrir</a>
                                    <a class="link-action" href="${pageContext.request.contextPath}/edificacoes?acao=editar&id=${e.id}">Editar</a>
                                    <a class="link-action danger" href="${pageContext.request.contextPath}/edificacoes?acao=excluir&id=${e.id}"
                                       onclick="return confirm('Excluir a edificação ${e.nome}? Isso também remove plantas e vistorias relacionadas.');">Excluir</a>
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
