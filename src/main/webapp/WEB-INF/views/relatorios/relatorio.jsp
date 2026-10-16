<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Relatório de Vistoria #${vistoria.id}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>

    <div class="no-print barra-impressao">
        <span>Pré-visualização do relatório — use a impressão do navegador para exportar em PDF.</span>
        <button class="btn-accent" onclick="window.print()">Imprimir / Exportar PDF</button>
    </div>

    <div class="relatorio-page">

        <div class="relatorio-capa">
            <svg class="brand-mark" viewBox="0 0 40 40" fill="none">
                <path d="M20 3C20 3 10 12 10 21a10 10 0 0020 0c0-4-2-6-3.5-8.5.3 3-.8 4.8-2.3 5.6C25 15 24 10 20 3z" fill="#c22a2a"/>
            </svg>
            <h1 class="relatorio-titulo-destaque">RELATÓRIO DE VISTORIA — SEGURANÇA CONTRA INCÊNDIO</h1>
            <h2>${edificacao.nome}</h2>
            <p class="endereco">${edificacao.enderecoCompleto}</p>
            <p class="meta">
                Data da vistoria: <fmt:parseDate value="${vistoria.dataVistoria}" pattern="yyyy-MM-dd" var="d"/><fmt:formatDate value="${d}" pattern="dd/MM/yyyy"/>
                &nbsp;·&nbsp; Responsável: ${vistoria.usuarioNome}
            </p>
        </div>

        <div class="relatorio-section">
            <h3>Dados da edificação</h3>
            <div class="info-row"><span class="k">Razão social</span><span class="v">${edificacao.razaoSocial}</span></div>
            <div class="info-row"><span class="k">CNPJ</span><span class="v">${edificacao.cnpj}</span></div>
            <div class="info-row"><span class="k">Tipo de ocupação</span><span class="v">${edificacao.tipoOcupacao}</span></div>
            <div class="info-row"><span class="k">Área construída</span><span class="v">${edificacao.areaConstruida} m²</span></div>
            <div class="info-row"><span class="k">Pavimentos</span><span class="v">${edificacao.pavimentos}</span></div>
            <div class="info-row"><span class="k">Ocupantes estimados</span><span class="v">${edificacao.ocupantes}</span></div>
        </div>

        <div class="relatorio-section">
            <h3>Resultado da vistoria</h3>
            <div class="stat-grid">
                <div class="stat-card c1"><div class="bar"></div><div class="tag">Total de itens</div><div class="num">${vistoria.totalItens}</div></div>
                <div class="stat-card c2"><div class="bar"></div><div class="tag">Conformes</div><div class="num">${vistoria.totalConformes}</div></div>
                <div class="stat-card c4"><div class="bar"></div><div class="tag">Não conformes</div><div class="num">${vistoria.totalNaoConformes}</div></div>
                <div class="stat-card c3"><div class="bar"></div><div class="tag">Não aplicáveis</div><div class="num">${vistoria.totalNaoAplicaveis}</div></div>
            </div>
        </div>

        <div class="relatorio-section">
            <h3>Checklist detalhado</h3>
            <table>
                <thead><tr><th>Categoria</th><th>Item</th><th>Status</th><th>Observação</th></tr></thead>
                <tbody>
                <c:forEach var="item" items="${vistoria.itens}">
                    <tr>
                        <td>${item.categoria}</td>
                        <td>${item.item}</td>
                        <td>
                            <c:choose>
                                <c:when test="${item.status == 'CONFORME'}"><span class="badge conforme">Conforme</span></c:when>
                                <c:when test="${item.status == 'NAO_CONFORME'}"><span class="badge naoconforme">Não conforme (${item.gravidade})</span></c:when>
                                <c:otherwise><span class="badge naoaplicavel">Não aplicável</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>${item.observacao}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <c:if test="${vistoria.totalNaoConformes > 0}">
            <div class="relatorio-section">
                <h3>Não conformidades identificadas</h3>
                <c:forEach var="item" items="${vistoria.itens}">
                    <c:if test="${item.status == 'NAO_CONFORME'}">
                        <div class="destaque-nao-conformidade">
                            <b>${item.categoria} — ${item.item}</b> (gravidade: ${item.gravidade})<br>
                            <span class="texto-pequeno">${item.observacao}</span>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </c:if>

        <c:if test="${not empty fotos}">
            <div class="relatorio-section">
                <h3>Registro fotográfico</h3>
                <div class="foto-grid">
                    <c:forEach var="f" items="${fotos}">
                        <div class="foto-card">
                            <img src="${pageContext.request.contextPath}/uploads/${f.arquivo}">
                            <div class="fc-body">${f.descricao}<br>${f.localizacao}</div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <div class="relatorio-section">
            <h3>Conclusão</h3>
            <p class="conclusao-texto">
                <c:out value="${empty vistoria.observacoes ? 'Nenhuma observação geral registrada pelo responsável.' : vistoria.observacoes}"/>
            </p>
        </div>

    </div>
</body>
</html>
