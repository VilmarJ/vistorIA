<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - ${edificacao.nome}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb">Edificações / <b>${edificacao.nome}</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head">
            <div>
                <h1>${edificacao.nome}</h1>
                <div class="sub">${edificacao.enderecoCompleto}</div>
            </div>
            <a class="btn-accent" href="${pageContext.request.contextPath}/vistorias?acao=novo&edificacaoId=${edificacao.id}">${ic_plus} Nova vistoria</a>
        </div>

        <div class="grid-2">
            <div class="panel">
                <div class="panel-head"><h3>Informações gerais</h3>
                    <a class="link-action" href="${pageContext.request.contextPath}/edificacoes?acao=editar&id=${edificacao.id}">Editar</a>
                </div>
                <div class="info-list">
                    <div class="info-row"><span class="k">Razão social</span><span class="v">${edificacao.razaoSocial}</span></div>
                    <div class="info-row"><span class="k">CNPJ</span><span class="v">${edificacao.cnpj}</span></div>
                    <div class="info-row"><span class="k">Tipo de ocupação</span><span class="v">${edificacao.tipoOcupacao}</span></div>
                    <div class="info-row"><span class="k">Área construída</span><span class="v">${edificacao.areaConstruida} m²</span></div>
                    <div class="info-row"><span class="k">Pavimentos</span><span class="v">${edificacao.pavimentos}</span></div>
                    <div class="info-row"><span class="k">Altura</span><span class="v">${edificacao.altura} m</span></div>
                    <div class="info-row"><span class="k">Ocupantes estimados</span><span class="v">${edificacao.ocupantes}</span></div>
                    <div class="info-row"><span class="k">Subsolo / Mezanino</span>
                        <span class="v">${edificacao.temSubsolo ? 'Subsolo' : 'Sem subsolo'} · ${edificacao.temMezanino ? 'Mezanino' : 'Sem mezanino'}</span>
                    </div>
                </div>
            </div>

            <div class="panel">
                <div class="panel-head"><h3>Plantas da edificação</h3></div>

                <c:forEach var="p" items="${plantas}">
                    <div class="plant-item">
                        <div class="fico">${ic_doc}</div>
                        <div class="meta">
                            <div class="name">${p.nome}</div>
                            <div class="pav">${p.pavimento}</div>
                        </div>
                        <a class="link-action" href="${pageContext.request.contextPath}/plantas?acao=visualizar&id=${p.id}">Visualizar</a>
                        <a class="link-action danger" href="${pageContext.request.contextPath}/plantas?acao=excluir&id=${p.id}"
                           onclick="return confirm('Excluir esta planta e todos os seus marcadores?');">Excluir</a>
                    </div>
                </c:forEach>
                <c:if test="${empty plantas}">
                    <p class="estado-vazio-pequeno">Nenhuma planta anexada ainda.</p>
                </c:if>

                <div class="rodape-painel-formulario">
                    <form method="post" action="${pageContext.request.contextPath}/plantas" enctype="multipart/form-data">
                        <input type="hidden" name="edificacaoId" value="${edificacao.id}">
                        <div class="form-grid grade-duas-colunas">
                            <div class="field"><label>Nome da planta</label><input type="text" name="nome" placeholder="Planta_Terreo" required></div>
                            <div class="field"><label>Pavimento</label><input type="text" name="pavimento" placeholder="Pavimento Térreo" required></div>
                        </div>
                        <div class="field"><label>Arquivo (PDF, PNG ou JPG)</label><input type="file" name="arquivo" accept=".pdf,.png,.jpg,.jpeg" required></div>
                        <button type="submit" class="btn-ghost">${ic_plus} Adicionar planta</button>
                    </form>
                </div>
            </div>
        </div>

        <div class="panel painel-espacado">
            <div class="panel-head"><h3>Vistorias relacionadas</h3></div>
            <c:choose>
                <c:when test="${empty vistorias}">
                    <p class="estado-vazio-medio">Nenhuma vistoria realizada para esta edificação.</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <thead><tr><th>Data</th><th>Responsável</th><th>Tipo</th><th>Status</th><th></th></tr></thead>
                        <tbody>
                        <c:forEach var="v" items="${vistorias}">
                            <tr>
                                <td><fmt:parseDate value="${v.dataVistoria}" pattern="yyyy-MM-dd" var="d"/><fmt:formatDate value="${d}" pattern="dd/MM/yyyy"/></td>
                                <td>${v.usuarioNome}</td>
                                <td>${v.tipo}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${v.totalNaoConformes > 0}"><span class="badge naoconforme">Com não conformidades</span></c:when>
                                        <c:otherwise><span class="badge concluida">Concluída</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td><a class="link-action" href="${pageContext.request.contextPath}/vistorias?acao=detalhe&id=${v.id}">Ver relatório</a></td>
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
