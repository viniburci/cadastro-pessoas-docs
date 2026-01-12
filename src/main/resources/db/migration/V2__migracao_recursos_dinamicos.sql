-- Migração de Recursos Dinâmicos
-- Este script migra os dados das tabelas legadas (carros, celulares, rocadeiras)
-- para o novo sistema de recursos dinâmicos

-- ===========================================
-- PASSO 1: Criar Tipos de Recurso
-- ===========================================

-- Tipo CARRO
INSERT INTO tipos_recurso (codigo, nome, descricao, ativo, legado, schema, created_at, updated_at)
VALUES (
    'CARRO',
    'Carro',
    'Veículo automotor',
    true,
    true,
    '{
        "fields": [
            {"nome": "marca", "rotulo": "Marca", "tipo": "STRING", "obrigatorio": true},
            {"nome": "modelo", "rotulo": "Modelo", "tipo": "STRING", "obrigatorio": true},
            {"nome": "cor", "rotulo": "Cor", "tipo": "STRING", "obrigatorio": false},
            {"nome": "chassi", "rotulo": "Chassi", "tipo": "STRING", "obrigatorio": true},
            {"nome": "placa", "rotulo": "Placa", "tipo": "STRING", "obrigatorio": true},
            {"nome": "anoModelo", "rotulo": "Ano/Modelo", "tipo": "STRING", "obrigatorio": false}
        ]
    }',
    NOW(),
    NOW()
) ON CONFLICT (codigo) DO NOTHING;

-- Tipo CELULAR
INSERT INTO tipos_recurso (codigo, nome, descricao, ativo, legado, schema, created_at, updated_at)
VALUES (
    'CELULAR',
    'Celular',
    'Aparelho celular',
    true,
    true,
    '{
        "fields": [
            {"nome": "marca", "rotulo": "Marca", "tipo": "STRING", "obrigatorio": true},
            {"nome": "modelo", "rotulo": "Modelo", "tipo": "STRING", "obrigatorio": true},
            {"nome": "chip", "rotulo": "Chip", "tipo": "STRING", "obrigatorio": false},
            {"nome": "imei", "rotulo": "IMEI", "tipo": "STRING", "obrigatorio": true}
        ]
    }',
    NOW(),
    NOW()
) ON CONFLICT (codigo) DO NOTHING;

-- Tipo ROCADEIRA
INSERT INTO tipos_recurso (codigo, nome, descricao, ativo, legado, schema, created_at, updated_at)
VALUES (
    'ROCADEIRA',
    'Roçadeira',
    'Equipamento de jardinagem',
    true,
    true,
    '{
        "fields": [
            {"nome": "marca", "rotulo": "Marca", "tipo": "STRING", "obrigatorio": true},
            {"nome": "numeroSerie", "rotulo": "Número de Série", "tipo": "STRING", "obrigatorio": true}
        ]
    }',
    NOW(),
    NOW()
) ON CONFLICT (codigo) DO NOTHING;

-- ===========================================
-- PASSO 2: Migrar Itens Existentes
-- ===========================================

-- Migrar CARROS para itens_dinamicos
INSERT INTO itens_dinamicos (tipo_recurso_id, identificador, atributos, ativo, created_at, updated_at)
SELECT
    (SELECT id FROM tipos_recurso WHERE codigo = 'CARRO'),
    placa,
    jsonb_build_object(
        'marca', marca,
        'modelo', modelo,
        'cor', cor,
        'chassi', chassi,
        'placa', placa,
        'anoModelo', ano_modelo
    ),
    true,
    NOW(),
    NOW()
FROM carros
WHERE NOT EXISTS (
    SELECT 1 FROM itens_dinamicos id
    JOIN tipos_recurso tr ON id.tipo_recurso_id = tr.id
    WHERE tr.codigo = 'CARRO' AND id.identificador = carros.placa
);

-- Migrar CELULARES para itens_dinamicos
INSERT INTO itens_dinamicos (tipo_recurso_id, identificador, atributos, ativo, created_at, updated_at)
SELECT
    (SELECT id FROM tipos_recurso WHERE codigo = 'CELULAR'),
    imei,
    jsonb_build_object(
        'marca', marca,
        'modelo', modelo,
        'chip', chip,
        'imei', imei
    ),
    true,
    NOW(),
    NOW()
FROM celulares
WHERE NOT EXISTS (
    SELECT 1 FROM itens_dinamicos id
    JOIN tipos_recurso tr ON id.tipo_recurso_id = tr.id
    WHERE tr.codigo = 'CELULAR' AND id.identificador = celulares.imei
);

-- Migrar ROCADEIRAS para itens_dinamicos
INSERT INTO itens_dinamicos (tipo_recurso_id, identificador, atributos, ativo, created_at, updated_at)
SELECT
    (SELECT id FROM tipos_recurso WHERE codigo = 'ROCADEIRA'),
    numero_serie,
    jsonb_build_object(
        'marca', marca,
        'numeroSerie', numero_serie
    ),
    true,
    NOW(),
    NOW()
FROM rocadeiras
WHERE NOT EXISTS (
    SELECT 1 FROM itens_dinamicos id
    JOIN tipos_recurso tr ON id.tipo_recurso_id = tr.id
    WHERE tr.codigo = 'ROCADEIRA' AND id.identificador = rocadeiras.numero_serie
);

-- ===========================================
-- PASSO 3: Migrar Empréstimos Existentes
-- ===========================================

-- Migrar recursos_carros para recursos_dinamicos
INSERT INTO recursos_dinamicos (pessoa_id, item_id, data_entrega, data_devolucao, created_at, updated_at)
SELECT
    rc.pessoa_id,
    id.id,
    rc.data_entrega,
    rc.data_devolucao,
    NOW(),
    NOW()
FROM recursos_carros rc
JOIN carros c ON rc.carro_id = c.id
JOIN itens_dinamicos id ON id.identificador = c.placa
    AND id.tipo_recurso_id = (SELECT tr.id FROM tipos_recurso tr WHERE tr.codigo = 'CARRO')
WHERE NOT EXISTS (
    SELECT 1 FROM recursos_dinamicos rd
    WHERE rd.pessoa_id = rc.pessoa_id
    AND rd.item_id = id.id
    AND rd.data_entrega = rc.data_entrega
);

-- Migrar recursos_celulares para recursos_dinamicos
INSERT INTO recursos_dinamicos (pessoa_id, item_id, data_entrega, data_devolucao, created_at, updated_at)
SELECT
    rc.pessoa_id,
    id.id,
    rc.data_entrega,
    rc.data_devolucao,
    NOW(),
    NOW()
FROM recursos_celulares rc
JOIN celulares c ON rc.celular_id = c.id
JOIN itens_dinamicos id ON id.identificador = c.imei
    AND id.tipo_recurso_id = (SELECT tr.id FROM tipos_recurso tr WHERE tr.codigo = 'CELULAR')
WHERE NOT EXISTS (
    SELECT 1 FROM recursos_dinamicos rd
    WHERE rd.pessoa_id = rc.pessoa_id
    AND rd.item_id = id.id
    AND rd.data_entrega = rc.data_entrega
);

-- Migrar recursos_rocadeiras para recursos_dinamicos
INSERT INTO recursos_dinamicos (pessoa_id, item_id, data_entrega, data_devolucao, created_at, updated_at)
SELECT
    rr.pessoa_id,
    id.id,
    rr.data_entrega,
    rr.data_devolucao,
    NOW(),
    NOW()
FROM recursos_rocadeiras rr
JOIN rocadeiras r ON rr.rocadeira_id = r.id
JOIN itens_dinamicos id ON id.identificador = r.numero_serie
    AND id.tipo_recurso_id = (SELECT tr.id FROM tipos_recurso tr WHERE tr.codigo = 'ROCADEIRA')
WHERE NOT EXISTS (
    SELECT 1 FROM recursos_dinamicos rd
    WHERE rd.pessoa_id = rr.pessoa_id
    AND rd.item_id = id.id
    AND rd.data_entrega = rr.data_entrega
);

-- ===========================================
-- PASSO 4: Criar índices para performance
-- ===========================================

CREATE INDEX IF NOT EXISTS idx_itens_dinamicos_tipo_recurso ON itens_dinamicos(tipo_recurso_id);
CREATE INDEX IF NOT EXISTS idx_itens_dinamicos_identificador ON itens_dinamicos(identificador);
CREATE INDEX IF NOT EXISTS idx_itens_dinamicos_atributos ON itens_dinamicos USING GIN (atributos);
CREATE INDEX IF NOT EXISTS idx_recursos_dinamicos_pessoa ON recursos_dinamicos(pessoa_id);
CREATE INDEX IF NOT EXISTS idx_recursos_dinamicos_item ON recursos_dinamicos(item_id);
CREATE INDEX IF NOT EXISTS idx_recursos_dinamicos_devolucao ON recursos_dinamicos(data_devolucao);
CREATE INDEX IF NOT EXISTS idx_vagas_atributos ON vagas USING GIN (atributos_dinamicos);

-- ===========================================
-- Verificação de migração (execute manualmente)
-- ===========================================

-- SELECT 'carros' as tabela, COUNT(*) as total FROM carros
-- UNION ALL
-- SELECT 'itens_dinamicos (CARRO)', COUNT(*) FROM itens_dinamicos WHERE tipo_recurso_id = (SELECT id FROM tipos_recurso WHERE codigo = 'CARRO')
-- UNION ALL
-- SELECT 'celulares', COUNT(*) FROM celulares
-- UNION ALL
-- SELECT 'itens_dinamicos (CELULAR)', COUNT(*) FROM itens_dinamicos WHERE tipo_recurso_id = (SELECT id FROM tipos_recurso WHERE codigo = 'CELULAR')
-- UNION ALL
-- SELECT 'rocadeiras', COUNT(*) FROM rocadeiras
-- UNION ALL
-- SELECT 'itens_dinamicos (ROCADEIRA)', COUNT(*) FROM itens_dinamicos WHERE tipo_recurso_id = (SELECT id FROM tipos_recurso WHERE codigo = 'ROCADEIRA');
