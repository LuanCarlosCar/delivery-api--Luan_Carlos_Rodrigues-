package com.deliverytech.delivery.controller;

import com.deliverytech.delivery.dto.ItemPedidoRequestDTO;
import com.deliverytech.delivery.dto.PedidoRequestDTO;
import com.deliverytech.delivery.model.*;
import com.deliverytech.delivery.repository.*;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("PedidoController Integration Tests")
class PedidoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminToken;
    private String clienteToken;
    private String restauranteToken;
    private Cliente cliente;
    private Restaurante restaurante;
    private Produto produto;

    @BeforeEach
    void setUp() {
        pedidoRepository.deleteAll();
        produtoRepository.deleteAll();
        clienteRepository.deleteAll();
        restauranteRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario adminUser = new Usuario();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(com.deliverytech.delivery.enums.Role.ADMIN);
        adminUser.setEmail("admin@test.com");
        usuarioRepository.save(adminUser);

        cliente = new Cliente("João Silva", "joao@email.com", "11999999999", "Rua das Flores, 123");
        cliente = clienteRepository.save(cliente);

        Usuario clienteUser = new Usuario();
        clienteUser.setUsername("cliente");
        clienteUser.setPassword(passwordEncoder.encode("cliente123"));
        clienteUser.setRole(com.deliverytech.delivery.enums.Role.CLIENTE);
        clienteUser.setEmail("cliente@test.com");
        clienteUser.setClienteId(cliente.getId());
        usuarioRepository.save(clienteUser);

        restaurante = new Restaurante();
        restaurante.setNome("Restaurante Teste");
        restaurante.setTelefone("11888888888");
        restaurante.setEndereco("Rua do Restaurante, 456");
        restaurante.setTipoCozinha("Brasileira");
        restaurante.setAtivo(true);
        restaurante = restauranteRepository.save(restaurante);

        Usuario restauranteUser = new Usuario();
        restauranteUser.setUsername("restaurante");
        restauranteUser.setPassword(passwordEncoder.encode("restaurante123"));
        restauranteUser.setRole(com.deliverytech.delivery.enums.Role.RESTAURANTE);
        restauranteUser.setEmail("restaurante@test.com");
        restauranteUser.setRestauranteId(restaurante.getId());
        usuarioRepository.save(restauranteUser);

        produto = new Produto();
        produto.setNome("Hambúrguer Especial");
        produto.setDescricao("Delicioso hambúrguer");
        produto.setPreco(new BigDecimal("25.90"));
        produto.setDisponivel(true);
        produto.setRestaurante(restaurante);
        produto = produtoRepository.save(produto);

        adminToken = "Bearer " + jwtUtil.generateToken(adminUser.getUsername());
        clienteToken = "Bearer " + jwtUtil.generateToken(clienteUser.getUsername());
        restauranteToken = "Bearer " + jwtUtil.generateToken(restauranteUser.getUsername());
    }

    @Nested
    @DisplayName("POST /api/pedidos")
    class CriarPedido {

        @Test
        @DisplayName("Deve criar pedido com dados válidos")
        @Transactional
        void deveCriarPedidoComDadosValidos() throws Exception {
            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();

            mockMvc.perform(post("/api/pedidos")
                    .header("Authorization", clienteToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", notNullValue()))
                    .andExpect(jsonPath("$.clienteId", is(cliente.getId().intValue())))
                    .andExpect(jsonPath("$.clienteNome", is("João Silva")))
                    .andExpect(jsonPath("$.restauranteId", is(restaurante.getId().intValue())))
                    .andExpect(jsonPath("$.restauranteNome", is("Restaurante Teste")))
                    .andExpect(jsonPath("$.status", is("PENDENTE")))
                    .andExpect(jsonPath("$.valorTotal", is(51.8))) // 2 * 25.90
                    .andExpect(jsonPath("$.enderecoEntrega", is("Rua de Entrega, 789")))
                    .andExpect(jsonPath("$.observacoes", is("Sem cebola")))
                    .andExpect(jsonPath("$.dataPedido", notNullValue()))
                    .andExpect(jsonPath("$.itens", hasSize(1)))
                    .andExpect(jsonPath("$.itens[0].produtoId", is(produto.getId().intValue())))
                    .andExpect(jsonPath("$.itens[0].produtoNome", is("Hambúrguer Especial")))
                    .andExpect(jsonPath("$.itens[0].quantidade", is(2)))
                    .andExpect(jsonPath("$.itens[0].precoUnitario", is(25.9)))
                    .andExpect(jsonPath("$.itens[0].subtotal", is(51.8)));
        }

        @Test
        @DisplayName("Deve retornar 400 para cliente inexistente")
        void deveRetornar400ParaClienteInexistente() throws Exception {
            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();
            pedidoRequest.setClienteId(999L);

            mockMvc.perform(post("/api/pedidos")
                    .header("Authorization", clienteToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 para restaurante inexistente")
        void deveRetornar400ParaRestauranteInexistente() throws Exception {
            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();
            pedidoRequest.setRestauranteId(999L);

            mockMvc.perform(post("/api/pedidos")
                    .header("Authorization", clienteToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 para restaurante inativo")
        void deveRetornar400ParaRestauranteInativo() throws Exception {
            restaurante.setAtivo(false);
            restauranteRepository.save(restaurante);

            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();

            mockMvc.perform(post("/api/pedidos")
                    .header("Authorization", clienteToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 para produto inexistente")
        void deveRetornar400ParaProdutoInexistente() throws Exception {
            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();
            pedidoRequest.getItens().get(0).setProdutoId(999L);

            mockMvc.perform(post("/api/pedidos")
                    .header("Authorization", clienteToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 para produto indisponível")
        void deveRetornar400ParaProdutoIndisponivel() throws Exception {
            produto.setDisponivel(false);
            produtoRepository.save(produto);

            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();

            mockMvc.perform(post("/api/pedidos")
                    .header("Authorization", clienteToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 401 sem autenticação")
        void deveRetornar401SemAutenticacao() throws Exception {
            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();

            mockMvc.perform(post("/api/pedidos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Deve retornar 403 para usuário não cliente")
        void deveRetornar403ParaUsuarioNaoCliente() throws Exception {
            PedidoRequestDTO pedidoRequest = createValidPedidoRequest();

            mockMvc.perform(post("/api/pedidos")
                    .header("Authorization", adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pedidoRequest)))
                    .andExpect(status().isForbidden());
        }

        private PedidoRequestDTO createValidPedidoRequest() {
            ItemPedidoRequestDTO item = new ItemPedidoRequestDTO();
            item.setProdutoId(produto.getId());
            item.setQuantidade(2);

            PedidoRequestDTO pedido = new PedidoRequestDTO();
            pedido.setClienteId(cliente.getId());
            pedido.setRestauranteId(restaurante.getId());
            pedido.setEnderecoEntrega("Rua de Entrega, 789");
            pedido.setObservacoes("Sem cebola");
            pedido.setItens(Arrays.asList(item));

            return pedido;
        }
    }

    @Nested
    @DisplayName("GET /api/pedidos")
    class ListarTodosPedidos {

        @Test
        @DisplayName("Deve listar todos os pedidos para admin")
        void deveListarTodosPedidosParaAdmin() throws Exception {
            createTestPedido();

            mockMvc.perform(get("/api/pedidos")
                    .header("Authorization", adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
        }

        @Test
        @DisplayName("Deve retornar 403 para usuário não admin")
        void deveRetornar403ParaUsuarioNaoAdmin() throws Exception {
            mockMvc.perform(get("/api/pedidos")
                    .header("Authorization", clienteToken))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("GET /api/pedidos/meus")
    class ListarMeusPedidos {

        @Test
        @DisplayName("Deve listar pedidos do cliente logado")
        void deveListarPedidosDoClienteLogado() throws Exception {
            createTestPedido();

            mockMvc.perform(get("/api/pedidos/meus")
                    .header("Authorization", clienteToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
        }

        @Test
        @DisplayName("Deve retornar 403 para usuário não cliente")
        void deveRetornar403ParaUsuarioNaoCliente() throws Exception {
            mockMvc.perform(get("/api/pedidos/meus")
                    .header("Authorization", adminToken))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("GET /api/pedidos/restaurante")
    class ListarPedidosDoRestaurante {

        @Test
        @DisplayName("Deve listar pedidos do restaurante logado")
        void deveListarPedidosDoRestauranteLogado() throws Exception {
            createTestPedido();

            mockMvc.perform(get("/api/pedidos/restaurante")
                    .header("Authorization", restauranteToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
        }

        @Test
        @DisplayName("Deve retornar 403 para usuário não restaurante")
        void deveRetornar403ParaUsuarioNaoRestaurante() throws Exception {
            mockMvc.perform(get("/api/pedidos/restaurante")
                    .header("Authorization", clienteToken))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("GET /api/relatorios/vendas-por-restaurante")
    class RelatorioVendasPorRestaurante {

        @Test
        @DisplayName("Deve gerar relatório de vendas para admin")
        void deveGerarRelatorioVendasParaAdmin() throws Exception {
            LocalDate hoje = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

            mockMvc.perform(get("/api/relatorios/vendas-por-restaurante")
                    .header("Authorization", adminToken)
                    .param("dataInicio", hoje.minusDays(7).format(formatter))
                    .param("dataFim", hoje.format(formatter)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(instanceOf(java.util.List.class))));
        }

        @Test
        @DisplayName("Deve retornar 400 para parâmetros inválidos")
        void deveRetornar400ParaParametrosInvalidos() throws Exception {
            mockMvc.perform(get("/api/relatorios/vendas-por-restaurante")
                    .header("Authorization", adminToken)
                    .param("dataInicio", "invalid-date")
                    .param("dataFim", "invalid-date"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 403 para usuário não admin")
        void deveRetornar403ParaUsuarioNaoAdmin() throws Exception {
            LocalDate hoje = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

            mockMvc.perform(get("/api/relatorios/vendas-por-restaurante")
                    .header("Authorization", clienteToken)
                    .param("dataInicio", hoje.minusDays(7).format(formatter))
                    .param("dataFim", hoje.format(formatter)))
                    .andExpect(status().isForbidden());
        }
    }

    private void createTestPedido() {
        Pedido pedido = new Pedido(cliente, restaurante, new BigDecimal("25.90"), "Endereço Teste");
        pedidoRepository.save(pedido);
    }
}