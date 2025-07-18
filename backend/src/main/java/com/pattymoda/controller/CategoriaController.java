package com.pattymoda.controller;

import com.pattymoda.entity.Categoria;
import com.pattymoda.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
@Tag(name = "Categorías", description = "API para gestión de categorías")
@CrossOrigin(origins = "*")
public class CategoriaController extends BaseController<Categoria, Long> {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        super(categoriaService);
        this.categoriaService = categoriaService;
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar categoría por código", description = "Busca una categoría específica por su código")
    public ResponseEntity<Categoria> getCategoriaByCodigo(@PathVariable String codigo) {
        return categoriaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener categorías activas", description = "Retorna todas las categorías activas")
    public ResponseEntity<Page<Categoria>> getCategoriasActivas(Pageable pageable) {
        Page<Categoria> categorias = categoriaService.findByActivo(true, pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/raiz")
    @Operation(summary = "Obtener categorías raíz", description = "Retorna las categorías principales (sin padre)")
    public ResponseEntity<List<Categoria>> getCategoriasRaiz() {
        List<Categoria> categorias = categoriaService.findCategoriasRaiz();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{padreId}/subcategorias")
    @Operation(summary = "Obtener subcategorías", description = "Retorna las subcategorías de una categoría padre")
    public ResponseEntity<List<Categoria>> getSubcategorias(@PathVariable Long padreId) {
        List<Categoria> subcategorias = categoriaService.findSubcategorias(padreId);
        return ResponseEntity.ok(subcategorias);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorías", description = "Busca categorías por nombre o código")
    public ResponseEntity<Page<Categoria>> buscarCategorias(
            @RequestParam String busqueda,
            Pageable pageable) {
        Page<Categoria> categorias = categoriaService.buscarCategorias(busqueda, pageable);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/count/activas")
    @Operation(summary = "Contar categorías activas", description = "Retorna el número total de categorías activas")
    public ResponseEntity<Long> countCategoriasActivas() {
        long count = categoriaService.countCategoriasActivas();
        return ResponseEntity.ok(count);
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría")
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        try {
            Categoria savedCategoria = categoriaService.save(categoria);
            return ResponseEntity.status(201).body(savedCategoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        if (!categoriaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        categoria.setId(id);
        try {
            Categoria updatedCategoria = categoriaService.save(categoria);
            return ResponseEntity.ok(updatedCategoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina lógicamente una categoría (marca como inactiva)")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (!categoriaService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}