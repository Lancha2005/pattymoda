package com.pattymoda.service;

import com.pattymoda.entity.Cliente;
import com.pattymoda.entity.ListaDeseos;
import com.pattymoda.entity.Producto;
import com.pattymoda.repository.ClienteRepository;
import com.pattymoda.repository.ListaDeseosRepository;
import com.pattymoda.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ListaDeseosService extends BaseService<ListaDeseos, Long> {

    private final ListaDeseosRepository listaDeseosRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public ListaDeseosService(ListaDeseosRepository listaDeseosRepository,
                             ClienteRepository clienteRepository,
                             ProductoRepository productoRepository) {
        super(listaDeseosRepository);
        this.listaDeseosRepository = listaDeseosRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public List<ListaDeseos> getListaDeseosCliente(Long clienteId) {
        return listaDeseosRepository.findByClienteIdAndActivoTrueOrderByPrioridadDescFechaAgregadoDesc(clienteId);
    }

    public Page<ListaDeseos> getListaDeseosClientePaginada(Long clienteId, Pageable pageable) {
        return listaDeseosRepository.findByClienteIdAndActivoTrue(clienteId, pageable);
    }

    public ListaDeseos agregarProducto(Long clienteId, Long productoId) {
        // Verificar si ya existe
        if (listaDeseosRepository.existsByClienteIdAndProductoId(clienteId, productoId)) {
            throw new RuntimeException("El producto ya está en la lista de deseos");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ListaDeseos item = new ListaDeseos();
        item.setCliente(cliente);
        item.setProducto(producto);
        item.setFechaAgregado(LocalDateTime.now());
        item.setActivo(true);

        return save(item);
    }

    public void removerProducto(Long clienteId, Long productoId) {
        Optional<ListaDeseos> item = listaDeseosRepository.findByClienteIdAndProductoId(clienteId, productoId);
        item.ifPresent(listaDeseos -> {
            listaDeseos.setActivo(false);
            save(listaDeseos);
        });
    }

    public boolean existeEnListaDeseos(Long clienteId, Long productoId) {
        return listaDeseosRepository.existsByClienteIdAndProductoId(clienteId, productoId);
    }

    public long contarDeseosProducto(Long productoId) {
        return listaDeseosRepository.countByProducto(productoId);
    }

    public List<Map<String, Object>> getProductosMasDeseados(int limite) {
        List<Object[]> resultados = listaDeseosRepository.findProductosMasDeseados(PageRequest.of(0, limite));
        
        return resultados.stream()
                .map(resultado -> Map.of(
                    "productoId", resultado[0],
                    "cantidadDeseos", resultado[1]
                ))
                .collect(Collectors.toList());
    }

    public List<ListaDeseos> getParaNotificarDisponibilidad(Long productoId) {
        return listaDeseosRepository.findParaNotificarDisponibilidad(productoId);
    }
}