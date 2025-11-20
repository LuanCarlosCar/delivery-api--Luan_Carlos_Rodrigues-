package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.*;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.UsuarioRepository;
import com.deliverytech.delivery.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro de usuários")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login", description = "Autentica um usuário e retorna um token JWT")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getSenha()
                    )
            );

            Usuario usuario = (Usuario) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(usuario);
            
            LoginResponse response = new LoginResponse(
                    jwt,
                    LocalDateTime.now().plusHours(24), // expiration
                    new UserResponse(usuario)
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Credenciais inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuário", description = "Cria um novo usuário no sistema")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Verificar se o email já existe
            if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Email já está em uso");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Criar novo usuário
            Usuario usuario = new Usuario(
                    registerRequest.getEmail(),
                    passwordEncoder.encode(registerRequest.getSenha()),
                    registerRequest.getNome(),
                    registerRequest.getRole()
            );
            
            if (registerRequest.getRestauranteId() != null) {
                usuario.setRestauranteId(registerRequest.getRestauranteId());
            }

            usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UserResponse(usuario));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erro ao criar usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/me")
    @Operation(summary = "Obter dados do usuário logado", description = "Retorna os dados do usuário autenticado")
    public ResponseEntity<?> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario usuario = (Usuario) authentication.getPrincipal();
            
            return ResponseEntity.ok(new UserResponse(usuario));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Usuário não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}