# Concession-ria
Uma concessionária do setor elétrico precisa desenvolver uma API REST para gerenciar solicitações feitas por consumidores.


Banco:

CREATE TABLE consumidor (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL
);

CREATE TABLE funcionario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    perfil VARCHAR(20) NOT NULL,
    area VARCHAR(100) NOT NULL
);

CREATE TABLE solicitacao (
    id BIGSERIAL PRIMARY KEY,
    data_abertura DATE NOT NULL DEFAULT CURRENT_DATE,
    tipo VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ABERTA',
    consumidor_id BIGINT NOT NULL REFERENCES consumidor(id),
    funcionario_responsavel_id BIGINT REFERENCES funcionario(id),
    data_conclusao DATE,
    resposta_final TEXT
);

CREATE TABLE analise (
    id BIGSERIAL PRIMARY KEY,
    solicitacao_id BIGINT NOT NULL REFERENCES solicitacao(id),
    funcionario_id BIGINT NOT NULL REFERENCES funcionario(id),
    data_analise DATE NOT NULL DEFAULT CURRENT_DATE,
    parecer TEXT NOT NULL,
    novo_valor_kwh_solicitado NUMERIC(10, 2)
);

CREATE TABLE meta_consumo (
    id BIGSERIAL PRIMARY KEY,
    consumidor_id BIGINT NOT NULL REFERENCES consumidor(id),
    mes INTEGER NOT NULL,
    ano INTEGER NOT NULL,
    valor_kwh NUMERIC(10, 2) NOT NULL,
    UNIQUE (consumidor_id, mes, ano)
);