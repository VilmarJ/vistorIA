<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Vistoria #${vistoria.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb">Vistorias / <b>#${vistoria.id} — ${vistoria.edificacaoNome}</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head">
            <div>
                <h1>${vistoria.edificacaoNome}</h1>
                <div class="sub">
                    <fmt:parseDate value="${vistoria.dataVistoria}" pattern="yyyy-MM-dd" var="d"/><fmt:formatDate value="${d}" pattern="dd/MM/yyyy"/>
                    · ${vistoria.tipo} · Responsável: ${vistoria.usuarioNome}
                </div>
            </div>
            <a class="btn-accent" href="${pageContext.request.contextPath}/relatorios?vistoriaId=${vistoria.id}" target="_blank">${ic_doc} Gerar relatório</a>
        </div>

        <div class="stat-grid">
            <div class="stat-card c1"><div class="bar"></div><div class="tag">Itens verificados</div><div class="num">${vistoria.totalItens}</div></div>
            <div class="stat-card c2"><div class="bar"></div><div class="tag">Conformes</div><div class="num">${vistoria.totalConformes}</div></div>
            <div class="stat-card c4"><div class="bar"></div><div class="tag">Não conformes</div><div class="num">${vistoria.totalNaoConformes}</div></div>
            <div class="stat-card c3"><div class="bar"></div><div class="tag">Não aplicáveis</div><div class="num">${vistoria.totalNaoAplicaveis}</div></div>
        </div>

        <div class="panel">
            <div class="panel-head"><h3>Itens do checklist</h3></div>
            <table>
                <thead><tr><th>Categoria</th><th>Item</th><th>Status</th><th>Gravidade</th><th>Observação</th></tr></thead>
                <tbody>
                <c:forEach var="item" items="${vistoria.itens}">
                    <tr>
                        <td>${item.categoria}</td>
                        <td>${item.item}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.status == 'CONFORME'}"><span class="badge conforme">Conforme</span></c:when>
                                <c:when test="${item.status == 'NAO_CONFORME'}"><span class="badge naoconforme">Não conforme</span></c:when>
                                <c:otherwise><span class="badge naoaplicavel">Não aplicável</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>${item.gravidade}</td>
                        <td class="celula-estreita">${item.observacao}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="panel painel-espacado">
            <div class="panel-head"><h3>Fotos da vistoria</h3></div>
            <div class="foto-grid">
                <c:forEach var="f" items="${fotos}">
                    <div class="foto-card">
                        <img src="${pageContext.request.contextPath}/uploads/${f.arquivo}">
                        <div class="fc-body">${f.descricao}<br>${f.localizacao}</div>
                    </div>
                </c:forEach>
            </div>
            <div class="rodape-painel-formulario">
                <form method="post" action="${pageContext.request.contextPath}/fotos" enctype="multipart/form-data">
                    <input type="hidden" name="vistoriaId" value="${vistoria.id}">
                    <div class="form-grid grade-duas-colunas">
                        <div class="field"><label>Descrição</label><input type="text" name="descricao" placeholder="Extintor com lacre rompido"></div>
                        <div class="field"><label>Localização</label><input type="text" name="localizacao" placeholder="Sala 03"></div>
                    </div>
                    <div class="field"><label>Foto</label><input type="file" name="arquivo" accept="image/*" required></div>
                    <button type="submit" class="btn-ghost">${ic_plus} Adicionar foto</button>
                </form>
            </div>
        </div>

        <c:if test="${not empty vistoria.observacoes}">
            <div class="panel painel-espacado">
                <div class="panel-head"><h3>Observações gerais</h3></div>
                <p class="texto-observacao">${vistoria.observacoes}</p>
            </div>
        </c:if>
    </div>

<%@ include file="/WEB-INF/views/_shell_close.jspf" %>
</body>
</html>
