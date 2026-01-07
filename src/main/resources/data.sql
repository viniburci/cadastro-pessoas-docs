-- Script para adicionar o primeiro usuário ADMIN
-- Execute este script após iniciar a aplicação pela primeira vez

-- Substitua 'seu-email@gmail.com' pelo seu email do Google
INSERT INTO emails_permitidos (email, role, created_at, created_by)
VALUES ('vpburci@gmail.com', 'ADMIN', CURRENT_TIMESTAMP, 'SYSTEM')
ON CONFLICT (email) DO NOTHING;

-- Exemplo: Adicionar usuários adicionais
-- INSERT INTO emails_permitidos (email, role, created_at, created_by)
-- VALUES ('usuario1@gmail.com', 'USER', CURRENT_TIMESTAMP, 'ADMIN');

-- INSERT INTO emails_permitidos (email, role, created_at, created_by)
-- VALUES ('usuario2@gmail.com', 'USER', CURRENT_TIMESTAMP, 'ADMIN');
