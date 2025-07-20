-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-07-2025 a las 04:08:12
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
-- Base de datos: `pattymoda_nueva`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `actualizar_stock_venta` (IN `p_producto_id` BIGINT, IN `p_talla_id` BIGINT, IN `p_cantidad` INT, IN `p_venta_id` BIGINT, IN `p_usuario_id` BIGINT)   BEGIN
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

CREATE DEFINER=`root`@`localhost` PROCEDURE `procesar_puntos_lealtad` (IN `p_cliente_id` BIGINT, IN `p_venta_id` BIGINT, IN `p_monto_compra` DECIMAL(10,2))   BEGIN
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

--
-- Funciones
--
CREATE DEFINER=`root`@`localhost` FUNCTION `calcular_puntos_compra` (`monto` DECIMAL(10,2)) RETURNS INT(11) DETERMINISTIC READS SQL DATA BEGIN
    DECLARE puntos INT DEFAULT 0;
    
    -- 1 punto por cada 10 soles de compra
    SET puntos = FLOOR(monto / 10);
    
    RETURN puntos;
END$$

CREATE DEFINER=`root`@`localhost` FUNCTION `generar_codigo_cliente` () RETURNS VARCHAR(20) CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci DETERMINISTIC READS SQL DATA BEGIN
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

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `arqueos_caja`
--

CREATE TABLE `arqueos_caja` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `auditoria_logs`
--

CREATE TABLE `auditoria_logs` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cajas`
--

CREATE TABLE `cajas` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `cajas`
--

INSERT INTO `cajas` (`id`, `codigo`, `nombre`, `descripcion`, `ubicacion`, `responsable_id`, `monto_inicial`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'CAJA01', 'Caja Principal', 'Caja registradora principal de la tienda', 'Mostrador Principal', 1, 200.00, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'CAJA02', 'Caja Secundaria', 'Caja registradora secundaria', 'Mostrador Lateral', 1, 100.00, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `canales_venta`
--

CREATE TABLE `canales_venta` (
  `id` bigint(20) NOT NULL,
  `codigo` varchar(20) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `comision_porcentaje` decimal(5,2) DEFAULT 0.00,
  `requiere_entrega` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `canales_venta`
--

INSERT INTO `canales_venta` (`id`, `codigo`, `nombre`, `descripcion`, `comision_porcentaje`, `requiere_entrega`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'TIENDA', 'Tienda Física', 'Ventas realizadas en la tienda física', 0.00, 0, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'ONLINE', 'Tienda Online', 'Ventas realizadas por la página web', 2.50, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'WHATSAPP', 'WhatsApp', 'Ventas realizadas por WhatsApp', 1.00, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'FACEBOOK', 'Facebook', 'Ventas realizadas por Facebook', 3.00, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'INSTAGRAM', 'Instagram', 'Ventas realizadas por Instagram', 3.00, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `categorias`
--

CREATE TABLE `categorias` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `categorias`
--

INSERT INTO `categorias` (`id`, `codigo`, `nombre`, `descripcion`, `categoria_padre_id`, `imagen`, `icono`, `orden`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'ROPA_MUJER', 'Ropa para Mujer', 'Prendas de vestir femeninas', NULL, NULL, NULL, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'ROPA_HOMBRE', 'Ropa para Hombre', 'Prendas de vestir masculinas', NULL, NULL, NULL, 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'CALZADO', 'Calzado', 'Zapatos y zapatillas', NULL, NULL, NULL, 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'ACCESORIOS', 'Accesorios', 'Complementos y accesorios', NULL, NULL, NULL, 4, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'INFANTIL', 'Ropa Infantil', 'Ropa para niños y niñas', NULL, NULL, NULL, 5, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'BLUSAS', 'Blusas', 'Blusas y camisas para mujer', 1, NULL, NULL, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'PANTALONES_M', 'Pantalones', 'Pantalones para mujer', 1, NULL, NULL, 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 'VESTIDOS', 'Vestidos', 'Vestidos casuales y elegantes', 1, NULL, NULL, 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(9, 'CAMISAS_H', 'Camisas', 'Camisas para hombre', 2, NULL, NULL, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(10, 'PANTALONES_H', 'Pantalones', 'Pantalones para hombre', 2, NULL, NULL, 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(11, 'POLOS', 'Polos', 'Polos y camisetas', 2, NULL, NULL, 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(12, 'ZAPATOS_M', 'Zapatos Mujer', 'Calzado femenino', 3, NULL, NULL, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(13, 'ZAPATOS_H', 'Zapatos Hombre', 'Calzado masculino', 3, NULL, NULL, 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(14, 'ZAPATILLAS', 'Zapatillas', 'Calzado deportivo', 3, NULL, NULL, 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

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
  `fecha_nacimiento` date DEFAULT NULL,
  `genero` enum('M','F','OTRO') DEFAULT NULL,
  `estado_civil` enum('SOLTERO','CASADO','DIVORCIADO','VIUDO','OTRO') DEFAULT NULL,
  `ocupacion` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `codigo_cliente`, `tipo_documento`, `numero_documento`, `nombre`, `apellido`, `razon_social`, `fecha_nacimiento`, `genero`, `estado_civil`, `ocupacion`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'CLI001', 'DNI', '12345678', 'María', 'García López', NULL, NULL, 'F', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'CLI002', 'DNI', '87654321', 'Juan', 'Pérez Rodríguez', NULL, NULL, 'M', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'CLI003', 'DNI', '11223344', 'Ana', 'Martínez Silva', NULL, NULL, 'F', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'CLI004', 'RUC', '20123456789', 'Empresa ABC', 'SAC', NULL, NULL, NULL, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes_comercial`
--

CREATE TABLE `clientes_comercial` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `clientes_comercial`
--

INSERT INTO `clientes_comercial` (`id`, `cliente_id`, `tipo_cliente`, `total_compras`, `cantidad_compras`, `ultima_compra`, `limite_credito`, `descuento_personalizado`, `puntos_disponibles`, `nivel_cliente`, `vendedor_asignado_id`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 1, 'REGULAR', 450.80, 3, NULL, 500.00, 0.00, 0, 'BRONCE', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 2, 'VIP', 1250.50, 8, NULL, 2000.00, 0.00, 0, 'PLATA', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 3, 'REGULAR', 320.90, 2, NULL, 500.00, 0.00, 0, 'BRONCE', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 4, 'MAYORISTA', 5680.00, 15, NULL, 10000.00, 0.00, 0, 'ORO', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes_contacto`
--

CREATE TABLE `clientes_contacto` (
  `id` bigint(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `tipo_contacto` enum('TELEFONO','CELULAR','EMAIL','WHATSAPP','FACEBOOK','INSTAGRAM','DIRECCION','OTROS') NOT NULL,
  `valor` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `es_principal` tinyint(1) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes_direcciones`
--

CREATE TABLE `clientes_direcciones` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes_preferencias`
--

CREATE TABLE `clientes_preferencias` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `colores`
--

CREATE TABLE `colores` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `colores`
--

INSERT INTO `colores` (`id`, `codigo`, `nombre`, `descripcion`, `codigo_hex`, `codigo_rgb`, `familia_color`, `orden_visualizacion`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'NEGRO', 'Negro', 'Color negro clásico', '#000000', NULL, 'NEUTROS', 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'BLANCO', 'Blanco', 'Color blanco puro', '#FFFFFF', NULL, 'NEUTROS', 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'GRIS', 'Gris', 'Color gris medio', '#808080', NULL, 'NEUTROS', 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'AZUL', 'Azul', 'Azul clásico', '#0000FF', NULL, 'FRIOS', 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'ROJO', 'Rojo', 'Rojo intenso', '#FF0000', NULL, 'CALIDOS', 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'VERDE', 'Verde', 'Verde natural', '#008000', NULL, 'FRIOS', 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'AMARILLO', 'Amarillo', 'Amarillo brillante', '#FFFF00', NULL, 'CALIDOS', 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 'ROSA', 'Rosa', 'Rosa suave', '#FFC0CB', NULL, 'CALIDOS', 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(9, 'MORADO', 'Morado', 'Morado elegante', '#800080', NULL, 'FRIOS', 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(10, 'NARANJA', 'Naranja', 'Naranja vibrante', '#FFA500', NULL, 'CALIDOS', 4, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(11, 'MARRON', 'Marrón', 'Marrón tierra', '#A52A2A', NULL, 'NEUTROS', 4, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(12, 'BEIGE', 'Beige', 'Beige natural', '#F5F5DC', NULL, 'NEUTROS', 5, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comentarios_tarea`
--

CREATE TABLE `comentarios_tarea` (
  `id` bigint(20) NOT NULL,
  `tarea_id` bigint(20) NOT NULL,
  `usuario_id` bigint(20) NOT NULL,
  `comentario` text NOT NULL,
  `fecha_comentario` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `compras`
--

CREATE TABLE `compras` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `configuracion_impuestos`
--

CREATE TABLE `configuracion_impuestos` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `configuracion_impuestos`
--

INSERT INTO `configuracion_impuestos` (`id`, `nombre`, `codigo`, `porcentaje`, `tipo`, `activo`, `aplicar_por_defecto`, `aplica_a_productos`, `aplica_a_servicios`, `descripcion`, `base_legal`, `fecha_vigencia_inicio`, `fecha_vigencia_fin`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'IGV', 'IGV', 18.00, 'IGV', 1, 1, 1, 1, 'Impuesto General a las Ventas', NULL, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'ISC', 'ISC', 10.00, 'ISC', 1, 0, 1, 1, 'Impuesto Selectivo al Consumo', NULL, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL);

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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `configuracion_tienda`
--

INSERT INTO `configuracion_tienda` (`id`, `nombre`, `slogan`, `descripcion`, `ruc`, `direccion`, `distrito_id`, `telefono`, `telefono_secundario`, `email`, `email_ventas`, `email_soporte`, `sitio_web`, `facebook`, `instagram`, `whatsapp`, `logo`, `favicon`, `moneda_principal`, `idioma_principal`, `zona_horaria`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'DPattyModa', 'Tu estilo, nuestra pasión', 'Tienda de ropa y accesorios con las últimas tendencias de la moda', '20123456789', 'Av. Principal 123, Pampa Hermosa', 1, '+51965123456', NULL, 'info@dpattymoda.com', 'ventas@dpattymoda.com', NULL, 'https://dpattymoda.com', NULL, NULL, '+51965123456', NULL, NULL, 'PEN', 'es_PE', 'America/Lima', 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizaciones`
--

CREATE TABLE `cotizaciones` (
  `id` bigint(20) NOT NULL,
  `numero_cotizacion` varchar(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `fecha_cotizacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `subtotal` decimal(12,2) DEFAULT 0.00,
  `total` decimal(12,2) NOT NULL DEFAULT 0.00,
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `departamentos`
--

CREATE TABLE `departamentos` (
  `id` bigint(20) NOT NULL,
  `pais_id` bigint(20) NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `departamentos`
--

INSERT INTO `departamentos` (`id`, `pais_id`, `codigo`, `nombre`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 1, '16', 'Loreto', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 1, '15', 'Lima', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 1, '04', 'Arequipa', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 1, '08', 'Cusco', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 1, '14', 'La Libertad', 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_compra`
--

CREATE TABLE `detalle_compra` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_cotizacion`
--

CREATE TABLE `detalle_cotizacion` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_devolucion`
--

CREATE TABLE `detalle_devolucion` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_transferencia`
--

CREATE TABLE `detalle_transferencia` (
  `id` bigint(20) NOT NULL,
  `transferencia_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `cantidad_enviada` int(11) NOT NULL,
  `cantidad_recibida` int(11) DEFAULT 0,
  `observaciones` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_venta`
--

CREATE TABLE `detalle_venta` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `devoluciones`
--

CREATE TABLE `devoluciones` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `distritos`
--

CREATE TABLE `distritos` (
  `id` bigint(20) NOT NULL,
  `provincia_id` bigint(20) NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `codigo_postal` varchar(10) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `distritos`
--

INSERT INTO `distritos` (`id`, `provincia_id`, `codigo`, `nombre`, `codigo_postal`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 1, '160101', 'Iquitos', '16001', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 1, '160102', 'Alto Nanay', '16002', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 1, '160103', 'Fernando Lores', '16003', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 1, '160104', 'Indiana', '16004', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 1, '160105', 'Las Amazonas', '16005', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 1, '160106', 'Mazan', '16006', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 1, '160107', 'Napo', '16007', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 1, '160108', 'Punchana', '16008', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(9, 1, '160109', 'Torres Causana', '16009', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(10, 1, '160110', 'Belén', '16010', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(11, 1, '160111', 'San Juan Bautista', '16011', 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horarios_atencion`
--

CREATE TABLE `horarios_atencion` (
  `id` bigint(20) NOT NULL,
  `dia_semana` enum('LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO') NOT NULL,
  `hora_apertura` varchar(5) DEFAULT NULL,
  `hora_cierre` varchar(5) DEFAULT NULL,
  `cerrado` tinyint(1) DEFAULT 0,
  `observaciones` varchar(200) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `horarios_atencion`
--

INSERT INTO `horarios_atencion` (`id`, `dia_semana`, `hora_apertura`, `hora_cierre`, `cerrado`, `observaciones`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'LUNES', '09:00', '18:00', 0, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'MARTES', '09:00', '18:00', 0, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'MIERCOLES', '09:00', '18:00', 0, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'JUEVES', '09:00', '18:00', 0, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'VIERNES', '09:00', '18:00', 0, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'SABADO', '09:00', '17:00', 0, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'DOMINGO', NULL, NULL, 1, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `marcas`
--

CREATE TABLE `marcas` (
  `id` bigint(20) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `logo` varchar(255) DEFAULT NULL,
  `sitio_web` varchar(200) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `marcas`
--

INSERT INTO `marcas` (`id`, `nombre`, `descripcion`, `logo`, `sitio_web`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'PattyModa', 'Marca propia de la tienda', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'Adidas', 'Marca deportiva internacional', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'Nike', 'Marca deportiva líder mundial', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'Zara', 'Moda contemporánea', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'H&M', 'Moda accesible', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'Forever 21', 'Moda juvenil', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'Levi\'s', 'Marca de jeans icónica', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 'Converse', 'Calzado casual', NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

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
  `numero_cuotas` int(11) DEFAULT 1,
  `tasa_interes` decimal(5,2) DEFAULT 0.00,
  `comision` decimal(10,2) DEFAULT 0.00,
  `estado` enum('PENDIENTE','APROBADO','RECHAZADO','ANULADO') NOT NULL DEFAULT 'APROBADO',
  `fecha_pago` timestamp NULL DEFAULT NULL,
  `notas` varchar(500) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimientos_inventario`
--

CREATE TABLE `movimientos_inventario` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `paises`
--

CREATE TABLE `paises` (
  `id` bigint(20) NOT NULL,
  `codigo_iso` varchar(3) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `codigo_telefono` varchar(5) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `paises`
--

INSERT INTO `paises` (`id`, `codigo_iso`, `nombre`, `codigo_telefono`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'PER', 'Perú', '+51', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'COL', 'Colombia', '+57', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'ECU', 'Ecuador', '+593', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'BOL', 'Bolivia', '+591', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'BRA', 'Brasil', '+55', 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

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
  `user_agent` varchar(500) DEFAULT NULL,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `permisos`
--

CREATE TABLE `permisos` (
  `id` bigint(20) NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `modulo` varchar(50) NOT NULL,
  `accion` varchar(50) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `permisos`
--

INSERT INTO `permisos` (`id`, `codigo`, `modulo`, `accion`, `descripcion`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'PRODUCTOS_READ', 'PRODUCTOS', 'READ', 'Ver productos', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'PRODUCTOS_CREATE', 'PRODUCTOS', 'CREATE', 'Crear productos', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'PRODUCTOS_UPDATE', 'PRODUCTOS', 'UPDATE', 'Actualizar productos', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'PRODUCTOS_DELETE', 'PRODUCTOS', 'DELETE', 'Eliminar productos', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'VENTAS_READ', 'VENTAS', 'READ', 'Ver ventas', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'VENTAS_CREATE', 'VENTAS', 'CREATE', 'Crear ventas', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'VENTAS_UPDATE', 'VENTAS', 'UPDATE', 'Actualizar ventas', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 'CLIENTES_READ', 'CLIENTES', 'READ', 'Ver clientes', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(9, 'CLIENTES_CREATE', 'CLIENTES', 'CREATE', 'Crear clientes', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(10, 'CLIENTES_UPDATE', 'CLIENTES', 'UPDATE', 'Actualizar clientes', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(11, 'USUARIOS_READ', 'USUARIOS', 'READ', 'Ver usuarios', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(12, 'USUARIOS_CREATE', 'USUARIOS', 'CREATE', 'Crear usuarios', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(13, 'USUARIOS_UPDATE', 'USUARIOS', 'UPDATE', 'Actualizar usuarios', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(14, 'REPORTES_READ', 'REPORTES', 'READ', 'Ver reportes', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(15, 'CONFIGURACION_UPDATE', 'CONFIGURACION', 'UPDATE', 'Configurar sistema', 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `codigo_producto`, `nombre`, `sku`, `codigo_barras`, `descripcion`, `descripcion_corta`, `categoria_id`, `marca_id`, `modelo`, `peso`, `dimensiones`, `imagen_principal`, `requiere_talla`, `requiere_color`, `es_perecedero`, `tiempo_entrega_dias`, `garantia_meses`, `destacado`, `nuevo`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'BLUSA001', 'Blusa Elegante Manga Larga', 'BLU-ELE-001', NULL, 'Blusa elegante de manga larga perfecta para ocasiones especiales', 'Blusa elegante manga larga', 6, 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 1, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'JEAN001', 'Jean Clásico Mujer', 'JEAN-CLA-001', NULL, 'Jean clásico de corte recto para mujer, cómodo y versátil', 'Jean clásico mujer', 7, 7, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 0, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'VESTIDO001', 'Vestido Casual Verano', 'VEST-CAS-001', NULL, 'Vestido casual perfecto para el verano, fresco y cómodo', 'Vestido casual verano', 8, 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 1, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'CAMISA001', 'Camisa Formal Hombre', 'CAM-FOR-001', NULL, 'Camisa formal para hombre, ideal para oficina', 'Camisa formal hombre', 9, 4, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 0, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'POLO001', 'Polo Deportivo', 'POL-DEP-001', NULL, 'Polo deportivo de algodón, cómodo para actividades físicas', 'Polo deportivo', 11, 2, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 0, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'ZAP001', 'Zapatos Casuales Mujer', 'ZAP-CAS-001', NULL, 'Zapatos casuales cómodos para uso diario', 'Zapatos casuales mujer', 12, 8, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 1, 0, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'ZAP002', 'Zapatos Formales Hombre', 'ZAP-FOR-001', NULL, 'Zapatos formales de cuero para hombre', 'Zapatos formales hombre', 13, 1, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 0, 0, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 'ZAPATILLA001', 'Zapatillas Deportivas', 'ZAP-DEP-001', NULL, 'Zapatillas deportivas para running y ejercicio', 'Zapatillas deportivas', 14, 3, NULL, NULL, NULL, NULL, 1, 1, 0, 1, 0, 1, 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

--
-- Disparadores `productos`
--
DELIMITER $$
CREATE TRIGGER `tr_productos_audit_insert` AFTER INSERT ON `productos` FOR EACH ROW BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_nuevos, fecha_accion)
    VALUES ('productos', NEW.id, 'INSERT', 
            JSON_OBJECT('codigo_producto', NEW.codigo_producto, 'nombre', NEW.nombre, 'sku', NEW.sku), 
            NOW());
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `tr_productos_audit_update` AFTER UPDATE ON `productos` FOR EACH ROW BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_anteriores, datos_nuevos, fecha_accion)
    VALUES ('productos', NEW.id, 'UPDATE',
            JSON_OBJECT('codigo_producto', OLD.codigo_producto, 'nombre', OLD.nombre, 'sku', OLD.sku),
            JSON_OBJECT('codigo_producto', NEW.codigo_producto, 'nombre', NEW.nombre, 'sku', NEW.sku),
            NOW());
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos_colores`
--

CREATE TABLE `productos_colores` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `color_id` bigint(20) NOT NULL,
  `stock_color` int(11) DEFAULT 0,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos_imagenes`
--

CREATE TABLE `productos_imagenes` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos_inventario`
--

CREATE TABLE `productos_inventario` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `stock_actual` int(11) DEFAULT 0,
  `stock_minimo` int(11) DEFAULT 0,
  `stock_maximo` int(11) DEFAULT 0,
  `stock_disponible` int(11) DEFAULT 0,
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos_precios`
--

CREATE TABLE `productos_precios` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `productos_precios`
--

INSERT INTO `productos_precios` (`id`, `producto_id`, `precio_venta`, `precio_oferta`, `costo`, `margen_porcentaje`, `fecha_inicio`, `fecha_fin`, `activo`, `motivo_cambio`, `usuario_id`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 1, 89.90, 79.90, 45.00, 99.78, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 2, 129.90, NULL, 65.00, 99.85, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 3, 149.90, 129.90, 75.00, 99.83, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 4, 99.90, NULL, 50.00, 99.80, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 5, 59.90, NULL, 30.00, 99.67, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 6, 179.90, 159.90, 90.00, 99.78, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 7, 249.90, NULL, 125.00, 99.80, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 8, 299.90, 269.90, 150.00, 99.83, '2025-07-18', NULL, 1, NULL, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos_seo`
--

CREATE TABLE `productos_seo` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `meta_title` varchar(255) DEFAULT NULL,
  `meta_description` varchar(500) DEFAULT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `tags` longtext DEFAULT NULL,
  `palabras_clave` text DEFAULT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos_tallas`
--

CREATE TABLE `productos_tallas` (
  `id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `talla_id` bigint(20) NOT NULL,
  `stock_talla` int(11) NOT NULL DEFAULT 0,
  `precio_adicional` decimal(10,2) DEFAULT 0.00,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `programa_lealtad`
--

CREATE TABLE `programa_lealtad` (
  `id` bigint(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `puntos_acumulados` int(11) DEFAULT 0,
  `puntos_canjeados` int(11) DEFAULT 0,
  `nivel_cliente` enum('BRONCE','PLATA','ORO','PLATINO') DEFAULT 'BRONCE',
  `fecha_ultimo_movimiento` timestamp NULL DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `programa_lealtad`
--

INSERT INTO `programa_lealtad` (`id`, `cliente_id`, `puntos_acumulados`, `puntos_canjeados`, `nivel_cliente`, `fecha_ultimo_movimiento`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 1, 450, 50, 'BRONCE', NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 2, 1250, 200, 'PLATA', NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 3, 320, 0, 'BRONCE', NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 4, 5680, 1000, 'ORO', NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `promociones`
--

CREATE TABLE `promociones` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `promociones_categorias`
--

CREATE TABLE `promociones_categorias` (
  `id` bigint(20) NOT NULL,
  `promocion_id` bigint(20) NOT NULL,
  `categoria_id` bigint(20) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `promociones_productos`
--

CREATE TABLE `promociones_productos` (
  `id` bigint(20) NOT NULL,
  `promocion_id` bigint(20) NOT NULL,
  `producto_id` bigint(20) NOT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `promociones_uso`
--

CREATE TABLE `promociones_uso` (
  `id` bigint(20) NOT NULL,
  `promocion_id` bigint(20) NOT NULL,
  `venta_id` bigint(20) NOT NULL,
  `cliente_id` bigint(20) NOT NULL,
  `monto_descuento` decimal(10,2) NOT NULL,
  `fecha_uso` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `provincias`
--

CREATE TABLE `provincias` (
  `id` bigint(20) NOT NULL,
  `departamento_id` bigint(20) NOT NULL,
  `codigo` varchar(10) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `provincias`
--

INSERT INTO `provincias` (`id`, `departamento_id`, `codigo`, `nombre`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 1, '1601', 'Maynas', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 1, '1602', 'Alto Amazonas', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 1, '1603', 'Loreto', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 1, '1604', 'Mariscal Ramón Castilla', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 1, '1605', 'Requena', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 1, '1606', 'Ucayali', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 1, '1607', 'Datem del Marañón', 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 1, '1608', 'Putumayo', 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `codigo` varchar(30) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `nivel_acceso` int(11) DEFAULT 1,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `codigo`, `nombre`, `descripcion`, `nivel_acceso`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'SUPER_ADMIN', 'Super Administrador', 'Acceso total al sistema', 5, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'ADMIN', 'Administrador', 'Administrador general', 4, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'MANAGER', 'Gerente', 'Gerente de tienda', 3, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'VENDEDOR', 'Vendedor', 'Personal de ventas', 2, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'CAJERO', 'Cajero', 'Personal de caja', 1, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles_permisos`
--

CREATE TABLE `roles_permisos` (
  `id` bigint(20) NOT NULL,
  `rol_id` bigint(20) NOT NULL,
  `permiso_id` bigint(20) NOT NULL,
  `concedido` tinyint(1) DEFAULT 1,
  `fecha_asignacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `asignado_por` bigint(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `roles_permisos`
--

INSERT INTO `roles_permisos` (`id`, `rol_id`, `permiso_id`, `concedido`, `fecha_asignacion`, `asignado_por`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 1, 9, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 1, 8, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 1, 10, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 1, 15, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 1, 2, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 1, 4, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 1, 1, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 1, 3, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(9, 1, 14, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(10, 1, 12, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(11, 1, 11, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(12, 1, 13, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(13, 1, 6, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(14, 1, 5, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL),
(15, 1, 7, 1, '2025-07-18 21:35:56', NULL, '2025-07-18 21:35:56', NULL, NULL, NULL);

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
  `fecha_ultimo_acceso` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_expiracion` timestamp NULL DEFAULT NULL,
  `activa` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tallas`
--

CREATE TABLE `tallas` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `tallas`
--

INSERT INTO `tallas` (`id`, `codigo`, `nombre`, `descripcion`, `categoria_talla`, `orden_visualizacion`, `medidas_cm`, `equivalencia_internacional`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'XS', 'Extra Small', 'Talla extra pequeña', 'ROPA_MUJER', 1, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'S', 'Small', 'Talla pequeña', 'ROPA_MUJER', 2, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'M', 'Medium', 'Talla mediana', 'ROPA_MUJER', 3, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'L', 'Large', 'Talla grande', 'ROPA_MUJER', 4, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'XL', 'Extra Large', 'Talla extra grande', 'ROPA_MUJER', 5, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'XXL', 'Double Extra Large', 'Talla doble extra grande', 'ROPA_MUJER', 6, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'S_H', 'Small Hombre', 'Talla pequeña hombre', 'ROPA_HOMBRE', 1, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(8, 'M_H', 'Medium Hombre', 'Talla mediana hombre', 'ROPA_HOMBRE', 2, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(9, 'L_H', 'Large Hombre', 'Talla grande hombre', 'ROPA_HOMBRE', 3, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(10, 'XL_H', 'Extra Large Hombre', 'Talla extra grande hombre', 'ROPA_HOMBRE', 4, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(11, '36', '36', 'Talla 36 calzado', 'CALZADO_MUJER', 1, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(12, '37', '37', 'Talla 37 calzado', 'CALZADO_MUJER', 2, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(13, '38', '38', 'Talla 38 calzado', 'CALZADO_MUJER', 3, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(14, '39', '39', 'Talla 39 calzado', 'CALZADO_MUJER', 4, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(15, '40', '40', 'Talla 40 calzado', 'CALZADO_MUJER', 5, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(16, '41', '41', 'Talla 41 calzado', 'CALZADO_HOMBRE', 1, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(17, '42', '42', 'Talla 42 calzado', 'CALZADO_HOMBRE', 2, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(18, '43', '43', 'Talla 43 calzado', 'CALZADO_HOMBRE', 3, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(19, '44', '44', 'Talla 44 calzado', 'CALZADO_HOMBRE', 4, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(20, '45', '45', 'Talla 45 calzado', 'CALZADO_HOMBRE', 5, NULL, NULL, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tareas_seguimiento`
--

CREATE TABLE `tareas_seguimiento` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transacciones_puntos`
--

CREATE TABLE `transacciones_puntos` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transferencias_inventario`
--

CREATE TABLE `transferencias_inventario` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `unidades_medida`
--

CREATE TABLE `unidades_medida` (
  `id` bigint(20) NOT NULL,
  `codigo` varchar(10) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `simbolo` varchar(10) DEFAULT NULL,
  `tipo` enum('PESO','LONGITUD','VOLUMEN','UNIDAD','TIEMPO') DEFAULT NULL,
  `factor_conversion` decimal(10,6) DEFAULT 1.000000,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_actualizacion` timestamp NULL DEFAULT NULL ON UPDATE current_timestamp(),
  `creado_por` bigint(20) DEFAULT NULL,
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `unidades_medida`
--

INSERT INTO `unidades_medida` (`id`, `codigo`, `nombre`, `simbolo`, `tipo`, `factor_conversion`, `activo`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'UND', 'Unidad', 'und', 'UNIDAD', 1.000000, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(2, 'KG', 'Kilogramo', 'kg', 'PESO', 1.000000, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(3, 'GR', 'Gramo', 'g', 'PESO', 0.001000, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(4, 'M', 'Metro', 'm', 'LONGITUD', 1.000000, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(5, 'CM', 'Centímetro', 'cm', 'LONGITUD', 0.010000, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(6, 'L', 'Litro', 'l', 'VOLUMEN', 1.000000, 1, '2025-07-18 21:35:56', NULL, NULL, NULL),
(7, 'ML', 'Mililitro', 'ml', 'VOLUMEN', 0.001000, 1, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `codigo_empleado`, `nombre`, `apellido`, `email`, `password`, `rol_id`, `telefono`, `direccion`, `fecha_nacimiento`, `fecha_ingreso`, `salario_base`, `comision_porcentaje`, `foto_perfil`, `activo`, `ultimo_acceso`, `intentos_login_fallidos`, `bloqueado_hasta`, `fecha_creacion`, `fecha_actualizacion`, `creado_por`, `modificado_por`) VALUES
(1, 'ADMIN001', 'Administrador', 'Sistema', 'admin@pattymoda.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 1, '+51965123456', NULL, NULL, NULL, NULL, 0.00, NULL, 1, NULL, 0, NULL, '2025-07-18 21:35:56', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas`
--

CREATE TABLE `ventas` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Disparadores `ventas`
--
DELIMITER $$
CREATE TRIGGER `tr_ventas_audit_insert` AFTER INSERT ON `ventas` FOR EACH ROW BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_nuevos, fecha_accion)
    VALUES ('ventas', NEW.id, 'INSERT',
            JSON_OBJECT('numero_venta', NEW.numero_venta, 'cliente_id', NEW.cliente_id, 'total', NEW.total),
            NOW());
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `tr_ventas_audit_update` AFTER UPDATE ON `ventas` FOR EACH ROW BEGIN
    INSERT INTO auditoria_logs (tabla, registro_id, accion, datos_anteriores, datos_nuevos, fecha_accion)
    VALUES ('ventas', NEW.id, 'UPDATE',
            JSON_OBJECT('estado', OLD.estado, 'total', OLD.total),
            JSON_OBJECT('estado', NEW.estado, 'total', NEW.total),
            NOW());
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas_descuentos`
--

CREATE TABLE `ventas_descuentos` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas_entrega`
--

CREATE TABLE `ventas_entrega` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventas_facturacion`
--

CREATE TABLE `ventas_facturacion` (
  `id` bigint(20) NOT NULL,
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
  `modificado_por` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_clientes_estadistica`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_clientes_estadistica` (
`id` bigint(20)
,`codigo_cliente` varchar(20)
,`nombre` varchar(100)
,`apellido` varchar(100)
,`numero_documento` varchar(20)
,`tipo_documento` enum('DNI','RUC','PASAPORTE','CARNET_EXTRANJERIA')
,`tipo_cliente` enum('REGULAR','VIP','MAYORISTA','MINORISTA','CORPORATIVO')
,`total_compras` decimal(12,2)
,`cantidad_compras` int(11)
,`ultima_compra` timestamp
,`limite_credito` decimal(10,2)
,`descuento_personalizado` decimal(5,2)
,`puntos_disponibles` int(11)
,`nivel_cliente` enum('BRONCE','PLATA','ORO','PLATINO')
,`activo` tinyint(1)
,`categoria_cliente` varchar(7)
,`ticket_promedio` decimal(16,6)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_productos_completa`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_productos_completa` (
`id` bigint(20)
,`codigo_producto` varchar(50)
,`nombre` varchar(255)
,`sku` varchar(100)
,`descripcion` text
,`descripcion_corta` varchar(500)
,`categoria_nombre` varchar(100)
,`categoria_id` bigint(20)
,`marca_nombre` varchar(100)
,`marca_id` bigint(20)
,`precio` decimal(10,2)
,`precio_oferta` decimal(10,2)
,`costo` decimal(10,2)
,`margen_porcentaje` decimal(5,2)
,`stock` decimal(32,0)
,`imagen_principal` varchar(255)
,`requiere_talla` tinyint(1)
,`requiere_color` tinyint(1)
,`destacado` tinyint(1)
,`activo` tinyint(1)
,`fecha_creacion` timestamp
,`estado_stock` varchar(10)
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
,`subtotal` decimal(12,2)
,`total` decimal(12,2)
,`estado` enum('PENDIENTE','PAGADA','PARCIALMENTE_PAGADA','ANULADA','DEVUELTA')
,`tipo_comprobante` enum('BOLETA','FACTURA','NOTA_VENTA','TICKET')
,`serie_comprobante` varchar(10)
,`numero_comprobante` varchar(20)
,`canal_venta` varchar(50)
,`cliente_nombre` varchar(201)
,`cliente_documento` varchar(20)
,`vendedor_nombre` varchar(201)
,`cantidad_items` int(11)
,`comision_vendedor` decimal(10,2)
,`estado_entrega` enum('PENDIENTE','EN_TRANSITO','ENTREGADO','DEVUELTO','CANCELADO')
,`fecha_entrega_real` timestamp
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_clientes_estadistica`
--
DROP TABLE IF EXISTS `vista_clientes_estadistica`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_clientes_estadistica`  AS SELECT `c`.`id` AS `id`, `c`.`codigo_cliente` AS `codigo_cliente`, `c`.`nombre` AS `nombre`, `c`.`apellido` AS `apellido`, `c`.`numero_documento` AS `numero_documento`, `c`.`tipo_documento` AS `tipo_documento`, `cc`.`tipo_cliente` AS `tipo_cliente`, `cc`.`total_compras` AS `total_compras`, `cc`.`cantidad_compras` AS `cantidad_compras`, `cc`.`ultima_compra` AS `ultima_compra`, `cc`.`limite_credito` AS `limite_credito`, `cc`.`descuento_personalizado` AS `descuento_personalizado`, `pl`.`puntos_acumulados` AS `puntos_disponibles`, `pl`.`nivel_cliente` AS `nivel_cliente`, `c`.`activo` AS `activo`, CASE WHEN `cc`.`total_compras` >= 5000 THEN 'VIP' WHEN `cc`.`total_compras` >= 2000 THEN 'PREMIUM' WHEN `cc`.`total_compras` >= 500 THEN 'REGULAR' ELSE 'NUEVO' END AS `categoria_cliente`, coalesce(`cc`.`total_compras` / nullif(`cc`.`cantidad_compras`,0),0) AS `ticket_promedio` FROM ((`clientes` `c` left join `clientes_comercial` `cc` on(`c`.`id` = `cc`.`cliente_id`)) left join `programa_lealtad` `pl` on(`c`.`id` = `pl`.`cliente_id`)) ;

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_productos_completa`
--
DROP TABLE IF EXISTS `vista_productos_completa`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_productos_completa`  AS SELECT `p`.`id` AS `id`, `p`.`codigo_producto` AS `codigo_producto`, `p`.`nombre` AS `nombre`, `p`.`sku` AS `sku`, `p`.`descripcion` AS `descripcion`, `p`.`descripcion_corta` AS `descripcion_corta`, `c`.`nombre` AS `categoria_nombre`, `c`.`id` AS `categoria_id`, `m`.`nombre` AS `marca_nombre`, `m`.`id` AS `marca_id`, `pp`.`precio_venta` AS `precio`, `pp`.`precio_oferta` AS `precio_oferta`, `pp`.`costo` AS `costo`, `pp`.`margen_porcentaje` AS `margen_porcentaje`, coalesce(sum(`pt`.`stock_talla`),0) AS `stock`, `p`.`imagen_principal` AS `imagen_principal`, `p`.`requiere_talla` AS `requiere_talla`, `p`.`requiere_color` AS `requiere_color`, `p`.`destacado` AS `destacado`, `p`.`activo` AS `activo`, `p`.`fecha_creacion` AS `fecha_creacion`, CASE WHEN coalesce(sum(`pt`.`stock_talla`),0) <= 0 THEN 'SIN_STOCK' WHEN coalesce(sum(`pt`.`stock_talla`),0) <= 5 THEN 'STOCK_BAJO' ELSE 'STOCK_OK' END AS `estado_stock` FROM ((((`productos` `p` left join `categorias` `c` on(`p`.`categoria_id` = `c`.`id`)) left join `marcas` `m` on(`p`.`marca_id` = `m`.`id`)) left join `productos_precios` `pp` on(`p`.`id` = `pp`.`producto_id` and `pp`.`activo` = 1 and (`pp`.`fecha_fin` is null or `pp`.`fecha_fin` >= curdate()))) left join `productos_tallas` `pt` on(`p`.`id` = `pt`.`producto_id` and `pt`.`activo` = 1)) GROUP BY `p`.`id`, `p`.`codigo_producto`, `p`.`nombre`, `p`.`sku`, `p`.`descripcion`, `p`.`descripcion_corta`, `c`.`nombre`, `c`.`id`, `m`.`nombre`, `m`.`id`, `pp`.`precio_venta`, `pp`.`precio_oferta`, `pp`.`costo`, `pp`.`margen_porcentaje`, `p`.`imagen_principal`, `p`.`requiere_talla`, `p`.`requiere_color`, `p`.`destacado`, `p`.`activo`, `p`.`fecha_creacion` ;

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_ventas_completa`
--
DROP TABLE IF EXISTS `vista_ventas_completa`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vista_ventas_completa`  AS SELECT `v`.`id` AS `id`, `v`.`numero_venta` AS `numero_venta`, `v`.`fecha` AS `fecha`, `v`.`subtotal` AS `subtotal`, `v`.`total` AS `total`, `v`.`estado` AS `estado`, `vf`.`tipo_comprobante` AS `tipo_comprobante`, `vf`.`serie_comprobante` AS `serie_comprobante`, `vf`.`numero_comprobante` AS `numero_comprobante`, `cv`.`nombre` AS `canal_venta`, concat(`c`.`nombre`,' ',coalesce(`c`.`apellido`,'')) AS `cliente_nombre`, `c`.`numero_documento` AS `cliente_documento`, concat(`u`.`nombre`,' ',coalesce(`u`.`apellido`,'')) AS `vendedor_nombre`, `v`.`cantidad_items` AS `cantidad_items`, `v`.`comision_vendedor` AS `comision_vendedor`, `ve`.`estado_entrega` AS `estado_entrega`, `ve`.`fecha_entrega_real` AS `fecha_entrega_real` FROM (((((`ventas` `v` left join `clientes` `c` on(`v`.`cliente_id` = `c`.`id`)) left join `usuarios` `u` on(`v`.`vendedor_id` = `u`.`id`)) left join `canales_venta` `cv` on(`v`.`canal_venta_id` = `cv`.`id`)) left join `ventas_facturacion` `vf` on(`v`.`id` = `vf`.`venta_id`)) left join `ventas_entrega` `ve` on(`v`.`id` = `ve`.`venta_id`)) ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `arqueos_caja`
--
ALTER TABLE `arqueos_caja`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_arqueos_caja_caja` (`caja_id`),
  ADD KEY `FK_arqueos_caja_usuario_apertura` (`usuario_apertura`),
  ADD KEY `FK_arqueos_caja_usuario_cierre` (`usuario_cierre`);

--
-- Indices de la tabla `auditoria_logs`
--
ALTER TABLE `auditoria_logs`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_auditoria_logs_usuario` (`usuario_id`),
  ADD KEY `IDX_auditoria_logs_tabla_registro` (`tabla`,`registro_id`),
  ADD KEY `IDX_auditoria_logs_fecha` (`fecha_accion`);

--
-- Indices de la tabla `cajas`
--
ALTER TABLE `cajas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_cajas_codigo` (`codigo`),
  ADD KEY `FK_cajas_responsable` (`responsable_id`);

--
-- Indices de la tabla `canales_venta`
--
ALTER TABLE `canales_venta`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_canales_venta_codigo` (`codigo`);

--
-- Indices de la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_categorias_codigo` (`codigo`),
  ADD KEY `FK_categorias_padre` (`categoria_padre_id`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_clientes_codigo` (`codigo_cliente`),
  ADD UNIQUE KEY `UK_clientes_documento` (`numero_documento`),
  ADD KEY `IDX_clientes_activo` (`activo`),
  ADD KEY `IDX_clientes_tipo_documento` (`tipo_documento`,`numero_documento`);

--
-- Indices de la tabla `clientes_comercial`
--
ALTER TABLE `clientes_comercial`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_clientes_comercial_cliente` (`cliente_id`),
  ADD KEY `FK_clientes_comercial_vendedor` (`vendedor_asignado_id`);

--
-- Indices de la tabla `clientes_contacto`
--
ALTER TABLE `clientes_contacto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_clientes_contacto_cliente` (`cliente_id`);

--
-- Indices de la tabla `clientes_direcciones`
--
ALTER TABLE `clientes_direcciones`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_clientes_direcciones_cliente` (`cliente_id`),
  ADD KEY `FK_clientes_direcciones_distrito` (`distrito_id`);

--
-- Indices de la tabla `clientes_preferencias`
--
ALTER TABLE `clientes_preferencias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_clientes_preferencias_cliente` (`cliente_id`),
  ADD KEY `FK_clientes_preferencias_categoria` (`categoria_preferida_id`),
  ADD KEY `FK_clientes_preferencias_talla` (`talla_preferida_id`),
  ADD KEY `FK_clientes_preferencias_color` (`color_preferido_id`),
  ADD KEY `FK_clientes_preferencias_marca` (`marca_preferida_id`);

--
-- Indices de la tabla `colores`
--
ALTER TABLE `colores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_colores_codigo` (`codigo`);

--
-- Indices de la tabla `comentarios_tarea`
--
ALTER TABLE `comentarios_tarea`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_comentarios_tarea_tarea` (`tarea_id`),
  ADD KEY `FK_comentarios_tarea_usuario` (`usuario_id`);

--
-- Indices de la tabla `compras`
--
ALTER TABLE `compras`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_compras_numero` (`numero_compra`),
  ADD KEY `FK_compras_proveedor` (`proveedor_id`);

--
-- Indices de la tabla `configuracion_impuestos`
--
ALTER TABLE `configuracion_impuestos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_configuracion_impuestos_codigo` (`codigo`);

--
-- Indices de la tabla `configuracion_tienda`
--
ALTER TABLE `configuracion_tienda`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_configuracion_tienda_distrito` (`distrito_id`);

--
-- Indices de la tabla `cotizaciones`
--
ALTER TABLE `cotizaciones`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_cotizaciones_numero` (`numero_cotizacion`),
  ADD KEY `FK_cotizaciones_cliente` (`cliente_id`);

--
-- Indices de la tabla `departamentos`
--
ALTER TABLE `departamentos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_departamentos_pais` (`pais_id`);

--
-- Indices de la tabla `detalle_compra`
--
ALTER TABLE `detalle_compra`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_detalle_compra_compra` (`compra_id`),
  ADD KEY `FK_detalle_compra_producto` (`producto_id`);

--
-- Indices de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_detalle_cotizacion_cotizacion` (`cotizacion_id`),
  ADD KEY `FK_detalle_cotizacion_producto` (`producto_id`),
  ADD KEY `FK_detalle_cotizacion_talla` (`talla_id`),
  ADD KEY `FK_detalle_cotizacion_color` (`color_id`);

--
-- Indices de la tabla `detalle_devolucion`
--
ALTER TABLE `detalle_devolucion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_detalle_devolucion_devolucion` (`devolucion_id`),
  ADD KEY `FK_detalle_devolucion_detalle_venta` (`detalle_venta_id`);

--
-- Indices de la tabla `detalle_transferencia`
--
ALTER TABLE `detalle_transferencia`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_detalle_transferencia_transferencia` (`transferencia_id`),
  ADD KEY `FK_detalle_transferencia_producto` (`producto_id`);

--
-- Indices de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_detalle_venta_venta` (`venta_id`),
  ADD KEY `FK_detalle_venta_producto` (`producto_id`),
  ADD KEY `FK_detalle_venta_talla` (`talla_id`),
  ADD KEY `FK_detalle_venta_color` (`color_id`);

--
-- Indices de la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_devoluciones_numero` (`numero_devolucion`),
  ADD KEY `FK_devoluciones_venta` (`venta_id`),
  ADD KEY `FK_devoluciones_cliente` (`cliente_id`),
  ADD KEY `FK_devoluciones_autorizado_por` (`autorizado_por`),
  ADD KEY `FK_devoluciones_procesado_por` (`procesado_por`);

--
-- Indices de la tabla `distritos`
--
ALTER TABLE `distritos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_distritos_provincia` (`provincia_id`);

--
-- Indices de la tabla `horarios_atencion`
--
ALTER TABLE `horarios_atencion`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_horarios_atencion_dia` (`dia_semana`);

--
-- Indices de la tabla `marcas`
--
ALTER TABLE `marcas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_marcas_nombre` (`nombre`);

--
-- Indices de la tabla `metodos_pago_venta`
--
ALTER TABLE `metodos_pago_venta`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_metodos_pago_venta_venta` (`venta_id`);

--
-- Indices de la tabla `movimientos_inventario`
--
ALTER TABLE `movimientos_inventario`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_movimientos_inventario_producto` (`producto_id`),
  ADD KEY `FK_movimientos_inventario_venta` (`venta_id`),
  ADD KEY `FK_movimientos_inventario_usuario` (`usuario_id`),
  ADD KEY `IDX_movimientos_inventario_fecha` (`fecha_movimiento`),
  ADD KEY `IDX_movimientos_inventario_producto_fecha` (`producto_id`,`fecha_movimiento`);

--
-- Indices de la tabla `paises`
--
ALTER TABLE `paises`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_paises_codigo_iso` (`codigo_iso`);

--
-- Indices de la tabla `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_password_reset_tokens_token` (`token`),
  ADD KEY `FK_password_reset_tokens_usuario` (`usuario_id`);

--
-- Indices de la tabla `permisos`
--
ALTER TABLE `permisos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_permisos_codigo` (`codigo`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_productos_codigo` (`codigo_producto`),
  ADD UNIQUE KEY `UK_productos_sku` (`sku`),
  ADD UNIQUE KEY `UK_productos_codigo_barras` (`codigo_barras`),
  ADD KEY `FK_productos_categoria` (`categoria_id`),
  ADD KEY `FK_productos_marca` (`marca_id`),
  ADD KEY `IDX_productos_activo` (`activo`),
  ADD KEY `IDX_productos_destacado` (`destacado`),
  ADD KEY `IDX_productos_nuevo` (`nuevo`),
  ADD KEY `IDX_productos_categoria_activo` (`categoria_id`,`activo`),
  ADD KEY `IDX_productos_marca_activo` (`marca_id`,`activo`);

--
-- Indices de la tabla `productos_colores`
--
ALTER TABLE `productos_colores`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_productos_colores_producto` (`producto_id`),
  ADD KEY `FK_productos_colores_color` (`color_id`);

--
-- Indices de la tabla `productos_imagenes`
--
ALTER TABLE `productos_imagenes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_productos_imagenes_producto` (`producto_id`),
  ADD KEY `FK_productos_imagenes_color` (`color_id`);

--
-- Indices de la tabla `productos_inventario`
--
ALTER TABLE `productos_inventario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_productos_inventario_producto` (`producto_id`);

--
-- Indices de la tabla `productos_precios`
--
ALTER TABLE `productos_precios`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_productos_precios_producto` (`producto_id`),
  ADD KEY `FK_productos_precios_usuario` (`usuario_id`);

--
-- Indices de la tabla `productos_seo`
--
ALTER TABLE `productos_seo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_productos_seo_producto` (`producto_id`),
  ADD UNIQUE KEY `UK_productos_seo_slug` (`slug`);

--
-- Indices de la tabla `productos_tallas`
--
ALTER TABLE `productos_tallas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_productos_tallas_producto` (`producto_id`),
  ADD KEY `FK_productos_tallas_talla` (`talla_id`);

--
-- Indices de la tabla `programa_lealtad`
--
ALTER TABLE `programa_lealtad`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_programa_lealtad_cliente` (`cliente_id`);

--
-- Indices de la tabla `promociones`
--
ALTER TABLE `promociones`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_promociones_codigo` (`codigo`),
  ADD KEY `FK_promociones_creado_por` (`creado_por`);

--
-- Indices de la tabla `promociones_categorias`
--
ALTER TABLE `promociones_categorias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_promociones_categorias_promocion` (`promocion_id`),
  ADD KEY `FK_promociones_categorias_categoria` (`categoria_id`);

--
-- Indices de la tabla `promociones_productos`
--
ALTER TABLE `promociones_productos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_promociones_productos_promocion` (`promocion_id`),
  ADD KEY `FK_promociones_productos_producto` (`producto_id`);

--
-- Indices de la tabla `promociones_uso`
--
ALTER TABLE `promociones_uso`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_promociones_uso_promocion` (`promocion_id`),
  ADD KEY `FK_promociones_uso_venta` (`venta_id`),
  ADD KEY `FK_promociones_uso_cliente` (`cliente_id`);

--
-- Indices de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_proveedores_codigo` (`codigo_proveedor`),
  ADD UNIQUE KEY `UK_proveedores_documento` (`numero_documento`),
  ADD KEY `FK_proveedores_distrito` (`distrito_id`);

--
-- Indices de la tabla `provincias`
--
ALTER TABLE `provincias`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_provincias_departamento` (`departamento_id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_roles_codigo` (`codigo`);

--
-- Indices de la tabla `roles_permisos`
--
ALTER TABLE `roles_permisos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_roles_permisos_rol` (`rol_id`),
  ADD KEY `FK_roles_permisos_permiso` (`permiso_id`);

--
-- Indices de la tabla `sesiones_usuario`
--
ALTER TABLE `sesiones_usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_sesiones_usuario_token` (`token_sesion`),
  ADD KEY `FK_sesiones_usuario_usuario` (`usuario_id`);

--
-- Indices de la tabla `tallas`
--
ALTER TABLE `tallas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_tallas_codigo` (`codigo`);

--
-- Indices de la tabla `tareas_seguimiento`
--
ALTER TABLE `tareas_seguimiento`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_tareas_seguimiento_usuario_asignado` (`usuario_asignado_id`),
  ADD KEY `FK_tareas_seguimiento_usuario_creador` (`usuario_creador_id`),
  ADD KEY `FK_tareas_seguimiento_cliente` (`cliente_id`),
  ADD KEY `FK_tareas_seguimiento_venta` (`venta_id`),
  ADD KEY `FK_tareas_seguimiento_cotizacion` (`cotizacion_id`);

--
-- Indices de la tabla `transacciones_puntos`
--
ALTER TABLE `transacciones_puntos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_transacciones_puntos_cliente` (`cliente_id`),
  ADD KEY `FK_transacciones_puntos_venta` (`venta_id`),
  ADD KEY `FK_transacciones_puntos_usuario` (`usuario_id`);

--
-- Indices de la tabla `transferencias_inventario`
--
ALTER TABLE `transferencias_inventario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_transferencias_inventario_numero` (`numero_transferencia`),
  ADD KEY `FK_transferencias_inventario_autorizado_por` (`autorizado_por`),
  ADD KEY `FK_transferencias_inventario_recibido_por` (`recibido_por`);

--
-- Indices de la tabla `unidades_medida`
--
ALTER TABLE `unidades_medida`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_unidades_medida_codigo` (`codigo`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_usuarios_email` (`email`),
  ADD UNIQUE KEY `UK_usuarios_codigo_empleado` (`codigo_empleado`),
  ADD KEY `FK_usuarios_rol` (`rol_id`);

--
-- Indices de la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ventas_numero` (`numero_venta`),
  ADD KEY `FK_ventas_cliente` (`cliente_id`),
  ADD KEY `FK_ventas_canal` (`canal_venta_id`),
  ADD KEY `FK_ventas_vendedor` (`vendedor_id`),
  ADD KEY `FK_ventas_cajero` (`cajero_id`),
  ADD KEY `IDX_ventas_fecha` (`fecha`),
  ADD KEY `IDX_ventas_estado` (`estado`),
  ADD KEY `IDX_ventas_cliente_fecha` (`cliente_id`,`fecha`),
  ADD KEY `IDX_ventas_vendedor_fecha` (`vendedor_id`,`fecha`);

--
-- Indices de la tabla `ventas_descuentos`
--
ALTER TABLE `ventas_descuentos`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_ventas_descuentos_venta` (`venta_id`),
  ADD KEY `FK_ventas_descuentos_aplicado_por` (`aplicado_por`);

--
-- Indices de la tabla `ventas_entrega`
--
ALTER TABLE `ventas_entrega`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ventas_entrega_venta` (`venta_id`),
  ADD KEY `FK_ventas_entrega_distrito` (`distrito_entrega_id`);

--
-- Indices de la tabla `ventas_facturacion`
--
ALTER TABLE `ventas_facturacion`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ventas_facturacion_venta` (`venta_id`),
  ADD UNIQUE KEY `UK_ventas_facturacion_comprobante` (`numero_comprobante`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `arqueos_caja`
--
ALTER TABLE `arqueos_caja`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `auditoria_logs`
--
ALTER TABLE `auditoria_logs`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cajas`
--
ALTER TABLE `cajas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `canales_venta`
--
ALTER TABLE `canales_venta`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `categorias`
--
ALTER TABLE `categorias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `clientes_comercial`
--
ALTER TABLE `clientes_comercial`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `clientes_contacto`
--
ALTER TABLE `clientes_contacto`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clientes_direcciones`
--
ALTER TABLE `clientes_direcciones`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `clientes_preferencias`
--
ALTER TABLE `clientes_preferencias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `colores`
--
ALTER TABLE `colores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `comentarios_tarea`
--
ALTER TABLE `comentarios_tarea`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `compras`
--
ALTER TABLE `compras`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `configuracion_impuestos`
--
ALTER TABLE `configuracion_impuestos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `configuracion_tienda`
--
ALTER TABLE `configuracion_tienda`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `cotizaciones`
--
ALTER TABLE `cotizaciones`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `departamentos`
--
ALTER TABLE `departamentos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `detalle_compra`
--
ALTER TABLE `detalle_compra`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_devolucion`
--
ALTER TABLE `detalle_devolucion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_transferencia`
--
ALTER TABLE `detalle_transferencia`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `distritos`
--
ALTER TABLE `distritos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `horarios_atencion`
--
ALTER TABLE `horarios_atencion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `marcas`
--
ALTER TABLE `marcas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

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
-- AUTO_INCREMENT de la tabla `paises`
--
ALTER TABLE `paises`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `permisos`
--
ALTER TABLE `permisos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `productos_colores`
--
ALTER TABLE `productos_colores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos_imagenes`
--
ALTER TABLE `productos_imagenes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos_inventario`
--
ALTER TABLE `productos_inventario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos_precios`
--
ALTER TABLE `productos_precios`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `productos_seo`
--
ALTER TABLE `productos_seo`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `productos_tallas`
--
ALTER TABLE `productos_tallas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `programa_lealtad`
--
ALTER TABLE `programa_lealtad`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `promociones`
--
ALTER TABLE `promociones`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `promociones_categorias`
--
ALTER TABLE `promociones_categorias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `promociones_productos`
--
ALTER TABLE `promociones_productos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `promociones_uso`
--
ALTER TABLE `promociones_uso`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `proveedores`
--
ALTER TABLE `proveedores`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `provincias`
--
ALTER TABLE `provincias`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `roles_permisos`
--
ALTER TABLE `roles_permisos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `sesiones_usuario`
--
ALTER TABLE `sesiones_usuario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tallas`
--
ALTER TABLE `tallas`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT de la tabla `tareas_seguimiento`
--
ALTER TABLE `tareas_seguimiento`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `transacciones_puntos`
--
ALTER TABLE `transacciones_puntos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `transferencias_inventario`
--
ALTER TABLE `transferencias_inventario`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `unidades_medida`
--
ALTER TABLE `unidades_medida`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

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
-- AUTO_INCREMENT de la tabla `ventas_descuentos`
--
ALTER TABLE `ventas_descuentos`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ventas_entrega`
--
ALTER TABLE `ventas_entrega`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ventas_facturacion`
--
ALTER TABLE `ventas_facturacion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `arqueos_caja`
--
ALTER TABLE `arqueos_caja`
  ADD CONSTRAINT `FK_arqueos_caja_caja` FOREIGN KEY (`caja_id`) REFERENCES `cajas` (`id`),
  ADD CONSTRAINT `FK_arqueos_caja_usuario_apertura` FOREIGN KEY (`usuario_apertura`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_arqueos_caja_usuario_cierre` FOREIGN KEY (`usuario_cierre`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `auditoria_logs`
--
ALTER TABLE `auditoria_logs`
  ADD CONSTRAINT `FK_auditoria_logs_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `cajas`
--
ALTER TABLE `cajas`
  ADD CONSTRAINT `FK_cajas_responsable` FOREIGN KEY (`responsable_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `categorias`
--
ALTER TABLE `categorias`
  ADD CONSTRAINT `FK_categorias_padre` FOREIGN KEY (`categoria_padre_id`) REFERENCES `categorias` (`id`);

--
-- Filtros para la tabla `clientes_comercial`
--
ALTER TABLE `clientes_comercial`
  ADD CONSTRAINT `FK_clientes_comercial_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_clientes_comercial_vendedor` FOREIGN KEY (`vendedor_asignado_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `clientes_contacto`
--
ALTER TABLE `clientes_contacto`
  ADD CONSTRAINT `FK_clientes_contacto_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

--
-- Filtros para la tabla `clientes_direcciones`
--
ALTER TABLE `clientes_direcciones`
  ADD CONSTRAINT `FK_clientes_direcciones_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_clientes_direcciones_distrito` FOREIGN KEY (`distrito_id`) REFERENCES `distritos` (`id`);

--
-- Filtros para la tabla `clientes_preferencias`
--
ALTER TABLE `clientes_preferencias`
  ADD CONSTRAINT `FK_clientes_preferencias_categoria` FOREIGN KEY (`categoria_preferida_id`) REFERENCES `categorias` (`id`),
  ADD CONSTRAINT `FK_clientes_preferencias_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_clientes_preferencias_color` FOREIGN KEY (`color_preferido_id`) REFERENCES `colores` (`id`),
  ADD CONSTRAINT `FK_clientes_preferencias_marca` FOREIGN KEY (`marca_preferida_id`) REFERENCES `marcas` (`id`),
  ADD CONSTRAINT `FK_clientes_preferencias_talla` FOREIGN KEY (`talla_preferida_id`) REFERENCES `tallas` (`id`);

--
-- Filtros para la tabla `comentarios_tarea`
--
ALTER TABLE `comentarios_tarea`
  ADD CONSTRAINT `FK_comentarios_tarea_tarea` FOREIGN KEY (`tarea_id`) REFERENCES `tareas_seguimiento` (`id`),
  ADD CONSTRAINT `FK_comentarios_tarea_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `compras`
--
ALTER TABLE `compras`
  ADD CONSTRAINT `FK_compras_proveedor` FOREIGN KEY (`proveedor_id`) REFERENCES `proveedores` (`id`);

--
-- Filtros para la tabla `configuracion_tienda`
--
ALTER TABLE `configuracion_tienda`
  ADD CONSTRAINT `FK_configuracion_tienda_distrito` FOREIGN KEY (`distrito_id`) REFERENCES `distritos` (`id`);

--
-- Filtros para la tabla `cotizaciones`
--
ALTER TABLE `cotizaciones`
  ADD CONSTRAINT `FK_cotizaciones_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

--
-- Filtros para la tabla `departamentos`
--
ALTER TABLE `departamentos`
  ADD CONSTRAINT `FK_departamentos_pais` FOREIGN KEY (`pais_id`) REFERENCES `paises` (`id`);

--
-- Filtros para la tabla `detalle_compra`
--
ALTER TABLE `detalle_compra`
  ADD CONSTRAINT `FK_detalle_compra_compra` FOREIGN KEY (`compra_id`) REFERENCES `compras` (`id`),
  ADD CONSTRAINT `FK_detalle_compra_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `detalle_cotizacion`
--
ALTER TABLE `detalle_cotizacion`
  ADD CONSTRAINT `FK_detalle_cotizacion_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`),
  ADD CONSTRAINT `FK_detalle_cotizacion_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizaciones` (`id`),
  ADD CONSTRAINT `FK_detalle_cotizacion_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `FK_detalle_cotizacion_talla` FOREIGN KEY (`talla_id`) REFERENCES `tallas` (`id`);

--
-- Filtros para la tabla `detalle_devolucion`
--
ALTER TABLE `detalle_devolucion`
  ADD CONSTRAINT `FK_detalle_devolucion_detalle_venta` FOREIGN KEY (`detalle_venta_id`) REFERENCES `detalle_venta` (`id`),
  ADD CONSTRAINT `FK_detalle_devolucion_devolucion` FOREIGN KEY (`devolucion_id`) REFERENCES `devoluciones` (`id`);

--
-- Filtros para la tabla `detalle_transferencia`
--
ALTER TABLE `detalle_transferencia`
  ADD CONSTRAINT `FK_detalle_transferencia_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `FK_detalle_transferencia_transferencia` FOREIGN KEY (`transferencia_id`) REFERENCES `transferencias_inventario` (`id`);

--
-- Filtros para la tabla `detalle_venta`
--
ALTER TABLE `detalle_venta`
  ADD CONSTRAINT `FK_detalle_venta_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`),
  ADD CONSTRAINT `FK_detalle_venta_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `FK_detalle_venta_talla` FOREIGN KEY (`talla_id`) REFERENCES `tallas` (`id`),
  ADD CONSTRAINT `FK_detalle_venta_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  ADD CONSTRAINT `FK_devoluciones_autorizado_por` FOREIGN KEY (`autorizado_por`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_devoluciones_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_devoluciones_procesado_por` FOREIGN KEY (`procesado_por`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_devoluciones_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `distritos`
--
ALTER TABLE `distritos`
  ADD CONSTRAINT `FK_distritos_provincia` FOREIGN KEY (`provincia_id`) REFERENCES `provincias` (`id`);

--
-- Filtros para la tabla `metodos_pago_venta`
--
ALTER TABLE `metodos_pago_venta`
  ADD CONSTRAINT `FK_metodos_pago_venta_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `movimientos_inventario`
--
ALTER TABLE `movimientos_inventario`
  ADD CONSTRAINT `FK_movimientos_inventario_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `FK_movimientos_inventario_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_movimientos_inventario_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `password_reset_tokens`
--
ALTER TABLE `password_reset_tokens`
  ADD CONSTRAINT `FK_password_reset_tokens_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `productos`
--
ALTER TABLE `productos`
  ADD CONSTRAINT `FK_productos_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  ADD CONSTRAINT `FK_productos_marca` FOREIGN KEY (`marca_id`) REFERENCES `marcas` (`id`);

--
-- Filtros para la tabla `productos_colores`
--
ALTER TABLE `productos_colores`
  ADD CONSTRAINT `FK_productos_colores_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`),
  ADD CONSTRAINT `FK_productos_colores_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `productos_imagenes`
--
ALTER TABLE `productos_imagenes`
  ADD CONSTRAINT `FK_productos_imagenes_color` FOREIGN KEY (`color_id`) REFERENCES `colores` (`id`),
  ADD CONSTRAINT `FK_productos_imagenes_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `productos_inventario`
--
ALTER TABLE `productos_inventario`
  ADD CONSTRAINT `FK_productos_inventario_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `productos_precios`
--
ALTER TABLE `productos_precios`
  ADD CONSTRAINT `FK_productos_precios_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `FK_productos_precios_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `productos_seo`
--
ALTER TABLE `productos_seo`
  ADD CONSTRAINT `FK_productos_seo_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`);

--
-- Filtros para la tabla `productos_tallas`
--
ALTER TABLE `productos_tallas`
  ADD CONSTRAINT `FK_productos_tallas_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `FK_productos_tallas_talla` FOREIGN KEY (`talla_id`) REFERENCES `tallas` (`id`);

--
-- Filtros para la tabla `programa_lealtad`
--
ALTER TABLE `programa_lealtad`
  ADD CONSTRAINT `FK_programa_lealtad_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`);

--
-- Filtros para la tabla `promociones`
--
ALTER TABLE `promociones`
  ADD CONSTRAINT `FK_promociones_creado_por` FOREIGN KEY (`creado_por`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `promociones_categorias`
--
ALTER TABLE `promociones_categorias`
  ADD CONSTRAINT `FK_promociones_categorias_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  ADD CONSTRAINT `FK_promociones_categorias_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`id`);

--
-- Filtros para la tabla `promociones_productos`
--
ALTER TABLE `promociones_productos`
  ADD CONSTRAINT `FK_promociones_productos_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`),
  ADD CONSTRAINT `FK_promociones_productos_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`id`);

--
-- Filtros para la tabla `promociones_uso`
--
ALTER TABLE `promociones_uso`
  ADD CONSTRAINT `FK_promociones_uso_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_promociones_uso_promocion` FOREIGN KEY (`promocion_id`) REFERENCES `promociones` (`id`),
  ADD CONSTRAINT `FK_promociones_uso_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `proveedores`
--
ALTER TABLE `proveedores`
  ADD CONSTRAINT `FK_proveedores_distrito` FOREIGN KEY (`distrito_id`) REFERENCES `distritos` (`id`);

--
-- Filtros para la tabla `provincias`
--
ALTER TABLE `provincias`
  ADD CONSTRAINT `FK_provincias_departamento` FOREIGN KEY (`departamento_id`) REFERENCES `departamentos` (`id`);

--
-- Filtros para la tabla `roles_permisos`
--
ALTER TABLE `roles_permisos`
  ADD CONSTRAINT `FK_roles_permisos_permiso` FOREIGN KEY (`permiso_id`) REFERENCES `permisos` (`id`),
  ADD CONSTRAINT `FK_roles_permisos_rol` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`);

--
-- Filtros para la tabla `sesiones_usuario`
--
ALTER TABLE `sesiones_usuario`
  ADD CONSTRAINT `FK_sesiones_usuario_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `tareas_seguimiento`
--
ALTER TABLE `tareas_seguimiento`
  ADD CONSTRAINT `FK_tareas_seguimiento_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_tareas_seguimiento_cotizacion` FOREIGN KEY (`cotizacion_id`) REFERENCES `cotizaciones` (`id`),
  ADD CONSTRAINT `FK_tareas_seguimiento_usuario_asignado` FOREIGN KEY (`usuario_asignado_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_tareas_seguimiento_usuario_creador` FOREIGN KEY (`usuario_creador_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_tareas_seguimiento_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `transacciones_puntos`
--
ALTER TABLE `transacciones_puntos`
  ADD CONSTRAINT `FK_transacciones_puntos_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_transacciones_puntos_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_transacciones_puntos_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `transferencias_inventario`
--
ALTER TABLE `transferencias_inventario`
  ADD CONSTRAINT `FK_transferencias_inventario_autorizado_por` FOREIGN KEY (`autorizado_por`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_transferencias_inventario_recibido_por` FOREIGN KEY (`recibido_por`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `FK_usuarios_rol` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`);

--
-- Filtros para la tabla `ventas`
--
ALTER TABLE `ventas`
  ADD CONSTRAINT `FK_ventas_cajero` FOREIGN KEY (`cajero_id`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_ventas_canal` FOREIGN KEY (`canal_venta_id`) REFERENCES `canales_venta` (`id`),
  ADD CONSTRAINT `FK_ventas_cliente` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `FK_ventas_vendedor` FOREIGN KEY (`vendedor_id`) REFERENCES `usuarios` (`id`);

--
-- Filtros para la tabla `ventas_descuentos`
--
ALTER TABLE `ventas_descuentos`
  ADD CONSTRAINT `FK_ventas_descuentos_aplicado_por` FOREIGN KEY (`aplicado_por`) REFERENCES `usuarios` (`id`),
  ADD CONSTRAINT `FK_ventas_descuentos_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `ventas_entrega`
--
ALTER TABLE `ventas_entrega`
  ADD CONSTRAINT `FK_ventas_entrega_distrito` FOREIGN KEY (`distrito_entrega_id`) REFERENCES `distritos` (`id`),
  ADD CONSTRAINT `FK_ventas_entrega_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);

--
-- Filtros para la tabla `ventas_facturacion`
--
ALTER TABLE `ventas_facturacion`
  ADD CONSTRAINT `FK_ventas_facturacion_venta` FOREIGN KEY (`venta_id`) REFERENCES `ventas` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
