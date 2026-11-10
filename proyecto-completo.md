# Proyecto: Yuju Repostería - Sistema Integral de Gestión

## Información General

| Aspecto | Detalle |
|---------|---------|
| **Nombre** | Yuju Repostería |
| **Framework** | Spring Boot 3.x |
| **Lenguaje** | Java 17+ |
| **Build** | Maven |
| **Frontend** | Thymeleaf + Bootstrap 5.3 + FontAwesome 6.4 |
| **Base de Datos** | H2 (en memoria, consola web habilitada) |
| **ORM** | Spring Data JPA / Hibernate |
| **Seguridad** | Sesiones HTTP + BCrypt (passwords) |
| **PDF** | iText 7 |
| **Email** | Spring Mail (SMTP Gmail) |
| **Puerto** | 8080 |

---

## Árbol de Archivos Completo

```
correosspring/
├── .github/
│   └── modernize/
│       └── java-upgrade/
│           └── .gitignore
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── enviarmail/
│   │   │           ├── EnviarmailApplication.java
│   │   │           ├── config/
│   │   │           │   └── DataInitializer.java
│   │   │           ├── controlador/
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── ClienteControlador.java
│   │   │           │   ├── DashboardController.java
│   │   │           │   ├── EmailControlador.java
│   │   │           │   ├── EmpleadoControlador.java
│   │   │           │   ├── InformeControlador.java
│   │   │           │   ├── InventarioControlador.java
│   │   │           │   ├── PedidoControlador.java
│   │   │           │   ├── ProductoControlador.java
│   │   │           │   └── ProduccionControlador.java
│   │   │           ├── modelo/
│   │   │           │   ├── entidad/
│   │   │           │   │   ├── Cliente.java
│   │   │           │   │   ├── Empleado.java
│   │   │           │   │   ├── MateriaPrima.java
│   │   │           │   │   ├── MovimientoInventario.java
│   │   │           │   │   ├── Pedido.java
│   │   │           │   │   ├── PedidoDetalle.java
│   │   │           │   │   ├── Produccion.java
│   │   │           │   │   ├── Producto.java
│   │   │           │   │   └── Usuario.java
│   │   │           │   ├── enums/
│   │   │           │   │   ├── CargoEmpleado.java
│   │   │           │   │   ├── EstadoPedido.java
│   │   │           │   │   ├── EstadoProduccion.java
│   │   │           │   │   ├── MetodoPago.java
│   │   │           │   │   ├── RolUsuario.java
│   │   │           │   │   ├── TipoCliente.java
        │   │           │   │   ├── TipoEntrega.java
│   │   │           │   │   ├── TipoMovimiento.java
│   │   │           │   │   ├── TipoProducto.java
│   │   │           │   │   ├── TipoVenta.java
│   │   │           │   │   └── TurnoEmpleado.java
│   │   │           │   ├── repositorio/
│   │   │           │   │   ├── ClienteRepositorio.java
│   │   │           │   │   ├── EmpleadoRepositorio.java
│   │   │           │   │   ├── MateriaPrimaRepositorio.java
│   │   │           │   │   ├── MovimientoInventarioRepositorio.java
│   │   │           │   │   ├── PedidoDetalleRepositorio.java
│   │   │           │   │   ├── PedidoRepositorio.java
│   │   │           │   │   ├── ProduccionRepositorio.java
│   │   │           │   │   ├── ProductoRepositorio.java
│   │   │           │   │   └── UsuarioRepositorio.java
│   │   │           │   └── servicio/
│   │   │           │       ├── ClienteServicio.java
│   │   │           │       ├── EmailServicio.java
│   │   │           │       ├── EmpleadoServicio.java
│   │   │           │       ├── InformeServicio.java
│   │   │           │       ├── InventarioServicio.java
│   │   │           │       ├── PdfServicio.java
│   │   │           │       ├── PedidoServicio.java
│   │   │           │       ├── ProduccionServicio.java
│   │   │           │       ├── ProductoServicio.java
│   │   │           │       └── UsuarioServicio.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── fragments/
│   │           │   └── base.html
│   │           ├── auth/
│   │           │   ├── login.html
│   │           │   └── registro.html
│   │           ├── clientes/
│   │           │   ├── formulario.html
│   │           │   ├── historial.html
│   │           │   └── lista.html
│   │           ├── empleados/
│   │           │   ├── formulario.html
│   │           │   ├── lista.html
│   │           │   ├── produccion.html
│   │           │   └── ventas.html
│   │           ├── informes/
│   │           │   └── reportes.html
│   │           ├── inventario/
│   │           │   ├── formulario.html
│   │           │   ├── kardex.html
│   │           │   └── lista.html
│   │           ├── pedidos/
│   │           │   ├── detalle.html
│   │           │   ├── formulario.html
│   │           │   └── lista.html
│   │           ├── productos/
│   │           │   ├── detalle.html
│   │           │   ├── formulario.html
│   │           │   └── lista.html
│   │           ├── produccion/
│   │           │   ├── formulario.html
│   │           │   └── lista.html
│   │           ├── correo.html
│   │           ├── dashboard.html
│   │           └── emailform.html
│   └── test/
│       └── java/
│           └── com/
│               └── enviarmail/
│                   └── EnviarmailApplicationTests.java
├── target/ (compilados - ignorar)
├── .gitattributes
├── .gitignore
├── HELP.md
├── classpath.txt
├── exposicion.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── read.md
```

---

## Arquitectura y Flujo de Datos

```
┌─────────────────────────────────────────────────────────────────┐
│                        NAVEGADOR (Thymeleaf)                    │
└─────────────────────────┬───────────────────────────────────────┘
                          │ HTTP Requests
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                     CONTROLADORES (9)                           │
│  Auth │ Dashboard │ Producto │ Cliente │ Empleado │ Producción  │
│  Pedido │ Inventario │ Informe                                │
└─────────────────────────┬───────────────────────────────────────┘
                          │ @Autowired Services
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                       SERVICIOS (10)                            │
│  UsuarioServicio │ ProductoServicio │ ClienteServicio           │
│  EmpleadoServicio │ ProduccionServicio │ PedidoServicio         │
│  InventarioServicio │ InformeServicio │ PdfServicio │ EmailServicio │
└─────────────────────────┬───────────────────────────────────────┘
                          │ JpaRepository
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                     REPOSITORIOS (9)                            │
│  Extienden JpaRepository<Entidad, Long> + queries personalizadas│
└─────────────────────────┬───────────────────────────────────────┘
                          │ Hibernate/JPA
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BASE DE DATOS H2 (9 Tablas)                  │
│  usuarios │ clientes │ empleados │ productos                   │
│  materias_primas │ producciones │ pedidos │ pedido_detalles    │
│  movimientos_inventario                                         │
└─────────────────────────────────────────────────────────────────┘
```

---

## Entidades JPA (9 Tablas)

### 1. Usuario (`usuarios`)
```java
- id (PK), username (UK), password (BCrypt), nombreCompleto, email, rol (ADMIN/VENDEDOR/PASTELERO/ALMACENISTA), activo
```

### 2. Cliente (`clientes`)
```java
- id (PK), documento (UK-NIT/CC), nombreCompleto, tipoCliente (MINORISTA/MAYORISTA), telefono, email, direccion, fechaRegistro
```

### 3. Empleado (`empleados`)
```java
- id (PK), identificacion (UK), nombreCompleto, cargo (PANADERO/PASTELERO/VENDEDOR/ADMINISTRADOR), telefono, email, turno (MANANA/TARDE/NOCHE/MIXTO), funciones, activo
```

### 4. Producto (`productos`)
```java
- id (PK), codigo (UK), nombre, tipo (TORTA/CUPCAKE/GALLETA/POSTRE/PANADERIA_FINA/OTRO)
- descripcion, ingredientes, alergenos, condicionesAlmacenamiento
- precioMinorista, precioMayorista (BigDecimal 12,2)
- presentacion, tamano, stock, stockMinimo, disponible, imagenUrl
- isBajoStock(): stock <= stockMinimo
```

### 5. MateriaPrima (`materias_primas`)
```java
- id (PK), codigo (UK), nombre, unidadMedida (kg/g/L/Unidades)
- stockActual, stockMinimo, costoUnitario (BigDecimal 12,2)
- isBajoStock(): stockActual <= stockMinimo
```

### 6. Produccion (`producciones`)
```java
- id (PK), codigoLote (UK), producto (ManyToOne), cantidadPlanificada, cantidadProducida, mermas, motivoMerma
- fechaPlanificada, fechaInicio, fechaFin, estado (PENDIENTE/EN_PROCESO/TERMINADO)
- responsable (ManyToOne Empleado - PANADERO/PASTELERO), observaciones
```

### 7. Pedido (`pedidos`)
```java
- id (PK), numeroFactura (UK), cliente (ManyToOne), vendedor (ManyToOne Empleado VENDEDOR)
- tipoVenta (MINORISTA/MAYORISTA), tipoEntrega (VENTA_DIRECTA/POR_ENCARGO)
- fechaPedido, fechaEntrega, estado (PENDIENTE/EN_PREPARACION/ENTREGADO/CANCELADO)
- total, abono, saldoPendiente, metodoPago (EFECTIVO/TRANSFERENCIA/TARJETA_CREDITO/TARJETA_DEBITO/OTRO)
- observaciones, detalles (OneToMany PedidoDetalle)
- recalcularTotales(): suma subtotales de detalles
```

### 8. PedidoDetalle (`pedido_detalles`)
```java
- id (PK), pedido (ManyToOne), producto (ManyToOne), cantidad, precioUnitario, subtotal
```

### 9. MovimientoInventario (`movimientos_inventario`)
```java
- id (PK), materiaPrima (ManyToOne), tipo (COMPRA/CONSUMO_PRODUCCION/AJUSTE_POSITIVO/AJUSTE_NEGATIVO/MERMA/VENTA/DEVOLUCION)
- cantidad (BigDecimal), fecha, motivo, responsable
```

---

## Enumeraciones (12)

| Enum | Valores | Uso |
|------|---------|-----|
| `CargoEmpleado` | PANADERO, PASTELERO, VENDEDOR, ADMINISTRADOR | Empleado.cargo |
| `EstadoPedido` | PENDIENTE, EN_PREPARACION, ENTREGADO, CANCELADO | Pedido.estado |
| `EstadoProduccion` | PENDIENTE, EN_PROCESO, TERMINADO | Produccion.estado |
| `MetodoPago` | EFECTIVO, TRANSFERENCIA, TARJETA_CREDITO, TARJETA_DEBITO, OTRO | Pedido.metodoPago |
| `RolUsuario` | ADMIN, VENDEDOR, PASTELERO, ALMACENISTA | Usuario.rol |
| `TipoCliente` | MINORISTA, MAYORISTA | Cliente.tipoCliente |
| `TipoEntrega` | VENTA_DIRECTA, POR_ENCARGO | Pedido.tipoEntrega |
| `TipoMovimiento` | COMPRA, CONSUMO_PRODUCCION, AJUSTE_POSITIVO, AJUSTE_NEGATIVO, MERMA, VENTA, DEVOLUCION | MovimientoInventario.tipo |
| `TipoProducto` | TORTAS, CUPCAKES, GALLETAS, POSTRES, PANADERIA_FINA, OTRO | Producto.tipo |
| `TipoVenta` | MINORISTA, MAYORISTA | Pedido.tipoVenta |
| `TurnoEmpleado` | MANANA, TARDE, NOCHE, MIXTO | Empleado.turno |

---

## Controladores y Endpoints (9 Controladores)

### AuthController (`/login`, `/logout`, `/registro`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/login` | Formulario login (error opcional) |
| POST | `/login` | Autentica usuario, crea sesión |
| GET | `/logout` | Invalida sesión |
| GET | `/registro` | Formulario registro usuario |
| POST | `/registro` | Crea usuario (password BCrypt) |

### DashboardController (`/`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/` | Dashboard principal con stats, pedidos recientes, alertas stock |

### ProductoController (`/productos`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/productos` | Listar (filtros: buscar, tipo) |
| GET | `/productos/nuevo` | Formulario crear |
| GET | `/productos/editar/{id}` | Formulario editar |
| POST | `/productos/guardar` | Guardar (crear/actualizar) |
| GET | `/productos/detalle/{id}` | Vista detalle completa |
| GET | `/productos/eliminar/{id}` | Eliminar (validación integridad) |
| GET | `/productos/catalogo-pdf` | Descargar catálogo PDF |

### ClienteController (`/clientes`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/clientes` | Listar (filtros: buscar, tipo) |
| GET | `/clientes/nuevo` | Formulario crear |
| GET | `/clientes/editar/{id}` | Formulario editar |
| POST | `/clientes/guardar` | Guardar |
| GET | `/clientes/historial/{id}` | Historial compras del cliente |
| GET | `/clientes/eliminar/{id}` | Eliminar (validación integridad) |

### EmpleadoController (`/empleados`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/empleados` | Listar (filtros: buscar, cargo) |
| GET | `/empleados/nuevo` | Formulario crear |
| GET | `/empleados/editar/{id}` | Formulario editar |
| POST | `/empleados/guardar` | Guardar |
| GET | `/empleados/ventas/{id}` | Ventas por vendedor |
| GET | `/empleados/produccion/{id}` | Producción por empleado |
| GET | `/empleados/eliminar/{id}` | Eliminar (validación integridad) |

### ProduccionController (`/produccion`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/produccion` | Listar (filtro: estado) |
| GET | `/produccion/nueva` | Formulario planificar (productos + pasteleros) |
| POST | `/produccion/guardar` | Crear orden producción |
| GET | `/produccion/iniciar/{id}` | Iniciar (PENDIENTE → EN_PROCESO) |
| POST | `/produccion/terminar/{id}` | Terminar (cantidad, mermas, motivo) → actualiza stock |
| GET | `/produccion/eliminar/{id}` | Eliminar |

### PedidoController (`/pedidos`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/pedidos` | Listar (filtros: estado, tipoVenta) |
| GET | `/pedidos/nuevo` | Formulario (clientes, vendedores, productos) |
| POST | `/pedidos/guardar` | Crear pedido multi-producto |
| GET | `/pedidos/detalle/{id}` | Detalle + acciones estado/abono |
| POST | `/pedidos/estado/{id}` | Cambiar estado |
| POST | `/pedidos/abono/{id}` | Registrar abono |
| GET | `/pedidos/pdf/{id}` | Descargar factura PDF |
| GET | `/pedidos/enviar-correo/{id}` | Enviar factura por email |
| GET | `/pedidos/eliminar/{id}` | Eliminar |

### InventarioController (`/inventario`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/inventario` | Listar materias primas (buscar + contador bajo stock) |
| GET | `/inventario/nuevo` | Formulario crear insumo |
| GET | `/inventario/editar/{id}` | Formulario editar insumo |
| POST | `/inventario/guardar` | Guardar insumo |
| POST | `/inventario/movimiento` | Registrar movimiento (actualiza stock) |
| GET | `/inventario/kardex` | Kardex completo (trazabilidad) |
| GET | `/inventario/eliminar/{id}` | Eliminar (validación integridad) |

### InformeController (`/informes`)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/informes` | Reportes (filtros: fechaInicio, fechaFin, tipoVenta) |
| GET | `/informes/pdf` | Descargar reporte PDF filtrado |
| POST | `/informes/enviar-correo` | Enviar reporte PDF por email |

---

## Servicios Principales (Lógica de Negocio)

### ProductoServicio
- `listarTodos()`, `listarDisponibles()`, `listarPorTipo(TipoProducto)`, `buscar(String)`, `buscarPorId(Long)`, `listarBajoStock()`
- `guardar(Producto)`, `eliminar(Long)`

### ClienteServicio
- `listarTodos()`, `listarPorTipo(TipoCliente)`, `buscar(String)`, `buscarPorId(Long)`
- `guardar(Cliente)`, `eliminar(Long)`
- `consultarHistorialCompras(Long clienteId)` → List<Pedido>

### EmpleadoServicio
- `listarTodos()`, `listarPorCargo(CargoEmpleado)`, `buscar(String)`, `buscarPorId(Long)`
- `guardar(Empleado)`, `eliminar(Long)`
- `consultarVentasPorVendedor(Long id)`, `consultarProduccionesPorEmpleado(Long id)`

### ProduccionServicio
- `listarTodas()`, `listarPorEstado(EstadoProduccion)`, `buscarPorId(Long)`
- `crear(Produccion)`, `eliminar(Long)`
- `iniciarProduccion(Long id)` → estado EN_PROCESO, fechaInicio = now()
- `terminarProduccion(Long id, int cantidadProducida, int mermas, String motivoMerma)` → 
  - estado TERMINADO, fechaFin = now()
  - **Incrementa Producto.stock**
  - Registra MovimientoInventario tipo CONSUMO_PRODUCCION para materias primas

### PedidoServicio
- `listarTodos()`, `listarPorEstado(EstadoPedido)`, `listarPorTipoVenta(TipoVenta)`, `buscarPorId(Long)`
- `crearPedido(Pedido, List<Long> productoIds, List<Integer> cantidades)` →
  - Crea PedidoDetalle por cada producto
  - Calcula total según tipoVenta (precioMinorista/Mayorista)
  - **Decrementa Producto.stock** (si VENTA_DIRECTA)
- `cambiarEstado(Long id, EstadoPedido)`, `registrarAbono(Long id, BigDecimal)`
- `eliminar(Long)`

### InventarioServicio
- `listarMateriasPrimas()`, `buscarMaterias(String)`, `listarBajoStock()`, `buscarPorId(Long)`
- `guardarMateriaPrima(MateriaPrima)`, `eliminarMateriaPrima(Long)`
- `registrarMovimiento(Long materiaPrimaId, TipoMovimiento, BigDecimal cantidad, String motivo, String responsable)` →
  - **Actualiza MateriaPrima.stockActual** según tipo (suma/restar)
  - Crea registro MovimientoInventario
- `listarMovimientosRecientes()` → para kardex

### InformeServicio
- `filtrarVentas(LocalDate inicio, LocalDate fin, TipoVenta tipo)` → List<Pedido>
- `obtenerEstadisticasDashboard()` → Map con:
  - totalVentasHoy, totalVentasMes, totalPedidosPendientes, totalClientes
  - productosBajoStock, materiasBajoStock
  - ingresosTotales, productosMasVendidos (Top 5)
  - ventasPorTipo (Map<TipoVenta, BigDecimal>)

### PdfServicio (iText 7)
- `generarFacturaPdf(Pedido)` → byte[]
- `generarCatalogoProductosPdf(List<Producto>)` → byte[]
- `generarReporteVentasPdf(List<Pedido>, String periodo)` → byte[]

### EmailServicio (Spring Mail)
- `enviarFacturaCliente(Pedido, byte[] pdf)`
- `enviarReportePdf(String destino, String asunto, String cuerpo, byte[] pdf, String filename)`

### UsuarioServicio
- `autenticar(String username, String password)` → Optional<Usuario> (verifica BCrypt + activo)
- `registrar(Usuario)` → encripta password con BCrypt
- `buscarPorUsername(String)`, `existeUsername(String)`

---

## Vistas Thymeleaf (25+ Templates)

### Layout Base
- `fragments/base.html` - Layout principal con:
  - Navbar superior (logo 🍰 Yuju Repostería, usuario, logout)
  - Sidebar lateral colapsable (menú módulos con iconos FA)
  - Content area (`th:fragment="content"`)
  - Footer fijo
  - Bootstrap 5.3 CDN + FontAwesome 6.4 CDN
  - Paleta: Rosa pastel (#fce4ec), Dorado (#ffd700)

### Autenticación
- `auth/login.html` - Card centrada, fondo degradado, inputs con iconos
- `auth/registro.html` - Formulario registro usuario (solo personal)

### Dashboard
- `dashboard.html` - 4 tarjetas stats + tablas: pedidos recientes, productos bajo stock, materias bajo stock

### Productos (3)
- `productos/lista.html` - Tabla con filtros, badges tipo/stock, botones acción, exportar PDF
- `productos/formulario.html` - Crear/editar con validación HTML5
- `productos/detalle.html` - Vista completa con imagen, ingredientes, alérgenos, precios

### Clientes (3)
- `clientes/lista.html` - Tabla con badges tipo cliente, botón historial
- `clientes/formulario.html` - Crear/editar
- `clientes/historial.html` - Tabla pedidos del cliente con totales

### Empleados (4)
- `empleados/lista.html` - Tabla con badges cargo/turno/estado
- `empleados/formulario.html` - Crear/editar (selects cargo, turno)
- `empleados/ventas.html` - Ventas de un vendedor
- `empleados/produccion.html` - Producción de un empleado

### Producción (2)
- `produccion/lista.html` - Tabla con badges estado, botones iniciar/terminar
- `produccion/formulario.html` - Planificación (select productos, pasteleros)

### Pedidos (3)
- `pedidos/lista.html` - Tabla con filtros estado/tipoVenta
- `pedidos/formulario.html` - Completo: cliente, vendedor, multi-producto (JS dinámico), tipoVenta/Entrega/Pago
- `pedidos/detalle.html` - Detalle completo + botones: cambiar estado, abono, PDF, email, eliminar

### Inventario (3)
- `inventario/lista.html` - Tabla materias primas, badge "STOCK BAJO" rojo, contador header
- `inventario/formulario.html` - Crear/editar insumo
- `inventario/kardex.html` - Trazabilidad completa: fecha, insumo, tipo, cantidad, stock resultante, motivo, responsable

### Informes (1)
- `informes/reportes.html` - Filtros fecha/tipo + tabla pedidos + tarjetas resumen + botones PDF/Email

---

## Datos Iniciales (DataInitializer)

Al arrancar la app (si BD vacía), se crean:

**Usuarios (3):**
- `admin` / `admin123` - ADMIN (Gerente General)
- `vendedor` / `vendedor123` - VENDEDOR (Asesor Comercial)
- `pastelero` / `pastelero123` - PASTELERO (Maestro Pastelero)

**Empleados (4):**
- Chef Marcel Dubois - PASTELERO - MAÑANA
- Juansito - VENDEDOR - TARDE
- Mateo Ríos - PANADERO - NOCHE
- Yulieth Ruiz - ADMINISTRADOR - COMPLETO

**Clientes (4):**
- Valentina Herrera - MINORISTA
- Eventos & Banquetes Élite S.A.S. - MAYORISTA
- Hotel Boutique La Casona - MAYORISTA
- Andrés Felipe Castro - MINORISTA

**Materias Primas (6):**
- Harina Trigo (120kg), Mantequilla (45kg), Chocolate Belga (25kg), Azúcar Glass (80kg), Huevos (600 und), Crema Leche (12L - **bajo stock**)

**Productos (6):**
- Torta Red Velvet (stock 18), Torta Selva Negra (12), Cupcakes Gourmet (25), Galletas NY (30), Cheesecake Frutos Rojos (8), Croissant Almendras (4 - **bajo stock**)

**Producciones (3):**
- LOTE-2026-001: Red Velvet TERMINADO (14 prod, 1 merma)
- LOTE-2026-002: Cupcakes EN_PROCESO
- LOTE-2026-003: Croissants PENDIENTE

**Pedidos (3):**
- FAC-00101: MINORISTA ENTREGADO (Red Velvet + 2 Galletas) - Pagado
- FAC-00102: MAYORISTA EN_PREPARACION (10 Cupcakes + 4 Selva Negra) - Abono parcial
- FAC-00103: MAYORISTA PENDIENTE (20 Croissants + 5 Galletas) - Abono parcial

---

## Configuración (application.properties)

```properties
spring.application.name=yuju-reposteria

# H2 Database
spring.datasource.url=jdbc:h2:mem:reposteriadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.open-in-view=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true

# Email SMTP (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=yuju.sas.empresa@gmail.com
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# File Upload
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

# Thymeleaf
spring.thymeleaf.cache=false
```

---

## Flujo de Negocio Clave

### 1. Venta Directa (Mostrador)
```
Cliente llega → Pedido nuevo (VENTA_DIRECTA) → Selecciona productos → 
Guarda → Stock decrementa INMEDIATO → Factura PDF/Email → Estado ENTREGADO
```

### 2. Venta por Encargo
```
Cliente encarga → Pedido nuevo (POR_ENCARGO, fechaEntrega futura) → 
Estado PENDIENTE → EN_PREPARACIÓN → Producción planificada → 
Producción TERMINADA (stock incrementa) → ENTREGADO
```

### 3. Ciclo de Producción
```
Planificar (nueva) → PENDIENTE → Iniciar → EN_PROCESO → 
Terminar (cantidad, mermas) → TERMINADO → 
Stock producto += cantidadProducida
Stock materias primas -= consumo (MovimientoInventario CONSUMO_PRODUCCION)
```

### 4. Control Inventario
```
Compra insumos → Movimiento COMPRA → stockActual += cantidad
Producción → Movimiento CONSUMO_PRODUCCION → stockActual -= cantidad
Merma → Movimiento MERMA → stockActual -= cantidad
Ajuste → AJUSTE_POSITIVO/NEGATIVO
Venta → Movimiento VENTA
Kardex → Historial completo trazabilidad
```

### 5. Reportes y Analytics
```
Dashboard → Stats tiempo real (ventas hoy/mes, pendientes, stock bajo)
Informes → Filtros fecha/tipo → Tabla + Resumen → PDF/Email ejecutivo
```

---

## Cómo Ejecutar

```bash
# Clonar / entrar al proyecto
cd correosspring

# Compilar y ejecutar (Maven Wrapper)
./mvnw spring-boot:run

# O en Windows
mvnw.cmd spring-boot:run

# Acceder a:
# App: http://localhost:8080
# H2 Console: http://localhost:8080/h2-console
#   JDBC URL: jdbc:h2:mem:reposteriadb
#   User: sa / Pass: (vacío)
```

**Credenciales de prueba:**
- Admin: `admin` / `admin123`
- Vendedor: `vendedor` / `vendedor123`
- Pastelero: `pastelero` / `pastelero123`

---

## Dependencias Principales (pom.xml)

```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>spring-boot-starter-web</dependency>
    <dependency>spring-boot-starter-thymeleaf</dependency>
    <dependency>spring-boot-starter-data-jpa</dependency>
    <dependency>spring-boot-starter-mail</dependency>
    <dependency>spring-boot-starter-validation</dependency>
    
    <!-- Base de Datos -->
    <dependency>com.h2database:h2</dependency>
    
    <!-- Seguridad -->
    <dependency>org.springframework.security:spring-security-crypto</dependency> <!-- BCrypt -->
    
    <!-- PDF -->
    <dependency>com.itextpdf:itext7-core:7.2.5</dependency>
    
    <!-- Testing -->
    <dependency>spring-boot-starter-test</dependency>
</dependencies>
```

---

## Resumen de Cumplimiento Requerimientos

| Módulo Requerido | Estado | Implementación |
|------------------|--------|----------------|
| Gestión Productos | ✅ | CRUD + búsqueda + detalle + catálogo PDF + precios minorista/mayorista |
| Gestión Clientes | ✅ | CRUD + historial compras + búsqueda + listado con comportamiento |
| Gestión Empleados | ✅ | CRUD + funciones/turnos + producción asociada + ventas por vendedor |
| Gestión Producción | ✅ | Planificación + materias primas + estados + mermas |
| Gestión Pedidos/Ventas | ✅ | Pedidos + venta directa/encargo + estados + pagos + facturas PDF/Email |
| Gestión Inventario | ✅ | Materias primas/productos + actualización auto + alertas + kardex |
| Informes/Estadísticas | ✅ | Ventas por producto/cliente/periodo + stats + consumo materias + mayorista vs minorista + filtros |
| Autenticación | ✅ | Login/Logout/Registro + roles + sesión |
| Base de Datos H2 | ✅ | 9 tablas + consola web + DDL auto |
| UI Moderna | ✅ | Bootstrap 5 + FontAwesome + Responsive + Logo + Colores |
| CRUD Completo | ✅ | En todos los módulos |

---

## Puntos de Extensión Futura

1. **Seguridad Spring Security** - Reemplazar sesiones manuales por SecurityFilterChain
2. **Roles/Permisos** - Usar `RolUsuario` en `@PreAuthorize` en controladores
3. **API REST** - Añadir `@RestController` para integración móvil/externos
4. **Tests** - JUnit 5 + MockMvc + Testcontainers
5. **Docker** - Dockerfile + docker-compose (H2 → PostgreSQL/MySQL)
6. **Auditoría** - `@CreatedBy`, `@LastModifiedBy` con Spring Data JPA Auditing
7. **Notificaciones** - WebSocket (STOMP) para alertas stock en tiempo real
8. **Reportes Avanzados** - JasperReports / Excel export (Apache POI)