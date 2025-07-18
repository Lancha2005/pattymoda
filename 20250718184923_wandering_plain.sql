-- =====================================================
-- BASE DE DATOS COMPLETA PATTYMODA
-- Compatible con Spring Boot Backend
-- Versión: 1.0.0
-- Fecha: 2024
-- =====================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

-- =====================================================
-- CREAR BASE DE DATOS
-- =====================================================
CREATE DATABASE IF NOT EXISTS `pattymoda_nueva` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `pattymoda_nueva`;

-- =====================================================
-- TABLA: paises
-- =====================================================
CREATE TABLE `paises` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_iso` varchar(3) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `codigo_telefono` varchar(5) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_paises_codigo_iso` (`codigo_iso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: departamentos
-- =====================================================
CREATE TABLE `departamentos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pais_id` bigint(20) NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_departamentos_pais` (`pais_id`),
  CONSTRAINT `FK_departamentos_pais` FOREIGN KEY (`pais_id`) REFERENCES `paises` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: provincias
-- =====================================================
CREATE TABLE `provincias` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `departamento_id` bigint(20) NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_provincias_departamento` (`departamento_id`),
  CONSTRAINT `FK_provincias_departamento` FOREIGN KEY (`departamento_id`) REFERENCES `departamentos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: distritos
-- =====================================================
CREATE TABLE `distritos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `provincia_id` bigint(20) NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `codigo_postal` varchar(10) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_distritos_provincia` (`provincia_id`),
  CONSTRAINT `FK_distritos_provincia` FOREIGN KEY (`provincia_id`) REFERENCES `provincias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: roles
-- =====================================================
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(30) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `nivel_acceso` int(11) DEFAULT 1,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_roles_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: usuarios
-- =====================================================
CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_empleado` varchar(20) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol_id` bigint(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `salario_base` decimal(10,2) DEFAULT NULL,
  `comision_porcentaje` decimal(5,2) DEFAULT 0.00,
  `foto_perfil` varchar(255) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `ultimo_acceso` timestamp NULL DEFAULT NULL,
  `intentos_login_fallidos` int(11) DEFAULT 0,
  `bloqueado_hasta` timestamp NULL DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_usuarios_email` (`email`),
  UNIQUE KEY `UK_usuarios_codigo_empleado` (`codigo_empleado`),
  KEY `FK_usuarios_rol` (`rol_id`),
  CONSTRAINT `FK_usuarios_rol` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: permisos
-- =====================================================
CREATE TABLE `permisos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) NOT NULL,
  `modulo` varchar(50) NOT NULL,
  `accion` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_permisos_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: roles_permisos
-- =====================================================
CREATE TABLE `roles_permisos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rol_id` bigint(20) NOT NULL,
  `permiso_id` bigint(20) NOT NULL,
  `concedido` tinyint(1) DEFAULT 1,
  `fecha_asignacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `asignado_por` bigint(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_roles_permisos_rol` (`rol_id`),
  KEY `FK_roles_permisos_permiso` (`permiso_id`),
  CONSTRAINT `FK_roles_permisos_rol` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK_roles_permisos_permiso` FOREIGN KEY (`permiso_id`) REFERENCES `permisos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: categorias
-- =====================================================
CREATE TABLE `categorias` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `categoria_padre_id` bigint(20) DEFAULT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `icono` varchar(100) DEFAULT NULL,
  `orden` int(11) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_categorias_codigo` (`codigo`),
  KEY `FK_categorias_padre` (`categoria_padre_id`),
  CONSTRAINT `FK_categorias_padre` FOREIGN KEY (`categoria_padre_id`) REFERENCES `categorias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: marcas
-- =====================================================
CREATE TABLE `marcas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `sitio_web` varchar(200) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_marcas_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: tallas
-- =====================================================
CREATE TABLE `tallas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(10) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `categoria_talla` enum('ROPA_MUJER','ROPA_HOMBRE','CALZADO_MUJER','CALZADO_HOMBRE','ACCESORIOS','INFANTIL') NOT NULL,
  `orden_visualizacion` int(11) DEFAULT NULL,
  `medidas_cm` varchar(200) DEFAULT NULL,
  `equivalencia_internacional` varchar(50) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_tallas_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: colores
-- =====================================================
CREATE TABLE `colores` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `codigo_hex` varchar(7) NOT NULL,
  `codigo_rgb` varchar(20) DEFAULT NULL,
  `familia_color` varchar(30) DEFAULT NULL,
  `orden_visualizacion` int(11) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_colores_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: unidades_medida
-- =====================================================
CREATE TABLE `unidades_medida` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(10) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `simbolo` varchar(10) DEFAULT NULL,
  `tipo` enum('PESO','LONGITUD','VOLUMEN','UNIDAD','TIEMPO') DEFAULT NULL,
  `factor_conversion` decimal(10,6) DEFAULT 1.000000,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_unidades_medida_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: productos
-- =====================================================
CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_producto` varchar(50) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `sku` varchar(100) NOT NULL,
  `codigo_barras` varchar(50) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `descripcion_corta` varchar(500) DEFAULT NULL,
  `categoria_id` bigint(20) NOT NULL,
  `marca_id` bigint(20) DEFAULT NULL,
  `modelo` varchar(100) DEFAULT NULL,
  `peso` decimal(8,3) DEFAULT NULL,
  `dimensiones` varchar(100) DEFAULT NULL,
  `imagen_principal` varchar(255) DEFAULT NULL,
  `requiere_talla` tinyint(1) DEFAULT 1,
  `requiere_color` tinyint(1) DEFAULT 1,
  `es_perecedero` tinyint(1) DEFAULT 0,
  `tiempo_entrega_dias` int(11) DEFAULT 1,
  `garantia_meses` int(11) DEFAULT 0,
  `destacado` tinyint(1) DEFAULT 0,
  `nuevo` tinyint(1) DEFAULT 1,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_productos_codigo` (`codigo_producto`),
  UNIQUE KEY `UK_productos_sku` (`sku`),
  UNIQUE KEY `UK_productos_codigo_barras` (`codigo_barras`),
  KEY `FK_productos_categoria` (`categoria_id`),
  KEY `FK_productos_marca` (`marca_id`),
  CONSTRAINT `FK_productos_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `FK_productos_marca` FOREIGN KEY (`marca_id`) REFERENCES `marcas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: productos_precios
-- =====================================================
CREATE TABLE `productos_precios` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) NOT NULL,
  `precio_venta` decimal(10,2) NOT NULL,
  `precio_oferta` decimal(10,2) DEFAULT NULL,
  `costo` decimal(10,2) DEFAULT NULL,
  `margen_porcentaje` decimal(5,2) DEFAULT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `motivo_cambio` varchar(200) DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_productos_precios_producto` (`producto_id`),
  KEY `FK_productos_precios_usuario` (`usuario_id`),
  CONSTRAINT `FK_productos_precios_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FK_productos_precios_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: productos_tallas
-- =====================================================
CREATE TABLE `productos_tallas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) NOT NULL,
  `talla_id` bigint(20) NOT NULL,
  `stock_talla` int(11) NOT NULL DEFAULT 0,
  `precio_adicional` decimal(10,2) DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_productos_tallas_producto` (`producto_id`),
  KEY `FK_productos_tallas_talla` (`talla_id`),
  CONSTRAINT `FK_productos_tallas_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FK_productos_tallas_talla` FOREIGN KEY (`talla_id`) REFERENCES `tallas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: productos_colores
-- =====================================================
CREATE TABLE `productos_colores` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) NOT NULL,
  `color_id` bigint(20) NOT NULL,
  `stock_color` int(11) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_productos_colores_producto` (`producto_id`),
  KEY `FK_productos_colores_color` (`color_id`),
  CONSTRAINT `FK_productos_colores_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FK_productos_colores_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: productos_imagenes
-- =====================================================
CREATE TABLE `productos_imagenes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) NOT NULL,
  `url_imagen` varchar(255) NOT NULL,
  `alt_text` varchar(255) DEFAULT NULL,
  `orden` int(11) DEFAULT 0,
  `es_principal` tinyint(1) DEFAULT 0,
  `color_id` bigint(20) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_productos_imagenes_producto` (`producto_id`),
  KEY `FK_productos_imagenes_color` (`color_id`),
  CONSTRAINT `FK_productos_imagenes_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FK_productos_imagenes_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: productos_seo
-- =====================================================
CREATE TABLE `productos_seo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) NOT NULL,
  `meta_title` varchar(255) DEFAULT NULL,
  `meta_description` varchar(500) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `tags` longtext DEFAULT NULL,
  `palabras_clave` text DEFAULT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_productos_seo_producto` (`producto_id`),
  UNIQUE KEY `UK_productos_seo_slug` (`slug`),
  CONSTRAINT `FK_productos_seo_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: clientes
-- =====================================================
CREATE TABLE `clientes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_cliente` varchar(20) DEFAULT NULL,
  `tipo_documento` enum('DNI','RUC','PASAPORTE','CARNET_EXTRANJERIA') DEFAULT 'DNI',
  `numero_documento` varchar(20) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `razon_social` varchar(200) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  `genero` enum('M','F','OTRO') DEFAULT NULL,
  `estado_civil` enum('SOLTERO','CASADO','DIVORCIADO','VIUDO','OTRO') DEFAULT NULL,
  `ocupacion` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_clientes_codigo` (`codigo_cliente`),
  UNIQUE KEY `UK_clientes_documento` (`numero_documento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: clientes_comercial
-- =====================================================
CREATE TABLE `clientes_comercial` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint(20) NOT NULL,
  `tipo_cliente` enum('REGULAR','VIP','MAYORISTA','MINORISTA','CORPORATIVO') NOT NULL,
  `total_compras` decimal(12,2) DEFAULT 0.00,
  `cantidad_compras` int(11) DEFAULT 0,
  `ultima_compra` timestamp NULL DEFAULT NULL,
  `limite_credito` decimal(10,2) DEFAULT 0.00,
  `descuento_personalizado` decimal(5,2) DEFAULT 0.00,
  `puntos_disponibles` int(11) DEFAULT 0,
  `nivel_cliente` enum('BRONCE','PLATA','ORO','PLATINO') DEFAULT 'BRONCE',
  `vendedor_asignado_id` bigint(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_clientes_comercial_cliente` (`cliente_id`),
  KEY `FK_clientes_comercial_vendedor` (`vendedor_asignado_id`),
  CONSTRAINT `FK_clientes_comercial_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_clientes_comercial_vendedor` FOREIGN KEY (`vendedor_asignado_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: clientes_contacto
-- =====================================================
CREATE TABLE `clientes_contacto` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint(20) NOT NULL,
  `tipo_contacto` enum('TELEFONO','CELULAR','EMAIL','WHATSAPP','FACEBOOK','INSTAGRAM','DIRECCION','OTROS') NOT NULL,
  `valor` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `es_principal` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_clientes_contacto_cliente` (`cliente_id`),
  CONSTRAINT `FK_clientes_contacto_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: clientes_direcciones
-- =====================================================
CREATE TABLE `clientes_direcciones` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint(20) NOT NULL,
  `tipo_direccion` varchar(50) DEFAULT NULL,
  `direccion` varchar(255) NOT NULL,
  `referencia` varchar(200) DEFAULT NULL,
  `distrito_id` bigint(20) DEFAULT NULL,
  `codigo_postal` varchar(10) DEFAULT NULL,
  `es_principal` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_clientes_direcciones_cliente` (`cliente_id`),
  KEY `FK_clientes_direcciones_distrito` (`distrito_id`),
  CONSTRAINT `FK_clientes_direcciones_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_clientes_direcciones_distrito` FOREIGN KEY (`distrito_id`) REFERENCES `distritos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: clientes_preferencias
-- =====================================================
CREATE TABLE `clientes_preferencias` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint(20) NOT NULL,
  `categoria_preferida_id` bigint(20) DEFAULT NULL,
  `talla_preferida_id` bigint(20) DEFAULT NULL,
  `color_preferido_id` bigint(20) DEFAULT NULL,
  `marca_preferida_id` bigint(20) DEFAULT NULL,
  `canal_preferido_id` bigint(20) DEFAULT NULL,
  `recibir_promociones` tinyint(1) DEFAULT 1,
  `recibir_newsletter` tinyint(1) DEFAULT 1,
  `frecuencia_contacto` enum('DIARIO','SEMANAL','MENSUAL','NUNCA') DEFAULT 'MENSUAL',
  `notas` text DEFAULT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_clientes_preferencias_cliente` (`cliente_id`),
  KEY `FK_clientes_preferencias_categoria` (`categoria_preferida_id`),
  KEY `FK_clientes_preferencias_talla` (`talla_preferida_id`),
  KEY `FK_clientes_preferencias_color` (`color_preferido_id`),
  KEY `FK_clientes_preferencias_marca` (`marca_preferida_id`),
  CONSTRAINT `FK_clientes_preferencias_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_clientes_preferencias_categoria` FOREIGN KEY (`categoria_preferida_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `FK_clientes_preferencias_talla` FOREIGN KEY (`talla_preferida_id`) REFERENCES `tallas` (`id`),
  CONSTRAINT `FK_clientes_preferencias_color` FOREIGN KEY (`color_preferido_id`) REFERENCES `colores` (`id`),
  CONSTRAINT `FK_clientes_preferencias_marca` FOREIGN KEY (`marca_preferida_id`) REFERENCES `marcas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: programa_lealtad
-- =====================================================
CREATE TABLE `programa_lealtad` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint(20) NOT NULL,
  `puntos_acumulados` int(11) DEFAULT 0,
  `puntos_canjeados` int(11) DEFAULT 0,
  `nivel_cliente` enum('BRONCE','PLATA','ORO','PLATINO') DEFAULT 'BRONCE',
  `fecha_ultimo_movimiento` timestamp NULL DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_programa_lealtad_cliente` (`cliente_id`),
  CONSTRAINT `FK_programa_lealtad_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: canales_venta
-- =====================================================
CREATE TABLE `canales_venta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `comision_porcentaje` decimal(5,2) DEFAULT 0.00,
  `requiere_entrega` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_canales_venta_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: ventas
-- =====================================================
CREATE TABLE `ventas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero_venta` varchar(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp(),
  `subtotal` decimal(12,2) DEFAULT 0.00,
  `total` decimal(12,2) NOT NULL DEFAULT 0.00,
  `estado` enum('PENDIENTE','PAGADA','PARCIALMENTE_PAGADA','ANULADA','DEVUELTA') DEFAULT 'PENDIENTE',
  `canal_venta_id` bigint(20) DEFAULT NULL,
  `vendedor_id` bigint(20) DEFAULT NULL,
  `cajero_id` bigint(20) DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `notas_internas` text DEFAULT NULL,
  `cantidad_items` int(11) DEFAULT 0,
  `peso_total` decimal(8,3) DEFAULT NULL,
  `comision_vendedor` decimal(10,2) DEFAULT 0.00,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ventas_numero` (`numero_venta`),
  KEY `FK_ventas_cliente` (`cliente_id`),
  KEY `FK_ventas_canal` (`canal_venta_id`),
  KEY `FK_ventas_vendedor` (`vendedor_id`),
  KEY `FK_ventas_cajero` (`cajero_id`),
  CONSTRAINT `FK_ventas_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_ventas_canal` FOREIGN KEY (`canal_venta_id`) REFERENCES `canales_venta` (`id`),
  CONSTRAINT `FK_ventas_vendedor` FOREIGN KEY (`vendedor_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK_ventas_cajero` FOREIGN KEY (`cajero_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: detalle_venta
-- =====================================================
CREATE TABLE `detalle_venta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `venta_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `talla_id` bigint(20) DEFAULT NULL,
  `color_id` bigint(20) DEFAULT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `descuento_monto` decimal(10,2) DEFAULT 0.00,
  `subtotal` decimal(10,2) NOT NULL,
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_detalle_venta_venta` (`venta_id`),
  KEY `FK_detalle_venta_producto` (`producto_id`),
  KEY `FK_detalle_venta_talla` (`talla_id`),
  KEY `FK_detalle_venta_color` (`color_id`),
  CONSTRAINT `FK_detalle_venta_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_detalle_venta_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FK_detalle_venta_talla` FOREIGN KEY (`talla_id`) REFERENCES `tallas` (`id`),
  CONSTRAINT `FK_detalle_venta_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: ventas_facturacion
-- =====================================================
CREATE TABLE `ventas_facturacion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `venta_id` bigint(20) NOT NULL,
  `tipo_comprobante` enum('BOLETA','FACTURA','NOTA_VENTA','TICKET') DEFAULT 'BOLETA',
  `serie_comprobante` varchar(10) DEFAULT NULL,
  `numero_comprobante` varchar(20) DEFAULT NULL,
  `fecha_emision` date DEFAULT NULL,
  `moneda` varchar(3) DEFAULT 'PEN',
  `tipo_cambio` decimal(8,4) DEFAULT 1.0000,
  `direccion_facturacion` varchar(255) DEFAULT NULL,
  `datos_adicionales` longtext DEFAULT NULL,
  `hash_comprobante` varchar(255) DEFAULT NULL,
  `enviado_sunat` tinyint(1) DEFAULT 0,
  `fecha_envio_sunat` timestamp NULL DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ventas_facturacion_venta` (`venta_id`),
  UNIQUE KEY `UK_ventas_facturacion_comprobante` (`numero_comprobante`),
  CONSTRAINT `FK_ventas_facturacion_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: ventas_entrega
-- =====================================================
CREATE TABLE `ventas_entrega` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `venta_id` bigint(20) NOT NULL,
  `tipo_entrega` enum('RECOJO_TIENDA','DELIVERY','ENVIO_COURIER','ENVIO_POSTAL') DEFAULT 'RECOJO_TIENDA',
  `direccion_entrega` varchar(255) DEFAULT NULL,
  `distrito_entrega_id` bigint(20) DEFAULT NULL,
  `fecha_programada` date DEFAULT NULL,
  `fecha_entrega_real` timestamp NULL DEFAULT NULL,
  `costo_envio` decimal(10,2) DEFAULT 0.00,
  `transportista` varchar(100) DEFAULT NULL,
  `numero_guia` varchar(50) DEFAULT NULL,
  `estado_entrega` enum('PENDIENTE','EN_TRANSITO','ENTREGADO','DEVUELTO','CANCELADO') DEFAULT 'PENDIENTE',
  `observaciones_entrega` varchar(255) DEFAULT NULL,
  `recibido_por` varchar(100) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ventas_entrega_venta` (`venta_id`),
  KEY `FK_ventas_entrega_distrito` (`distrito_entrega_id`),
  CONSTRAINT `FK_ventas_entrega_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_ventas_entrega_distrito` FOREIGN KEY (`distrito_entrega_id`) REFERENCES `distritos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: metodos_pago_venta
-- =====================================================
CREATE TABLE `metodos_pago_venta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `venta_id` bigint(20) NOT NULL,
  `tipo_pago` enum('EFECTIVO','TARJETA_DEBITO','TARJETA_CREDITO','YAPE','PLIN','TRANSFERENCIA','CHEQUE','CREDITO') NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `referencia` varchar(100) DEFAULT NULL,
  `numero_operacion` varchar(50) DEFAULT NULL,
  `banco` varchar(100) DEFAULT NULL,
  `tipo_tarjeta` varchar(50) DEFAULT NULL,
  `ultimos_4_digitos` varchar(4) DEFAULT NULL,
  `numero_cuotas` int(11) DEFAULT 1,
  `tasa_interes` decimal(5,2) DEFAULT 0.00,
  `comision` decimal(10,2) DEFAULT 0.00,
  `estado` enum('PENDIENTE','APROBADO','RECHAZADO','ANULADO') NOT NULL DEFAULT 'APROBADO',
  `fecha_pago` timestamp NULL DEFAULT NULL,
  `notas` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_metodos_pago_venta_venta` (`venta_id`),
  CONSTRAINT `FK_metodos_pago_venta_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: ventas_descuentos
-- =====================================================
CREATE TABLE `ventas_descuentos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `venta_id` bigint(20) NOT NULL,
  `tipo_descuento` enum('PORCENTAJE','MONTO_FIJO','PROMOCION','CLIENTE_VIP') NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `porcentaje` decimal(5,2) DEFAULT NULL,
  `monto` decimal(10,2) NOT NULL,
  `promocion_id` bigint(20) DEFAULT NULL,
  `aplicado_por` bigint(20) DEFAULT NULL,
  `fecha_aplicacion` timestamp NULL DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ventas_descuentos_venta` (`venta_id`),
  KEY `FK_ventas_descuentos_aplicado_por` (`aplicado_por`),
  CONSTRAINT `FK_ventas_descuentos_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_ventas_descuentos_aplicado_por` FOREIGN KEY (`aplicado_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: transacciones_puntos
-- =====================================================
CREATE TABLE `transacciones_puntos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cliente_id` bigint(20) NOT NULL,
  `tipo_transaccion` enum('GANANCIA','CANJE','EXPIRACION','AJUSTE','BONO') NOT NULL,
  `puntos` int(11) NOT NULL,
  `descripcion` varchar(500) DEFAULT NULL,
  `venta_id` bigint(20) DEFAULT NULL,
  `fecha_transaccion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_expiracion` date DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_transacciones_puntos_cliente` (`cliente_id`),
  KEY `FK_transacciones_puntos_venta` (`venta_id`),
  KEY `FK_transacciones_puntos_usuario` (`usuario_id`),
  CONSTRAINT `FK_transacciones_puntos_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_transacciones_puntos_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_transacciones_puntos_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: proveedores
-- =====================================================
CREATE TABLE `proveedores` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo_proveedor` varchar(20) DEFAULT NULL,
  `razon_social` varchar(200) NOT NULL,
  `nombre_comercial` varchar(200) DEFAULT NULL,
  `tipo_documento` enum('RUC','DNI','PASAPORTE') DEFAULT 'RUC',
  `numero_documento` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `distrito_id` bigint(20) DEFAULT NULL,
  `contacto_principal` varchar(100) DEFAULT NULL,
  `telefono_contacto` varchar(20) DEFAULT NULL,
  `email_contacto` varchar(100) DEFAULT NULL,
  `condiciones_pago` varchar(200) DEFAULT NULL,
  `tiempo_entrega_dias` int(11) DEFAULT NULL,
  `calificacion` decimal(3,2) DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1,
  `notas` text DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_proveedores_codigo` (`codigo_proveedor`),
  UNIQUE KEY `UK_proveedores_documento` (`numero_documento`),
  KEY `FK_proveedores_distrito` (`distrito_id`),
  CONSTRAINT `FK_proveedores_distrito` FOREIGN KEY (`distrito_id`) REFERENCES `distritos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: compras
-- =====================================================
CREATE TABLE `compras` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero_compra` varchar(20) NOT NULL,
  `proveedor_id` bigint(20) NOT NULL,
  `fecha_compra` date NOT NULL,
  `fecha_entrega_esperada` date DEFAULT NULL,
  `fecha_entrega_real` date DEFAULT NULL,
  `subtotal` decimal(12,2) NOT NULL DEFAULT 0.00,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `descuento_monto` decimal(10,2) DEFAULT 0.00,
  `impuesto_porcentaje` decimal(5,2) DEFAULT 0.00,
  `impuesto_monto` decimal(10,2) DEFAULT 0.00,
  `total` decimal(12,2) NOT NULL,
  `estado` enum('PENDIENTE','CONFIRMADA','PARCIAL','RECIBIDA','CANCELADA') DEFAULT 'PENDIENTE',
  `tipo_comprobante` enum('FACTURA','BOLETA','RECIBO','ORDEN_COMPRA') DEFAULT 'FACTURA',
  `numero_comprobante` varchar(50) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_compras_numero` (`numero_compra`),
  KEY `FK_compras_proveedor` (`proveedor_id`),
  CONSTRAINT `FK_compras_proveedor` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: detalle_compra
-- =====================================================
CREATE TABLE `detalle_compra` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `compra_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `cantidad_pedida` int(11) NOT NULL,
  `cantidad_recibida` int(11) DEFAULT 0,
  `precio_unitario` decimal(10,2) NOT NULL,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `descuento_monto` decimal(10,2) DEFAULT 0.00,
  `subtotal` decimal(10,2) NOT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `lote` varchar(50) DEFAULT NULL,
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_detalle_compra_compra` (`compra_id`),
  KEY `FK_detalle_compra_producto` (`producto_id`),
  CONSTRAINT `FK_detalle_compra_compra` FOREIGN KEY (`compra_id`) REFERENCES `compras` (`id`),
  CONSTRAINT `FK_detalle_compra_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: devoluciones
-- =====================================================
CREATE TABLE `devoluciones` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero_devolucion` varchar(20) NOT NULL,
  `venta_id` bigint(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `fecha_devolucion` timestamp NOT NULL DEFAULT current_timestamp(),
  `motivo` enum('DEFECTO_PRODUCTO','TALLA_INCORRECTA','COLOR_INCORRECTO','NO_SATISFECHO','CAMBIO_OPINION','GARANTIA','OTRO') NOT NULL,
  `descripcion_motivo` text DEFAULT NULL,
  `subtotal_devuelto` decimal(10,2) NOT NULL DEFAULT 0.00,
  `total_devuelto` decimal(10,2) NOT NULL,
  `estado` enum('PENDIENTE','APROBADA','RECHAZADA','PROCESADA') DEFAULT 'PENDIENTE',
  `tipo_devolucion` enum('REEMBOLSO','CAMBIO','NOTA_CREDITO') DEFAULT 'REEMBOLSO',
  `autorizado_por` bigint(20) DEFAULT NULL,
  `procesado_por` bigint(20) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_devoluciones_numero` (`numero_devolucion`),
  KEY `FK_devoluciones_venta` (`venta_id`),
  KEY `FK_devoluciones_cliente` (`cliente_id`),
  KEY `FK_devoluciones_autorizado_por` (`autorizado_por`),
  KEY `FK_devoluciones_procesado_por` (`procesado_por`),
  CONSTRAINT `FK_devoluciones_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_devoluciones_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_devoluciones_autorizado_por` FOREIGN KEY (`autorizado_por`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK_devoluciones_procesado_por` FOREIGN KEY (`procesado_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: detalle_devolucion
-- =====================================================
CREATE TABLE `detalle_devolucion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `devolucion_id` bigint(20) NOT NULL,
  `detalle_venta_id` bigint(20) NOT NULL,
  `cantidad_devuelta` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL,
  `condicion_producto` enum('NUEVO','USADO_BUENO','USADO_REGULAR','DEFECTUOSO') DEFAULT 'NUEVO',
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_detalle_devolucion_devolucion` (`devolucion_id`),
  KEY `FK_detalle_devolucion_detalle_venta` (`detalle_venta_id`),
  CONSTRAINT `FK_detalle_devolucion_devolucion` FOREIGN KEY (`devolucion_id`) REFERENCES `devoluciones` (`id`),
  CONSTRAINT `FK_detalle_devolucion_detalle_venta` FOREIGN KEY (`detalle_venta_id`) REFERENCES `detalle_venta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: cotizaciones
-- =====================================================
CREATE TABLE `cotizaciones` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero_cotizacion` varchar(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `fecha_cotizacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `subtotal` decimal(12,2) DEFAULT 0.00,
  `total` decimal(12,2) NOT NULL DEFAULT 0.00,
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cotizaciones_numero` (`numero_cotizacion`),
  KEY `FK_cotizaciones_cliente` (`cliente_id`),
  CONSTRAINT `FK_cotizaciones_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: detalle_cotizacion
-- =====================================================
CREATE TABLE `detalle_cotizacion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cotizacion_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `talla_id` bigint(20) DEFAULT NULL,
  `color_id` bigint(20) DEFAULT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `descuento_monto` decimal(10,2) DEFAULT 0.00,
  `subtotal` decimal(10,2) NOT NULL,
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_detalle_cotizacion_cotizacion` (`cotizacion_id`),
  KEY `FK_detalle_cotizacion_producto` (`producto_id`),
  KEY `FK_detalle_cotizacion_talla` (`talla_id`),
  KEY `FK_detalle_cotizacion_color` (`color_id`),
  CONSTRAINT `FK_detalle_cotizacion_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizaciones` (`id`),
  CONSTRAINT `FK_detalle_cotizacion_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FK_detalle_cotizacion_talla` FOREIGN KEY (`talla_id`) REFERENCES `tallas` (`id`),
  CONSTRAINT `FK_detalle_cotizacion_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: promociones
-- =====================================================
CREATE TABLE `promociones` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(50) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `tipo_promocion` enum('DESCUENTO_PORCENTAJE','DESCUENTO_MONTO','_2X1','_3X2','ENVIO_GRATIS','REGALO') NOT NULL,
  `valor_descuento` decimal(10,2) DEFAULT NULL,
  `porcentaje_descuento` decimal(5,2) DEFAULT NULL,
  `monto_minimo_compra` decimal(10,2) DEFAULT NULL,
  `cantidad_maxima_usos` int(11) DEFAULT NULL,
  `usos_por_cliente` int(11) DEFAULT 1,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `aplica_a` enum('TODOS','PRODUCTOS','CATEGORIAS','CLIENTES') DEFAULT 'TODOS',
  `creado_por` bigint(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_promociones_codigo` (`codigo`),
  KEY `FK_promociones_creado_por` (`creado_por`),
  CONSTRAINT `FK_promociones_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: promociones_productos
-- =====================================================
CREATE TABLE `promociones_productos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promocion_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_promociones_productos_promocion` (`promocion_id`),
  KEY `FK_promociones_productos_producto` (`producto_id`),
  CONSTRAINT `FK_promociones_productos_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`id`),
  CONSTRAINT `FK_promociones_productos_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: promociones_categorias
-- =====================================================
CREATE TABLE `promociones_categorias` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promocion_id` bigint(20) NOT NULL,
  `categoria_id` bigint(20) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_promociones_categorias_promocion` (`promocion_id`),
  KEY `FK_promociones_categorias_categoria` (`categoria_id`),
  CONSTRAINT `FK_promociones_categorias_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`id`),
  CONSTRAINT `FK_promociones_categorias_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: promociones_uso
-- =====================================================
CREATE TABLE `promociones_uso` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promocion_id` bigint(20) NOT NULL,
  `venta_id` bigint(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `monto_descuento` decimal(10,2) NOT NULL,
  `fecha_uso` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_promociones_uso_promocion` (`promocion_id`),
  KEY `FK_promociones_uso_venta` (`venta_id`),
  KEY `FK_promociones_uso_cliente` (`cliente_id`),
  CONSTRAINT `FK_promociones_uso_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`id`),
  CONSTRAINT `FK_promociones_uso_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_promociones_uso_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: cajas
-- =====================================================
CREATE TABLE `cajas` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `ubicacion` varchar(100) DEFAULT NULL,
  `responsable_id` bigint(20) DEFAULT NULL,
  `monto_inicial` decimal(10,2) DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cajas_codigo` (`codigo`),
  KEY `FK_cajas_responsable` (`responsable_id`),
  CONSTRAINT `FK_cajas_responsable` FOREIGN KEY (`responsable_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: arqueos_caja
-- =====================================================
CREATE TABLE `arqueos_caja` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `caja_id` bigint(20) NOT NULL,
  `fecha_arqueo` date NOT NULL,
  `turno` enum('MAÑANA','TARDE','NOCHE','COMPLETO') DEFAULT 'COMPLETO',
  `efectivo_inicio` decimal(10,2) NOT NULL DEFAULT 0.00,
  `efectivo_fin_sistema` decimal(10,2) NOT NULL DEFAULT 0.00,
  `efectivo_fin_fisico` decimal(10,2) NOT NULL DEFAULT 0.00,
  `diferencia` decimal(10,2) DEFAULT NULL,
  `total_ventas` decimal(10,2) DEFAULT 0.00,
  `total_gastos` decimal(10,2) DEFAULT 0.00,
  `observaciones` text DEFAULT NULL,
  `estado` enum('ABIERTO','CERRADO','CUADRADO','DESCUADRADO') DEFAULT 'ABIERTO',
  `usuario_apertura` bigint(20) DEFAULT NULL,
  `usuario_cierre` bigint(20) DEFAULT NULL,
  `fecha_apertura` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_cierre` timestamp NULL DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_arqueos_caja_caja` (`caja_id`),
  KEY `FK_arqueos_caja_usuario_apertura` (`usuario_apertura`),
  KEY `FK_arqueos_caja_usuario_cierre` (`usuario_cierre`),
  CONSTRAINT `FK_arqueos_caja_caja` FOREIGN KEY (`caja_id`) REFERENCES `cajas` (`id`),
  CONSTRAINT `FK_arqueos_caja_usuario_apertura` FOREIGN KEY (`usuario_apertura`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK_arqueos_caja_usuario_cierre` FOREIGN KEY (`usuario_cierre`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: configuracion_tienda
-- =====================================================
CREATE TABLE `configuracion_tienda` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `slogan` varchar(200) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `ruc` varchar(11) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `distrito_id` bigint(20) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `telefono_secundario` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `email_ventas` varchar(100) DEFAULT NULL,
  `email_soporte` varchar(100) DEFAULT NULL,
  `sitio_web` varchar(100) DEFAULT NULL,
  `facebook` varchar(100) DEFAULT NULL,
  `instagram` varchar(100) DEFAULT NULL,
  `whatsapp` varchar(20) DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `favicon` varchar(255) DEFAULT NULL,
  `moneda_principal` varchar(3) DEFAULT 'PEN',
  `idioma_principal` varchar(5) DEFAULT 'es_PE',
  `zona_horaria` varchar(50) DEFAULT 'America/Lima',
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_configuracion_tienda_distrito` (`distrito_id`),
  CONSTRAINT `FK_configuracion_tienda_distrito` FOREIGN KEY (`distrito_id`) REFERENCES `distritos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: horarios_atencion
-- =====================================================
CREATE TABLE `horarios_atencion` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dia_semana` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO') NOT NULL,
  `hora_apertura` varchar(5) DEFAULT NULL,
  `hora_cierre` varchar(5) DEFAULT NULL,
  `cerrado` tinyint(1) DEFAULT 0,
  `observaciones` varchar(200) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_horarios_atencion_dia` (`dia_semana`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: configuracion_impuestos
-- =====================================================
CREATE TABLE `configuracion_impuestos` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  `porcentaje` decimal(5,2) NOT NULL,
  `tipo` enum('IGV','ISC','MUNICIPAL','OTROS') NOT NULL DEFAULT 'OTROS',
  `activo` tinyint(1) DEFAULT 1,
  `aplicar_por_defecto` tinyint(1) DEFAULT 0,
  `aplica_a_productos` tinyint(1) DEFAULT 1,
  `aplica_a_servicios` tinyint(1) DEFAULT 1,
  `descripcion` varchar(200) DEFAULT NULL,
  `base_legal` varchar(500) DEFAULT NULL,
  `fecha_vigencia_inicio` date DEFAULT NULL,
  `fecha_vigencia_fin` date DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_configuracion_impuestos_codigo` (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: movimientos_inventario
-- =====================================================
CREATE TABLE `movimientos_inventario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `producto_id` bigint(20) NOT NULL,
  `tipo_movimiento` enum('ENTRADA','SALIDA','AJUSTE','TRANSFERENCIA','DEVOLUCION') NOT NULL,
  `motivo` enum('COMPRA','VENTA','AJUSTE_INVENTARIO','MERMA','ROBO','DEVOLUCION_CLIENTE','DEVOLUCION_PROVEEDOR','PROMOCION','MUESTRA','TRANSFERENCIA','OTROS') NOT NULL,
  `cantidad_anterior` int(11) NOT NULL,
  `cantidad_movimiento` int(11) NOT NULL,
  `cantidad_actual` int(11) NOT NULL,
  `costo_unitario` decimal(10,2) DEFAULT NULL,
  `valor_total` decimal(12,2) DEFAULT NULL,
  `referencia_documento` varchar(100) DEFAULT NULL,
  `lote` varchar(50) DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `venta_id` bigint(20) DEFAULT NULL,
  `compra_id` bigint(20) DEFAULT NULL,
  `devolucion_id` bigint(20) DEFAULT NULL,
  `transferencia_id` bigint(20) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `fecha_movimiento` timestamp NOT NULL DEFAULT current_timestamp(),
  `usuario_id` bigint(20) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_movimientos_inventario_producto` (`producto_id`),
  KEY `FK_movimientos_inventario_venta` (`venta_id`),
  KEY `FK_movimientos_inventario_usuario` (`usuario_id`),
  CONSTRAINT `FK_movimientos_inventario_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  CONSTRAINT `FK_movimientos_inventario_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_movimientos_inventario_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: transferencias_inventario
-- =====================================================
CREATE TABLE `transferencias_inventario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `numero_transferencia` varchar(20) NOT NULL,
  `ubicacion_origen` varchar(100) NOT NULL,
  `ubicacion_destino` varchar(100) NOT NULL,
  `fecha_transferencia` date NOT NULL,
  `estado` enum('PENDIENTE','EN_TRANSITO','RECIBIDA','CANCELADA') DEFAULT 'PENDIENTE',
  `observaciones` text DEFAULT NULL,
  `autorizado_por` bigint(20) DEFAULT NULL,
  `recibido_por` bigint(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_transferencias_inventario_numero` (`numero_transferencia`),
  KEY `FK_transferencias_inventario_autorizado_por` (`autorizado_por`),
  KEY `FK_transferencias_inventario_recibido_por` (`recibido_por`),
  CONSTRAINT `FK_transferencias_inventario_autorizado_por` FOREIGN KEY (`autorizado_por`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK_transferencias_inventario_recibido_por` FOREIGN KEY (`recibido_por`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: detalle_transferencia
-- =====================================================
CREATE TABLE `detalle_transferencia` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transferencia_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `cantidad_enviada` int(11) NOT NULL,
  `cantidad_recibida` int(11) DEFAULT 0,
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_detalle_transferencia_transferencia` (`transferencia_id`),
  KEY `FK_detalle_transferencia_producto` (`producto_id`),
  CONSTRAINT `FK_detalle_transferencia_transferencia` FOREIGN KEY (`transferencia_id`) REFERENCES `transferencias_inventario` (`id`),
  CONSTRAINT `FK_detalle_transferencia_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: tareas_seguimiento
-- =====================================================
CREATE TABLE `tareas_seguimiento` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `estado` enum('PENDIENTE','EN_PROGRESO','COMPLETADA','CANCELADA') NOT NULL DEFAULT 'PENDIENTE',
  `prioridad` enum('BAJA','MEDIA','ALTA','URGENTE') NOT NULL DEFAULT 'MEDIA',
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_limite` timestamp NULL DEFAULT NULL,
  `fecha_cierre` timestamp NULL DEFAULT NULL,
  `usuario_asignado_id` bigint(20) DEFAULT NULL,
  `usuario_creador_id` bigint(20) DEFAULT NULL,
  `cliente_id` bigint(20) DEFAULT NULL,
  `venta_id` bigint(20) DEFAULT NULL,
  `cotizacion_id` bigint(20) DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tareas_seguimiento_usuario_asignado` (`usuario_asignado_id`),
  KEY `FK_tareas_seguimiento_usuario_creador` (`usuario_creador_id`),
  KEY `FK_tareas_seguimiento_cliente` (`cliente_id`),
  KEY `FK_tareas_seguimiento_venta` (`venta_id`),
  KEY `FK_tareas_seguimiento_cotizacion` (`cotizacion_id`),
  CONSTRAINT `FK_tareas_seguimiento_usuario_asignado` FOREIGN KEY (`usuario_asignado_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK_tareas_seguimiento_usuario_creador` FOREIGN KEY (`usuario_creador_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `FK_tareas_seguimiento_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  CONSTRAINT `FK_tareas_seguimiento_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`),
  CONSTRAINT `FK_tareas_seguimiento_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizaciones` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: comentarios_tarea
-- =====================================================
CREATE TABLE `comentarios_tarea` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tarea_id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  `comentario` text NOT NULL,
  `fecha_comentario` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_comentarios_tarea_tarea` (`tarea_id`),
  KEY `FK_comentarios_tarea_usuario` (`usuario_id`),
  CONSTRAINT `FK_comentarios_tarea_tarea` FOREIGN KEY (`tarea_id`) REFERENCES `tareas_seguimiento` (`id`),
  CONSTRAINT `FK_comentarios_tarea_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: sesiones_usuario
-- =====================================================
CREATE TABLE `sesiones_usuario` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint(20) NOT NULL,
  `token_sesion` varchar(255) NOT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `fecha_inicio` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_ultimo_acceso` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_expiracion` timestamp NULL DEFAULT NULL,
  `activa` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sesiones_usuario_token` (`token_sesion`),
  KEY `FK_sesiones_usuario_usuario` (`usuario_id`),
  CONSTRAINT `FK_sesiones_usuario_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: password_reset_tokens
-- =====================================================
CREATE TABLE `password_reset_tokens` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint(20) NOT NULL,
  `token` varchar(255) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_expiracion` timestamp NOT NULL,
  `usado` tinyint(1) DEFAULT 0,
  `ip_solicitud` varchar(45) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_password_reset_tokens_token` (`token`),
  KEY `FK_password_reset_tokens_usuario` (`usuario_id`),
  CONSTRAINT `FK_password_reset_tokens_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: auditoria_logs
-- =====================================================
CREATE TABLE `auditoria_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tabla` varchar(50) NOT NULL,
  `registro_id` bigint(20) NOT NULL,
  `accion` enum('INSERT','UPDATE','DELETE') NOT NULL,
  `datos_anteriores` longtext DEFAULT NULL,
  `datos_nuevos` longtext DEFAULT NULL,
  `usuario_id` bigint(20) DEFAULT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `fecha_accion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_auditoria_logs_usuario` (`usuario_id`),
  KEY `IDX_auditoria_logs_tabla_registro` (`tabla`,`registro_id`),
  KEY `IDX_auditoria_logs_fecha` (`fecha_accion`),
  CONSTRAINT `FK_auditoria_logs_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- INSERTAR DATOS INICIALES
-- =====================================================

-- Insertar países
INSERT INTO `paises` (`codigo_iso`, `nombre`, `codigo_telefono`, `activo`) VALUES
('PER', 'Perú', '+51', 1),
('COL', 'Colombia', '+57', 1),
('ECU', 'Ecuador', '+593', 1),
('BOL', 'Bolivia', '+591', 1),
('BRA', 'Brasil', '+55', 1);

-- Insertar departamentos de Perú
INSERT INTO `departamentos` (`pais_id`, `codigo`, `nombre`, `activo`) VALUES
(1, '16', 'Loreto', 1),
(1, '15', 'Lima', 1),
(1, '04', 'Arequipa', 1),
(1, '08', 'Cusco', 1),
(1, '14', 'La Libertad', 1);

-- Insertar provincias de Loreto
INSERT INTO `provincias` (`departamento_id`, `codigo`, `nombre`, `activo`) VALUES
(1, '1601', 'Maynas', 1),
(1, '1602', 'Alto Amazonas', 1),
(1, '1603', 'Loreto', 1),
(1, '1604', 'Mariscal Ramón Castilla', 1),
(1, '1605', 'Requena', 1),
(1, '1606', 'Ucayali', 1),
(1, '1607', 'Datem del Marañón', 1),
(1, '1608', 'Putumayo', 1);

-- Insertar distritos de Maynas
INSERT INTO `distritos` (`provincia_id`, `codigo`, `nombre`, `codigo_postal`, `activo`) VALUES
(1, '160101', 'Iquitos', '16001', 1),
(1, '160102', 'Alto Nanay', '16002', 1),
(1, '160103', 'Fernando Lores', '16003', 1),
(1, '160104', 'Indiana', '16004', 1),
(1, '160105', 'Las Amazonas', '16005', 1),
(1, '160106', 'Mazan', '16006', 1),
(1, '160107', 'Napo', '16007', 1),
(1, '160108', 'Punchana', '16008', 1),
(1, '160109', 'Torres Causana', '16009', 1),
(1, '160110', 'Belén', '16010', 1),
(1, '160111', 'San Juan Bautista', '16011', 1);

-- Insertar roles
INSERT INTO `roles` (`codigo`, `nombre`, `descripcion`, `nivel_acceso`, `activo`) VALUES
('SUPER_ADMIN', 'Super Administrador', 'Acceso total al sistema', 5, 1),
('ADMIN', 'Administrador', 'Administrador general', 4, 1),
('MANAGER', 'Gerente', 'Gerente de tienda', 3, 1),
('VENDEDOR', 'Vendedor', 'Personal de ventas', 2, 1),
('CAJERO', 'Cajero', 'Personal de caja', 1, 1);

-- Insertar usuario administrador por defecto
INSERT INTO `usuarios` (`codigo_empleado`, `nombre`, `apellido`, `email`, `password`, `rol_id`, `telefono`, `activo`) VALUES
('ADMIN001', 'Administrador', 'Sistema', 'admin@pattymoda.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 1, '+51965123456', 1);

-- Insertar permisos básicos
INSERT INTO `permisos` (`codigo`, `modulo`, `accion`, `descripcion`, `activo`) VALUES
('PRODUCTOS_READ', 'PRODUCTOS', 'READ', 'Ver productos', 1),
('PRODUCTOS_CREATE', 'PRODUCTOS', 'CREATE', 'Crear productos', 1),
('PRODUCTOS_UPDATE', 'PRODUCTOS', 'UPDATE', 'Actualizar productos', 1),
('PRODUCTOS_DELETE', 'PRODUCTOS', 'DELETE', 'Eliminar productos', 1),
('VENTAS_READ', 'VENTAS', 'READ', 'Ver ventas', 1),
('VENTAS_CREATE', 'VENTAS', 'CREATE', 'Crear ventas', 1),
('VENTAS_UPDATE', 'VENTAS', 'UPDATE', 'Actualizar ventas', 1),
('CLIENTES_READ', 'CLIENTES', 'READ', 'Ver clientes', 1),
('CLIENTES_CREATE', 'CLIENTES', 'CREATE', 'Crear clientes', 1),
('CLIENTES_UPDATE', 'CLIENTES', 'UPDATE', 'Actualizar clientes', 1),
('USUARIOS_READ', 'USUARIOS', 'READ', 'Ver usuarios', 1),
('USUARIOS_CREATE', 'USUARIOS', 'CREATE', 'Crear usuarios', 1),
('USUARIOS_UPDATE', 'USUARIOS', 'UPDATE', 'Actualizar usuarios', 1),
('REPORTES_READ', 'REPORTES', 'READ', 'Ver reportes', 1),
('CONFIGURACION_UPDATE', 'CONFIGURACION', 'UPDATE', 'Configurar sistema', 1);

-- Asignar todos los permisos al Super Admin
INSERT INTO `roles_permisos` (`rol_id`, `permiso_id`, `concedido`, `fecha_asignacion`) 
SELECT 1, id, 1, NOW() FROM `permisos`;

-- Insertar categorías principales
INSERT INTO `categorias` (`codigo`, `nombre`, `descripcion`, `orden`, `activo`) VALUES
('ROPA_MUJER', 'Ropa para Mujer', 'Prendas de vestir femeninas', 1, 1),
('ROPA_HOMBRE', 'Ropa para Hombre', 'Prendas de vestir masculinas', 2, 1),
('CALZADO', 'Calzado', 'Zapatos y zapatillas', 3, 1),
('ACCESORIOS', 'Accesorios', 'Complementos y accesorios', 4, 1),
('INFANTIL', 'Ropa Infantil', 'Ropa para niños y niñas', 5, 1);

-- Insertar subcategorías
INSERT INTO `categorias` (`codigo`, `nombre`, `descripcion`, `categoria_padre_id`, `orden`, `activo`) VALUES
('BLUSAS', 'Blusas', 'Blusas y camisas para mujer', 1, 1, 1),
('PANTALONES_M', 'Pantalones', 'Pantalones para mujer', 1, 2, 1),
('VESTIDOS', 'Vestidos', 'Vestidos casuales y elegantes', 1, 3, 1),
('CAMISAS_H', 'Camisas', 'Camisas para hombre', 2, 1, 1),
('PANTALONES_H', 'Pantalones', 'Pantalones para hombre', 2, 2, 1),
('POLOS', 'Polos', 'Polos y camisetas', 2, 3, 1),
('ZAPATOS_M', 'Zapatos Mujer', 'Calzado femenino', 3, 1, 1),
('ZAPATOS_H', 'Zapatos Hombre', 'Calzado masculino', 3, 2, 1),
('ZAPATILLAS', 'Zapatillas', 'Calzado deportivo', 3, 3, 1);

-- Insertar marcas
INSERT INTO `marcas` (`nombre`, `descripcion`, `activo`) VALUES
('PattyModa', 'Marca propia de la tienda', 1),
('Adidas', 'Marca deportiva internacional', 1),
('Nike', 'Marca deportiva líder mundial', 1),
('Zara', 'Moda contemporánea', 1),
('H&M', 'Moda accesible', 1),
('Forever 21', 'Moda juvenil', 1),
('Levi\'s', 'Marca de jeans icónica', 1),
('Converse', 'Calzado casual', 1);

-- Insertar tallas
INSERT INTO `tallas` (`codigo`, `nombre`, `descripcion`, `categoria_talla`, `orden_visualizacion`, `activo`) VALUES
('XS', 'Extra Small', 'Talla extra pequeña', 'ROPA_MUJER', 1, 1),
('S', 'Small', 'Talla pequeña', 'ROPA_MUJER', 2, 1),
('M', 'Medium', 'Talla mediana', 'ROPA_MUJER', 3, 1),
('L', 'Large', 'Talla grande', 'ROPA_MUJER', 4, 1),
('XL', 'Extra Large', 'Talla extra grande', 'ROPA_MUJER', 5, 1),
('XXL', 'Double Extra Large', 'Talla doble extra grande', 'ROPA_MUJER', 6, 1),
('S_H', 'Small Hombre', 'Talla pequeña hombre', 'ROPA_HOMBRE', 1, 1),
('M_H', 'Medium Hombre', 'Talla mediana hombre', 'ROPA_HOMBRE', 2, 1),
('L_H', 'Large Hombre', 'Talla grande hombre', 'ROPA_HOMBRE', 3, 1),
('XL_H', 'Extra Large Hombre', 'Talla extra grande hombre', 'ROPA_HOMBRE', 4, 1),
('36', '36', 'Talla 36 calzado', 'CALZADO_MUJER', 1, 1),
('37', '37', 'Talla 37 calzado', 'CALZADO_MUJER', 2, 1),
('38', '38', 'Talla 38 calzado', 'CALZADO_MUJER', 3, 1),
('39', '39', 'Talla 39 calzado', 'CALZADO_MUJER', 4, 1),
('40', '40', 'Talla 40 calzado', 'CALZADO_MUJER', 5, 1),
('41', '41', 'Talla 41 calzado', 'CALZADO_HOMBRE', 1, 1),
('42', '42', 'Talla 42 calzado', 'CALZADO_HOMBRE', 2, 1),
('43', '43', 'Talla 43 calzado', 'CALZADO_HOMBRE', 3, 1),
('44', '44', 'Talla 44 calzado', 'CALZADO_HOMBRE', 4, 1),
('45', '45', 'Talla 45 calzado', 'CALZADO_HOMBRE', 5, 1);

-- Insertar colores
INSERT INTO `colores` (`codigo`, `nombre`, `descripcion`, `codigo_hex`, `familia_color`, `orden_visualizacion`, `activo`) VALUES
('NEGRO', 'Negro', 'Color negro clásico', '#000000', 'NEUTROS', 1, 1),
('BLANCO', 'Blanco', 'Color blanco puro', '#FFFFFF', 'NEUTROS', 2, 1),
('GRIS', 'Gris', 'Color gris medio', '#808080', 'NEUTROS', 3, 1),
('AZUL', 'Azul', 'Azul clásico', '#0000FF', 'FRIOS', 1, 1),
('ROJO', 'Rojo', 'Rojo intenso', '#FF0000', 'CALIDOS', 1, 1),
('VERDE', 'Verde', 'Verde natural', '#008000', 'FRIOS', 2, 1),
('AMARILLO', 'Amarillo', 'Amarillo brillante', '#FFFF00', 'CALIDOS', 2, 1),
('ROSA', 'Rosa', 'Rosa suave', '#FFC0CB', 'CALIDOS', 3, 1),
('MORADO', 'Morado', 'Morado elegante', '#800080', 'FRIOS', 3, 1),
('NARANJA', 'Naranja', 'Naranja vibrante', '#FFA500', 'CALIDOS', 4, 1),
('MARRON', 'Marrón', 'Marrón tierra', '#A52A2A', 'NEUTROS', 4, 1),
('BEIGE', 'Beige', 'Beige natural', '#F5F5DC', 'NEUTROS', 5, 1);

-- Insertar unidades de medida
INSERT INTO `unidades_medida` (`codigo`, `nombre`, `simbolo`, `tipo`, `factor_conversion`, `activo`) VALUES
('UND', 'Unidad', 'und', 'UNIDAD', 1.000000, 1),
('KG', 'Kilogramo', 'kg', 'PESO', 1.000000, 1),
('GR', 'Gramo', 'g', 'PESO', 0.001000, 1),
('M', 'Metro', 'm', 'LONGITUD', 1.000000, 1),
('CM', 'Centímetro', 'cm', 'LONGITUD', 0.010000, 1),
('L', 'Litro', 'l', 'VOLUMEN', 1.000000, 1),
('ML', 'Mililitro', 'ml', 'VOLUMEN', 0.001000, 1);

-- Insertar productos de ejemplo
INSERT INTO `productos` (`codigo_producto`, `nombre`, `sku`, `descripcion`, `descripcion_corta`, `categoria_id`, `marca_id`, `requiere_talla`, `requiere_color`, `destacado`, `nuevo`, `activo`) VALUES
('BLUSA001', 'Blusa Elegante Manga Larga', 'BLU-ELE-001', 'Blusa elegante de manga larga perfecta para ocasiones especiales', 'Blusa elegante manga larga', 6, 1, 1, 1, 1, 1, 1),
('JEAN001', 'Jean Clásico Mujer', 'JEAN-CLA-001', 'Jean clásico de corte recto para mujer, cómodo y versátil', 'Jean clásico mujer', 7, 7, 1, 1, 0, 1, 1),
('VESTIDO001', 'Vestido Casual Verano', 'VEST-CAS-001', 'Vestido casual perfecto para el verano, fresco y cómodo', 'Vestido casual verano', 8, 1, 1, 1, 1, 1, 1),
('CAMISA001', 'Camisa Formal Hombre', 'CAM-FOR-001', 'Camisa formal para hombre, ideal para oficina', 'Camisa formal hombre', 9, 4, 1, 1, 0, 1, 1),
('POLO001', 'Polo Deportivo', 'POL-DEP-001', 'Polo deportivo de algodón, cómodo para actividades físicas', 'Polo deportivo', 11, 2, 1, 1, 0, 1, 1),
('ZAP001', 'Zapatos Casuales Mujer', 'ZAP-CAS-001', 'Zapatos casuales cómodos para uso diario', 'Zapatos casuales mujer', 12, 8, 1, 1, 1, 0, 1),
('ZAP002', 'Zapatos Formales Hombre', 'ZAP-FOR-001', 'Zapatos formales de cuero para hombre', 'Zapatos formales hombre', 13, 1, 1, 1, 0, 0, 1),
('ZAPATILLA001', 'Zapatillas Deportivas', 'ZAP-DEP-001', 'Zapatillas deportivas para running y ejercicio', 'Zapatillas deportivas', 14, 3, 1, 1, 1, 1, 1);

-- Insertar precios para los productos
INSERT INTO `productos_precios` (`producto_id`, `precio_venta`, `precio_oferta`, `costo`, `margen_porcentaje`, `fecha_inicio`, `activo`) VALUES
(1, 89.90, 79.90, 45.00, 99.78, CURDATE(), 1),
(2, 129.90, NULL, 65.00, 99.85, CURDATE(), 1),
(3, 149.90, 129.90, 75.00, 99.83, CURDATE(), 1),
(4, 99.90, NULL, 50.00, 99.80, CURDATE(), 1),
(5, 59.90, NULL, 30.00, 99.67, CURDATE(), 1),
(6, 179.90, 159.90, 90.00, 99.78, CURDATE(), 1),
(7, 249.90, NULL, 125.00, 99.80, CURDATE(), 1),
(8, 299.90, 269.90, 150.00, 99.83, CURDATE(), 1);

-- Insertar canales de venta
INSERT INTO `canales_venta` (`codigo`, `nombre`, `descripcion`, `comision_porcentaje`, `requiere_entrega`, `activo`) VALUES
('TIENDA', 'Tienda Física', 'Ventas realizadas en la tienda física', 0.00, 0, 1),
('ONLINE', 'Tienda Online', 'Ventas realizadas por la página web', 2.50, 1, 1),
('WHATSAPP', 'WhatsApp', 'Ventas realizadas por WhatsApp', 1.00, 1, 1),
('FACEBOOK', 'Facebook', 'Ventas realizadas por Facebook', 3.00, 1, 1),
('INSTAGRAM', 'Instagram', 'Ventas realizadas por Instagram', 3.00, 1, 1);

-- Insertar configuración de la tienda
INSERT INTO `configuracion_tienda` (`nombre`, `slogan`, `descripcion`, `ruc`, `direccion`, `distrito_id`, `telefono`, `email`, `email_ventas`, `sitio_web`, `whatsapp`, `moneda_principal`, `activo`) VALUES
('DPattyModa', 'Tu estilo, nuestra pasión', 'Tienda de ropa y accesorios con las últimas tendencias de la moda', '20123456789', 'Av. Principal 123, Pampa Hermosa', 1, '+51965123456', 'info@dpattymoda.com', 'ventas@dpattymoda.com', 'https://dpattymoda.com', '+51965123456', 'PEN', 1);

-- Insertar horarios de atención
INSERT INTO `horarios_atencion` (`dia_semana`, `hora_apertura`, `hora_cierre`, `cerrado`, `activo`) VALUES
('LUNES', '09:00', '18:00', 0, 1),
('MARTES', '09:00', '18:00', 0, 1),
('MIERCOLES', '09:00', '18:00', 0, 1),
('JUEVES', '09:00', '18:00', 0, 1),
('VIERNES', '09:00', '18:00', 0, 1),
('SABADO', '09:00', '17:00', 0, 1),
('DOMINGO', NULL, NULL, 1, 1);

-- Insertar configuración de impuestos
INSERT INTO `configuracion_impuestos` (`nombre`, `codigo`, `porcentaje`, `tipo`, `activo`, `aplicar_por_defecto`, `descripcion`) VALUES
('IGV', 'IGV', 18.00, 'IGV', 1, 1, 'Impuesto General a las Ventas'),
('ISC', 'ISC', 10.00, 'ISC', 1, 0, 'Impuesto Selectivo al Consumo');

-- Insertar cajas
INSERT INTO `cajas` (`codigo`, `nombre`, `descripcion`, `ubicacion`, `responsable_id`, `monto_inicial`, `activo`) VALUES
('CAJA01', 'Caja Principal', 'Caja registradora principal de la tienda', 'Mostrador Principal', 1, 200.00, 1),
('CAJA02', 'Caja Secundaria', 'Caja registradora secundaria', 'Mostrador Lateral', 1, 100.00, 1);

-- Insertar clientes de ejemplo
INSERT INTO `clientes` (`codigo_cliente`, `tipo_documento`, `numero_documento`, `nombre`, `apellido`, `genero`, `activo`) VALUES
('CLI001', 'DNI', '12345678', 'María', 'García López', 'F', 1),
('CLI002', 'DNI', '87654321', 'Juan', 'Pérez Rodríguez', 'M', 1),
('CLI003', 'DNI', '11223344', 'Ana', 'Martínez Silva', 'F', 1),
('CLI004', 'RUC', '20123456789', 'Empresa ABC', 'SAC', NULL, 1);

-- Insertar información comercial de clientes
INSERT INTO `clientes_comercial` (`cliente_id`, `tipo_cliente`, `total_compras`, `cantidad_compras`, `limite_credito`, `nivel_cliente`) VALUES
(1, 'REGULAR', 450.80, 3, 500.00, 'BRONCE'),
(2, 'VIP', 1250.50, 8, 2000.00, 'PLATA'),
(3, 'REGULAR', 320.90, 2, 500.00, 'BRONCE'),
(4, 'MAYORISTA', 5680.00, 15, 10000.00, 'ORO');

-- Insertar programa de lealtad
INSERT INTO `programa_lealtad` (`cliente_id`, `puntos_acumulados`, `puntos_canjeados`, `nivel_cliente`, `activo`) VALUES
(1, 450, 50, 'BRONCE', 1),
(2, 1250, 200, 'PLATA', 1),
(3, 320, 0, 'BRONCE', 1),
(4, 5680, 1000, 'ORO', 1);

-- =====================================================
-- CREAR ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- =====================================================

-- Índices para productos
CREATE INDEX IDX_productos_activo ON productos(activo);
CREATE INDEX IDX_productos_destacado ON productos(destacado);
CREATE INDEX IDX_productos_nuevo ON productos(nuevo);
CREATE INDEX IDX_productos_categoria_activo ON productos(categoria_id, activo);
CREATE INDEX IDX_productos_marca_activo ON productos(marca_id, activo);

-- Índices para ventas
CREATE INDEX IDX_ventas_fecha ON ventas(fecha);
CREATE INDEX IDX_ventas_estado ON ventas(estado);
CREATE INDEX IDX_ventas_cliente_fecha ON ventas(cliente_id, fecha);
CREATE INDEX IDX_ventas_vendedor_fecha ON ventas(vendedor_id, fecha);

-- Índices para clientes
CREATE INDEX IDX_clientes_activo ON clientes(activo);
CREATE INDEX IDX_clientes_tipo_documento ON clientes(tipo_documento, numero_documento);

-- Índices para inventario
CREATE INDEX IDX_movimientos_inventario_fecha ON movimientos_inventario(fecha_movimiento);
CREATE INDEX IDX_movimientos_inventario_producto_fecha ON movimientos_inventario(producto_id, fecha_movimiento);

-- =====================================================
-- CREAR VISTAS PARA REPORTES
-- =====================================================

-- Vista de productos completos
CREATE VIEW vista_productos_completa AS
SELECT 
    p.id,
    p.codigo_producto,
    p.nombre,
    p.sku,
    p.descripcion,
    p.descripcion_corta,
    c.nombre as categoria_nombre,
    c.id as categoria_id,
    m.nombre as marca_nombre,
    m.id as marca_id,
    pp.precio_venta as precio,
    pp.precio_oferta,
    pp.costo,
    pp.margen_porcentaje,
    COALESCE(SUM(pt.stock_talla), 0) as stock,
    p.imagen_principal,
    p.requiere_talla,
    p.requiere_color,
    p.destacado,
    p.activo,
    p.fecha_creacion,
    CASE 
        WHEN COALESCE(SUM(pt.stock_talla), 0) <= 0 THEN 'SIN_STOCK'
        WHEN COALESCE(SUM(pt.stock_talla), 0) <= 5 THEN 'STOCK_BAJO'
        ELSE 'STOCK_OK'
    END as estado_stock
FROM productos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN marcas m ON p.marca_id = m.id
LEFT JOIN productos_precios pp ON p.id = pp.producto_id AND pp.activo = 1 
    AND (pp.fecha_fin IS NULL OR pp.fecha_fin >= CURDATE())
LEFT JOIN productos_tallas pt ON p.id = pt.producto_id AND pt.activo = 1
GROUP BY p.id, p.codigo_producto, p.nombre, p.sku, p.descripcion, p.descripcion_corta,
         c.nombre, c.id, m.nombre, m.id, pp.precio_venta, pp.precio_oferta, 
         pp.costo, pp.margen_porcentaje, p.imagen_principal, p.requiere_talla,
         p.requiere_color, p.destacado, p.activo, p.fecha_creacion;

-- Vista de ventas completas
CREATE VIEW vista_ventas_completa AS
SELECT 
    v.id,
    v.numero_venta,
    v.fecha,
    v.subtotal,
    v.total,
    v.estado,
    vf.tipo_comprobante,
    vf.serie_comprobante,
    vf.numero_comprobante,
    cv.nombre as canal_venta,
    CONCAT(c.nombre, ' ', COALESCE(c.apellido, '')) as cliente_nombre,
    c.numero_documento as cliente_documento,
    CONCAT(u.nombre, ' ', COALESCE(u.apellido, '')) as vendedor_nombre,
    v.cantidad_items,
    v.comision_vendedor,
    ve.estado_entrega,
    ve.fecha_entrega_real
FROM ventas v
LEFT JOIN clientes c ON v.cliente_id = c.id
LEFT JOIN usuarios u ON v.vendedor_id = u.id
LEFT JOIN canales_venta cv ON v.canal_venta_id = cv.id
LEFT JOIN ventas_facturacion vf ON v.id = vf.venta_id
LEFT JOIN ventas_entrega ve ON v.id = ve.venta_id;

-- Vista de clientes con estadísticas
CREATE VIEW vista_clientes_estadistica AS
SELECT 
    c.id,
    c.codigo_cliente,
    c.nombre,
    c.apellido,
    c.numero_documento,
    c.tipo_documento,
    cc.tipo_cliente,
    cc.total_compras,
    cc.cantidad_compras,
    cc.ultima_compra,
    cc.limite_credito,
    cc.descuento_personalizado,
    pl.puntos_acumulados as puntos_disponibles,
    pl.nivel_cliente,
    c.activo,
    CASE 
        WHEN cc.total_compras >= 5000 THEN 'VIP'
        WHEN cc.total_compras >= 2000 THEN 'PREMIUM'
        WHEN cc.total_compras >= 500 THEN 'REGULAR'
        ELSE 'NUEVO'
    END as categoria_cliente,
    COALESCE(cc.total_compras / NULLIF(cc.cantidad_compras, 0), 0) as ticket_promedio
FROM clientes c
LEFT JOIN clientes_comercial cc ON c.id = cc.cliente_id
LEFT JOIN programa_lealtad pl ON c.id = pl.cliente_id;

-- =====================================================
-- TRIGGERS PARA AUDITORÍA AUTOMÁTICA
-- =====================================================

DELIMITER $$

-- Trigger para auditoría de productos
CREATE TRIGGER tr_productos_audit_insert
AFTER INSERT ON productos
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_nuevos, fecha_accion)
    VALUES ('productos', NEW.id, 'INSERT', 
            JSON_OBJECT('codigo_producto', NEW.codigo_producto, 'nombre', NEW.nombre, 'sku', NEW.sku), 
            NOW());
END$$

CREATE TRIGGER tr_productos_audit_update
AFTER UPDATE ON productos
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_anteriores, datos_nuevos, fecha_accion)
    VALUES ('productos', NEW.id, 'UPDATE',
            JSON_OBJECT('codigo_producto', OLD.codigo_producto, 'nombre', OLD.nombre, 'sku', OLD.sku),
            JSON_OBJECT('codigo_producto', NEW.codigo_producto, 'nombre', NEW.nombre, 'sku', NEW.sku),
            NOW());
END$$

-- Trigger para auditoría de ventas
CREATE TRIGGER tr_ventas_audit_insert
AFTER INSERT ON ventas
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_nuevos, fecha_accion)
    VALUES ('ventas', NEW.id, 'INSERT',
            JSON_OBJECT('numero_venta', NEW.numero_venta, 'cliente_id', NEW.cliente_id, 'total', NEW.total),
            NOW());
END$$

CREATE TRIGGER tr_ventas_audit_update
AFTER UPDATE ON ventas
FOR EACH ROW
BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_anteriores, datos_nuevos, fecha_accion)
    VALUES ('ventas', NEW.id, 'UPDATE',
            JSON_OBJECT('estado', OLD.estado, 'total', OLD.total),
            JSON_OBJECT('estado', NEW.estado, 'total', NEW.total),
            NOW());
END$$

DELIMITER ;

-- =====================================================
-- FUNCIONES ÚTILES
-- =====================================================

DELIMITER $$

-- Función para generar código de cliente automático
CREATE FUNCTION generar_codigo_cliente() RETURNS VARCHAR(20)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE nuevo_codigo VARCHAR(20);
    DECLARE contador INT;
    
    SELECT COUNT(*) + 1 INTO contador FROM clientes;
    SET nuevo_codigo = CONCAT('CLI', LPAD(contador, 6, '0'));
    
    WHILE EXISTS(SELECT 1 FROM clientes WHERE codigo_cliente = nuevo_codigo) DO
        SET contador = contador + 1;
        SET nuevo_codigo = CONCAT('CLI', LPAD(contador, 6, '0'));
    END WHILE;
    
    RETURN nuevo_codigo;
END$$

-- Función para calcular puntos por compra
CREATE FUNCTION calcular_puntos_compra(monto DECIMAL(10,2)) RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE puntos INT DEFAULT 0;
    
    -- 1 punto por cada 10 soles de compra
    SET puntos = FLOOR(monto / 10);
    
    RETURN puntos;
END$$

DELIMITER ;

-- =====================================================
-- PROCEDIMIENTOS ALMACENADOS
-- =====================================================

DELIMITER $$

-- Procedimiento para actualizar stock después de venta
CREATE PROCEDURE actualizar_stock_venta(
    IN p_producto_id BIGINT,
    IN p_talla_id BIGINT,
    IN p_cantidad INT,
    IN p_venta_id BIGINT,
    IN p_usuario_id BIGINT
)
BEGIN
    DECLARE stock_actual INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Obtener stock actual
    SELECT stock_talla INTO stock_actual 
    FROM productos_tallas 
    WHERE producto_id = p_producto_id AND talla_id = p_talla_id;
    
    -- Verificar si hay suficiente stock
    IF stock_actual < p_cantidad THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuficiente';
    END IF;
    
    -- Actualizar stock
    UPDATE productos_tallas 
    SET stock_talla = stock_talla - p_cantidad
    WHERE producto_id = p_producto_id AND talla_id = p_talla_id;
    
    -- Registrar movimiento de inventario
    INSERT INTO movimientos_inventario (
        producto_id, tipo_movimiento, motivo, cantidad_anterior, 
        cantidad_movimiento, cantidad_actual, venta_id, usuario_id, fecha_movimiento
    ) VALUES (
        p_producto_id, 'SALIDA', 'VENTA', stock_actual, 
        p_cantidad, stock_actual - p_cantidad, p_venta_id, p_usuario_id, NOW()
    );
    
    COMMIT;
END$$

-- Procedimiento para procesar puntos de lealtad
CREATE PROCEDURE procesar_puntos_lealtad(
    IN p_cliente_id BIGINT,
    IN p_venta_id BIGINT,
    IN p_monto_compra DECIMAL(10,2)
)
BEGIN
    DECLARE puntos_ganados INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;
    
    START TRANSACTION;
    
    -- Calcular puntos ganados
    SET puntos_ganados = calcular_puntos_compra(p_monto_compra);
    
    -- Actualizar programa de lealtad
    INSERT INTO programa_lealtad (cliente_id, puntos_acumulados, fecha_ultimo_movimiento, activo)
    VALUES (p_cliente_id, puntos_ganados, NOW(), 1)
    ON DUPLICATE KEY UPDATE 
        puntos_acumulados = puntos_acumulados + puntos_ganados,
        fecha_ultimo_movimiento = NOW();
    
    -- Registrar transacción de puntos
    INSERT INTO transacciones_puntos (
        cliente_id, tipo_transaccion, puntos, descripcion, 
        venta_id, fecha_transaccion
    ) VALUES (
        p_cliente_id, 'GANANCIA', puntos_ganados, 
        CONCAT('Puntos ganados por compra #', p_venta_id),
        p_venta_id, NOW()
    );
    
    COMMIT;
END$$

DELIMITER ;

-- =====================================================
-- FINALIZAR TRANSACCIÓN
-- =====================================================

COMMIT;

-- =====================================================
-- MENSAJE DE CONFIRMACIÓN
-- =====================================================
SELECT 'Base de datos PattyModa creada exitosamente' as mensaje,
       COUNT(*) as total_tablas 
FROM information_schema.tables 
WHERE table_schema = 'pattymoda_nueva';

SELECT 'Datos iniciales insertados correctamente' as mensaje,
       (SELECT COUNT(*) FROM productos) as productos,
       (SELECT COUNT(*) FROM clientes) as clientes,
       (SELECT COUNT(*) FROM usuarios) as usuarios,
       (SELECT COUNT(*) FROM categorias) as categorias,
       (SELECT COUNT(*) FROM marcas) as marcas;