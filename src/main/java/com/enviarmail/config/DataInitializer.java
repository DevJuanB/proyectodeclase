package com.enviarmail.config;

import com.enviarmail.modelo.entidad.*;
import com.enviarmail.modelo.enums.*;
import com.enviarmail.modelo.repositorio.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepo;
    private final EmpleadoRepositorio empleadoRepo;
    private final ClienteRepositorio clienteRepo;
    private final ProductoRepositorio productoRepo;
    private final MateriaPrimaRepositorio materiaPrimaRepo;
    private final MovimientoInventarioRepositorio movimientoRepo;
    private final ProduccionRepositorio produccionRepo;
    private final PedidoRepositorio pedidoRepo;

    public DataInitializer(UsuarioRepositorio usuarioRepo,
                           EmpleadoRepositorio empleadoRepo,
                           ClienteRepositorio clienteRepo,
                           ProductoRepositorio productoRepo,
                           MateriaPrimaRepositorio materiaPrimaRepo,
                           MovimientoInventarioRepositorio movimientoRepo,
                           ProduccionRepositorio produccionRepo,
                           PedidoRepositorio pedidoRepo) {
        this.usuarioRepo = usuarioRepo;
        this.empleadoRepo = empleadoRepo;
        this.clienteRepo = clienteRepo;
        this.productoRepo = productoRepo;
        this.materiaPrimaRepo = materiaPrimaRepo;
        this.movimientoRepo = movimientoRepo;
        this.produccionRepo = produccionRepo;
        this.pedidoRepo = pedidoRepo;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepo.count() > 0) return;

        // 1. Usuarios del sistema
        Usuario admin = new Usuario("admin", "admin123", "Yulieth Ruiz (Gerente General)", "yuli.admin@yujureposteria.com", RolUsuario.ADMIN);
        Usuario vendedor = new Usuario("vendedor", "vendedor123", "Juansito (Asesor Comercial)", "jroo.ventas@yujureposteria.com", RolUsuario.VENDEDOR);
        Usuario pastelero = new Usuario("pastelero", "pastelero123", "Chef Marcel Dubois (Maestro Pastelero)", "marcel.chef@yujureposteria.com", RolUsuario.PASTELERO);
        usuarioRepo.save(admin);
        usuarioRepo.save(vendedor);
        usuarioRepo.save(pastelero);

        // 2. Empleados
        Empleado e1 = new Empleado("1018273645", "Chef Marcel Dubois", CargoEmpleado.PASTELERO, "3154829102", "marcel.chef@yujureposteria.com", TurnoEmpleado.MANANA, "Elaboración de pastelería fina, decoración y control de recetas");
        Empleado e2 = new Empleado("1029384756", "Juansito", CargoEmpleado.VENDEDOR, "3209847162", "jroo.ventas@yujureposteria.com", TurnoEmpleado.TARDE, "Atención en punto de venta, toma de pedidos mayoristas y facturación");
        Empleado e3 = new Empleado("1038475629", "Mateo Ríos", CargoEmpleado.PANADERO, "3118746352", "jroo2.panaderia@yujureposteria.com", TurnoEmpleado.NOCHE, "Horneado nocturno de croissants, baguettes y masas fermentadas");
        Empleado e4 = new Empleado("1047586930", "Yulieth Ruiz", CargoEmpleado.ADMINISTRADOR, "3176543210", "yuli.admin@yujureposteria.com", TurnoEmpleado.COMPLETO, "Gestión de compras, coordinación de eventos y supervisión general");
        empleadoRepo.save(e1);
        empleadoRepo.save(e2);
        empleadoRepo.save(e3);
        empleadoRepo.save(e4);

        // 3. Clientes
        Cliente c1 = new Cliente("1098765432", "Valentina Herrera", TipoCliente.MINORISTA, "3127894561", "valentina.h@gmail.com", "Cra 15 #85-30, Apto 402");
        Cliente c2 = new Cliente("901458923-1", "Eventos & Banquetes Élite S.A.S.", TipoCliente.MAYORISTA, "3006541289", "contacto@eventoselite.com.co", "Av. El Dorado #68C-61");
        Cliente c3 = new Cliente("800987654-2", "Hotel Boutique La Casona", TipoCliente.MAYORISTA, "3189874523", "compras@hotelcasona.com", "Calle 11 #4-22, La Candelaria");
        Cliente c4 = new Cliente("1032456789", "Andrés Felipe Castro", TipoCliente.MINORISTA, "3167891234", "andres.castro@hotmail.com", "Transversal 23 #104-50");
        clienteRepo.save(c1);
        clienteRepo.save(c2);
        clienteRepo.save(c3);
        clienteRepo.save(c4);

        // 4. Materias Primas e Insumos
        MateriaPrima m1 = new MateriaPrima("MP-001", "Harina de Trigo Especial Repostería", "kg", new BigDecimal("120.00"), new BigDecimal("30.00"), new BigDecimal("4500.00"));
        MateriaPrima m2 = new MateriaPrima("MP-002", "Mantequilla Francesa sin Sal", "kg", new BigDecimal("45.00"), new BigDecimal("15.00"), new BigDecimal("22000.00"));
        MateriaPrima m3 = new MateriaPrima("MP-003", "Chocolate Belga 70% Cacao", "kg", new BigDecimal("25.00"), new BigDecimal("10.00"), new BigDecimal("48000.00"));
        MateriaPrima m4 = new MateriaPrima("MP-004", "Azúcar Glass Refinada", "kg", new BigDecimal("80.00"), new BigDecimal("20.00"), new BigDecimal("5200.00"));
        MateriaPrima m5 = new MateriaPrima("MP-005", "Huevos Campesinos Grado AA", "Unidades", new BigDecimal("600.00"), new BigDecimal("150.00"), new BigDecimal("700.00"));
        MateriaPrima m6 = new MateriaPrima("MP-006", "Crema de Leche Pastelera 35%", "Litros", new BigDecimal("12.00"), new BigDecimal("15.00"), new BigDecimal("18500.00")); // Bajo stock
        materiaPrimaRepo.save(m1);
        materiaPrimaRepo.save(m2);
        materiaPrimaRepo.save(m3);
        materiaPrimaRepo.save(m4);
        materiaPrimaRepo.save(m5);
        materiaPrimaRepo.save(m6);

        // Movimientos de inventario iniciales
        movimientoRepo.save(new MovimientoInventario(m1, TipoMovimiento.ENTRADA, new BigDecimal("150.00"), "Compra factura inicial Molinos del Valle #892", "Laura Gómez"));
        movimientoRepo.save(new MovimientoInventario(m2, TipoMovimiento.ENTRADA, new BigDecimal("50.00"), "Compra Lácteos Los Alpes #402", "Laura Gómez"));
        movimientoRepo.save(new MovimientoInventario(m3, TipoMovimiento.ENTRADA, new BigDecimal("30.00"), "Importación directa ChocoBelga #112", "Laura Gómez"));

        // 5. Productos de Repostería
        Producto p1 = new Producto(
                "TOR-001", "Torta Red Velvet Suprema", TipoProducto.TORTAS,
                "Bizcocho aterciopelado de cacao fino con capas de frosting de queso crema Philadelphia y vainilla natural.",
                "Harina, cacao, buttermilk, queso crema, mantequilla, extracto de vainilla, azúcar.",
                "Contiene gluten, lácteos y huevo.",
                "Mantener refrigerada entre 2°C y 6°C. Consumir antes de 5 días.",
                new BigDecimal("85000.00"), new BigDecimal("68000.00"),
                "Unidad entera en caja de lujo", "1.5 kg (12-15 porciones)",
                18, 5, true,
                "https://images.unsplash.com/photo-1586788680434-30d324b2d46f?w=400&q=80"
        );

        Producto p2 = new Producto(
                "TOR-002", "Torta Trufada Selva Negra", TipoProducto.TORTAS,
                "Capas húmedas de bizcocho de chocolate belga, cerezas amarenas confitadas y chantilly aromatizada con kirsch.",
                "Chocolate amargo 70%, cerezas, crema de leche fresca, harina, licor kirsch.",
                "Contiene gluten, lácteos y licor.",
                "Refrigeración 4°C.",
                new BigDecimal("92000.00"), new BigDecimal("74000.00"),
                "Unidad entera decorada", "1.8 kg (16 porciones)",
                12, 4, true,
                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=400&q=80"
        );

        Producto p3 = new Producto(
                "CUP-001", "Caja Cupcakes Gourmet Surtidos", TipoProducto.CUPCAKES,
                "Selección de cupcakes premium: Vainilla Francesa, Chocolate Avellana, Zanahoria Nueces y Limón Merengue.",
                "Harina de trigo, azúcar glass, mantequilla, avellanas, nueces, limón.",
                "Contiene frutos secos, gluten, lácteos y huevo.",
                "Conservar en lugar fresco y seco a temperatura ambiente máx 22°C.",
                new BigDecimal("36000.00"), new BigDecimal("28000.00"),
                "Caja x 6 unidades con visor", "6 Unidades (75g c/u)",
                25, 8, true,
                "https://images.unsplash.com/photo-1519869325930-281384150729?w=400&q=80"
        );

        Producto p4 = new Producto(
                "GAL-001", "Galletas New York Chocochip & Pecanas", TipoProducto.GALLETAS,
                "Galletas artesanales gigantes estilo New York, centro suave y crujiente exterior con trozos de chocolate y pecanas.",
                "Mantequilla de avellana, harina especial, gotas de chocolate belga, nueces pecanas.",
                "Contiene gluten, huevo y nueces.",
                "Almacenar en recipiente hermético hasta 15 días.",
                new BigDecimal("28000.00"), new BigDecimal("22000.00"),
                "Bandeja hermética x 6 unidades", "6 unidades grandes (120g c/u)",
                30, 10, true,
                "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?w=400&q=80"
        );

        Producto p5 = new Producto(
                "POS-001", "Cheesecake Horneado de Frutos Rojos", TipoProducto.POSTRES,
                "Clásico cheesecake estilo New York horneado a baño maría con coulis casero de fresas, moras y arándanos silvestres.",
                "Queso crema, galleta Graham, mantequilla, coulis de frutos del bosque frescos.",
                "Contiene lácteos y gluten.",
                "Mantener estrictamente refrigerado a 4°C.",
                new BigDecimal("78000.00"), new BigDecimal("62000.00"),
                "Molde desmoldable en caja térmica", "1.2 kg (10 porciones)",
                8, 3, true,
                "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?w=400&q=80"
        );

        Producto p6 = new Producto(
                "PAN-001", "Croissant Mantequilla & Almendras", TipoProducto.PANADERIA_FINA,
                "Hojaldrado tradicional francés con 72 capas de mantequilla, relleno de crema frangipane y almendras tostadas fileteadas.",
                "Harina de fuerza, mantequilla francesa 84% grasa, almendras dulces, huevo.",
                "Contiene frutos secos, gluten y lácteos.",
                "Consumir en el día para óptima textura hojaldrada.",
                new BigDecimal("12000.00"), new BigDecimal("9500.00"),
                "Unidad individual o caja x 4", "130g",
                4, 6, true, // Bajo stock para prueba de alertas
                "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=400&q=80"
        );

        productoRepo.save(p1);
        productoRepo.save(p2);
        productoRepo.save(p3);
        productoRepo.save(p4);
        productoRepo.save(p5);
        productoRepo.save(p6);

        // 6. Producciones
        Produccion pr1 = new Produccion("LOTE-2026-001", p1, 15, e1, "Producción para abastecimiento semanal de vitrina y pedidos");
        pr1.setEstado(EstadoProduccion.TERMINADO);
        pr1.setFechaInicio(LocalDateTime.now().minusDays(2));
        pr1.setFechaFin(LocalDateTime.now().minusDays(2).plusHours(5));
        pr1.setCantidadProducida(14);
        pr1.setMermas(1);
        pr1.setMotivoMerma("Torta con borde desmoronado al desmoldar en caliente");
        produccionRepo.save(pr1);

        Produccion pr2 = new Produccion("LOTE-2026-002", p3, 30, e1, "Horneado de cupcakes para evento corporativo de fin de semana");
        pr2.setEstado(EstadoProduccion.EN_PROCESO);
        pr2.setFechaInicio(LocalDateTime.now().minusHours(3));
        produccionRepo.save(pr2);

        Produccion pr3 = new Produccion("LOTE-2026-003", p6, 25, e3, "Masa laminada en fermentación en cámara fría");
        pr3.setEstado(EstadoProduccion.PENDIENTE);
        produccionRepo.save(pr3);

        // 7. Pedidos y Ventas
        Pedido ped1 = new Pedido("FAC-00101", c1, e2, TipoVenta.MINORISTA, TipoEntrega.VENTA_DIRECTA, LocalDate.now(), MetodoPago.TARJETA, "Entrega inmediata en mostrador");
        ped1.setEstado(EstadoPedido.ENTREGADO);
        PedidoDetalle d1 = new PedidoDetalle(ped1, p1, 1, p1.getPrecioMinorista());
        PedidoDetalle d2 = new PedidoDetalle(ped1, p4, 2, p4.getPrecioMinorista());
        ped1.getDetalles().add(d1);
        ped1.getDetalles().add(d2);
        ped1.setTotal(new BigDecimal("141000.00"));
        ped1.setAbono(new BigDecimal("141000.00"));
        ped1.setSaldoPendiente(BigDecimal.ZERO);
        pedidoRepo.save(ped1);

        Pedido ped2 = new Pedido("FAC-00102", c2, e2, TipoVenta.MAYORISTA, TipoEntrega.POR_ENCARGO, LocalDate.now().plusDays(2), MetodoPago.TRANSFERENCIA, "Banquete lanzamiento corporativo. Entregar en sede norte.");
        ped2.setEstado(EstadoPedido.EN_PREPARACION);
        PedidoDetalle d3 = new PedidoDetalle(ped2, p3, 10, p3.getPrecioMayorista());
        PedidoDetalle d4 = new PedidoDetalle(ped2, p2, 4, p2.getPrecioMayorista());
        ped2.getDetalles().add(d3);
        ped2.getDetalles().add(d4);
        ped2.setTotal(new BigDecimal("576000.00"));
        ped2.setAbono(new BigDecimal("300000.00"));
        ped2.setSaldoPendiente(new BigDecimal("276000.00"));
        pedidoRepo.save(ped2);

        Pedido ped3 = new Pedido("FAC-00103", c3, e2, TipoVenta.MAYORISTA, TipoEntrega.POR_ENCARGO, LocalDate.now().plusDays(4), MetodoPago.TRANSFERENCIA, "Desayunos buffet ejecutivos Hotel La Casona.");
        ped3.setEstado(EstadoPedido.PENDIENTE);
        PedidoDetalle d5 = new PedidoDetalle(ped3, p6, 20, p6.getPrecioMayorista());
        PedidoDetalle d6 = new PedidoDetalle(ped3, p4, 5, p4.getPrecioMayorista());
        ped3.getDetalles().add(d5);
        ped3.getDetalles().add(d6);
        ped3.setTotal(new BigDecimal("300000.00"));
        ped3.setAbono(new BigDecimal("100000.00"));
        ped3.setSaldoPendiente(new BigDecimal("200000.00"));
        pedidoRepo.save(ped3);
    }
}

