<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - <c:out value="${edificacao.id > 0 ? 'Editar edificação' : 'Nova edificação'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_sidebar.jspf" %>

    <div class="topbar">
        <div class="crumb">Edificações / <b>${edificacao.id > 0 ? 'Editar' : 'Nova'}</b></div>
        <div class="user-chip"><div class="avatar">${sessionScope.usuarioIniciais}</div> ${sessionScope.usuarioNome}</div>
    </div>

    <div class="content">
        <div class="page-head">
            <div><h1>${edificacao.id > 0 ? 'Editar edificação' : 'Nova edificação'}</h1></div>
        </div>

        <c:if test="${not empty mensagemErro}"><div class="error-banner espaco-baixo-grande">${mensagemErro}</div></c:if>

        <form method="post" action="${pageContext.request.contextPath}/edificacoes" class="form-card">
            <input type="hidden" name="id" value="${edificacao.id}">

            <div class="form-section-title">Informações gerais</div>
            <div class="form-grid">
                <div class="field span-2"><label>Nome da edificação *</label><input type="text" name="nome" required value="${edificacao.nome}"></div>
                <div class="field"><label>CPF/CNPJ</label><input type="text" name="cnpj" id="campoCnpj" inputmode="numeric" placeholder="000.000.000-00" maxlength="18" value="${edificacao.cnpj}"></div>
                <div class="field span-3"><label>Razão social</label><input type="text" name="razaoSocial" value="${edificacao.razaoSocial}"></div>
                <div class="field span-2"><label>Endereço</label><input type="text" name="endereco" value="${edificacao.endereco}"></div>
                <div class="field"><label>Número</label><input type="text" name="numero" value="${edificacao.numero}"></div>
                <div class="field"><label>Bairro</label><input type="text" name="bairro" value="${edificacao.bairro}"></div>
                <div class="field"><label>Cidade</label><input type="text" name="cidade" value="${edificacao.cidade}"></div>
                <div class="field"><label>Estado (UF)</label><input type="text" name="estado" maxlength="2" value="${edificacao.estado}"></div>
                <div class="field"><label>CEP</label><input type="text" name="cep" id="campoCep" inputmode="numeric" placeholder="00000-000" maxlength="9" value="${edificacao.cep}"></div>
                <div class="field"><label>Área construída (m²)</label><input type="number" step="0.01" name="areaConstruida" value="${edificacao.areaConstruida}"></div>
                <div class="field"><label>Número de pavimentos</label><input type="number" name="pavimentos" value="${edificacao.pavimentos}"></div>
                <div class="field"><label>Altura da edificação (m)</label><input type="number" step="0.01" name="altura" value="${edificacao.altura}"></div>
                <div class="field"><label>Ocupantes estimados</label><input type="number" name="ocupantes" value="${edificacao.ocupantes}"></div>
                <div class="field"><label>Tipo de ocupação</label><input type="text" name="tipoOcupacao" placeholder="Comercial, Residencial..." value="${edificacao.tipoOcupacao}"></div>
            </div>

            <div class="form-section-title">Características</div>
            <div class="form-grid">
                <div class="field"><label>Tipo de edificação</label><input type="text" name="tipoEdificacao" value="${edificacao.tipoEdificacao}"></div>
                <div class="field"><label>Uso da edificação</label><input type="text" name="usoEdificacao" value="${edificacao.usoEdificacao}"></div>
                <div class="field"><label>Área por pavimento (m²)</label><input type="number" step="0.01" name="areaPavimento" value="${edificacao.areaPavimento}"></div>
                <div class="field"><label>Quantidade de blocos</label><input type="number" name="qtdBlocos" value="${edificacao.qtdBlocos}"></div>
                <div class="field checkbox-row linha-opcao-espacada">
                    <input type="checkbox" name="temSubsolo" class="largura-automatica" ${edificacao.temSubsolo ? 'checked' : ''}> Possui subsolo
                </div>
                <div class="field checkbox-row linha-opcao-espacada">
                    <input type="checkbox" name="temMezanino" class="largura-automatica" ${edificacao.temMezanino ? 'checked' : ''}> Possui mezanino
                </div>
            </div>

            <div class="alinhar-direita-espacado">
                <a class="btn-ghost" href="${pageContext.request.contextPath}/edificacoes">Cancelar</a>
                <button type="submit" class="btn-accent">Salvar edificação</button>
            </div>
        </form>
    </div>

<script>
    function formatarCpfCnpj(valor) {
        const digitos = valor.replace(/\D/g, '').slice(0, 14);
        if (digitos.length <= 11) {
            return digitos
                .replace(/(\d{3})(\d)/, '$1.$2')
                .replace(/(\d{3})(\d)/, '$1.$2')
                .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        }
        return digitos
            .replace(/(\d{2})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d)/, '$1/$2')
            .replace(/(\d{4})(\d{1,2})$/, '$1-$2');
    }

    function formatarCep(valor) {
        const digitos = valor.replace(/\D/g, '').slice(0, 8);
        return digitos.replace(/(\d{5})(\d{1,3})$/, '$1-$2');
    }

    const campoCnpj = document.getElementById('campoCnpj');
    if (campoCnpj) {
        campoCnpj.addEventListener('input', () => { campoCnpj.value = formatarCpfCnpj(campoCnpj.value); });
        campoCnpj.value = formatarCpfCnpj(campoCnpj.value);
    }

    const campoCep = document.getElementById('campoCep');
    if (campoCep) {
        campoCep.addEventListener('input', () => { campoCep.value = formatarCep(campoCep.value); });
        campoCep.value = formatarCep(campoCep.value);
    }
</script>

<%@ include file="/WEB-INF/views/_shell_close.jspf" %>
</body>
</html>
