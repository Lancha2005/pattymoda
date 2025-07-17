-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-07-2025 a las 21:24:35
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `pattymoda_mejorada`
--

DELIMITER $$
--
-- Funciones
--
CREATE DEFINER=`root`@`localhost` FUNCTION `obtener_igv_activo` () RETURNS DECIMAL(5,2) DETERMINISTIC READS SQL DATA BEGIN
    DECLARE porcentaje DECIMAL(5,2) DEFAULT 0.00;
    
    SELECT ci.porcentaje INTO porcentaje
    FROM configuracion_impuestos ci
    WHERE ci.tipo = 'IGV' 
      AND ci.activo = 1 
      AND ci.aplicar_por_defecto = 1
    LIMIT 1;
    
    RETURN COALESCE(porcentaje, 0.00);
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `siguiente_numero_venta` () RETURNS VARCHAR(20) CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci DETERMINISTIC READS SQL DATA BEGIN
    DECLARE siguiente VARCHAR(20);
    
    SELECT CONCAT('V', YEAR(NOW()), LPAD(
        COALESCE(
            MAX(CAST(SUBSTRING(numero_venta, 6) AS UNSIGNED)), 0
        ) + 1, 6, '0'
    )) INTO siguiente
    FROM ventas 
    WHERE numero_venta LIKE CONCAT('V', YEAR(NOW()), '%');
    
    RETURN siguiente;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `categoria_padre_id` bigint(20) DEFAULT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `orden` int(11) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id`, `nombre`, `descripcion`, `categoria_padre_id`, `imagen`, `orden`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'Ropa Femenina', 'Toda la ropa para mujeres', NULL, NULL, 1, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(2, 'Ropa Masculina', 'Ropa para hombres', NULL, NULL, 2, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(3, 'Accesorios', 'Complementos y accesorios', NULL, NULL, 3, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(4, 'Calzado', 'Zapatos y sandalias', NULL, NULL, 4, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(5, 'Blusas', 'Blusas elegantes y casuales', 1, NULL, 1, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(6, 'Vestidos', 'Vestidos para toda ocasión', 1, NULL, 2, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(7, 'Pantalones Femeninos', 'Pantalones y jeans para mujeres', 1, NULL, 3, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(8, 'Faldas', 'Faldas modernas', 1, NULL, 4, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(9, 'Camisas Masculinas', 'Camisas formales y casuales', 2, NULL, 1, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(10, 'Pantalones Masculinos', 'Pantalones para hombres', 2, NULL, 2, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(11, 'Polos', 'Polos deportivos y casuales', 2, NULL, 3, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(12, 'Bolsos', 'Carteras y bolsos', 3, NULL, 1, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(13, 'Cinturones', 'Cinturones de cuero y tela', 3, NULL, 2, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(14, 'Joyas', 'Collares, pulseras y aretes', 3, NULL, 3, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(15, 'Zapatos Formales', 'Zapatos para ocasiones especiales', 4, NULL, 1, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(16, 'Zapatillas', 'Calzado deportivo y casual', 4, NULL, 2, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL),
(17, 'Sandalias', 'Sandalias y chanclas', 4, NULL, 3, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` bigint(20) NOT NULL,
  `codigo_cliente` varchar(20) DEFAULT NULL,
  `tipo_documento` enum('DNI','RUC','PASAPORTE','CARNET_EXTRANJERIA') DEFAULT 'DNI',
  `numero_documento` varchar(20) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `razon_social` varchar(200) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `telefono_secundario` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `distrito` varchar(100) DEFAULT NULL,
  `provincia` varchar(100) DEFAULT NULL,
  `departamento` varchar(100) DEFAULT 'Loreto',
  `codigo_postal` varchar(10) DEFAULT NULL,
  `pais` varchar(50) DEFAULT 'Perú',
  `fecha_nacimiento` date DEFAULT NULL,
  `genero` enum('M','F','OTRO') DEFAULT NULL,
  `estado_civil` enum('SOLTERO','CASADO','DIVORCIADO','VIUDO','OTRO') DEFAULT NULL,
  `ocupacion` varchar(100) DEFAULT NULL,
  `tipo_cliente` enum('REGULAR','VIP','MAYORISTA','MINORISTA') DEFAULT 'REGULAR',
  `limite_credito` decimal(10,2) DEFAULT 0.00,
  `descuento_personalizado` decimal(5,2) DEFAULT 0.00,
  `metodo_pago_preferido` varchar(50) DEFAULT NULL,
  `total_compras` decimal(10,2) DEFAULT NULL,
  `cantidad_compras` int(11) DEFAULT 0,
  `ultima_compra` timestamp NULL DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `activo` tinyint(1) DEFAULT 1,
  `notas` text DEFAULT NULL,
  `preferencias` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`preferencias`)),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  `ciudad` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Disparadores `clientes`
--
DELIMITER $$
CREATE TRIGGER `before_insert_cliente` BEFORE INSERT ON `clientes` FOR EACH ROW BEGIN
    IF NEW.codigo_cliente IS NULL OR NEW.codigo_cliente = '' THEN
        SET NEW.codigo_cliente = CONCAT('CLI', LPAD(
            COALESCE(
                (SELECT MAX(CAST(SUBSTRING(codigo_cliente, 4) AS UNSIGNED))
                 FROM clientes 
                 WHERE codigo_cliente LIKE 'CLI%'), 0
            ) + 1, 6, '0'
        ));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion_impuestos`
--

CREATE TABLE `configuracion_impuestos` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  `porcentaje` decimal(5,2) NOT NULL,
  `tipo` enum('IGV','ISC','MUNICIPAL','OTROS') DEFAULT 'OTROS',
  `activo` tinyint(1) DEFAULT 1,
  `aplicar_por_defecto` tinyint(1) DEFAULT 0,
  `aplica_a_productos` tinyint(1) DEFAULT 1,
  `aplica_a_servicios` tinyint(1) DEFAULT 1,
  `descripcion` varchar(200) DEFAULT NULL,
  `base_legal` varchar(500) DEFAULT NULL,
  `fecha_vigencia_inicio` date DEFAULT NULL,
  `fecha_vigencia_fin` date DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `configuracion_impuestos`
--

INSERT INTO `configuracion_impuestos` (`id`, `nombre`, `codigo`, `porcentaje`, `tipo`, `activo`, `aplicar_por_defecto`, `aplica_a_productos`, `aplica_a_servicios`, `descripcion`, `base_legal`, `fecha_vigencia_inicio`, `fecha_vigencia_fin`, `created_at`, `updated_at`, `creado_por`) VALUES
(1, 'IGV Selva', 'IGV_SEL', 0.00, 'IGV', 1, 1, 1, 1, 'IGV para región de la selva - Exonerado según Ley de Promoción de la Inversión en la Amazonía', 'Ley N° 27037 - Ley de Promoción de la Inversión en la Amazonía', NULL, NULL, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL),
(2, 'IGV Estándar', 'IGV_STD', 18.00, 'IGV', 0, 0, 1, 1, 'IGV estándar del 18% para otras regiones del Perú', 'TUO del IGV - D.S. N° 055-99-EF', NULL, NULL, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL),
(3, 'ISC Textiles', 'ISC_TEX', 0.00, 'ISC', 0, 0, 1, 1, 'Impuesto Selectivo al Consumo para productos textiles', 'TUO del ISC - D.S. N° 055-99-EF', NULL, NULL, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion_tienda`
--

CREATE TABLE `configuracion_tienda` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `slogan` varchar(200) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `ruc` varchar(11) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `distrito` varchar(100) DEFAULT NULL,
  `provincia` varchar(100) DEFAULT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `codigo_postal` varchar(10) DEFAULT NULL,
  `pais` varchar(50) DEFAULT 'Perú',
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
  `horario_atencion` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`horario_atencion`)),
  `moneda_principal` varchar(3) DEFAULT 'PEN',
  `idioma_principal` varchar(5) DEFAULT 'es_PE',
  `zona_horaria` varchar(50) DEFAULT 'America/Lima',
  `configuracion_facturacion` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`configuracion_facturacion`)),
  `configuracion_notificaciones` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`configuracion_notificaciones`)),
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `configuracion_tienda`
--

INSERT INTO `configuracion_tienda` (`id`, `nombre`, `slogan`, `descripcion`, `ruc`, `direccion`, `distrito`, `provincia`, `departamento`, `codigo_postal`, `pais`, `telefono`, `telefono_secundario`, `email`, `email_ventas`, `email_soporte`, `sitio_web`, `facebook`, `instagram`, `whatsapp`, `logo`, `favicon`, `horario_atencion`, `moneda_principal`, `idioma_principal`, `zona_horaria`, `configuracion_facturacion`, `configuracion_notificaciones`, `activo`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(1, 'DPattyModa', 'Moda y Estilo en la Selva', 'Tienda especializada en ropa moderna y elegante ubicada en Pampa Hermosa, Loreto. Ofrecemos las últimas tendencias en moda femenina y masculina.', NULL, 'Av. Principal 123', 'Pampa Hermosa', 'Ucayali', 'Loreto', NULL, 'Perú', '+51 965 123 456', NULL, 'info@dpattymoda.com', NULL, NULL, NULL, NULL, NULL, '+51 965 123 456', NULL, NULL, '{\"lunes_viernes\": \"9:00-20:00\", \"sabado\": \"9:00-21:00\", \"domingo\": \"10:00-18:00\"}', 'PEN', 'es_PE', 'America/Lima', NULL, NULL, 1, '2025-07-17 18:50:06', '2025-07-17 18:50:06');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_venta`
--

CREATE TABLE `detalle_venta` (
  `id` bigint(20) NOT NULL,
  `venta_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `talla` varchar(10) DEFAULT NULL,
  `color` varchar(50) DEFAULT NULL,
  `precio_unitario` decimal(10,2) NOT NULL,
  `precio_original` decimal(10,2) DEFAULT NULL,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `descuento_monto` decimal(10,2) DEFAULT 0.00,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `costo_unitario` decimal(10,2) DEFAULT NULL,
  `margen_ganancia` decimal(10,2) GENERATED ALWAYS AS (`precio_unitario` - `costo_unitario`) STORED,
  `notas` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Disparadores `detalle_venta`
--
DELIMITER $$
CREATE TRIGGER `after_insert_detalle_venta` AFTER INSERT ON `detalle_venta` FOR EACH ROW BEGIN
    -- Actualizar stock del producto
    UPDATE productos 
    SET stock = stock - NEW.cantidad 
    WHERE id = NEW.producto_id;
    
    -- Registrar movimiento de inventario
    INSERT INTO movimientos_inventario 
    (producto_id, tipo_movimiento, motivo, cantidad_anterior, cantidad_movimiento, cantidad_actual, venta_id, usuario_id)
    SELECT 
        NEW.producto_id,
        'SALIDA',
        'VENTA',
        p.stock + NEW.cantidad,
        NEW.cantidad,
        p.stock,
        NEW.venta_id,
        COALESCE((SELECT creado_por FROM ventas WHERE id = NEW.venta_id), 1)
    FROM productos p 
    WHERE p.id = NEW.producto_id;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `metodos_pago_venta`
--

CREATE TABLE `metodos_pago_venta` (
  `id` bigint(20) NOT NULL,
  `venta_id` bigint(20) NOT NULL,
  `tipo_pago` enum('EFECTIVO','TARJETA_DEBITO','TARJETA_CREDITO','YAPE','PLIN','TRANSFERENCIA','CHEQUE','CREDITO') NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `referencia` varchar(100) DEFAULT NULL,
  `numero_operacion` varchar(50) DEFAULT NULL,
  `banco` varchar(100) DEFAULT NULL,
  `tipo_tarjeta` varchar(50) DEFAULT NULL,
  `ultimos_4_digitos` varchar(4) DEFAULT NULL,
  `fecha_vencimiento_tarjeta` varchar(7) DEFAULT NULL,
  `numero_cuotas` int(11) DEFAULT 1,
  `tasa_interes` decimal(5,2) DEFAULT 0.00,
  `comision` decimal(10,2) DEFAULT 0.00,
  `estado` enum('PENDIENTE','APROBADO','RECHAZADO','ANULADO') DEFAULT 'APROBADO',
  `fecha_pago` timestamp NOT NULL DEFAULT current_timestamp(),
  `notas` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientos_inventario`
--

CREATE TABLE `movimientos_inventario` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `tipo_movimiento` enum('ENTRADA','SALIDA','AJUSTE','TRANSFERENCIA','DEVOLUCION') NOT NULL,
  `motivo` enum('COMPRA','VENTA','AJUSTE_INVENTARIO','MERMA','ROBO','DEVOLUCION_CLIENTE','DEVOLUCION_PROVEEDOR','PROMOCION','MUESTRA','OTROS') NOT NULL,
  `cantidad_anterior` int(11) NOT NULL,
  `cantidad_movimiento` int(11) NOT NULL,
  `cantidad_actual` int(11) NOT NULL,
  `costo_unitario` decimal(10,2) DEFAULT NULL,
  `valor_total` decimal(12,2) DEFAULT NULL,
  `referencia_documento` varchar(100) DEFAULT NULL,
  `venta_id` bigint(20) DEFAULT NULL,
  `proveedor_id` bigint(20) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `fecha_movimiento` timestamp NOT NULL DEFAULT current_timestamp(),
  `usuario_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `password_reset_tokens`
--

CREATE TABLE `password_reset_tokens` (
  `id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  `token` varchar(255) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_expiracion` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `usado` tinyint(1) DEFAULT 0,
  `ip_solicitud` varchar(45) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `sku` varchar(100) NOT NULL,
  `codigo_barras` varchar(50) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `descripcion_corta` varchar(500) DEFAULT NULL,
  `marca` varchar(100) DEFAULT NULL,
  `modelo` varchar(100) DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `precio_oferta` decimal(10,2) DEFAULT NULL,
  `costo` decimal(10,2) DEFAULT NULL,
  `stock` int(11) NOT NULL DEFAULT 0,
  `stock_minimo` int(11) DEFAULT 5,
  `stock_maximo` int(11) DEFAULT NULL,
  `unidad_medida` varchar(20) DEFAULT 'UNIDAD',
  `peso` decimal(8,3) DEFAULT NULL,
  `dimensiones` varchar(100) DEFAULT NULL,
  `imagen_principal` varchar(255) DEFAULT NULL,
  `categoria_id` bigint(20) NOT NULL,
  `proveedor_id` bigint(20) DEFAULT NULL,
  `ubicacion_almacen` varchar(100) DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `es_perecedero` tinyint(1) DEFAULT 0,
  `requiere_talla` tinyint(1) DEFAULT 1,
  `requiere_color` tinyint(1) DEFAULT 1,
  `destacado` tinyint(1) DEFAULT 0,
  `nuevo` tinyint(1) DEFAULT 1,
  `en_oferta` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  `meta_title` varchar(255) DEFAULT NULL,
  `meta_description` varchar(500) DEFAULT NULL,
  `tags` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`tags`)),
  `imagen` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_colores`
--

CREATE TABLE `producto_colores` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `color` varchar(50) NOT NULL,
  `codigo_hex` varchar(7) DEFAULT '#000000',
  `stock_color` int(11) NOT NULL DEFAULT 0,
  `precio_adicional` decimal(10,2) DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_imagenes`
--

CREATE TABLE `producto_imagenes` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `url_imagen` varchar(255) NOT NULL,
  `alt_text` varchar(255) DEFAULT NULL,
  `orden` int(11) DEFAULT 0,
  `es_principal` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto_tallas`
--

CREATE TABLE `producto_tallas` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `talla` varchar(10) NOT NULL,
  `stock_talla` int(11) NOT NULL DEFAULT 0,
  `precio_adicional` decimal(10,2) DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedores`
--

CREATE TABLE `proveedores` (
  `id` bigint(20) NOT NULL,
  `codigo_proveedor` varchar(20) DEFAULT NULL,
  `razon_social` varchar(200) NOT NULL,
  `nombre_comercial` varchar(200) DEFAULT NULL,
  `ruc` varchar(11) DEFAULT NULL,
  `tipo_documento` enum('RUC','DNI','PASAPORTE') DEFAULT 'RUC',
  `numero_documento` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `distrito` varchar(100) DEFAULT NULL,
  `provincia` varchar(100) DEFAULT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `pais` varchar(50) DEFAULT 'Perú',
  `contacto_principal` varchar(100) DEFAULT NULL,
  `telefono_contacto` varchar(20) DEFAULT NULL,
  `email_contacto` varchar(100) DEFAULT NULL,
  `condiciones_pago` varchar(200) DEFAULT NULL,
  `tiempo_entrega_dias` int(11) DEFAULT NULL,
  `calificacion` decimal(3,2) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `notas` text DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sesiones_usuario`
--

CREATE TABLE `sesiones_usuario` (
  `id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  `token_sesion` varchar(255) NOT NULL,
  `ip_address` varchar(45) DEFAULT NULL,
  `user_agent` varchar(500) DEFAULT NULL,
  `fecha_inicio` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_ultimo_acceso` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_expiracion` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `activa` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('SUPER_ADMIN','ADMIN','MANAGER','VENDEDOR','CAJERO','INVENTARIO') NOT NULL DEFAULT 'VENDEDOR',
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(255) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `ultimo_acceso` timestamp NULL DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `foto_perfil` varchar(255) DEFAULT NULL,
  `intentos_recuperacion` int(11) DEFAULT 0,
  `ultimo_intento_recuperacion` timestamp NULL DEFAULT NULL,
  `configuracion_usuario` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`configuracion_usuario`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `apellido`, `email`, `password`, `rol`, `telefono`, `direccion`, `fecha_creacion`, `fecha_actualizacion`, `ultimo_acceso`, `activo`, `foto_perfil`, `intentos_recuperacion`, `ultimo_intento_recuperacion`, `configuracion_usuario`) VALUES
(1, 'Super', 'Administrador', 'admin@dpattymoda.com', '$2a$10$UVF7iyQYdCWkqzXHFm77EOxpNwmUg1VqISzollJhWg8jlpVUD.Huu', 'SUPER_ADMIN', NULL, NULL, '2025-07-17 18:50:06', '2025-07-17 18:50:06', NULL, 1, NULL, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id` bigint(20) NOT NULL,
  `numero_venta` varchar(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp(),
  `subtotal` decimal(10,2) DEFAULT NULL,
  `descuento_porcentaje` decimal(5,2) DEFAULT 0.00,
  `descuento_monto` decimal(10,2) NOT NULL DEFAULT 0.00,
  `impuesto_porcentaje` decimal(5,2) DEFAULT 0.00,
  `impuesto_monto` decimal(10,2) NOT NULL DEFAULT 0.00,
  `total` decimal(12,2) NOT NULL,
  `estado` enum('PENDIENTE','PAGADA','PARCIALMENTE_PAGADA','ANULADA','DEVUELTA') NOT NULL DEFAULT 'PENDIENTE',
  `tipo_comprobante` enum('BOLETA','FACTURA','NOTA_VENTA') DEFAULT 'BOLETA',
  `serie_comprobante` varchar(10) DEFAULT NULL,
  `numero_comprobante` varchar(20) DEFAULT NULL,
  `moneda` varchar(3) DEFAULT 'PEN',
  `tipo_cambio` decimal(8,4) DEFAULT 1.0000,
  `observaciones` text DEFAULT NULL,
  `notas_internas` text DEFAULT NULL,
  `canal_venta` enum('TIENDA','ONLINE','TELEFONO','WHATSAPP','FACEBOOK','INSTAGRAM') DEFAULT 'TIENDA',
  `vendedor_id` bigint(20) DEFAULT NULL,
  `cajero_id` bigint(20) DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL,
  `fecha_entrega` date DEFAULT NULL,
  `direccion_entrega` varchar(500) DEFAULT NULL,
  `costo_envio` decimal(10,2) DEFAULT 0.00,
  `peso_total` decimal(8,3) DEFAULT NULL,
  `cantidad_items` int(11) DEFAULT 0,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL,
  `datos_adicionales` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL CHECK (json_valid(`datos_adicionales`)),
  `descuento` decimal(10,2) DEFAULT NULL,
  `impuesto` decimal(10,2) DEFAULT NULL,
  `metodo_pago` varchar(50) DEFAULT NULL,
  `notas` text DEFAULT NULL,
  `precio_total` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Disparadores `ventas`
--
DELIMITER $$
CREATE TRIGGER `after_update_venta_pagada` AFTER UPDATE ON `ventas` FOR EACH ROW BEGIN
    IF OLD.estado != 'PAGADA' AND NEW.estado = 'PAGADA' THEN
        UPDATE clientes 
        SET 
            total_compras = total_compras + NEW.total,
            cantidad_compras = cantidad_compras + 1,
            ultima_compra = NEW.fecha
        WHERE id = NEW.cliente_id;
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `before_insert_venta` BEFORE INSERT ON `ventas` FOR EACH ROW BEGIN
    IF NEW.numero_venta IS NULL OR NEW.numero_venta = '' THEN
        SET NEW.numero_venta = CONCAT('V', YEAR(NOW()), LPAD(
            COALESCE(
                (SELECT MAX(CAST(SUBSTRING(numero_venta, 6) AS UNSIGNED))
                 FROM ventas 
                 WHERE numero_venta LIKE CONCAT('V', YEAR(NOW()), '%')), 0
            ) + 1, 6, '0'
        ));
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_clientes_estadisticas`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_clientes_estadisticas` (
`id` bigint(20)
,`codigo_cliente` varchar(20)
,`nombre` varchar(100)
,`apellido` varchar(100)
,`email` varchar(100)
,`telefono` varchar(20)
,`tipo_cliente` enum('REGULAR','VIP','MAYORISTA','MINORISTA')
,`total_compras` decimal(10,2)
,`cantidad_compras` int(11)
,`ultima_compra` timestamp
,`activo` tinyint(1)
,`categoria_cliente` varchar(7)
,`ticket_promedio` decimal(14,6)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_productos_completa`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_productos_completa` (
`id` bigint(20)
,`nombre` varchar(255)
,`sku` varchar(100)
,`descripcion` text
,`precio` decimal(10,2)
,`precio_oferta` decimal(10,2)
,`costo` decimal(10,2)
,`stock` int(11)
,`stock_minimo` int(11)
,`imagen_principal` varchar(255)
,`categoria_nombre` varchar(100)
,`categoria_id` bigint(20)
,`marca` varchar(100)
,`activo` tinyint(1)
,`destacado` tinyint(1)
,`en_oferta` tinyint(1)
,`fecha_creacion` timestamp
,`estado_stock` varchar(10)
,`margen_porcentaje` decimal(17,2)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_ventas_completa`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_ventas_completa` (
`id` bigint(20)
,`numero_venta` varchar(20)
,`fecha` timestamp
,`subtotal` decimal(10,2)
,`descuento_monto` decimal(10,2)
,`impuesto_monto` decimal(10,2)
,`total` decimal(12,2)
,`estado` enum('PENDIENTE','PAGADA','PARCIALMENTE_PAGADA','ANULADA','DEVUELTA')
,`tipo_comprobante` enum('BOLETA','FACTURA','NOTA_VENTA')
,`canal_venta` enum('TIENDA','ONLINE','TELEFONO','WHATSAPP','FACEBOOK','INSTAGRAM')
,`cliente_nombre` varchar(201)
,`cliente_email` varchar(100)
,`cliente_telefono` varchar(20)
,`vendedor_nombre` varchar(201)
,`cantidad_items` int(11)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_clientes_estadisticas`
--
DROP TABLE IF EXISTS `vista_clientes_estadisticas`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_clientes_estadisticas`  AS SELECT `c`.`id` AS `id`, `c`.`codigo_cliente` AS `codigo_cliente`, `c`.`nombre` AS `nombre`, `c`.`apellido` AS `apellido`, `c`.`email` AS `email`, `c`.`telefono` AS `telefono`, `c`.`tipo_cliente` AS `tipo_cliente`, `c`.`total_compras` AS `total_compras`, `c`.`cantidad_compras` AS `cantidad_compras`, `c`.`ultima_compra` AS `ultima_compra`, `c`.`activo` AS `activo`, CASE WHEN `c`.`total_compras` >= 5000 THEN 'VIP' WHEN `c`.`total_compras` >= 2000 THEN 'PREMIUM' WHEN `c`.`total_compras` >= 500 THEN 'REGULAR' ELSE 'NUEVO' END AS `categoria_cliente`, coalesce(`c`.`total_compras` / nullif(`c`.`cantidad_compras`,0),0) AS `ticket_promedio` FROM `clientes` AS `c` ;

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_productos_completa`
--
DROP TABLE IF EXISTS `vista_productos_completa`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_productos_completa`  AS SELECT `p`.`id` AS `id`, `p`.`nombre` AS `nombre`, `p`.`sku` AS `sku`, `p`.`descripcion` AS `descripcion`, `p`.`precio` AS `precio`, `p`.`precio_oferta` AS `precio_oferta`, `p`.`costo` AS `costo`, `p`.`stock` AS `stock`, `p`.`stock_minimo` AS `stock_minimo`, `p`.`imagen_principal` AS `imagen_principal`, `c`.`nombre` AS `categoria_nombre`, `c`.`id` AS `categoria_id`, `p`.`marca` AS `marca`, `p`.`activo` AS `activo`, `p`.`destacado` AS `destacado`, `p`.`en_oferta` AS `en_oferta`, `p`.`fecha_creacion` AS `fecha_creacion`, CASE WHEN `p`.`stock` <= 0 THEN 'SIN_STOCK' WHEN `p`.`stock` <= `p`.`stock_minimo` THEN 'STOCK_BAJO' ELSE 'STOCK_OK' END AS `estado_stock`, round((`p`.`precio` - `p`.`costo`) / `p`.`precio` * 100,2) AS `margen_porcentaje` FROM (`productos` `p` left join `categorias` `c` on(`p`.`categoria_id` = `c`.`id`)) ;

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_ventas_completa`
--
DROP TABLE IF EXISTS `vista_ventas_completa`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_ventas_completa`  AS SELECT `v`.`id` AS `id`, `v`.`numero_venta` AS `numero_venta`, `v`.`fecha` AS `fecha`, `v`.`subtotal` AS `subtotal`, `v`.`descuento_monto` AS `descuento_monto`, `v`.`impuesto_monto` AS `impuesto_monto`, `v`.`total` AS `total`, `v`.`estado` AS `estado`, `v`.`tipo_comprobante` AS `tipo_comprobante`, `v`.`canal_venta` AS `canal_venta`, concat(`c`.`nombre`,' ',coalesce(`c`.`apellido`,'')) AS `cliente_nombre`, `c`.`email` AS `cliente_email`, `c`.`telefono` AS `cliente_telefono`, concat(`u`.`nombre`,' ',coalesce(`u`.`apellido`,'')) AS `vendedor_nombre`, `v`.`cantidad_items` AS `cantidad_items` FROM ((`ventas` `v` left join `clientes` `c` on(`v`.`cliente_id` = `c`.`id`)) left join `usuarios` `u` on(`v`.`vendedor_id` = `u`.`id`)) ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre_categoria_padre` (`nombre`,`categoria_padre_id`),
  ADD KEY `idx_categorias_padre` (`categoria_padre_id`),
  ADD KEY `idx_categorias_activo` (`activo`),
  ADD KEY `idx_categorias_orden` (`orden`),
  ADD KEY `fk_categoria_creador` (`creado_por`),
  ADD KEY `fk_categoria_modificador` (`modificado_por`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `codigo_cliente` (`codigo_cliente`),
  ADD KEY `idx_clientes_documento` (`tipo_documento`,`numero_documento`),
  ADD KEY `idx_clientes_tipo` (`tipo_cliente`),
  ADD KEY `idx_clientes_activo` (`activo`),
  ADD KEY `idx_clientes_total_compras` (`total_compras`),
  ADD KEY `fk_cliente_creador` (`creado_por`),
  ADD KEY `fk_cliente_modificador` (`modificado_por`),
  ADD KEY `idx_clientes_total_compras_desc` (`total_compras`);

--
-- Indices de la tabla `configuracion_impuestos`
--
ALTER TABLE `configuracion_impuestos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codigo` (`codigo`),
  ADD KEY `idx_impuestos_activo` (`activo`),
  ADD KEY `idx_impuestos_por_defecto` (`aplicar_por_defecto`),
  ADD KEY `idx_impuestos_tipo` (`tipo`),
  ADD KEY `fk_impuesto_creador` (`creado_por`);

--
-- Indices de la tabla `configuracion_tienda`
--
ALTER TABLE `configuracion_tienda`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_detalle_venta` (`venta_id`),
  ADD KEY `idx_detalle_producto` (`producto_id`);

--
-- Indices de la tabla `metodos_pago_venta`
--
ALTER TABLE `metodos_pago_venta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_metodos_pago_venta` (`venta_id`),
  ADD KEY `idx_metodos_pago_tipo` (`tipo_pago`);

--
-- Indices de la tabla `movimientos_inventario`
--
ALTER TABLE `movimientos_inventario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_movimientos_producto` (`producto_id`),
  ADD KEY `idx_movimientos_tipo` (`tipo_movimiento`),
  ADD KEY `idx_movimientos_fecha` (`fecha_movimiento`),
  ADD KEY `idx_movimientos_usuario` (`usuario_id`),
  ADD KEY `idx_movimientos_venta` (`venta_id`),
  ADD KEY `fk_movimiento_proveedor` (`proveedor_id`);

--
-- Indices de la tabla `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token` (`token`),
  ADD KEY `idx_token_usuario` (`usuario_id`),
  ADD KEY `idx_token_expiracion` (`fecha_expiracion`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sku` (`sku`),
  ADD UNIQUE KEY `codigo_barras` (`codigo_barras`),
  ADD KEY `idx_productos_categoria` (`categoria_id`),
  ADD KEY `idx_productos_activo` (`activo`),
  ADD KEY `idx_productos_stock` (`stock`),
  ADD KEY `idx_productos_precio` (`precio`),
  ADD KEY `idx_productos_destacado` (`destacado`),
  ADD KEY `idx_productos_oferta` (`en_oferta`),
  ADD KEY `idx_productos_marca` (`marca`),
  ADD KEY `fk_producto_creador` (`creado_por`),
  ADD KEY `fk_producto_modificador` (`modificado_por`),
  ADD KEY `idx_productos_categoria_activo` (`categoria_id`,`activo`);

--
-- Indices de la tabla `producto_colores`
--
ALTER TABLE `producto_colores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_producto_color` (`producto_id`,`color`),
  ADD KEY `idx_producto_colores_producto` (`producto_id`);

--
-- Indices de la tabla `producto_imagenes`
--
ALTER TABLE `producto_imagenes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_producto_imagenes_producto` (`producto_id`),
  ADD KEY `idx_producto_imagenes_principal` (`es_principal`);

--
-- Indices de la tabla `producto_tallas`
--
ALTER TABLE `producto_tallas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_producto_talla` (`producto_id`,`talla`),
  ADD KEY `idx_producto_tallas_producto` (`producto_id`);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codigo_proveedor` (`codigo_proveedor`),
  ADD KEY `idx_proveedores_activo` (`activo`),
  ADD KEY `fk_proveedor_creador` (`creado_por`);

--
-- Indices de la tabla `sesiones_usuario`
--
ALTER TABLE `sesiones_usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token_sesion` (`token_sesion`),
  ADD KEY `idx_sesiones_usuario` (`usuario_id`),
  ADD KEY `idx_sesiones_activa` (`activa`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_usuarios_rol` (`rol`),
  ADD KEY `idx_usuarios_activo` (`activo`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `numero_venta` (`numero_venta`),
  ADD KEY `idx_ventas_cliente` (`cliente_id`),
  ADD KEY `idx_ventas_estado` (`estado`),
  ADD KEY `idx_ventas_fecha` (`fecha`),
  ADD KEY `idx_ventas_vendedor` (`vendedor_id`),
  ADD KEY `idx_ventas_total` (`total`),
  ADD KEY `idx_ventas_comprobante` (`tipo_comprobante`,`serie_comprobante`,`numero_comprobante`),
  ADD KEY `fk_venta_cajero` (`cajero_id`),
  ADD KEY `fk_venta_creador` (`creado_por`),
  ADD KEY `idx_ventas_fecha_estado` (`fecha`,`estado`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `configuracion_impuestos`
--
ALTER TABLE `configuracion_impuestos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `configuracion_tienda`
--
ALTER TABLE `configuracion_tienda`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `metodos_pago_venta`
--
ALTER TABLE `metodos_pago_venta`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `movimientos_inventario`
--
ALTER TABLE `movimientos_inventario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto_colores`
--
ALTER TABLE `producto_colores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto_imagenes`
--
ALTER TABLE `producto_imagenes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `producto_tallas`
--
ALTER TABLE `producto_tallas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `sesiones_usuario`
--
ALTER TABLE `sesiones_usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `ventas`
--
ALTER TABLE `ventas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD CONSTRAINT `fk_categoria_creador` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_categoria_modificador` FOREIGN KEY (`modificado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_categoria_padre` FOREIGN KEY (`categoria_padre_id`) REFERENCES `categorias` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD CONSTRAINT `fk_cliente_creador` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_cliente_modificador` FOREIGN KEY (`modificado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `configuracion_impuestos`
--
ALTER TABLE `configuracion_impuestos`
  ADD CONSTRAINT `fk_impuesto_creador` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD CONSTRAINT `fk_detalle_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `fk_detalle_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `metodos_pago_venta`
--
ALTER TABLE `metodos_pago_venta`
  ADD CONSTRAINT `fk_metodo_pago_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `movimientos_inventario`
--
ALTER TABLE `movimientos_inventario`
  ADD CONSTRAINT `fk_movimiento_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_movimiento_proveedor` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_movimiento_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `fk_movimiento_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD CONSTRAINT `fk_token_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `fk_producto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  ADD CONSTRAINT `fk_producto_creador` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_producto_modificador` FOREIGN KEY (`modificado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `producto_colores`
--
ALTER TABLE `producto_colores`
  ADD CONSTRAINT `fk_producto_color` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `producto_imagenes`
--
ALTER TABLE `producto_imagenes`
  ADD CONSTRAINT `fk_producto_imagen` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `producto_tallas`
--
ALTER TABLE `producto_tallas`
  ADD CONSTRAINT `fk_producto_talla` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD CONSTRAINT `fk_proveedor_creador` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `sesiones_usuario`
--
ALTER TABLE `sesiones_usuario`
  ADD CONSTRAINT `fk_sesion_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD CONSTRAINT `fk_venta_cajero` FOREIGN KEY (`cajero_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_venta_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `fk_venta_creador` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `fk_venta_vendedor` FOREIGN KEY (`vendedor_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
