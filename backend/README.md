# PattyModa Backend - Spring Boot API

Backend completo para el sistema de gestión de tienda de ropa PattyModa, desarrollado con Spring Boot.

## 🚀 Características Principales

### **Autenticación y Seguridad**
- ✅ JWT Authentication
- ✅ Role-based access control
- ✅ Password encryption
- ✅ Session management
- ✅ CORS configuration

### **Gestión de Usuarios y Roles**
- ✅ Usuarios con roles y permisos
- ✅ Super Admin, Admin, Manager, Vendedor, Cajero
- ✅ Gestión de sesiones
- ✅ Reset de contraseñas

### **Catálogo de Productos**
- ✅ Productos con categorías y marcas
- ✅ Gestión de tallas y colores
- ✅ Precios y costos
- ✅ Stock por talla y color
- ✅ Imágenes y descripciones
- ✅ Productos destacados y nuevos

### **Gestión de Clientes**
- ✅ Clientes con información completa
- ✅ Tipos de cliente (Regular, VIP, Mayorista, etc.)
- ✅ Información comercial y preferencias
- ✅ Múltiples contactos y direcciones
- ✅ Sistema de puntos y niveles

### **Ventas y Facturación**
- ✅ Ventas con múltiples canales
- ✅ Detalles de venta con tallas y colores
- ✅ Facturación electrónica
- ✅ Múltiples métodos de pago
- ✅ Gestión de entregas

### **Inventario**
- ✅ Control de stock por talla y color
- ✅ Movimientos de inventario
- ✅ Transferencias entre ubicaciones
- ✅ Ajustes y mermas
- ✅ Alertas de stock mínimo

### **Gestión de Caja**
- ✅ Múltiples cajas registradoras
- ✅ Arqueos de caja
- ✅ Control de efectivo
- ✅ Gastos y egresos
- ✅ Reportes de caja

### **Configuración del Sistema**
- ✅ Configuración de la tienda
- ✅ Horarios de atención
- ✅ Impuestos y descuentos
- ✅ Configuración de correo

## 📋 Entidades Implementadas

### **Entidades Base**
- ✅ `BaseEntity` - Entidad base con campos comunes
- ✅ `Pais`, `Departamento`, `Provincia`, `Distrito` - Ubicaciones

### **Usuarios y Seguridad**
- ✅ `Rol` - Roles del sistema
- ✅ `Usuario` - Usuarios del sistema
- ✅ `Permiso` - Permisos del sistema
- ✅ `SesionUsuario` - Sesiones activas
- ✅ `PasswordResetToken` - Tokens de reset

### **Catálogo**
- ✅ `Categoria` - Categorías de productos
- ✅ `Marca` - Marcas de productos
- ✅ `Producto` - Productos principales
- ✅ `Talla` - Tallas disponibles
- ✅ `Color` - Colores disponibles
- ✅ `UnidadMedida` - Unidades de medida
- ✅ `ProductoPrecio` - Precios de productos
- ✅ `ProductoTalla` - Stock por talla
- ✅ `ProductoColor` - Stock por color

### **Clientes**
- ✅ `Cliente` - Clientes principales
- ✅ `ClienteComercial` - Información comercial
- ✅ `ClienteContacto` - Contactos del cliente
- ✅ `ClienteDireccion` - Direcciones del cliente
- ✅ `ClientePreferencia` - Preferencias del cliente
- ✅ `InteraccionCliente` - Interacciones con clientes

### **Ventas**
- ✅ `Venta` - Ventas principales
- ✅ `DetalleVenta` - Detalles de venta
- ✅ `CanalVenta` - Canales de venta
- ✅ `VentaFacturacion` - Facturación
- ✅ `VentaEntrega` - Entregas
- ✅ `VentaPago` - Pagos

### **Inventario**
- ✅ `MovimientoInventario` - Movimientos de stock
- ✅ `TransferenciaInventario` - Transferencias
- ✅ `Devolucion` - Devoluciones
- ✅ `DetalleDevolucion` - Detalles de devolución

### **Compras**
- ✅ `Compra` - Compras a proveedores
- ✅ `DetalleCompra` - Detalles de compra
- ✅ `Proveedor` - Proveedores

### **Caja y Finanzas**
- ✅ `Caja` - Cajas registradoras
- ✅ `ArqueoCaja` - Arqueos de caja
- ✅ `Gasto` - Gastos y egresos

### **Configuración**
- ✅ `ConfiguracionTienda` - Configuración general
- ✅ `ConfiguracionImpuesto` - Impuestos
- ✅ `HorarioAtencion` - Horarios
- ✅ `Cotizacion` - Cotizaciones
- ✅ `DetalleCotizacion` - Detalles de cotización

### **Sistema de Puntos**
- ✅ `TransaccionPunto` - Transacciones de puntos

### **Auditoría**
- ✅ `AuditoriaLog` - Logs de auditoría

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3.2.x**
- **Spring Security** con JWT
- **Spring Data JPA** con Hibernate
- **MySQL 8.0**
- **Maven**
- **Swagger/OpenAPI 3**
- **Lombok**
- **Spring Mail**
- **Spring Cache**

## 📦 Instalación y Configuración

### **Prerrequisitos**
- Java 17 o superior
- MySQL 8.0 o superior
- Maven 3.6+

### **1. Clonar el repositorio**
```bash
git clone <repository-url>
cd pattymoda-backend
```

### **2. Configurar la base de datos**
- Crear base de datos: `pattymoda_nueva`
- Importar el archivo SQL: `pattymoda_nueva.sql`
- Configurar credenciales en `application.yml`

### **3. Configurar variables de entorno**
```bash
# Crear archivo .env o configurar variables del sistema
MAIL_USERNAME=tu-email@gmail.com
MAIL_PASSWORD=tu-password-app
JWT_SECRET=tu-jwt-secret-muy-seguro
```

### **4. Compilar y ejecutar**
```bash
mvn clean install
mvn spring-boot:run
```

### **5. Verificar la instalación**
- API disponible en: `http://localhost:8080/api`
- Swagger UI: `http://localhost:8080/api/swagger-ui.html`
- Health check: `http://localhost:8080/api/actuator/health`

## 🔐 Configuración de Seguridad

### **Roles Disponibles**
- `SUPER_ADMIN` - Acceso total al sistema
- `ADMIN` - Administrador general
- `MANAGER` - Gerente de tienda
- `VENDEDOR` - Personal de ventas
- `CAJERO` - Personal de caja

### **Endpoints Públicos**
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registro de usuarios
- `POST /api/auth/refresh` - Renovar token
- `GET /api/auth/validate` - Validar token

### **Endpoints Protegidos**
Todos los demás endpoints requieren autenticación JWT válida.

## 📡 Endpoints Principales

### **Autenticación**
```
POST /api/auth/login
POST /api/auth/register
POST /api/auth/refresh
GET  /api/auth/validate
```

### **Usuarios**
```
GET    /api/usuarios
GET    /api/usuarios/{id}
POST   /api/usuarios
PUT    /api/usuarios/{id}
DELETE /api/usuarios/{id}
```

### **Productos**
```
GET    /api/productos
GET    /api/productos/{id}
POST   /api/productos
PUT    /api/productos/{id}
DELETE /api/productos/{id}
GET    /api/productos/categoria/{categoriaId}
GET    /api/productos/marca/{marcaId}
```

### **Tallas**
```
GET    /api/tallas
GET    /api/tallas/activas
GET    /api/tallas/categoria/{categoria}
GET    /api/tallas/codigo/{codigo}
POST   /api/tallas
PUT    /api/tallas/{id}
DELETE /api/tallas/{id}
```

### **Colores**
```
GET    /api/colores
GET    /api/colores/activos
GET    /api/colores/familia/{familia}
GET    /api/colores/codigo/{codigo}
GET    /api/colores/hex/{codigoHex}
POST   /api/colores
PUT    /api/colores/{id}
DELETE /api/colores/{id}
```

### **Clientes**
```
GET    /api/clientes
GET    /api/clientes/{id}
POST   /api/clientes
PUT    /api/clientes/{id}
DELETE /api/clientes/{id}
```

### **Ventas**
```
GET    /api/ventas
GET    /api/ventas/{id}
POST   /api/ventas
PUT    /api/ventas/{id}
DELETE /api/ventas/{id}
```

### **Compras y Proveedores**
```
GET    /api/proveedores
GET    /api/proveedores/{id}
GET    /api/proveedores/codigo/{codigo}
GET    /api/proveedores/documento/{numero}
POST   /api/proveedores
PUT    /api/proveedores/{id}
DELETE /api/proveedores/{id}

GET    /api/compras
GET    /api/compras/{id}
GET    /api/compras/numero/{numero}
POST   /api/compras
PUT    /api/compras/{id}
DELETE /api/compras/{id}

GET    /api/detalle-compra
GET    /api/detalle-compra/{id}
GET    /api/detalle-compra/compra/{compraId}
GET    /api/detalle-compra/producto/{productoId}
POST   /api/detalle-compra
PUT    /api/detalle-compra/{id}
DELETE /api/detalle-compra/{id}
```

### **Devoluciones**
```
GET    /api/devoluciones
GET    /api/devoluciones/{id}
GET    /api/devoluciones/numero/{numero}
GET    /api/devoluciones/cliente/{clienteId}
GET    /api/devoluciones/venta/{ventaId}
POST   /api/devoluciones
PUT    /api/devoluciones/{id}
DELETE /api/devoluciones/{id}

GET    /api/detalle-devolucion
GET    /api/detalle-devolucion/{id}
GET    /api/detalle-devolucion/devolucion/{devolucionId}
GET    /api/detalle-devolucion/detalle-venta/{detalleVentaId}
POST   /api/detalle-devolucion
PUT    /api/detalle-devolucion/{id}
DELETE /api/detalle-devolucion/{id}
```

## 🔧 Configuración de Base de Datos

### **Estructura Principal**
- **45+ tablas** con relaciones complejas
- **Triggers** para auditoría automática
- **Vistas** para reportes optimizados
- **Funciones** para cálculos especializados

### **Características**
- Soporte completo para UTF-8
- Índices optimizados para consultas
- Constraints de integridad referencial
- Triggers para auditoría automática

## 📊 Reportes y Analytics

### **Reportes Disponibles**
- Ventas por período
- Productos más vendidos
- Clientes con mayor facturación
- Stock bajo
- Rendimiento de vendedores
- Arqueos de caja

### **Vistas Optimizadas**
- `vista_productos_completa` - Productos con stock y precios
- `vista_ventas_completa` - Ventas con información completa
- `vista_clientes_estadisticas` - Estadísticas de clientes

## 🚀 Despliegue

### **Despliegue Local**
```bash
mvn spring-boot:run
```

### **Despliegue en Producción**
```bash
mvn clean package -DskipTests
java -jar target/pattymoda-backend-1.0.0.jar
```

### **Docker (Opcional)**
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/pattymoda-backend-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🔗 Integración con Frontend React

### **Configuración CORS**
El backend está configurado para aceptar peticiones desde:
- `http://localhost:3000` (desarrollo)
- `https://tu-dominio.com` (producción)

### **Headers Requeridos**
```javascript
headers: {
  'Content-Type': 'application/json',
  'Authorization': 'Bearer ' + token
}
```

### **Ejemplo de Uso**
```javascript
// Login
const response = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});

// Obtener productos
const productos = await fetch('/api/productos', {
  headers: { 'Authorization': 'Bearer ' + token }
});
```

## 🐛 Troubleshooting

### **Problemas Comunes**

1. **Error de conexión a BD**
   - Verificar credenciales en `application.yml`
   - Asegurar que MySQL esté ejecutándose
   - Verificar que la BD `pattymoda_nueva` exista

2. **Error de JWT**
   - Verificar `JWT_SECRET` en variables de entorno
   - Limpiar caché del navegador
   - Verificar formato del token

3. **Error de CORS**
   - Verificar configuración en `SecurityConfig`
   - Asegurar que el frontend esté en los orígenes permitidos

## 📝 Próximas Funcionalidades

- [ ] Sistema de notificaciones push
- [ ] Integración con redes sociales
- [ ] Sistema de cupones y promociones
- [ ] Reportes avanzados con gráficos
- [ ] Integración con pasarelas de pago
- [ ] Sistema de fidelización avanzado
- [ ] API para aplicaciones móviles

## 🤝 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 📞 Soporte

Para soporte técnico o consultas:
- Email: soporte@pattymoda.com
- WhatsApp: +51 965 123 456
- Documentación: [Wiki del proyecto](wiki-url)

---

**Desarrollado con ❤️ para PattyModa** 