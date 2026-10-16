<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>VistorIA - ${planta.nome}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
</head>
<body>
<%@ include file="/WEB-INF/views/_icons.jspf" %>

<div class="viewer-topbar">
    <div class="vt-info">
        <a href="${pageContext.request.contextPath}/edificacoes?acao=detalhe&id=${planta.edificacaoId}">${ic_back}</a>
        <div class="divider"></div>
        <b>${planta.edificacaoNome}</b>
        <span class="separador-cabecalho">·</span>
        <span>${planta.pavimento}</span>
        <span class="separador-cabecalho">·</span>
        <span class="nome-arquivo-cabecalho">${planta.nome}</span>
    </div>
    <div class="vt-tools">
        <button type="button" class="btn-ghost botao-contorno-escuro" onclick="aplicarZoom(-0.15)">- Zoom</button>
        <span id="indicadorZoom" class="indicador-zoom">100%</span>
        <button type="button" class="btn-ghost botao-contorno-escuro" onclick="aplicarZoom(0.15)">+ Zoom</button>
        <button type="button" class="btn-ghost botao-contorno-escuro" onclick="ajustarZoom()">Ajustar</button>
    </div>
</div>

<div class="viewer-body">
    <div class="tools-rail">
        <button type="button" class="tool-btn active" data-tipo="EXTINTOR" draggable="true" onclick="selecionarTipo(this)" ondragstart="iniciarArrastoIcone(event,'EXTINTOR')"><span class="dot extintor">${ic_equip_extintor}</span>Extintor</button>
        <button type="button" class="tool-btn" data-tipo="SAIDA" draggable="true" onclick="selecionarTipo(this)" ondragstart="iniciarArrastoIcone(event,'SAIDA')"><span class="dot saida">${ic_equip_saida}</span>Saída</button>
        <button type="button" class="tool-btn" data-tipo="HIDRANTE" draggable="true" onclick="selecionarTipo(this)" ondragstart="iniciarArrastoIcone(event,'HIDRANTE')"><span class="dot hidrante">${ic_equip_hidrante}</span>Hidrante</button>
        <button type="button" class="tool-btn" data-tipo="ILUMINACAO" draggable="true" onclick="selecionarTipo(this)" ondragstart="iniciarArrastoIcone(event,'ILUMINACAO')"><span class="dot iluminacao">${ic_equip_iluminacao}</span>Iluminação</button>
        <button type="button" class="tool-btn" data-tipo="ALARME" draggable="true" onclick="selecionarTipo(this)" ondragstart="iniciarArrastoIcone(event,'ALARME')"><span class="dot alarme">${ic_equip_alarme}</span>Alarme</button>
        <button type="button" class="tool-btn" data-tipo="OBSERVACAO" draggable="true" onclick="selecionarTipo(this)" ondragstart="iniciarArrastoIcone(event,'OBSERVACAO')"><span class="dot observacao">${ic_equip_observacao}</span>Observação</button>
        <hr class="linha-divisoria-barra">
        <button type="button" class="tool-btn" id="botaoRota" onclick="ativarModoRota()"><span class="dot rota">${ic_equip_rota}</span>Rota fuga</button>
    </div>

    <div class="canvas-wrap" id="canvasWrap">
        <div class="painel-camadas" id="painelCamadas">
            <div class="painel-camadas-cabecalho" onclick="alternarPainelCamadas()">
                <h4>Camadas</h4>
                <button type="button" class="botao-minimizar-camadas" id="botaoMinimizarCamadas" aria-label="Minimizar camadas">&minus;</button>
            </div>
            <div class="painel-camadas-conteudo" id="painelCamadasConteudo">
                <label><input type="checkbox" checked onchange="alternarCamada('extintor',this)" class="largura-automatica"> Extintores</label>
                <label><input type="checkbox" checked onchange="alternarCamada('saida',this)" class="largura-automatica"> Saídas</label>
                <label><input type="checkbox" checked onchange="alternarCamada('hidrante',this)" class="largura-automatica"> Hidrantes</label>
                <label><input type="checkbox" checked onchange="alternarCamada('iluminacao',this)" class="largura-automatica"> Iluminação</label>
                <label><input type="checkbox" checked onchange="alternarCamada('alarme',this)" class="largura-automatica"> Alarmes</label>
                <label><input type="checkbox" checked id="caixaRotas" onchange="alternarRotas(this)" class="largura-automatica"> Rotas de fuga</label>
            </div>
        </div>

        <div id="areaComZoom" class="area-com-zoom">
            <div class="plan-img-frame" id="quadroPlanta" onclick="aoClicarNaPlanta(event)" ondragover="permitirSoltarIcone(event)" ondrop="aoSoltarIcone(event)">
                <c:choose>
                    <c:when test="${planta.imagem}">
                        <img src="${pageContext.request.contextPath}/uploads/${planta.arquivo}" id="planImg" draggable="false">
                    </c:when>
                    <c:otherwise>
                        <embed src="${pageContext.request.contextPath}/uploads/${planta.arquivo}#toolbar=0&navpanes=0&scrollbar=0" type="application/pdf" id="planImg" class="planta-tamanho-fixo">
                    </c:otherwise>
                </c:choose>

                <svg id="rotaOverlay" class="camada-rota" viewBox="0 0 100 100" preserveAspectRatio="none">
                    <defs>
                        <marker id="arrow" markerWidth="6" markerHeight="6" refX="3" refY="3" orient="auto"><path d="M0,0 L6,3 L0,6 z" fill="#c22a2a"/></marker>
                    </defs>
                    <c:forEach var="r" items="${rotas}">
                        <polyline class="rota-linha" points="${r.pontosSvg}" fill="none" stroke="#c22a2a" stroke-width="0.6"
                                  stroke-dasharray="2 1.5" marker-end="url(#arrow)" vector-effect="non-scaling-stroke"/>
                    </c:forEach>
                    <polyline id="previaRota" points="" fill="none" stroke="#c22a2a" stroke-width="0.6" stroke-dasharray="2 1.5" vector-effect="non-scaling-stroke"/>
                    <g id="pontosRotaPreview"></g>
                </svg>

                <c:forEach var="equip" items="${equipamentos}">
                    <a href="?id=${planta.id}&verId=${equip.id}" class="plan-marker ${equip.cssClasse}"
                       style="--mx:${equip.posX}%; --my:${equip.posY}%" onclick="event.stopPropagation()"
                       title="${equip.identificacao}">
                        <c:choose>
                            <c:when test="${equip.tipo == 'EXTINTOR'}">${ic_equip_extintor}</c:when>
                            <c:when test="${equip.tipo == 'SAIDA'}">${ic_equip_saida}</c:when>
                            <c:when test="${equip.tipo == 'HIDRANTE'}">${ic_equip_hidrante}</c:when>
                            <c:when test="${equip.tipo == 'ILUMINACAO'}">${ic_equip_iluminacao}</c:when>
                            <c:when test="${equip.tipo == 'ALARME'}">${ic_equip_alarme}</c:when>
                            <c:otherwise>${ic_equip_observacao}</c:otherwise>
                        </c:choose>
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>

    <!-- Painel lateral: detalhe/edição de equipamento existente, ou formulário de novo -->
    <aside class="side-form" id="sideForm">
        <c:choose>
            <c:when test="${not empty equipamentoSelecionado}">
                <h4>Editar equipamento — <span class="fonte-monoespacada">${equipamentoSelecionado.identificacao}</span></h4>
                <form method="post" action="${pageContext.request.contextPath}/equipamentos">
                    <input type="hidden" name="id" value="${equipamentoSelecionado.id}">
                    <input type="hidden" name="plantaId" value="${planta.id}">
                    <input type="hidden" name="tipo" value="${equipamentoSelecionado.tipo}">
                    <input type="hidden" name="posX" value="${equipamentoSelecionado.posX}">
                    <input type="hidden" name="posY" value="${equipamentoSelecionado.posY}">
                    <div class="field"><label>Identificação</label><input type="text" name="identificacao" value="${equipamentoSelecionado.identificacao}"></div>
                    <div class="field"><label>${equipamentoSelecionado.campo1Label}</label><input type="text" name="campo1Label" value="${equipamentoSelecionado.campo1Label}" hidden></div>
                    <div class="field"><label>Campo técnico 1 (ex: Tipo/Capacidade)</label><input type="text" name="campo1Valor" value="${equipamentoSelecionado.campo1Valor}"></div>
                    <div class="field"><label>Campo técnico 2 (ex: Largura/Diâmetro)</label><input type="text" name="campo2Label" value="${equipamentoSelecionado.campo2Label}" placeholder="rótulo" class="campo-compacto"></div>
                    <div class="field"><input type="text" name="campo2Valor" value="${equipamentoSelecionado.campo2Valor}" placeholder="valor"></div>
                    <div class="field"><label>Localização</label><input type="text" name="localizacao" value="${equipamentoSelecionado.localizacao}"></div>
                    <div class="field">
                        <label>Status</label>
                        <select name="status">
                            <option value="CONFORME" ${equipamentoSelecionado.status == 'CONFORME' ? 'selected' : ''}>Conforme</option>
                            <option value="NAO_CONFORME" ${equipamentoSelecionado.status == 'NAO_CONFORME' ? 'selected' : ''}>Não conforme</option>
                            <option value="NAO_APLICAVEL" ${equipamentoSelecionado.status == 'NAO_APLICAVEL' ? 'selected' : ''}>Não aplicável</option>
                        </select>
                    </div>
                    <div class="field"><label>Observações</label><textarea name="observacoes" rows="3" class="caixa-texto-larga">${equipamentoSelecionado.observacoes}</textarea></div>
                    <div class="flex-espaco-medio">
                        <button type="submit" class="btn-accent botao-expandido">Salvar</button>
                    </div>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/equipamentos" onsubmit="return confirm('Excluir este equipamento?');" class="formulario-espacado">
                    <input type="hidden" name="acao" value="excluir">
                    <input type="hidden" name="id" value="${equipamentoSelecionado.id}">
                    <input type="hidden" name="plantaId" value="${planta.id}">
                    <button type="submit" class="btn-danger-ghost botao-largura-total">Excluir equipamento</button>
                </form>
                <a class="link-action link-em-bloco" href="${pageContext.request.contextPath}/plantas?acao=visualizar&id=${planta.id}">Fechar</a>
            </c:when>
            <c:otherwise>
                <h4>Novo equipamento</h4>
                <div class="hint-box" id="caixaDica">Selecione um tipo na barra à esquerda e clique na planta para posicionar.</div>
                <form method="post" action="${pageContext.request.contextPath}/equipamentos" id="formNovoEquip">
                    <input type="hidden" name="plantaId" value="${planta.id}">
                    <input type="hidden" name="tipo" id="campoTipo" value="EXTINTOR">
                    <input type="hidden" name="posX" id="campoPosX">
                    <input type="hidden" name="posY" id="campoPosY">
                    <div class="field"><label>Identificação *</label><input type="text" name="identificacao" id="campoIdentificacao" placeholder="EXT-03" required></div>
                    <div class="field"><label>Campo técnico 1 — rótulo</label><input type="text" name="campo1Label" id="campoRotulo1" placeholder="Tipo"></div>
                    <div class="field"><label>Campo técnico 1 — valor</label><input type="text" name="campo1Valor" placeholder="ABC"></div>
                    <div class="field"><label>Campo técnico 2 — rótulo</label><input type="text" name="campo2Label" id="campoRotulo2" placeholder="Capacidade"></div>
                    <div class="field"><label>Campo técnico 2 — valor</label><input type="text" name="campo2Valor" placeholder="6 kg"></div>
                    <div class="field"><label>Localização</label><input type="text" name="localizacao" placeholder="Próximo à circulação principal"></div>
                    <div class="field">
                        <label>Status</label>
                        <select name="status"><option value="CONFORME">Conforme</option><option value="NAO_CONFORME">Não conforme</option><option value="NAO_APLICAVEL">Não aplicável</option></select>
                    </div>
                    <div class="field"><label>Observações</label><textarea name="observacoes" rows="3" class="caixa-texto-larga"></textarea></div>
                    <button type="submit" class="btn-accent botao-largura-total" id="botaoSalvarEquipamento" disabled>Clique na planta primeiro</button>
                </form>

                <hr class="linha-divisoria-painel">

                <div id="caixaRota" class="oculto">
                    <h4>Nova rota de fuga</h4>
                    <div class="hint-box">Clique vários pontos na planta, do início ao fim da rota. <span id="contadorPontos">0</span> ponto(s) marcado(s).</div>
                    <form method="post" action="${pageContext.request.contextPath}/rotas">
                        <input type="hidden" name="plantaId" value="${planta.id}">
                        <input type="hidden" name="pontos" id="campoPontos">
                        <div class="field"><label>Origem</label><input type="text" name="origem" placeholder="Sala 03"></div>
                        <div class="field"><label>Destino</label><input type="text" name="destino" placeholder="Saída S-01"></div>
                        <button type="submit" class="btn-accent botao-largura-total" id="botaoSalvarRota" disabled>Marque ao menos 2 pontos</button>
                    </form>
                    <button type="button" class="btn-ghost botao-largura-total-espacado" onclick="cancelarRota()">Cancelar rota</button>
                </div>
            </c:otherwise>
        </c:choose>
    </aside>
</div>

<script>
    let tipoAtual = 'EXTINTOR';
    let modoRotaAtivo = false;
    let pontosDaRota = [];
    let houveArrastoComMovimento = false;

    const rotulosPorTipo = {
        EXTINTOR: ['Tipo', 'Capacidade'], SAIDA: ['Tipo', 'Largura'],
        HIDRANTE: ['Diâmetro', 'Pressão'], ILUMINACAO: ['Tipo', 'Autonomia'],
        ALARME: ['Tipo', 'Zona'], OBSERVACAO: ['Categoria', '']
    };

    function selecionarTipo(botaoClicado){
        document.querySelectorAll('.tool-btn').forEach(botao=>botao.classList.remove('active'));
        botaoClicado.classList.add('active');
        tipoAtual = botaoClicado.dataset.tipo;
        modoRotaAtivo = false;
        document.getElementById('caixaRota') && document.getElementById('caixaRota').classList.add('oculto');
        const campoTipo = document.getElementById('campoTipo');
        if (campoTipo) campoTipo.value = tipoAtual;
        const rotulos = rotulosPorTipo[tipoAtual] || ['Campo 1','Campo 2'];
        const campoRotulo1 = document.getElementById('campoRotulo1'), campoRotulo2 = document.getElementById('campoRotulo2');
        if (campoRotulo1) campoRotulo1.value = rotulos[0]; if (campoRotulo2) campoRotulo2.value = rotulos[1];
    }

    function ativarModoRota(){
        document.querySelectorAll('.tool-btn').forEach(botao=>botao.classList.remove('active'));
        document.getElementById('botaoRota').classList.add('active');
        modoRotaAtivo = true;
        pontosDaRota = [];
        atualizarPreviaRota();
        const caixaRota = document.getElementById('caixaRota');
        if (caixaRota) caixaRota.classList.remove('oculto');
    }

    function cancelarRota(){
        modoRotaAtivo = false;
        pontosDaRota = [];
        atualizarPreviaRota();
        document.getElementById('caixaRota').classList.add('oculto');
    }

    function iniciarArrastoIcone(evento, tipo){
        evento.dataTransfer.setData('text/tipo-equipamento', tipo);
        evento.dataTransfer.effectAllowed = 'copy';
    }

    function permitirSoltarIcone(evento){
        if (evento.dataTransfer.types.includes('text/tipo-equipamento')) {
            evento.preventDefault();
            evento.dataTransfer.dropEffect = 'copy';
        }
    }

    function aoSoltarIcone(evento){
        const tipo = evento.dataTransfer.getData('text/tipo-equipamento');
        if (!tipo) return;
        evento.preventDefault();

        const botaoDoTipo = document.querySelector('.tool-btn[data-tipo="' + tipo + '"]');
        if (botaoDoTipo) selecionarTipo(botaoDoTipo);

        const quadroDaPlanta = document.getElementById('quadroPlanta');
        const retangulo = quadroDaPlanta.getBoundingClientRect();
        const posX = ((evento.clientX - retangulo.left) / retangulo.width) * 100;
        const posY = ((evento.clientY - retangulo.top) / retangulo.height) * 100;

        const campoPosX = document.getElementById('campoPosX');
        if (!campoPosX) return;
        campoPosX.value = posX.toFixed(2);
        document.getElementById('campoPosY').value = posY.toFixed(2);
        document.getElementById('botaoSalvarEquipamento').disabled = false;
        document.getElementById('botaoSalvarEquipamento').textContent = 'Salvar equipamento';
        document.getElementById('caixaDica').textContent = 'Posição marcada! Preencha os dados e salve.';
    }

    function aoClicarNaPlanta(evento){
        if (houveArrastoComMovimento) { houveArrastoComMovimento = false; return; }
        const quadroDaPlanta = document.getElementById('quadroPlanta');
        const retangulo = quadroDaPlanta.getBoundingClientRect();
        const posX = ((evento.clientX - retangulo.left) / retangulo.width) * 100;
        const posY = ((evento.clientY - retangulo.top) / retangulo.height) * 100;

        if (modoRotaAtivo) {
            pontosDaRota.push(posX.toFixed(2) + ',' + posY.toFixed(2));
            atualizarPreviaRota();
            const contadorPontos = document.getElementById('contadorPontos');
            if (contadorPontos) contadorPontos.textContent = pontosDaRota.length;
            const botaoSalvarRota = document.getElementById('botaoSalvarRota');
            if (botaoSalvarRota) {
                botaoSalvarRota.disabled = pontosDaRota.length < 2;
                botaoSalvarRota.textContent = pontosDaRota.length < 2 ? 'Marque ao menos 2 pontos' : 'Salvar rota de fuga';
            }
            document.getElementById('campoPontos').value = pontosDaRota.join(';');
            return;
        }

        const campoPosX = document.getElementById('campoPosX');
        if (!campoPosX) return;
        campoPosX.value = posX.toFixed(2);
        document.getElementById('campoPosY').value = posY.toFixed(2);
        document.getElementById('botaoSalvarEquipamento').disabled = false;
        document.getElementById('botaoSalvarEquipamento').textContent = 'Salvar equipamento';
        document.getElementById('caixaDica').textContent = 'Posição marcada! Preencha os dados e salve.';
    }

    function atualizarPreviaRota(){
        const pontosSvg = pontosDaRota.join(' ');
        const previaRota = document.getElementById('previaRota');
        if (previaRota) previaRota.setAttribute('points', pontosSvg);

        const grupoPontos = document.getElementById('pontosRotaPreview');
        if (grupoPontos) {
            grupoPontos.innerHTML = '';
            pontosDaRota.forEach(function(ponto){
                const partes = ponto.split(',');
                const circulo = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
                circulo.setAttribute('cx', partes[0]);
                circulo.setAttribute('cy', partes[1]);
                circulo.setAttribute('r', '1.1');
                circulo.setAttribute('vector-effect', 'non-scaling-stroke');
                circulo.setAttribute('class', 'ponto-rota-preview');
                grupoPontos.appendChild(circulo);
            });
        }
    }

    function alternarPainelCamadas(){
        const painel = document.getElementById('painelCamadas');
        const botao = document.getElementById('botaoMinimizarCamadas');
        const recolhido = painel.classList.toggle('recolhido');
        botao.innerHTML = recolhido ? '+' : '&minus;';
        botao.setAttribute('aria-label', recolhido ? 'Expandir camadas' : 'Minimizar camadas');
    }

    function alternarCamada(tipo, caixaSelecao){
        document.querySelectorAll('.plan-marker.' + tipo).forEach(marcador=>{
            marcador.style.display = caixaSelecao.checked ? 'flex' : 'none';
        });
    }
    function alternarRotas(caixaSelecao){
        document.querySelectorAll('.rota-linha').forEach(linha=>{
            linha.style.display = caixaSelecao.checked ? '' : 'none';
        });
    }

    let nivelZoom = 1;

    function aplicarTransformZoom(){
        document.getElementById('areaComZoom').style.transform = 'scale(' + nivelZoom + ')';
        const indicador = document.getElementById('indicadorZoom');
        if (indicador) indicador.textContent = Math.round(nivelZoom * 100) + '%';
    }

    function aplicarZoom(variacao){
        nivelZoom = Math.max(0.15, Math.min(5, nivelZoom + variacao));
        aplicarTransformZoom();
    }

    /** Calcula a escala que mostra a planta inteira dentro da área visível e a aplica. */
    function ajustarZoom(){
        const canvasWrap = document.getElementById('canvasWrap');
        const planImg = document.getElementById('planImg');
        const folga = 60;
        const larguraNatural = planImg.naturalWidth || planImg.offsetWidth || 760;
        const alturaNatural = planImg.naturalHeight || planImg.offsetHeight || 520;
        const escalaLargura = (canvasWrap.clientWidth - folga) / larguraNatural;
        const escalaAltura = (canvasWrap.clientHeight - folga) / alturaNatural;
        const escalaCalculada = Math.min(escalaLargura, escalaAltura);
        nivelZoom = escalaCalculada > 0 ? Math.min(escalaCalculada, 3) : 1;
        aplicarTransformZoom();
    }

    /** Zoom com a roda do mouse (segurando Ctrl), centrado onde o usuário rolar. */
    document.getElementById('canvasWrap').addEventListener('wheel', function(evento){
        if (!evento.ctrlKey) return;
        evento.preventDefault();
        aplicarZoom(evento.deltaY < 0 ? 0.15 : -0.15);
    }, { passive: false });

    /** Arrastar para navegar pela planta quando ela estiver maior que a área visível. */
    (function habilitarArrastarParaNavegar(){
        const canvasWrap = document.getElementById('canvasWrap');
        let arrastando = false, ultimoX = 0, ultimoY = 0;
        canvasWrap.addEventListener('mousedown', function(evento){
            if (evento.target.closest('.plan-marker') || modoRotaAtivo) return;
            arrastando = true;
            houveArrastoComMovimento = false;
            ultimoX = evento.clientX; ultimoY = evento.clientY;
            canvasWrap.classList.add('modo-pan');
        });
        window.addEventListener('mousemove', function(evento){
            if (!arrastando) return;
            const deltaX = evento.clientX - ultimoX, deltaY = evento.clientY - ultimoY;
            if (Math.abs(deltaX) + Math.abs(deltaY) > 3) houveArrastoComMovimento = true;
            canvasWrap.scrollLeft -= deltaX;
            canvasWrap.scrollTop -= deltaY;
            ultimoX = evento.clientX; ultimoY = evento.clientY;
        });
        window.addEventListener('mouseup', function(){
            arrastando = false;
            canvasWrap.classList.remove('modo-pan');
        });
    })();

    selecionarTipo(document.querySelector('.tool-btn.active'));

    const planImgEl = document.getElementById('planImg');
    if (planImgEl.tagName === 'IMG' && !planImgEl.complete) {
        planImgEl.addEventListener('load', ajustarZoom);
    } else {
        ajustarZoom();
    }
    window.addEventListener('resize', ajustarZoom);
</script>

</body>
</html>
