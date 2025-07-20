package com.pattymoda.service;

import com.pattymoda.entity.Cliente;
import com.pattymoda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService extends BaseService<Cliente, Long> {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        super(clienteRepository);
        this.clienteRepository = clienteRepository;
    }

    @Cacheable("clientes")
    public Optional<Cliente> findByCodigoCliente(String codigoCliente) {
        return clienteRepository.findByCodigoCliente(codigoCliente);
    }

    public Optional<Cliente> findByNumeroDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento);
    }

    public boolean existsByCodigoCliente(String codigoCliente) {
        return clienteRepository.existsByCodigoCliente(codigoCliente);
    }

    public boolean existsByNumeroDocumento(String numeroDocumento) {
        return clienteRepository.existsByNumeroDocumento(numeroDocumento);
    }

    public Page<Cliente> findByActivo(Boolean activo, Pageable pageable) {
        return clienteRepository.findByActivo(activo, pageable);
    }

    public Page<Cliente> buscarClientes(String busqueda, Pageable pageable) {
        return clienteRepository.buscarClientes(busqueda, pageable);
    }

    public Optional<Cliente> findByTipoDocumentoAndNumeroDocumento(Cliente.TipoDocumento tipoDocumento, String numeroDocumento) {
        return clienteRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    public long countClientesActivos() {
        return clienteRepository.countClientesActivos();
    }

    public List<Cliente> findClientesNuevosUltimoMes() {
        return clienteRepository.findClientesNuevosUltimoMes();
    }

    @Override
    public Cliente save(Cliente cliente) {
        // Validaciones específicas para clientes
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }

        // Generar código automáticamente si no se proporciona
        if (cliente.getCodigoCliente() == null || cliente.getCodigoCliente().trim().isEmpty()) {
            cliente.setCodigoCliente(generarCodigoCliente());
        }

        // Verificar si el código ya existe (excepto para actualizaciones)
        if (cliente.getId() == null && existsByCodigoCliente(cliente.getCodigoCliente())) {
            throw new IllegalArgumentException("Ya existe un cliente con el código: " + cliente.getCodigoCliente());
        }

        // Verificar si el documento ya existe (excepto para actualizaciones)
        if (cliente.getNumeroDocumento() != null && !cliente.getNumeroDocumento().trim().isEmpty()) {
            if (cliente.getId() == null && existsByNumeroDocumento(cliente.getNumeroDocumento())) {
                throw new IllegalArgumentException("Ya existe un cliente con el documento: " + cliente.getNumeroDocumento());
            }
        }

        return super.save(cliente);
    }

    @Override
    public void deleteById(Long id) {
        Cliente cliente = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        // Verificar si el cliente tiene ventas asociadas
        // Aquí podrías agregar validaciones adicionales

        cliente.setActivo(false);
        save(cliente);
    }

    private String generarCodigoCliente() {
        // Generar código secuencial
        long count = clienteRepository.count();
        return String.format("CLI%06d", count + 1);
    }
}