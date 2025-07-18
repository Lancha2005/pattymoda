package com.pattymoda.repository;

import com.pattymoda.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends BaseRepository<Cliente, Long> {

    Optional<Cliente> findByCodigoCliente(String codigoCliente);
    
    Optional<Cliente> findByNumeroDocumento(String numeroDocumento);
    
    boolean existsByCodigoCliente(String codigoCliente);
    
    boolean existsByNumeroDocumento(String numeroDocumento);
    
    Page<Cliente> findByActivo(Boolean activo, Pageable pageable);
    
    @Query("SELECT c FROM Cliente c WHERE c.activo = true AND (c.nombre LIKE %:busqueda% OR c.apellido LIKE %:busqueda% OR c.numeroDocumento LIKE %:busqueda% OR c.codigoCliente LIKE %:busqueda%)")
    Page<Cliente> buscarClientes(@Param("busqueda") String busqueda, Pageable pageable);
    
    @Query("SELECT c FROM Cliente c WHERE c.activo = true AND c.tipoDocumento = :tipoDocumento AND c.numeroDocumento = :numeroDocumento")
    Optional<Cliente> findByTipoDocumentoAndNumeroDocumento(@Param("tipoDocumento") Cliente.TipoDocumento tipoDocumento, @Param("numeroDocumento") String numeroDocumento);
    
    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.activo = true")
    long countClientesActivos();
    
    @Query("SELECT c FROM Cliente c WHERE c.activo = true AND c.fechaCreacion >= CURRENT_DATE - 30")
    List<Cliente> findClientesNuevosUltimoMes();
} 