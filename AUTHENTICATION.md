# Sistema de Autenticação OAuth2 com Google

## Visão Geral

O sistema implementa autenticação OAuth2 com Google, permitindo apenas usuários selecionados através de uma lista de emails permitidos no banco de dados.

## Componentes

### 1. Entidades

- **Usuario**: Armazena dados do usuário autenticado
  - Email (único)
  - Nome
  - Foto (URL do Google)
  - Role (USER ou ADMIN)
  - Status ativo/inativo
  - Data de criação e último login

- **EmailPermitido**: Lista de emails autorizados a fazer login
  - Email (único)
  - Role padrão para o usuário
  - Quem adicionou o email
  - Data de criação

### 2. Fluxo de Autenticação

1. **Usuário clica em "Login com Google"** no frontend
2. **Redirecionamento para Google** para autenticação
3. **Google retorna para o backend** em `/login/oauth2/code/google`
4. **OAuth2AuthenticationSuccessHandler valida:**
   - Se o email está na lista de `emails_permitidos`
   - Se NÃO estiver: retorna erro "Email não autorizado"
   - Se estiver: cria/atualiza usuário e gera token JWT
5. **Redirecionamento para frontend** com token JWT
6. **Frontend armazena o token** e o inclui em todas as requisições

### 3. Endpoints

#### Autenticação (Público)

- `GET /oauth2/authorization/google` - Inicia login OAuth2
- `GET /api/v1/auth/me` - Retorna dados do usuário atual
- `GET /api/v1/auth/validate` - Valida token JWT

#### Administração (Somente ADMIN)

**Gerenciar Emails Permitidos:**
- `GET /api/v1/admin/emails-permitidos` - Lista todos os emails permitidos
- `POST /api/v1/admin/emails-permitidos` - Adiciona email à lista
  ```json
  {
    "email": "usuario@exemplo.com",
    "role": "USER"  // ou "ADMIN"
  }
  ```
- `DELETE /api/v1/admin/emails-permitidos/{id}` - Remove email da lista

**Gerenciar Usuários:**
- `GET /api/v1/admin/usuarios` - Lista todos os usuários
- `PUT /api/v1/admin/usuarios/{id}/ativar` - Ativa usuário
- `PUT /api/v1/admin/usuarios/{id}/desativar` - Desativa usuário
- `PUT /api/v1/admin/usuarios/{id}/role` - Altera role do usuário
  ```json
  {
    "role": "ADMIN"  // ou "USER"
  }
  ```

### 4. Proteção de Endpoints

Todos os endpoints em `/api/v1/*` (exceto `/api/v1/auth/**`) requerem autenticação JWT.

Para proteger um endpoint específico com role, use:

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/endpoint-admin")
public ResponseEntity<?> endpointAdmin() {
    // ...
}
```

### 5. Frontend Integration

**Incluir token nas requisições:**

```typescript
// Angular HTTP Interceptor
intercept(request: HttpServletRequest, next: HttpHandler): Observable<HttpEvent<any>> {
  const token = localStorage.getItem('token');
  
  if (token) {
    request = request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  
  return next.handle(request);
}
```

**Tratar callback do OAuth2:**

```typescript
// auth-callback.component.ts
ngOnInit() {
  const urlParams = new URLSearchParams(window.location.search);
  
  if (urlParams.has('error')) {
    // Login falhou - email não autorizado
    const message = urlParams.get('message');
    this.showError(message);
  } else if (urlParams.has('token')) {
    // Login sucesso - salvar token
    const token = urlParams.get('token');
    localStorage.setItem('token', token);
    this.router.navigate(['/dashboard']);
  }
}
```

## Configuração

### 1. Criar Credenciais Google OAuth2

1. Acesse https://console.cloud.google.com/apis/credentials
2. Crie um novo projeto ou selecione um existente
3. Vá em "Credenciais" → "Criar credenciais" → "ID do cliente OAuth 2.0"
4. Tipo de aplicativo: "Aplicativo da Web"
5. **URIs de redirecionamento autorizados:**
   - `http://localhost:8080/login/oauth2/code/google`
   - Adicione outros domínios em produção
6. Copie o **Client ID** e **Client Secret**

### 2. Configurar Variáveis de Ambiente

Adicione ao seu arquivo `.env`:

```bash
GOOGLE_CLIENT_ID=seu-client-id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=seu-client-secret
JWT_SECRET=sua-chave-secreta-min-256-bits
```

### 3. Adicionar Primeiro Usuário Admin

Como o sistema requer que o email esteja na lista de permitidos, você precisa adicionar o primeiro admin manualmente no banco de dados:

```sql
INSERT INTO emails_permitidos (email, role, created_at, created_by) 
VALUES ('seu-email@gmail.com', 'ADMIN', NOW(), 'SYSTEM');
```

Após fazer login como admin, você pode gerenciar outros usuários pela interface.

## Segurança

### Boas Práticas Implementadas

1. **JWT com expiração**: Tokens expiram em 24 horas
2. **Lista de emails permitidos**: Controle granular de acesso
3. **Roles separadas**: USER e ADMIN com permissões diferentes
4. **Stateless authentication**: Não armazena sessões no servidor
5. **CORS configurado**: Apenas localhost:4200 permitido
6. **Password encoder**: BCrypt para futuras senhas locais

### Melhorias Futuras Recomendadas

1. **Refresh tokens**: Renovar tokens sem re-login
2. **Rate limiting**: Prevenir ataques de força bruta
3. **Audit log**: Registrar todas as ações administrativas
4. **Email de notificação**: Avisar admins sobre novos pedidos de acesso
5. **IP whitelist**: Restringir origem das requisições
6. **2FA**: Autenticação de dois fatores

## Troubleshooting

### "Email não autorizado"
- Verifique se o email está na tabela `emails_permitidos`
- Verifique se o campo `ativo` está como `true`

### "Token inválido"
- Verifique se o token não expirou (24h)
- Verifique se o JWT_SECRET é o mesmo usado para gerar o token
- Verifique se o header Authorization está correto: `Bearer {token}`

### "CORS error"
- Verifique se o frontend está rodando em `http://localhost:4200`
- Adicione outras origens em `SecurityConfig.corsConfigurationSource()`

### Erro ao redirecionar do Google
- Verifique se o redirect URI está configurado no Google Console
- Verifique se a variável `GOOGLE_REDIRECT_URI` está correta
