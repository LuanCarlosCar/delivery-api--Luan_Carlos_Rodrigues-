package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.ClienteDTO;
import com.deliverytech.delivery.model.Cliente;
import com.deliverytech.delivery.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public ClienteDTO salvarCliente(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        Cliente cliente = new Cliente(
            clienteDTO.getNome(),
            clienteDTO.getEmail(),
            clienteDTO.getTelefone(),
            clienteDTO.getEndereco()
        );

        Cliente clienteSalvo = clienteRepository.save(cliente);
        return convertToDTO(clienteSalvo);
    }

    public Optional<ClienteDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id)
            .map(this::convertToDTO);
    }

    public Optional<ClienteDTO> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email)
            .map(this::convertToDTO);
    }

    public List<ClienteDTO> listarTodosClientes() {
        return clienteRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Page<ClienteDTO> listarClientesPaginado(Pageable pageable) {
        return clienteRepository.findAll(pageable)
            .map(this::convertToDTO);
    }

    public List<ClienteDTO> listarClientesAtivos() {
        return clienteRepository.findByAtivoTrue().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ClienteDTO> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!cliente.getEmail().equals(clienteDTO.getEmail()) && 
            clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setTelefone(clienteDTO.getTelefone());
        cliente.setEndereco(clienteDTO.getEndereco());

        Cliente clienteAtualizado = clienteRepository.save(cliente);
        return convertToDTO(clienteAtualizado);
    }

    @Transactional
    public void desativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }

    @Transactional
    public void ativarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        cliente.setAtivo(true);
        clienteRepository.save(cliente);
    }

    public boolean existeEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setEndereco(cliente.getEndereco());
        dto.setAtivo(cliente.getAtivo());
        dto.setDataCadastro(cliente.getDataCadastro());
        return dto;
    }
}