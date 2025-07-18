package com.pattymoda.service;

import com.pattymoda.entity.Categoria;
import com.pattymoda.repository.CategoriaRepository;
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
public class CategoriaService extends BaseService<Categoria, Long> {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        super(categoriaRepository);
        this.categoriaRepository = categoriaRepository;
    }

    @Cacheable("categorias")
    public Optional<Categoria> findByCodigo(String codigo) {
        return categoriaRepository.findByCodigo(codigo);
    }

    public boolean existsByCodigo(String codigo) {
        return categoriaRepository.existsByCodigo(codigo);
    }

    public Page<Categoria> findByActivo(Boolean activo, Pageable pageable) {
        return categoriaRepository.findByActivo(activo, pageable);
    }

    @Cacheable("categorias")
    public List<Categoria> findCategoriasRaiz() {
        return categoriaRepository.findCategoriasRaiz();
    }

    public List<Categoria> findSubcategorias(Long padreId) {
        return categoriaRepository.findSubcategorias(padreId);
    }

    public Page<Categoria> buscarCategorias(String busqueda, Pageable pageable) {
        return categoriaRepository.buscarCategorias(busqueda, pageable);
    }

    public long countCategoriasActivas() {
        return categoriaRepository.countCategoriasActivas();
    }

    @Override
    public Categoria save(Categoria categoria) {
        // Validaciones específicas para categorías
        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        // Generar código automáticamente si no se proporciona
        if (categoria.getCodigo() == null || categoria.getCodigo().trim().isEmpty()) {
            categoria.setCodigo(generarCodigo(categoria.getNombre()));
        }

        // Verificar si el código ya existe (excepto para actualizaciones)
        if (categoria.getId() == null && existsByCodigo(categoria.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una categoría con el código: " + categoria.getCodigo());
        }

        return super.save(categoria);
    }

    @Override
    public void deleteById(Long id) {
        Categoria categoria = findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

        // Verificar si la categoría tiene productos asociados
        // Aquí podrías agregar validaciones adicionales

        categoria.setActivo(false);
        save(categoria);
    }

    private String generarCodigo(String nombre) {
        // Generar código basado en el nombre
        String codigo = nombre.toUpperCase()
                .replaceAll("[^A-Z0-9]", "")
                .substring(0, Math.min(nombre.length(), 10));
        
        // Asegurar que sea único
        int contador = 1;
        String codigoOriginal = codigo;
        while (existsByCodigo(codigo)) {
            codigo = codigoOriginal + contador;
            contador++;
        }
        
        return codigo;
    }
}