package com.tienda.service;

import com.tienda.entity.Cliente;
import com.tienda.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findByActivoTrue();
    }
    
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }
    
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    public Cliente crearCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con ese email");
        }
        return clienteRepository.save(cliente);
    }
    
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        // Validar email único si se cambió
        if (!cliente.getEmail().equals(clienteActualizado.getEmail())) {
            if (clienteRepository.existsByEmail(clienteActualizado.getEmail())) {
                throw new RuntimeException("Ya existe un cliente con ese email");
            }
        }
        
        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setApellido(clienteActualizado.getApellido());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setDireccion(clienteActualizado.getDireccion());
        cliente.setDistrito(clienteActualizado.getDistrito());
        cliente.setCiudad(clienteActualizado.getCiudad());
        cliente.setMetodoPagoPreferido(clienteActualizado.getMetodoPagoPreferido());
        cliente.setNotas(clienteActualizado.getNotas());
        
        return clienteRepository.save(cliente);
    }
    
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }
    
    public void actualizarTotalCompras(Long clienteId, BigDecimal montoCompra) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        BigDecimal totalActual = cliente.getTotalCompras() != null ? cliente.getTotalCompras() : BigDecimal.ZERO;
        cliente.setTotalCompras(totalActual.add(montoCompra));
        cliente.setUltimaCompra(LocalDateTime.now());
        
        clienteRepository.save(cliente);
    }
}