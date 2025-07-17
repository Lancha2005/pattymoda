package com.tienda.repository;

import com.tienda.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByActivoTrue();
    boolean existsByEmail(String email);
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}