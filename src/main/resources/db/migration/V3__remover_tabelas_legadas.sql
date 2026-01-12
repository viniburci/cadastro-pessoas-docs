-- ATENÇÃO: Execute este script SOMENTE após validar que a migração foi bem-sucedida!
-- Este script remove as tabelas legadas de recursos.

-- ===========================================
-- PASSO 1: Remover tabelas de empréstimos legados
-- ===========================================

DROP TABLE IF EXISTS recursos_carros CASCADE;
DROP TABLE IF EXISTS recursos_celulares CASCADE;
DROP TABLE IF EXISTS recursos_rocadeiras CASCADE;

-- ===========================================
-- PASSO 2: Remover tabela base de recursos (herança)
-- ===========================================

DROP TABLE IF EXISTS recursos CASCADE;

-- ===========================================
-- PASSO 3: Remover tabelas de itens legados
-- ===========================================

DROP TABLE IF EXISTS carros CASCADE;
DROP TABLE IF EXISTS celulares CASCADE;
DROP TABLE IF EXISTS rocadeiras CASCADE;

-- ===========================================
-- Nota: As classes Java correspondentes também devem ser removidas:
--
-- domain/carro/ (todo o pacote)
-- domain/celular/ (todo o pacote)
-- domain/rocadeira/ (todo o pacote)
-- recurso/Recurso.java (classe abstrata)
-- recurso/recurso_carro/ (todo o pacote)
-- recurso/recurso_celular/ (todo o pacote)
-- recurso/recurso_rocadeira/ (todo o pacote)
-- ===========================================
