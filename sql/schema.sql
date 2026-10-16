-- =====================================================================
-- VistorIA — Sistema de Vistorias de Segurança Contra Incêndio
-- Schema completo do banco de dados (MySQL)
-- =====================================================================

SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS vistoria_ia CHARACTER SET utf8mb4;
USE vistoria_ia;

DROP TABLE IF EXISTS foto;
DROP TABLE IF EXISTS item_vistoria;
DROP TABLE IF EXISTS vistoria;
DROP TABLE IF EXISTS rota;
DROP TABLE IF EXISTS equipamento;
DROP TABLE IF EXISTS planta;
DROP TABLE IF EXISTS edificacao;
DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(120)  NOT NULL,
    email           VARCHAR(150)  NOT NULL UNIQUE,
    senha_hash      VARCHAR(64)   NOT NULL,
    telefone        VARCHAR(20),
    identificacao   VARCHAR(30),
    data_cadastro   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE edificacao (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id          INT NOT NULL,
    nome                VARCHAR(150) NOT NULL,
    razao_social        VARCHAR(150),
    cnpj                VARCHAR(20),
    tipo_ocupacao       VARCHAR(100),
    endereco            VARCHAR(150),
    numero              VARCHAR(20),
    bairro              VARCHAR(100),
    cidade              VARCHAR(100),
    estado              VARCHAR(2),
    cep                 VARCHAR(10),
    area_construida     DECIMAL(10,2),
    pavimentos          INT,
    altura              DECIMAL(6,2),
    ocupantes           INT,
    tipo_edificacao     VARCHAR(100),
    uso_edificacao      VARCHAR(100),
    area_pavimento      DECIMAL(10,2),
    qtd_blocos          INT DEFAULT 1,
    tem_subsolo         BOOLEAN DEFAULT FALSE,
    tem_mezanino        BOOLEAN DEFAULT FALSE,
    data_cadastro       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_edificacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE planta (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    edificacao_id   INT NOT NULL,
    nome            VARCHAR(150) NOT NULL,
    pavimento       VARCHAR(80) NOT NULL,
    arquivo         VARCHAR(255) NOT NULL,
    data_upload     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_planta_edificacao FOREIGN KEY (edificacao_id) REFERENCES edificacao(id)
);

CREATE TABLE equipamento (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    planta_id       INT NOT NULL,
    tipo            VARCHAR(20) NOT NULL,
    pos_x           DECIMAL(6,2) NOT NULL,
    pos_y           DECIMAL(6,2) NOT NULL,
    identificacao   VARCHAR(30) NOT NULL,
    campo1_label    VARCHAR(40),
    campo1_valor    VARCHAR(100),
    campo2_label    VARCHAR(40),
    campo2_valor    VARCHAR(100),
    localizacao     VARCHAR(150),
    status          VARCHAR(20) NOT NULL DEFAULT 'CONFORME',
    observacoes     TEXT,
    CONSTRAINT fk_equipamento_planta FOREIGN KEY (planta_id) REFERENCES planta(id)
);

CREATE TABLE rota (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    planta_id   INT NOT NULL,
    pontos      TEXT NOT NULL,
    origem      VARCHAR(100),
    destino     VARCHAR(100),
    CONSTRAINT fk_rota_planta FOREIGN KEY (planta_id) REFERENCES planta(id)
);

CREATE TABLE vistoria (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    edificacao_id   INT NOT NULL,
    usuario_id      INT NOT NULL,
    planta_id       INT,
    data_vistoria   DATE NOT NULL,
    tipo            VARCHAR(40) NOT NULL DEFAULT 'PERIODICA',
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    observacoes     TEXT,
    CONSTRAINT fk_vistoria_edificacao FOREIGN KEY (edificacao_id) REFERENCES edificacao(id),
    CONSTRAINT fk_vistoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_vistoria_planta FOREIGN KEY (planta_id) REFERENCES planta(id)
);

CREATE TABLE item_vistoria (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    vistoria_id     INT NOT NULL,
    categoria       VARCHAR(60) NOT NULL,
    item            VARCHAR(150) NOT NULL,
    status          VARCHAR(20) NOT NULL,
    gravidade       VARCHAR(20),
    observacao      TEXT,
    CONSTRAINT fk_item_vistoria FOREIGN KEY (vistoria_id) REFERENCES vistoria(id)
);

CREATE TABLE foto (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    vistoria_id     INT NOT NULL,
    arquivo         VARCHAR(255) NOT NULL,
    descricao       VARCHAR(255),
    localizacao     VARCHAR(150),
    data_upload     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_foto_vistoria FOREIGN KEY (vistoria_id) REFERENCES vistoria(id)
);

-- Usuário demo: demo@vistoria-ia.com / 123456
INSERT INTO usuario (nome, email, senha_hash, telefone, identificacao) VALUES
('Renata Torres', 'demo@vistoria-ia.com',
 '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92',
 '(47) 99999-0000', 'CREA-SC 123456');

-- Edificação de demonstração (pertence ao usuário demo)
INSERT INTO edificacao (usuario_id, nome, razao_social, cnpj, tipo_ocupacao, endereco, numero, bairro,
                        cidade, estado, cep, area_construida, pavimentos, altura, ocupantes,
                        tipo_edificacao, uso_edificacao, area_pavimento, qtd_blocos, tem_subsolo, tem_mezanino)
VALUES ((SELECT id FROM usuario WHERE email = 'demo@vistoria-ia.com'),
        'Edifício Comercial Aurora', 'Aurora Empreendimentos Ltda', '12.345.678/0001-90', 'Comercial',
        'Rua XV de Novembro', '1200', 'Centro', 'Joinville', 'SC', '89201-001',
        1850.00, 3, 11.50, 120, 'Vertical', 'Comercial e serviços', 620.00, 1, FALSE, TRUE);

-- Planta de demonstração (imagem em src/main/webapp/uploads/planta-demo-terreo.png)
INSERT INTO planta (edificacao_id, nome, pavimento, arquivo)
VALUES ((SELECT id FROM edificacao WHERE nome = 'Edifício Comercial Aurora'),
        'Planta baixa - Térreo', 'Térreo', 'planta-demo-terreo.png');
SET @edificacao_demo = (SELECT id FROM edificacao WHERE nome = 'Edifício Comercial Aurora');
SET @planta_demo = LAST_INSERT_ID();

-- Equipamentos de demonstração (pos_x / pos_y em % da imagem da planta)
INSERT INTO equipamento (planta_id, tipo, pos_x, pos_y, identificacao, campo1_label, campo1_valor,
                         campo2_label, campo2_valor, localizacao, status, observacoes) VALUES
(@planta_demo, 'EXTINTOR',   31.67, 40.00, 'EXT-01', 'Tipo', 'PQS ABC',        'Capacidade', '6 kg',    'Recepção',                 'CONFORME',     NULL),
(@planta_demo, 'EXTINTOR',   50.00, 42.50, 'EXT-02', 'Tipo', 'CO2',            'Capacidade', '6 kg',    'Corredor, em frente à Sala 102', 'NAO_CONFORME', 'Acesso parcialmente obstruído por caixas de papelão.'),
(@planta_demo, 'EXTINTOR',   83.33, 52.50, 'EXT-03', 'Tipo', 'Água pressurizada', 'Capacidade', '10 L', 'Corredor, junto à escada',  'NAO_CONFORME', 'Carga vencida em 03/2026; lacre rompido.'),
(@planta_demo, 'HIDRANTE',   75.00, 47.50, 'HID-01', 'Diâmetro', '63 mm',      'Pressão',    '4 kgf/cm²', 'Corredor, próximo à escada', 'NAO_CONFORME', 'Gotejamento no registro de manobra.'),
(@planta_demo, 'SAIDA',       6.67, 25.00, 'SAI-01', 'Tipo', 'Porta de entrada', 'Largura',   '1,60 m',  'Recepção',                 'CONFORME',     NULL),
(@planta_demo, 'SAIDA',      85.83, 58.75, 'SAI-02', 'Tipo', 'Porta corta-fogo', 'Largura',  '1,20 m',  'Acesso à escada',          'NAO_CONFORME', 'Porta abre no sentido contrário ao fluxo de fuga.'),
(@planta_demo, 'ILUMINACAO', 40.00, 41.25, 'ILU-01', 'Tipo', 'Bloco autônomo', 'Autonomia',  '3 h',     'Corredor',                 'CONFORME',     NULL),
(@planta_demo, 'ILUMINACAO', 71.67, 41.25, 'ILU-02', 'Tipo', 'Bloco autônomo', 'Autonomia',  '1 h',     'Corredor',                 'NAO_CONFORME', 'Autonomia da bateria abaixo do mínimo de 3 h.'),
(@planta_demo, 'ALARME',     13.33, 42.50, 'ALM-01', 'Tipo', 'Acionador manual', 'Zona',     '1',       'Recepção',                 'CONFORME',     NULL);

-- Vistoria de demonstração (itens conforme o checklist do sistema)
INSERT INTO vistoria (edificacao_id, usuario_id, planta_id, data_vistoria, tipo, status, observacoes)
VALUES (@edificacao_demo, (SELECT id FROM usuario WHERE email = 'demo@vistoria-ia.com'), @planta_demo,
        '2026-09-15', 'PERIODICA', 'CONCLUIDA',
        'Vistoria periódica do pavimento térreo. Sete não conformidades identificadas, três de gravidade alta.');
SET @vistoria_demo = LAST_INSERT_ID();

INSERT INTO item_vistoria (vistoria_id, categoria, item, status, gravidade, observacao) VALUES
(@vistoria_demo, 'Extintores', 'Quantidade adequada', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Extintores', 'Localização adequada', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Extintores', 'Sinalização', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Extintores', 'Acesso desobstruído', 'NAO_CONFORME', 'MEDIA', 'EXT-02 com acesso parcialmente obstruído por caixas de papelão.'),
(@vistoria_demo, 'Extintores', 'Validade', 'NAO_CONFORME', 'ALTA', 'EXT-03 com carga vencida em 03/2026.'),
(@vistoria_demo, 'Extintores', 'Lacre', 'NAO_CONFORME', 'ALTA', 'EXT-03 com lacre rompido.'),
(@vistoria_demo, 'Extintores', 'Manômetro', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Saídas de emergência', 'Sinalização fotoluminescente', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Saídas de emergência', 'Largura adequada', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Saídas de emergência', 'Desobstrução', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Saídas de emergência', 'Sentido de abertura da porta', 'NAO_CONFORME', 'ALTA', 'Porta de acesso à escada (SAI-02) abre contra o sentido de fuga.'),
(@vistoria_demo, 'Saídas de emergência', 'Iluminação de emergência próxima', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Rotas de fuga', 'Sinalização ao longo do trajeto', 'NAO_CONFORME', 'BAIXA', 'Falta placa direcional junto à Sala 103.'),
(@vistoria_demo, 'Rotas de fuga', 'Largura mínima respeitada', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Rotas de fuga', 'Ausência de obstáculos', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Rotas de fuga', 'Piso antiderrapante', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Hidrantes', 'Mangueira em bom estado', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Hidrantes', 'Pressão adequada', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Hidrantes', 'Abrigo sinalizado', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Hidrantes', 'Registro sem vazamentos', 'NAO_CONFORME', 'MEDIA', 'HID-01 com gotejamento no registro de manobra.'),
(@vistoria_demo, 'Iluminação de emergência', 'Acionamento automático', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Iluminação de emergência', 'Autonomia da bateria', 'NAO_CONFORME', 'MEDIA', 'ILU-02 com autonomia de apenas 1 h.'),
(@vistoria_demo, 'Iluminação de emergência', 'Blocos sem danos', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Alarmes', 'Acionadores manuais acessíveis', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Alarmes', 'Central de alarme operante', 'CONFORME', NULL, NULL),
(@vistoria_demo, 'Alarmes', 'Sirenes audíveis em todo o pavimento', 'CONFORME', NULL, NULL);
