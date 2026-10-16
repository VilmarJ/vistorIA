<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb"><b>Dashboard</b></div>
        <div class="user-chip">
            <div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}
        </div>
    </div>

    <div class="content">
        <div class="page-head">
            <div>
                <h1>Visão geral</h1>
                <div class="sub">Acompanhamento das edificações e vistorias sob sua responsabilidade.</div>
            </div>
            <a class="btn-accent" href="${pageContext.request.contextPath}/vistorias?acao=novo">${ic_plus} Nova vistoria</a>
        </div>

        <div class="stat-grid">
            <a class="stat-card c1" href="${pageContext.request.contextPath}/edificacoes"><div class="bar"></div><div class="tag">Edificações</div><div class="num">${totalEdificacoes}</div></a>
            <a class="stat-card c2" href="${pageContext.request.contextPath}/vistorias"><div class="bar"></div><div class="tag">Vistorias realizadas</div><div class="num">${totalVistorias}</div></a>
            <a class="stat-card c3" href="${pageContext.request.contextPath}/vistorias?status=pendentes"><div class="bar"></div><div class="tag">Vistorias pendentes</div><div class="num">${vistoriasPendentes}</div></a>
            <a class="stat-card c4" href="${pageContext.request.contextPath}/naoconformidades"><div class="bar"></div><div class="tag">Não conformidades</div><div class="num">${naoConformidades}</div></a>
        </div>

        <div class="panel">
            <div class="panel-head">
                <h3>Vistorias recentes</h3>
                <a class="link-action" href="${pageContext.request.contextPath}/historico">Ver histórico completo</a>
            </div>
            <c:choose>
                <c:when test="${empty vistoriasRecentes}">
                    <p class="estado-vazio">
                        Nenhuma vistoria registrada ainda.
                    </p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Edificação</th><th>Data</th><th>Responsável</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="v" items="${vistoriasRecentes}">
                            <tr>
                                <td><b>${v.edificacaoNome}</b></td>
                                <td><fmt:parseDate value="${v.dataVistoria}" pattern="yyyy-MM-dd" var="d"/><fmt:formatDate value="${d}" pattern="dd/MM/yyyy"/></td>
                                <td>${v.usuarioNome}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.totalNaoConformes > 0}"><span class="badge naoconforme">Com não conformidades</span></c:when>
                                        <c:when test="${v.status == 'CONCLUIDA'}"><span class="badge concluida">Concluída</span></c:when>
                                        <c:when test="${v.status == 'ANDAMENTO'}"><span class="badge andamento">Em andamento</span></c:when>
                                        <c:otherwise><span class="badge pendente">Pendente</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td><a class="link-action" href="${pageContext.request.contextPath}/vistorias?acao=detalhe&id=${v.id}">Abrir</a></td>
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
