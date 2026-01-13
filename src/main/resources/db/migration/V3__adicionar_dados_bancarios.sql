-- Migração V3: Adicionar Dados Bancários
-- Cria tabela para armazenar informações bancárias das pessoas

CREATE TABLE dados_bancarios (
    id BIGSERIAL PRIMARY KEY,
    pessoa_id BIGINT NOT NULL UNIQUE,
    banco VARCHAR(100),
    agencia VARCHAR(20),
    conta VARCHAR(30),
    tipo_conta VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pessoa_id) REFERENCES pessoas(id) ON DELETE CASCADE
);

-- Índice para otimizar buscas por pessoa
CREATE INDEX idx_dados_bancarios_pessoa ON dados_bancarios(pessoa_id);

-- Comentários para documentação
COMMENT ON TABLE dados_bancarios IS 'Dados bancários das pessoas para pagamentos';
COMMENT ON COLUMN dados_bancarios.banco IS 'Nome do banco (ex: ITAU, BRADESCO, CAIXA)';
COMMENT ON COLUMN dados_bancarios.agencia IS 'Número da agência bancária';
COMMENT ON COLUMN dados_bancarios.conta IS 'Número da conta bancária';
COMMENT ON COLUMN dados_bancarios.tipo_conta IS 'Tipo de conta: CORRENTE, POUPANCA ou SALARIO';
