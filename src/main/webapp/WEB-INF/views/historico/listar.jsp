<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Histórico</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb"><b>Histórico</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head"><div><h1>Histórico de vistorias</h1></div></div>

        <form method="get" action="${pageContext.request.contextPath}/historico" class="form-card espaco-baixo-extra">
            <div class="form-grid grade-filtro">
                <div class="field">
                    <label>Edificação</label>
                    <select name="edificacaoId">
                        <option value="">Todas</option>
                        <c:forEach var="e" items="${edificacoes}">
                            <option value="${e.id}" ${e.id == filtroEdificacaoId ? 'selected' : ''}>${e.nome}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="field">
                    <label>Status</label>
                    <select name="status">
                        <option value="">Todos</option>
                        <option value="CONCLUIDA" ${filtroStatus == 'CONCLUIDA' ? 'selected' : ''}>Concluída</option>
                        <option value="ANDAMENTO" ${filtroStatus == 'ANDAMENTO' ? 'selected' : ''}>Em andamento</option>
                        <option value="PENDENTE" ${filtroStatus == 'PENDENTE' ? 'selected' : ''}>Pendente</option>
                    </select>
                </div>
                <button type="submit" class="btn-ghost">Filtrar</button>
            </div>
        </form>

        <div class="panel">
            <c:choose>
                <c:when test="${empty vistorias}">
                    <p class="estado-vazio">Nenhuma vistoria encontrada com esses filtros.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Edificação</th><th>Data</th><th>Responsável</th><th>Tipo</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="v" items="${vistorias}">
                            <tr>
                                <td><b>${v.edificacaoNome}</b></td>
                                <td><fmt:parseDate value="${v.dataVistoria}" pattern="yyyy-MM-dd" var="d"/><fmt:formatDate value="${d}" pattern="dd/MM/yyyy"/></td>
                                <td>${v.usuarioNome}</td>
                                <td>${v.tipo}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.totalNaoConformes > 0}"><span class="badge naoconforme">Com não conformidades</span></c:when>
                                        <c:otherwise><span class="badge concluida">Concluída</span></c:otherwise>
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
