package com.pattymoda.controller;

import com.pattymoda.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public abstract class BaseController<T, ID> {

    protected final BaseService<T, ID> service;

    public BaseController(BaseService<T, ID> service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        List<T> entities = service.findAll();
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<T>> findAll(Pageable pageable) {
        Page<T> entities = service.findAll(pageable);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable ID id) {
        Optional<T> entity = service.findById(id);
        return entity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        T savedEntity = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        T updatedEntity = service.save(entity);
        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = service.count();
        return ResponseEntity.ok(count);
    }
} 