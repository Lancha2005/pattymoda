package com.pattymoda.service;

import com.pattymoda.entity.Venta;
import com.pattymoda.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VentaService extends BaseService<Venta, Long> {

    private final VentaRepository ventaRepository;

    @Autowired
    public VentaService(VentaRepository ventaRepository) {
        super(ventaRepository);
        this.ventaRepository = ventaRepository;
    }

    public Optional<Venta> findByNumeroVenta(String numeroVenta) {
        return ventaRepository.findByNumeroVenta(numeroVenta);
    }

    public boolean existsByNumeroVenta(String numeroVenta) {
        return ventaRepository.existsByNumeroVenta(numeroVenta);
    }

    public Page<Venta> findByClienteId(Long clienteId, Pageable pageable) {
        return ventaRepository.findByClienteId(clienteId, pageable);
    }

    public Page<Venta> findByEstado(Venta.EstadoVenta estado, Pageable pageable) {
        return ventaRepository.findByEstado(estado, pageable);
    }

    public Page<Venta> findByVendedorId(Long vendedorId, Pageable pageable) {
        return ventaRepository.findByVendedorId(vendedorId, pageable);
    }

    public Page<Venta> findByCajeroId(Long cajeroId, Pageable pageable) {
        return ventaRepository.findByCajeroId(cajeroId, pageable);
    }

    public Page<Venta> findByCanalVentaId(Long canalVentaId, Pageable pageable) {
        return ventaRepository.findByCanalVentaId(canalVentaId, pageable);
    }

    public Page<Venta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin, pageable);
    }

    public List<Venta> findByClienteIdAndEstado(Long clienteId, Venta.EstadoVenta estado) {
        return ventaRepository.findByClienteIdAndEstado(clienteId, estado);
    }

    public BigDecimal sumTotalVentasPagadas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.sumTotalVentasPagadas(fechaInicio, fechaFin);
    }

    public long countVentasPagadas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.countVentasPagadas(fechaInicio, fechaFin);
    }

    public List<Venta> findVentasVencidas(LocalDate fecha) {
        return ventaRepository.findVentasVencidas(fecha);
    }

    @Override
    public Venta save(Venta venta) {
        // Validaciones específicas para ventas
        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (venta.getFecha() == null) {
            venta.setFecha(LocalDateTime.now());
        }

        // Generar número de venta automáticamente si no se proporciona
        if (venta.getNumeroVenta() == null || venta.getNumeroVenta().trim().isEmpty()) {
            venta.setNumeroVenta(generarNumeroVenta());
        }

        // Verificar si el número ya existe (excepto para actualizaciones)
        if (venta.getId() == null && existsByNumeroVenta(venta.getNumeroVenta())) {
            throw new IllegalArgumentException("Ya existe una venta con el número: " + venta.getNumeroVenta());
        }

        // Calcular totales si no están establecidos
        if (venta.getTotal() == null) {
            venta.setTotal(venta.getSubtotal() != null ? venta.getSubtotal() : BigDecimal.ZERO);
        }

        return super.save(venta);
    }

    public void cambiarEstado(Long id, Venta.EstadoVenta estado) {
        Venta venta = findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
        venta.setEstado(estado);
        save(venta);
    }

    public void anularVenta(Long id) {
        cambiarEstado(id, Venta.EstadoVenta.ANULADA);
    }

    private String generarNumeroVenta() {
        // Generar número secuencial con formato VEN-YYYYMMDD-NNNN
        LocalDate hoy = LocalDate.now();
        String fecha = hoy.toString().replace("-", "");
        long count = ventaRepository.count();
        return String.format("VEN-%s-%04d", fecha, count + 1);
    }
}