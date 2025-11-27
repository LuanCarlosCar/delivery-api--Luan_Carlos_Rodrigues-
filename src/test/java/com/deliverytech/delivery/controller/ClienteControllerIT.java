package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.entity.Usuario;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.UsuarioRepository;
import com.deliverytech.delivery.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("ClienteController Integration Tests")
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private String clienteToken;
    private Cliente clienteExistente;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario adminUser = new Usuario("admin@test.com", passwordEncoder.encode("admin123"), 
                                        "Admin User", com.deliverytech.delivery.enums.Role.ADMIN);
        adminUser = usuarioRepository.save(adminUser);

        Usuario clienteUser = new Usuario("cliente@test.com", passwordEncoder.encode("cliente123"), 
                                          "Cliente User", com.deliverytech.delivery.enums.Role.CLIENTE);
        clienteUser = usuarioRepository.save(clienteUser);

        adminToken = "Bearer " + jwtUtil.generateToken(adminUser);
        clienteToken = "Bearer " + jwtUtil.generateToken(clienteUser);

        clienteExistente = new Cliente("João Silva", "joao@email.com", "11999999999", "Rua das Flores, 123");
        clienteExistente = clienteRepository.save(clienteExistente);
    }

    @Nested
    @DisplayName("POST /api/clientes")
    class CriarCliente {

        @Test
        @DisplayName("Deve criar cliente com dados válidos")
        @Transactional
        void deveCriarClienteComDadosValidos() throws Exception {
            ClienteDTO novoCliente = new ClienteDTO();
            novoCliente.setNome("Maria Santos");
            novoCliente.setEmail("maria@email.com");
            novoCliente.setTelefone("11888888888");
            novoCliente.setEndereco("Rua das Palmeiras, 456");

            mockMvc.perform(post("/api/clientes")
                    .header("Authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(novoCliente)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nome", is("Maria Santos")))
                    .andExpect(jsonPath("$.email", is("maria@email.com")))
                    .andExpect(jsonPath("$.telefone", is("11888888888")))
                    .andExpect(jsonPath("$.endereco", is("Rua das Palmeiras, 456")))
                    .andExpect(jsonPath("$.ativo", is(true)))
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.dataCadastro", notNullValue()));
        }

        @Test
        @DisplayName("Deve retornar 409 para email duplicado")
        void deveRetornar409ParaEmailDuplicado() throws Exception {
            ClienteDTO clienteComEmailDuplicado = new ClienteDTO();
            clienteComEmailDuplicado.setNome("Cliente Duplicado");
            clienteComEmailDuplicado.setEmail("joao@email.com"); // Email já existe
            clienteComEmailDuplicado.setTelefone("11777777777");
            clienteComEmailDuplicado.setEndereco("Endereço Teste");

            mockMvc.perform(post("/api/clientes")
                    .header("Authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clienteComEmailDuplicado)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Deve retornar 400 para dados inválidos")
        void deveRetornar400ParaDadosInvalidos() throws Exception {
            ClienteDTO clienteInvalido = new ClienteDTO();
            // Nome e email obrigatórios não informados

            mockMvc.perform(post("/api/clientes")
                    .header("Authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clienteInvalido)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 401 sem token de autenticação")
        void deveRetornar401SemToken() throws Exception {
            ClienteDTO novoCliente = new ClienteDTO();
            novoCliente.setNome("Cliente Teste");
            novoCliente.setEmail("teste@email.com");

            mockMvc.perform(post("/api/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(novoCliente)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("GET /api/clientes/{id}")
    class BuscarClientePorId {

        @Test
        @DisplayName("Deve retornar cliente existente")
        void deveRetornarClienteExistente() throws Exception {
            mockMvc.perform(get("/api/clientes/{id}", clienteExistente.getId())
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(clienteExistente.getId().intValue())))
                    .andExpect(jsonPath("$.nome", is("João Silva")))
                    .andExpect(jsonPath("$.email", is("joao@email.com")))
                    .andExpect(jsonPath("$.telefone", is("11999999999")))
                    .andExpect(jsonPath("$.endereco", is("Rua das Flores, 123")))
                    .andExpect(jsonPath("$.ativo", is(true)));
        }

        @Test
        @DisplayName("Deve retornar 404 para cliente inexistente")
        void deveRetornar404ParaClienteInexistente() throws Exception {
            mockMvc.perform(get("/api/clientes/{id}", 999L)
                    .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/clientes")
    class ListarClientes {

        @Test
        @DisplayName("Deve retornar lista paginada de clientes")
        void deveRetornarListaPaginadaDeClientes() throws Exception {
            Cliente cliente2 = new Cliente("Maria Santos", "maria@email.com", "11888888888", "Rua B, 456");
            clienteRepository.save(cliente2);

            mockMvc.perform(get("/api/clientes")
                    .header("Authorization", adminToken)
                    .param("page", "0")
                    .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements", is(2)))
                    .andExpect(jsonPath("$.number", is(0)))
                    .andExpect(jsonPath("$.size", is(10)));
        }
    }

    @Nested
    @DisplayName("GET /api/clientes/ativos")
    class ListarClientesAtivos {

        @Test
        @DisplayName("Deve retornar apenas clientes ativos")
        void deveRetornarApenasClientesAtivos() throws Exception {
            Cliente clienteInativo = new Cliente("Cliente Inativo", "inativo@email.com", "11777777777", "Endereço Teste");
            clienteInativo.setAtivo(false);
            clienteRepository.save(clienteInativo);

            mockMvc.perform(get("/api/clientes/ativos")
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].ativo", is(true)));
        }
    }

    @Nested
    @DisplayName("GET /api/clientes/buscar")
    class BuscarClientesPorNome {

        @Test
        @DisplayName("Deve buscar clientes por nome")
        void deveBuscarClientesPorNome() throws Exception {
            mockMvc.perform(get("/api/clientes/buscar")
                    .header("Authorization", adminToken)
                    .param("nome", "João"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nome", containsString("João")));
        }
    }

    @Nested
    @DisplayName("PUT /api/clientes/{id}")
    class AtualizarCliente {

        @Test
        @DisplayName("Deve atualizar cliente com dados válidos")
        @Transactional
        void deveAtualizarClienteComDadosValidos() throws Exception {
            ClienteDTO clienteAtualizado = new ClienteDTO();
            clienteAtualizado.setNome("João Santos");
            clienteAtualizado.setEmail("joao@email.com");
            clienteAtualizado.setTelefone("11777777777");
            clienteAtualizado.setEndereco("Nova Rua, 789");

            mockMvc.perform(put("/api/clientes/{id}", clienteExistente.getId())
                    .header("Authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clienteAtualizado)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome", is("João Santos")))
                    .andExpect(jsonPath("$.telefone", is("11777777777")))
                    .andExpect(jsonPath("$.endereco", is("Nova Rua, 789")));
        }

        @Test
        @DisplayName("Deve retornar 404 para cliente inexistente")
        void deveRetornar404ParaClienteInexistente() throws Exception {
            ClienteDTO clienteAtualizado = new ClienteDTO();
            clienteAtualizado.setNome("Cliente Teste");
            clienteAtualizado.setEmail("teste@email.com");

            mockMvc.perform(put("/api/clientes/{id}", 999L)
                    .header("Authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clienteAtualizado)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /api/clientes/{id}/desativar")
    class DesativarCliente {

        @Test
        @DisplayName("Deve desativar cliente existente")
        @Transactional
        void deveDesativarClienteExistente() throws Exception {
            mockMvc.perform(put("/api/clientes/{id}/desativar", clienteExistente.getId())
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk());

            // Verificar se foi realmente desativado
            mockMvc.perform(get("/api/clientes/{id}", clienteExistente.getId())
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is(false)));
        }

        @Test
        @DisplayName("Deve retornar 404 para cliente inexistente")
        void deveRetornar404ParaClienteInexistente() throws Exception {
            mockMvc.perform(put("/api/clientes/{id}/desativar", 999L)
                    .header("Authorization", adminToken))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PUT /api/clientes/{id}/ativar")
    class AtivarCliente {

        @Test
        @DisplayName("Deve ativar cliente desativado")
        @Transactional
        void deveAtivarClienteDesativado() throws Exception {
            clienteExistente.setAtivo(false);
            clienteRepository.save(clienteExistente);

            mockMvc.perform(put("/api/clientes/{id}/ativar", clienteExistente.getId())
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk());

            // Verificar se foi realmente ativado
            mockMvc.perform(get("/api/clientes/{id}", clienteExistente.getId())
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is(true)));
        }
    }

    @Nested
    @DisplayName("GET /api/clientes/email/{email}/existe")
    class VerificarEmail {

        @Test
        @DisplayName("Deve retornar true para email existente")
        void deveRetornarTrueParaEmailExistente() throws Exception {
            mockMvc.perform(get("/api/clientes/email/{email}/existe", "joao@email.com")
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk())
                    .andExpect(content().string("true"));
        }

        @Test
        @DisplayName("Deve retornar false para email inexistente")
        void deveRetornarFalseParaEmailInexistente() throws Exception {
            mockMvc.perform(get("/api/clientes/email/{email}/existe", "inexistente@email.com")
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk())
                    .andExpect(content().string("false"));
        }
    }
}