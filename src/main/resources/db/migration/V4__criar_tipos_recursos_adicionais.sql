-- Migração V4: Criar Tipos de Recursos Adicionais
-- Adiciona tipos de recursos para Uniformes, EPIs e Vale Transporte

-- ===========================================
-- UNIFORME
-- ===========================================
INSERT INTO tipos_recurso (codigo, nome, descricao, ativo, legado, schema, created_at, updated_at)
VALUES (
    'UNIFORME',
    'Uniforme',
    'Uniformes e vestimentas de trabalho',
    true,
    false,
    '{
        "fields": [
            {
                "nome": "tipo",
                "rotulo": "Tipo",
                "tipo": "ENUM",
                "obrigatorio": true,
                "opcoes": ["CAMISA", "CALCA", "BOTA", "COLETE", "BONE", "JAQUETA", "CAPA_CHUVA"]
            },
            {
                "nome": "tamanho",
                "rotulo": "Tamanho",
                "tipo": "STRING",
                "obrigatorio": true,
                "tamanhoMaximo": 10
            },
            {
                "nome": "quantidade",
                "rotulo": "Quantidade",
                "tipo": "INTEGER",
                "obrigatorio": true,
                "valorMinimo": 1,
                "valorMaximo": 100
            },
            {
                "nome": "cor",
                "rotulo": "Cor",
                "tipo": "STRING",
                "obrigatorio": false,
                "tamanhoMaximo": 50
            }
        ]
    }'::jsonb,
    NOW(),
    NOW()
) ON CONFLICT (codigo) DO NOTHING;

-- ===========================================
-- EPI (Equipamento de Proteção Individual)
-- ===========================================
INSERT INTO tipos_recurso (codigo, nome, descricao, ativo, legado, schema, created_at, updated_at)
VALUES (
    'EPI',
    'EPI (Equipamento de Proteção Individual)',
    'Equipamentos de segurança do trabalho',
    true,
    false,
    '{
        "fields": [
            {
                "nome": "tipo",
                "rotulo": "Tipo",
                "tipo": "ENUM",
                "obrigatorio": true,
                "opcoes": [
                    "CAPACETE",
                    "OCULOS_PROTECAO",
                    "LUVA",
                    "BOTA_SEGURANCA",
                    "PROTETOR_AURICULAR",
                    "MASCARA",
                    "CINTO_SEGURANCA",
                    "COLETE_REFLETIVO"
                ]
            },
            {
                "nome": "ca",
                "rotulo": "CA (Certificado de Aprovação)",
                "tipo": "STRING",
                "obrigatorio": false,
                "tamanhoMaximo": 20,
                "mensagemErro": "CA deve ter no máximo 20 caracteres"
            },
            {
                "nome": "validade",
                "rotulo": "Data de Validade",
                "tipo": "DATE",
                "obrigatorio": false
            },
            {
                "nome": "fabricante",
                "rotulo": "Fabricante",
                "tipo": "STRING",
                "obrigatorio": false,
                "tamanhoMaximo": 100
            },
            {
                "nome": "tamanho",
                "rotulo": "Tamanho",
                "tipo": "STRING",
                "obrigatorio": false,
                "tamanhoMaximo": 10
            }
        ]
    }'::jsonb,
    NOW(),
    NOW()
) ON CONFLICT (codigo) DO NOTHING;

-- ===========================================
-- VALE TRANSPORTE
-- ===========================================
INSERT INTO tipos_recurso (codigo, nome, descricao, ativo, legado, schema, created_at, updated_at)
VALUES (
    'VALE_TRANSPORTE',
    'Vale Transporte',
    'Benefício de vale transporte',
    true,
    false,
    '{
        "fields": [
            {
                "nome": "tipoTransporte",
                "rotulo": "Tipo de Transporte",
                "tipo": "ENUM",
                "obrigatorio": true,
                "opcoes": [
                    "ONIBUS_URBANO",
                    "ONIBUS_INTERMUNICIPAL",
                    "METRO",
                    "TREM",
                    "LOTACAO",
                    "VAN"
                ]
            },
            {
                "nome": "linha",
                "rotulo": "Linha/Número",
                "tipo": "STRING",
                "obrigatorio": false,
                "tamanhoMaximo": 50
            },
            {
                "nome": "quantidadeMensal",
                "rotulo": "Quantidade Mensal",
                "tipo": "INTEGER",
                "obrigatorio": true,
                "valorMinimo": 1,
                "valorMaximo": 200,
                "mensagemErro": "Quantidade deve estar entre 1 e 200"
            },
            {
                "nome": "valorUnitario",
                "rotulo": "Valor Unitário (R$)",
                "tipo": "DECIMAL",
                "obrigatorio": true,
                "valorMinimo": 0.01,
                "valorMaximo": 100.00
            },
            {
                "nome": "trajeto",
                "rotulo": "Trajeto (Ida/Volta)",
                "tipo": "STRING",
                "obrigatorio": false,
                "tamanhoMaximo": 200
            }
        ]
    }'::jsonb,
    NOW(),
    NOW()
) ON CONFLICT (codigo) DO NOTHING;

-- ===========================================
-- Comentários para documentação
-- ===========================================
COMMENT ON COLUMN tipos_recurso.schema IS 'Schema JSON que define campos dinâmicos para cada tipo de recurso';

-- Verificar criação dos tipos
-- SELECT codigo, nome, ativo FROM tipos_recurso WHERE codigo IN ('UNIFORME', 'EPI', 'VALE_TRANSPORTE');
