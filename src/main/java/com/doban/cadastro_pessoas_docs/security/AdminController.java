package com.doban.cadastro_pessoas_docs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final EmailPermitidoRepository emailPermitidoRepository;
    private final UsuarioRepository usuarioRepository;

    // Gerenciamento de emails permitidos
    @GetMapping("/emails-permitidos")
    public ResponseEntity<List<Map<String, Object>>> listarEmailsPermitidos() {
        return ResponseEntity.ok(
                emailPermitidoRepository.findAll().stream()
                        .map(e -> Map.of(
                                "id", (Object) e.getId(),
                                "email", e.getEmail(),
                                "role", e.getRole().name(),
                                "createdAt", e.getCreatedAt().toString(),
                                "createdBy", e.getCreatedBy() != null ? e.getCreatedBy() : ""
                        ))
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/emails-permitidos")
    public ResponseEntity<?> adicionarEmailPermitido(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal Usuario admin
    ) {
        String email = request.get("email");
        String roleStr = request.get("role");

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email é obrigatório"));
        }

        if (emailPermitidoRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email já está na lista de permitidos"));
        }

        EmailPermitido emailPermitido = new EmailPermitido();
        emailPermitido.setEmail(email);
        emailPermitido.setRole(roleStr != null && roleStr.equals("ADMIN") ? Role.ADMIN : Role.USER);
        emailPermitido.setCreatedBy(admin.getEmail());

        emailPermitidoRepository.save(emailPermitido);

        return ResponseEntity.ok(Map.of(
                "message", "Email adicionado à lista de permitidos",
                "email", email,
                "role", emailPermitido.getRole().name()
        ));
    }

    @DeleteMapping("/emails-permitidos/{id}")
    public ResponseEntity<?> removerEmailPermitido(@PathVariable Long id) {
        if (!emailPermitidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        emailPermitidoRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Email removido da lista de permitidos"));
    }

    // Gerenciamento de usuários
    @GetMapping("/usuarios")
    public ResponseEntity<List<Map<String, Object>>> listarUsuarios() {
        return ResponseEntity.ok(
                usuarioRepository.findAll().stream()
                        .map(u -> Map.of(
                                "id", (Object) u.getId(),
                                "email", u.getEmail(),
                                "nome", u.getNome() != null ? u.getNome() : "",
                                "role", u.getRole().name(),
                                "ativo", u.getAtivo(),
                                "createdAt", u.getCreatedAt().toString(),
                                "lastLogin", u.getLastLogin() != null ? u.getLastLogin().toString() : ""
                        ))
                        .collect(Collectors.toList())
        );
    }

    @PutMapping("/usuarios/{id}/ativar")
    public ResponseEntity<?> ativarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("message", "Usuário ativado", "email", usuario.getEmail()));
    }

    @PutMapping("/usuarios/{id}/desativar")
    public ResponseEntity<?> desativarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of("message", "Usuário desativado", "email", usuario.getEmail()));
    }

    @PutMapping("/usuarios/{id}/role")
    public ResponseEntity<?> alterarRole(
            @PathVariable Long id,
            @RequestBody Map<String, String> request
    ) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String roleStr = request.get("role");
        Role novaRole = roleStr != null && roleStr.equals("ADMIN") ? Role.ADMIN : Role.USER;

        usuario.setRole(novaRole);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok(Map.of(
                "message", "Role alterada",
                "email", usuario.getEmail(),
                "role", novaRole.name()
        ));
    }
}
