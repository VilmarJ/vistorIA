<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Não conformidades</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb"><b>Não conformidades</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head">
            <div>
                <h1>Não conformidades</h1>
                <div class="sub">Todos os itens marcados como "Não conforme" em vistorias já realizadas.</div>
            </div>
        </div>

        <div class="panel">
            <c:choose>
                <c:when test="${empty naoConformidades}">
                    <p class="estado-vazio">Nenhuma não conformidade registrada. 🎉</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Nº</th><th>Edificação</th><th>Item</th><th>Data</th><th>Gravidade</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="nc" items="${naoConformidades}">
                            <tr>
                                <td class="fonte-monoespacada">NC-<fmt:formatNumber value="${nc.id}" minIntegerDigits="3"/></td>
                                <td>${nc.edificacaoNome}</td>
                                <td>${nc.categoria} — ${nc.item}<br><span class="texto-discreto-pequeno">${nc.observacao}</span></td>
                                <td><fmt:parseDate value="${nc.dataVistoria}" pattern="yyyy-MM-dd" var="d"/><fmt:formatDate value="${d}" pattern="dd/MM/yyyy"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${nc.gravidade == 'ALTA'}"><span class="badge naoconforme">Alta</span></c:when>
                                        <c:when test="${nc.gravidade == 'BAIXA'}"><span class="badge naoaplicavel">Baixa</span></c:when>
                                        <c:otherwise><span class="badge pendente">Média</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>Pendente</td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/naoconformidades" class="flex-espaco-pequeno">
                                        <input type="hidden" name="id" value="${nc.id}">
                                        <select name="gravidade" class="select-compacto">
                                            <option value="BAIXA" ${nc.gravidade == 'BAIXA' ? 'selected' : ''}>Baixa</option>
                                            <option value="MEDIA" ${nc.gravidade == 'MEDIA' ? 'selected' : ''}>Média</option>
                                            <option value="ALTA" ${nc.gravidade == 'ALTA' ? 'selected' : ''}>Alta</option>
                                        </select>
                                        <select name="status" class="select-compacto">
                                            <option value="NAO_CONFORME">Pendente</option>
                                            <option value="CONFORME">Resolvida</option>
                                        </select>
                                        <button type="submit" class="link-action">Atualizar</button>
                                    </form>
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
