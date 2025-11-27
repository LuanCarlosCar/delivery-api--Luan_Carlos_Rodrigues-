package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteService Tests")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDTO clienteDTO;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO();
        clienteDTO.setNome("João Silva");
        clienteDTO.setEmail("joao@email.com");
        clienteDTO.setTelefone("11999999999");
        clienteDTO.setEndereco("Rua das Flores, 123");

        cliente = new Cliente(
            clienteDTO.getNome(),
            clienteDTO.getEmail(),
            clienteDTO.getTelefone(),
            clienteDTO.getEndereco()
        );
        cliente.setId(1L);
        cliente.setDataCadastro(LocalDateTime.now());
    }

    @Nested
    @DisplayName("Salvar Cliente")
    class SalvarCliente {

        @Test
        @DisplayName("Deve salvar cliente com dados válidos")
        void deveSalvarClienteComDadosValidos() {
            when(clienteRepository.existsByEmail(clienteDTO.getEmail())).thenReturn(false);
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            ClienteDTO resultado = clienteService.salvarCliente(clienteDTO);

            assertNotNull(resultado);
            assertEquals(clienteDTO.getNome(), resultado.getNome());
            assertEquals(clienteDTO.getEmail(), resultado.getEmail());
            assertEquals(clienteDTO.getTelefone(), resultado.getTelefone());
            assertEquals(clienteDTO.getEndereco(), resultado.getEndereco());
            assertEquals(1L, resultado.getId());

            verify(clienteRepository).existsByEmail(clienteDTO.getEmail());
            verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve lançar exceção para email duplicado")
        void deveLancarExcecaoParaEmailDuplicado() {
            when(clienteRepository.existsByEmail(clienteDTO.getEmail())).thenReturn(true);

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clienteService.salvarCliente(clienteDTO));

            assertEquals("Email já está em uso", exception.getMessage());
            verify(clienteRepository).existsByEmail(clienteDTO.getEmail());
            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Nested
    @DisplayName("Buscar Cliente")
    class BuscarCliente {

        @Test
        @DisplayName("Deve retornar cliente por ID existente")
        void deveRetornarClientePorIdExistente() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

            Optional<ClienteDTO> resultado = clienteService.buscarPorId(1L);

            assertTrue(resultado.isPresent());
            assertEquals(cliente.getNome(), resultado.get().getNome());
            assertEquals(cliente.getEmail(), resultado.get().getEmail());
            verify(clienteRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar Optional vazio para ID inexistente")
        void deveRetornarOptionalVazioParaIdInexistente() {
            when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<ClienteDTO> resultado = clienteService.buscarPorId(999L);

            assertTrue(resultado.isEmpty());
            verify(clienteRepository).findById(999L);
        }

        @Test
        @DisplayName("Deve retornar cliente por email existente")
        void deveRetornarClientePorEmailExistente() {
            when(clienteRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(cliente));

            Optional<ClienteDTO> resultado = clienteService.buscarPorEmail("joao@email.com");

            assertTrue(resultado.isPresent());
            assertEquals(cliente.getEmail(), resultado.get().getEmail());
            verify(clienteRepository).findByEmail("joao@email.com");
        }
    }

    @Nested
    @DisplayName("Listar Clientes")
    class ListarClientes {

        @Test
        @DisplayName("Deve retornar lista de todos os clientes")
        void deveRetornarListaDeTodosOsClientes() {
            Cliente cliente2 = new Cliente("Maria", "maria@email.com", "11888888888", "Rua B, 456");
            cliente2.setId(2L);

            when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente, cliente2));

            List<ClienteDTO> resultado = clienteService.listarTodosClientes();

            assertEquals(2, resultado.size());
            assertEquals("João Silva", resultado.get(0).getNome());
            assertEquals("Maria", resultado.get(1).getNome());
            verify(clienteRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar clientes paginados")
        void deveRetornarClientesPaginados() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Cliente> pageClientes = new PageImpl<>(Arrays.asList(cliente));
            
            when(clienteRepository.findAll(pageable)).thenReturn(pageClientes);

            Page<ClienteDTO> resultado = clienteService.listarClientesPaginado(pageable);

            assertEquals(1, resultado.getTotalElements());
            assertEquals("João Silva", resultado.getContent().get(0).getNome());
            verify(clienteRepository).findAll(pageable);
        }

        @Test
        @DisplayName("Deve retornar apenas clientes ativos")
        void deveRetornarApenasClientesAtivos() {
            when(clienteRepository.findByAtivoTrue()).thenReturn(Arrays.asList(cliente));

            List<ClienteDTO> resultado = clienteService.listarClientesAtivos();

            assertEquals(1, resultado.size());
            assertTrue(resultado.get(0).getAtivo());
            verify(clienteRepository).findByAtivoTrue();
        }

        @Test
        @DisplayName("Deve buscar clientes por nome")
        void deveBuscarClientesPorNome() {
            when(clienteRepository.findByNomeContainingIgnoreCase("João")).thenReturn(Arrays.asList(cliente));

            List<ClienteDTO> resultado = clienteService.buscarPorNome("João");

            assertEquals(1, resultado.size());
            assertEquals("João Silva", resultado.get(0).getNome());
            verify(clienteRepository).findByNomeContainingIgnoreCase("João");
        }
    }

    @Nested
    @DisplayName("Atualizar Cliente")
    class AtualizarCliente {

        @Test
        @DisplayName("Deve atualizar cliente com dados válidos")
        void deveAtualizarClienteComDadosValidos() {
            ClienteDTO clienteAtualizado = new ClienteDTO();
            clienteAtualizado.setNome("João Santos");
            clienteAtualizado.setEmail("joao@email.com");
            clienteAtualizado.setTelefone("11777777777");
            clienteAtualizado.setEndereco("Rua Nova, 789");

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            ClienteDTO resultado = clienteService.atualizarCliente(1L, clienteAtualizado);

            assertNotNull(resultado);
            verify(clienteRepository).findById(1L);
            verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve lançar exceção para cliente inexistente na atualização")
        void deveLancarExcecaoParaClienteInexistenteNaAtualizacao() {
            ClienteDTO clienteAtualizado = new ClienteDTO();
            when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clienteService.atualizarCliente(999L, clienteAtualizado));

            assertEquals("Cliente não encontrado", exception.getMessage());
            verify(clienteRepository).findById(999L);
            verify(clienteRepository, never()).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve lançar exceção para email duplicado na atualização")
        void deveLancarExcecaoParaEmailDuplicadoNaAtualizacao() {
            ClienteDTO clienteAtualizado = new ClienteDTO();
            clienteAtualizado.setEmail("outro@email.com");

            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clienteRepository.existsByEmail("outro@email.com")).thenReturn(true);

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clienteService.atualizarCliente(1L, clienteAtualizado));

            assertEquals("Email já está em uso", exception.getMessage());
            verify(clienteRepository).findById(1L);
            verify(clienteRepository).existsByEmail("outro@email.com");
            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Nested
    @DisplayName("Ativação/Desativação")
    class AtivacaoDesativacao {

        @Test
        @DisplayName("Deve desativar cliente existente")
        void deveDesativarClienteExistente() {
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            assertDoesNotThrow(() -> clienteService.desativarCliente(1L));

            verify(clienteRepository).findById(1L);
            verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve ativar cliente existente")
        void deveAtivarClienteExistente() {
            cliente.setAtivo(false);
            when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

            assertDoesNotThrow(() -> clienteService.ativarCliente(1L));

            verify(clienteRepository).findById(1L);
            verify(clienteRepository).save(any(Cliente.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao desativar cliente inexistente")
        void deveLancarExcecaoAoDesativarClienteInexistente() {
            when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clienteService.desativarCliente(999L));

            assertEquals("Cliente não encontrado", exception.getMessage());
            verify(clienteRepository).findById(999L);
            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Test
    @DisplayName("Deve verificar se email existe")
    void deveVerificarSeEmailExiste() {
        when(clienteRepository.existsByEmail("joao@email.com")).thenReturn(true);

        boolean existe = clienteService.existeEmail("joao@email.com");

        assertTrue(existe);
        verify(clienteRepository).existsByEmail("joao@email.com");
    }

    @Test
    @DisplayName("Deve retornar false para email que não existe")
    void deveRetornarFalseParaEmailQueNaoExiste() {
        when(clienteRepository.existsByEmail("inexistente@email.com")).thenReturn(false);

        boolean existe = clienteService.existeEmail("inexistente@email.com");

        assertFalse(existe);
        verify(clienteRepository).existsByEmail("inexistente@email.com");
    }
}