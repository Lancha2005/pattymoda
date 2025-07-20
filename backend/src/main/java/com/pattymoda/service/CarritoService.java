package com.pattymoda.service;

import com.pattymoda.entity.Cliente;
import com.pattymoda.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CarritoService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CARRITO_PREFIX = "carrito:";
    private static final long CARRITO_TTL = 24; // 24 horas

    public void agregarProducto(Long clienteId, Long productoId, Integer cantidad, Long tallaId, Long colorId) {
        String carritoKey = CARRITO_PREFIX + clienteId;
        String itemKey = productoId + ":" + (tallaId != null ? tallaId : "0") + ":" + (colorId != null ? colorId : "0");
        
        Map<String, Object> item = new HashMap<>();
        item.put("productoId", productoId);
        item.put("cantidad", cantidad);
        item.put("tallaId", tallaId);
        item.put("colorId", colorId);
        item.put("fechaAgregado", System.currentTimeMillis());
        
        redisTemplate.opsForHash().put(carritoKey, itemKey, item);
        redisTemplate.expire(carritoKey, CARRITO_TTL, TimeUnit.HOURS);
    }

    public void removerProducto(Long clienteId, Long productoId, Long tallaId, Long colorId) {
        String carritoKey = CARRITO_PREFIX + clienteId;
        String itemKey = productoId + ":" + (tallaId != null ? tallaId : "0") + ":" + (colorId != null ? colorId : "0");
        
        redisTemplate.opsForHash().delete(carritoKey, itemKey);
    }

    public Map<Object, Object> obtenerCarrito(Long clienteId) {
        String carritoKey = CARRITO_PREFIX + clienteId;
        return redisTemplate.opsForHash().entries(carritoKey);
    }

    public void limpiarCarrito(Long clienteId) {
        String carritoKey = CARRITO_PREFIX + clienteId;
        redisTemplate.delete(carritoKey);
    }

    public void actualizarCantidad(Long clienteId, Long productoId, Integer nuevaCantidad, Long tallaId, Long colorId) {
        String carritoKey = CARRITO_PREFIX + clienteId;
        String itemKey = productoId + ":" + (tallaId != null ? tallaId : "0") + ":" + (colorId != null ? colorId : "0");
        
        Map<Object, Object> item = redisTemplate.opsForHash().entries(carritoKey + ":" + itemKey);
        if (!item.isEmpty()) {
            item.put("cantidad", nuevaCantidad);
            redisTemplate.opsForHash().put(carritoKey, itemKey, item);
        }
    }

    public boolean tieneProductos(Long clienteId) {
        String carritoKey = CARRITO_PREFIX + clienteId;
        return redisTemplate.opsForHash().size(carritoKey) > 0;
    }
}