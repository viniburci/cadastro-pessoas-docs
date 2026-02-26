--liquibase formatted sql

--changeset doban:015-seed-admin
INSERT INTO emails_permitidos (email, role, created_at, created_by)
VALUES ('vpburci@gmail.com', 'ADMIN', CURRENT_TIMESTAMP, 'SYSTEM')
ON CONFLICT (email) DO NOTHING;
