<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - Nova vistoria</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb">Vistorias / <b>Nova vistoria</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head"><div><h1>Nova vistoria</h1></div></div>

        <div class="form-card espaco-baixo-extra">
            <div class="form-grid">
                <div class="field">
                    <label>Edificação *</label>
                    <select id="selEdificacao" onchange="location.href='${pageContext.request.contextPath}/vistorias?acao=novo&edificacaoId=' + this.value">
                        <option value="">Selecione...</option>
                        <c:forEach var="e" items="${edificacoes}">
                            <option value="${e.id}" ${e.id == edificacaoIdSelecionada ? 'selected' : ''}>${e.nome}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="field">
                    <label>Planta (opcional)</label>
                    <select name="plantaId" form="formVistoria">
                        <option value="">Nenhuma</option>
                        <c:forEach var="p" items="${plantas}">
                            <option value="${p.id}">${p.nome} — ${p.pavimento}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="field"><label>Data da vistoria *</label><input type="date" name="dataVistoria" form="formVistoria" required></div>
                <div class="field">
                    <label>Tipo de vistoria</label>
                    <select name="tipo" form="formVistoria">
                        <option value="INICIAL">Inicial</option>
                        <option value="PERIODICA" selected>Periódica</option>
                    </select>
                </div>
            </div>
        </div>

        <c:if test="${empty edificacaoIdSelecionada}">
            <div class="panel"><p class="estado-vazio">
                Selecione uma edificação acima para carregar o checklist.
            </p></div>
        </c:if>

        <c:if test="${not empty edificacaoIdSelecionada}">
            <form method="post" action="${pageContext.request.contextPath}/vistorias" id="formVistoria">
                <input type="hidden" name="edificacaoId" value="${edificacaoIdSelecionada}">

                <c:forEach var="grupoChecklist" items="${checklist}" varStatus="statusCategoria">
                    <div class="checklist-cat">
                        <h4>${grupoChecklist.key}</h4>
                        <c:forEach var="itemNome" items="${grupoChecklist.value}" varStatus="statusItem">
                            <c:set var="chave" value="${statusCategoria.index}_${statusItem.index}"/>
                            <div class="checklist-item">
                                <div class="item-nome">${itemNome}</div>
                                <div class="status-options">
                                    <label><input type="radio" name="status_${chave}" value="CONFORME" onchange="aoMudarStatus('${chave}')" class="largura-automatica"> Conforme</label>
                                    <label><input type="radio" name="status_${chave}" value="NAO_CONFORME" onchange="aoMudarStatus('${chave}')" class="largura-automatica"> Não conforme</label>
                                    <label><input type="radio" name="status_${chave}" value="NAO_APLICAVEL" onchange="aoMudarStatus('${chave}')" class="largura-automatica"> Não aplicável</label>
                                </div>
                                <div class="gravidade-row" id="gravidade_${chave}">
                                    Gravidade:
                                    <label><input type="radio" name="gravidade_${chave}" value="BAIXA" class="largura-automatica"> Baixa</label>
                                    <label><input type="radio" name="gravidade_${chave}" value="MEDIA" checked class="largura-automatica"> Média</label>
                                    <label><input type="radio" name="gravidade_${chave}" value="ALTA" class="largura-automatica"> Alta</label>
                                </div>
                                <textarea name="obs_${chave}" placeholder="Observação (opcional)"></textarea>
                            </div>
                        </c:forEach>
                    </div>
                </c:forEach>

                <div class="form-card">
                    <div class="field"><label>Observações gerais da vistoria</label>
                        <textarea name="observacoesGerais" rows="3" class="caixa-texto-larga"></textarea>
                    </div>
                    <div class="alinhar-direita">
                        <a class="btn-ghost" href="${pageContext.request.contextPath}/vistorias">Cancelar</a>
                        <button type="submit" class="btn-accent">Finalizar vistoria</button>
                    </div>
                </div>
            </form>
        </c:if>
    </div>

<%@ include file="/WEB-INF/views/_shell_close.jspf" %>

<script>
    function aoMudarStatus(chave){
        const linhaGravidade = document.getElementById('gravidade_' + chave);
        const marcadoNaoConforme = document.querySelector('input[name="status_' + chave + '"][value="NAO_CONFORME"]').checked;
        linhaGravidade.classList.toggle('show', marcadoNaoConforme);
    }
</script>
</body>
</html>
