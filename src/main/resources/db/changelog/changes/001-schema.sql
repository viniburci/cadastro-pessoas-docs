--liquibase formatted sql

--changeset doban:001-emails-permitidos
CREATE TABLE emails_permitidos (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    role        VARCHAR(50)  NOT NULL,
    created_at  TIMESTAMP,
    created_by  VARCHAR(255)
);

--changeset doban:002-usuarios
CREATE TABLE usuarios (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    nome        VARCHAR(255),
    picture_url VARCHAR(255),
    role        VARCHAR(50)  NOT NULL,
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP    NOT NULL,
    last_login  TIMESTAMP
);

--changeset doban:003-refresh-tokens
CREATE TABLE refresh_tokens (
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    usuario_id  BIGINT       NOT NULL REFERENCES usuarios(id),
    expiry_date TIMESTAMP    NOT NULL
);

--changeset doban:004-clientes
CREATE TABLE clientes (
    id         BIGSERIAL PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL UNIQUE,
    descricao  VARCHAR(255),
    ativo      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

--changeset doban:005-tipos-vaga
CREATE TABLE tipos_vaga (
    id               BIGSERIAL PRIMARY KEY,
    codigo           VARCHAR(50)  NOT NULL UNIQUE,
    nome             VARCHAR(100) NOT NULL,
    descricao        VARCHAR(255),
    ativo            BOOLEAN      NOT NULL DEFAULT TRUE,
    schema_json      TEXT,
    itens_padrao_json TEXT,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP
);

--changeset doban:006-pessoas
CREATE TABLE pessoas (
    id                  BIGSERIAL PRIMARY KEY,
    nome                VARCHAR(255),
    endereco            VARCHAR(255),
    complemento         VARCHAR(255),
    bairro              VARCHAR(255),
    cidade              VARCHAR(255),
    estado              VARCHAR(255),
    cep                 VARCHAR(255),
    telefone            VARCHAR(255),
    email               VARCHAR(255),
    numero_ctps         VARCHAR(255),
    serie_ctps          VARCHAR(255),
    data_emissao_ctps   DATE,
    numero_rg           VARCHAR(255),
    data_emissao_rg     DATE,
    uf_rg               VARCHAR(255),
    cpf                 VARCHAR(255),
    pis                 VARCHAR(255),
    data_emissao_pis    DATE,
    titulo_eleitor      VARCHAR(255),
    data_nascimento     DATE,
    local_nascimento    VARCHAR(255),
    mae                 VARCHAR(255),
    pai                 VARCHAR(255),
    estado_civil        VARCHAR(255),
    categoria_cnh       VARCHAR(255),
    validade_cnh        DATE,
    numero_cnh          VARCHAR(255),
    registro_cnh        VARCHAR(255),
    chave_pix           VARCHAR(255),
    tamanho_camisa      VARCHAR(255),
    tamanho_calca       VARCHAR(255),
    tamanho_calcado     VARCHAR(255),
    tamanho_luva        VARCHAR(255),
    tamanho_capacete    VARCHAR(255),
    foto                BYTEA
);

--changeset doban:007-dados-bancarios
CREATE TABLE dados_bancarios (
    id         BIGSERIAL PRIMARY KEY,
    pessoa_id  BIGINT      NOT NULL UNIQUE REFERENCES pessoas(id),
    banco      VARCHAR(100),
    agencia    VARCHAR(20),
    conta      VARCHAR(30),
    tipo_conta VARCHAR(20),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

--changeset doban:008-vagas
CREATE TABLE vagas (
    id                       BIGSERIAL PRIMARY KEY,
    pessoa_id                BIGINT         NOT NULL REFERENCES pessoas(id),
    cliente_id               BIGINT         REFERENCES clientes(id),
    tipo_vaga_id             BIGINT         REFERENCES tipos_vaga(id),
    cidade                   VARCHAR(255),
    uf                       VARCHAR(255),
    salario                  NUMERIC(10,2),
    tipo_contrato            VARCHAR(50),
    data_admissao            DATE,
    data_demissao            DATE,
    acrescimo_ou_substituicao VARCHAR(50),
    aso                      VARCHAR(50),
    optantevt                BOOLEAN,
    horario_entrada          TIME,
    horario_saida            TIME,
    contratante              VARCHAR(100),
    atributos_dinamicos_json TEXT
);

--changeset doban:009-tipos-recurso
CREATE TABLE tipos_recurso (
    id          BIGSERIAL PRIMARY KEY,
    codigo      VARCHAR(50)  NOT NULL UNIQUE,
    nome        VARCHAR(100) NOT NULL,
    descricao   VARCHAR(255),
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    legado      BOOLEAN      NOT NULL DEFAULT FALSE,
    schema_json TEXT,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

--changeset doban:010-itens-dinamicos
CREATE TABLE itens_dinamicos (
    id              BIGSERIAL PRIMARY KEY,
    tipo_recurso_id BIGINT       NOT NULL REFERENCES tipos_recurso(id),
    identificador   VARCHAR(255) NOT NULL,
    atributos_json  TEXT,
    ativo           BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP,
    updated_at      TIMESTAMP,
    CONSTRAINT uq_tipo_recurso_identificador UNIQUE (tipo_recurso_id, identificador)
);

--changeset doban:011-recursos-dinamicos
CREATE TABLE recursos_dinamicos (
    id                     BIGSERIAL PRIMARY KEY,
    pessoa_id              BIGINT NOT NULL REFERENCES pessoas(id),
    item_id                BIGINT NOT NULL REFERENCES itens_dinamicos(id),
    data_entrega           DATE,
    data_devolucao         DATE,
    atributos_snapshot_json TEXT,
    itens_extras_json      TEXT,
    created_at             TIMESTAMP,
    updated_at             TIMESTAMP
);

--changeset doban:012-recursos
CREATE TABLE recursos (
    id             BIGSERIAL PRIMARY KEY,
    pessoa_id      BIGINT NOT NULL REFERENCES pessoas(id),
    data_entrega   DATE,
    data_devolucao DATE
);

--changeset doban:013-templates-documento
CREATE TABLE templates_documento (
    id                      BIGSERIAL PRIMARY KEY,
    codigo                  VARCHAR(50)  NOT NULL UNIQUE,
    nome                    VARCHAR(100) NOT NULL,
    descricao               VARCHAR(255),
    conteudo_html           TEXT         NOT NULL,
    schema_itens_json       TEXT,
    variaveis_disponiveis_json TEXT,
    ativo                   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP,
    updated_at              TIMESTAMP
);

--changeset doban:014-template-documento-tipo-vaga
CREATE TABLE template_documento_tipo_vaga (
    template_documento_id BIGINT NOT NULL REFERENCES templates_documento(id),
    tipo_vaga_id          BIGINT NOT NULL REFERENCES tipos_vaga(id),
    PRIMARY KEY (template_documento_id, tipo_vaga_id)
);
