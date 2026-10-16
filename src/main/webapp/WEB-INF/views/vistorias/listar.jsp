<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - <c:out value="${filtroPendentes ? 'Vistorias pendentes' : 'Vistorias'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb">
            <c:choose>
                <c:when test="${filtroPendentes}">Vistorias / <b>Pendentes</b></c:when>
                <c:otherwise><b>Vistorias</b></c:otherwise>
            </c:choose>
        </div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head">
            <div>
                <h1><c:out value="${filtroPendentes ? 'Vistorias pendentes' : 'Vistorias'}"/></h1>
                <c:if test="${filtroPendentes}">
                    <div class="sub">Vistorias pendentes ou em andamento. <a class="link-action" href="${pageContext.request.contextPath}/vistorias">Ver todas</a></div>
                </c:if>
            </div>
            <a class="btn-accent" href="${pageContext.request.contextPath}/vistorias?acao=novo">${ic_plus} Nova vistoria</a>
        </div>

        <div class="panel">
            <c:choose>
                <c:when test="${empty vistorias}">
                    <p class="estado-vazio"><c:out value="${filtroPendentes ? 'Nenhuma vistoria pendente. 🎉' : 'Nenhuma vistoria registrada.'}"/></p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Edificação</th><th>Data</th><th>Tipo</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="v" items="${vistorias}">
                            <tr>
                                <td><b>${v.edificacaoNome}</b></td>
                                <td><fmt:parseDate value="${v.dataVistoria}" pattern="yyyy-MM-dd" var="d"/><fmt:formatDate value="${d}" pattern="dd/MM/yyyy"/></td>
                                <td>${v.tipo}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.totalNaoConformes > 0}"><span class="badge naoconforme">Com não conformidades</span></c:when>
                                        <c:when test="${v.status == 'CONCLUIDA'}"><span class="badge concluida">Concluída</span></c:when>
                                        <c:when test="${v.status == 'ANDAMENTO'}"><span class="badge andamento">Em andamento</span></c:when>
                                        <c:otherwise><span class="badge pendente">Pendente</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a class="link-action" href="${pageContext.request.contextPath}/vistorias?acao=detalhe&id=${v.id}">Ver</a>
                                    <a class="link-action" href="${pageContext.request.contextPath}/relatorios?vistoriaId=${v.id}" target="_blank">Relatório</a>
                                    <a class="link-action danger" href="${pageContext.request.contextPath}/vistorias?acao=excluir&id=${v.id}"
                                       onclick="return confirm('Excluir esta vistoria?');">Excluir</a>
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
