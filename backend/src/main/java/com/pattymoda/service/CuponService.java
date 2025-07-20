package com.pattymoda.service;

import com.pattymoda.entity.Cupon;
import com.pattymoda.entity.CuponUso;
import com.pattymoda.entity.Cliente;
import com.pattymoda.entity.Venta;
import com.pattymoda.repository.CuponRepository;
import com.pattymoda.repository.CuponUsoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CuponService extends BaseService<Cupon, Long> {

    private final CuponRepository cuponRepository;
    private final CuponUsoRepository cuponUsoRepository;

    @Autowired
    public CuponService(CuponRepository cuponRepository, CuponUsoRepository cuponUsoRepository) {
        super(cuponRepository);
        this.cuponRepository = cuponRepository;
        this.cuponUsoRepository = cuponUsoRepository;
    }

    public Optional<Cupon> findByCodigo(String codigo) {
        return cuponRepository.findByCodigo(codigo);
    }

    public List<Cupon> getCuponesDisponibles() {
        return cuponRepository.findCuponesDisponibles(LocalDate.now());
    }

    public Optional<Cupon> validarCupon(String codigo, Cliente cliente, BigDecimal montoCompra) {
        Optional<Cupon> cuponOpt = cuponRepository.findCuponValido(codigo, LocalDate.now());
        
        if (cuponOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Cupon cupon = cuponOpt.get();
        
        // Validar monto mínimo
        if (cupon.getMontoMinimoCompra() != null && 
            montoCompra.compareTo(cupon.getMontoMinimoCompra()) < 0) {
            return Optional.empty();
        }
        
        // Validar usos máximos
        if (cupon.getCantidadMaximaUsos() != null && 
            cupon.getUsosActuales() >= cupon.getCantidadMaximaUsos()) {
            return Optional.empty();
        }
        
        // Validar usos por cliente
        long usosCliente = cuponUsoRepository.countByClienteIdAndCuponId(cliente.getId(), cupon.getId());
        if (cupon.getUsosPorCliente() != null && usosCliente >= cupon.getUsosPorCliente()) {
            return Optional.empty();
        }
        
        return Optional.of(cupon);
    }

    public BigDecimal calcularDescuento(Cupon cupon, BigDecimal montoCompra) {
        BigDecimal descuento = BigDecimal.ZERO;
        
        switch (cupon.getTipoDescuento()) {
            case PORCENTAJE:
                descuento = montoCompra.multiply(cupon.getPorcentajeDescuento())
                    .divide(BigDecimal.valueOf(100));
                if (cupon.getDescuentoMaximo() != null && 
                    descuento.compareTo(cupon.getDescuentoMaximo()) > 0) {
                    descuento = cupon.getDescuentoMaximo();
                }
                break;
            case MONTO_FIJO:
                descuento = cupon.getValorDescuento();
                break;
            case ENVIO_GRATIS:
                // Lógica específica para envío gratis
                descuento = BigDecimal.ZERO; // Se maneja en el cálculo de envío
                break;
        }
        
        return descuento;
    }

    public void registrarUso(Cupon cupon, Cliente cliente, Venta venta, BigDecimal montoDescuento) {
        // Registrar uso del cupón
        CuponUso uso = new CuponUso();
        uso.setCupon(cupon);
        uso.setCliente(cliente);
        uso.setVenta(venta);
        uso.setMontoDescuento(montoDescuento);
        uso.setFechaUso(LocalDateTime.now());
        
        cuponUsoRepository.save(uso);
        
        // Actualizar contador de usos
        cupon.setUsosActuales(cupon.getUsosActuales() + 1);
        save(cupon);
    }

    @Override
    public Cupon save(Cupon cupon) {
        if (cupon.getCodigo() == null || cupon.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del cupón es obligatorio");
        }
        if (cupon.getNombre() == null || cupon.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cupón es obligatorio");
        }
        if (cupon.getTipoDescuento() == null) {
            throw new IllegalArgumentException("El tipo de descuento es obligatorio");
        }
        if (cupon.getFechaInicio() == null || cupon.getFechaFin() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (cupon.getId() == null && cuponRepository.existsByCodigo(cupon.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un cupón con ese código");
        }

        return super.save(cupon);
    }
}