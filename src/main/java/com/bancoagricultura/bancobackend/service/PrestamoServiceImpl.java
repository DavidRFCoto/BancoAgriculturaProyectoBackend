package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.*;
import com.bancoagricultura.bancobackend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    // Constantes para la logica de negocio
    private static final BigDecimal PORCENTAJE_MAX_CUOTA = new BigDecimal("0.30");
    private static final int SCALE = 10; // Escala para calculos de interes
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    // Repositorios
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PrestamoRepository prestamoRepository;
    private final CasoPrestamoRepository casoPrestamoRepository;
    private final CriterioPrestamoRepository criterioPrestamoRepository;
    private final TipoEstadoPrestamoRepository tipoEstadoPrestamoRepository;

    public PrestamoServiceImpl(ClienteRepository clienteRepository,
                               EmpleadoRepository empleadoRepository,
                               PrestamoRepository prestamoRepository,
                               CasoPrestamoRepository casoPrestamoRepository,
                               CriterioPrestamoRepository criterioPrestamoRepository,
                               TipoEstadoPrestamoRepository tipoEstadoPrestamoRepository) {
        this.clienteRepository = clienteRepository;
        this.empleadoRepository = empleadoRepository;
        this.prestamoRepository = prestamoRepository;
        this.casoPrestamoRepository = casoPrestamoRepository;
        this.criterioPrestamoRepository = criterioPrestamoRepository;
        this.tipoEstadoPrestamoRepository = tipoEstadoPrestamoRepository;
    }

    @Override
    @Transactional
    public Prestamo solicitarPrestamo(Integer clienteId, Integer cajeroId, BigDecimal monto) {

        // 1. Validar Entidades
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con ID: " + clienteId));

        Empleado cajero = empleadoRepository.findById(cajeroId)
                .orElseThrow(() -> new NoSuchElementException("Cajero no encontrado con ID: " + cajeroId));

        BigDecimal salarioCliente = cliente.getSalario();
        if (salarioCliente == null || salarioCliente.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El cliente no tiene un salario registrado.");
        }

        //  Buscar Criterio de Prestamo
        CriterioPrestamo criterio = criterioPrestamoRepository.findAll().stream()
                .filter(c -> salarioCliente.compareTo(c.getSalarioMinimo()) >= 0 &&
                        salarioCliente.compareTo(c.getSalarioMaximo()) <= 0)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No se encontró un criterio de prestamo para el salario del cliente."));

        // Validar Monto Máximo
        if (monto.compareTo(criterio.getMontoMaximo()) > 0) {
            throw new IllegalArgumentException("El monto solicitado (" + monto +
                    ") excede el maximo permitido (" + criterio.getMontoMaximo() + ") para este salario.");
        }

        // Calcular Cuota y Plazo (Requisito "calcular cantidad de años")
        // Asumimos que la cuota se fija al 30% del salario (el máximo permitido)

        BigDecimal cuotaMensual = salarioCliente.multiply(PORCENTAJE_MAX_CUOTA).setScale(2, ROUNDING_MODE);

        BigDecimal interesAnual = criterio.getPorcentajeInteres();
        BigDecimal interesMensual = interesAnual.divide(new BigDecimal("100"), SCALE, ROUNDING_MODE)
                .divide(new BigDecimal("12"), SCALE, ROUNDING_MODE);

        // Formula de numero de periodos (meses): n = -log(1 - (P * i) / C) / log(1 + i)
        // Donde: P=Monto, i=Interes Mensual, C=Cuota Mensual
        double p = monto.doubleValue();
        double i = interesMensual.doubleValue();
        double c = cuotaMensual.doubleValue();

        if (c <= p * i) {
            throw new IllegalArgumentException("La cuota mensual calculada (30% del salario) no es suficiente para cubrir los intereses del prestamo.");
        }

        double n_double = -Math.log(1 - (p * i) / c) / Math.log(1 + i);
        int plazoEnMeses = (int) Math.ceil(n_double); // Redondear hacia arriba al siguiente mes

        // 5. Buscar estado "en espera"
        TipoEstadoPrestamo estadoEnEspera = tipoEstadoPrestamoRepository.findByNombreEstado("en espera")
                .orElseThrow(() -> new NoSuchElementException("No se encontro el estado 'en espera'. Insertelo en la BD."));

        // 6. Crear y Guardar el Préstamo
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setCliente(cliente);
        nuevoPrestamo.setMontoTotal(monto);
        nuevoPrestamo.setCriterio(criterio);
        nuevoPrestamo.setEstado(estadoEnEspera);
        nuevoPrestamo.setCuotaMensual(cuotaMensual); // Guardamos el calculo
        nuevoPrestamo.setPlazoEnMeses(plazoEnMeses); // Guardamos el calculo

        Prestamo prestamoGuardado = prestamoRepository.save(nuevoPrestamo);

        // 7. Crear y Guardar el Caso
        CasoPrestamo caso = new CasoPrestamo();
        caso.setPrestamo(prestamoGuardado);
        caso.setCajero(cajero);

        double plazoEnAnios = (double) plazoEnMeses / 12.0;
        caso.setDescripcion("Solicitud de prestamo de " + monto + " creada por " + cajero.getNombre() +
                ". Plazo estimado: " + String.format("%.1f", plazoEnAnios) + " años.");

        casoPrestamoRepository.save(caso);

        return prestamoGuardado;
    }

    @Override
    @Transactional
    public Prestamo aprobarPrestamo(Integer prestamoId, Integer gerenteId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new NoSuchElementException("Prestamo no encontrado: " + prestamoId));

        TipoEstadoPrestamo estadoAprobado = tipoEstadoPrestamoRepository.findByNombreEstado("aprobado")
                .orElseThrow(() -> new NoSuchElementException("No se encontro el estado 'aprobado'."));

        prestamo.setEstado(estadoAprobado);
        prestamo.setFechaAprobacion(LocalDate.now());
        return prestamoRepository.save(prestamo);
    }

    @Override
    @Transactional
    public Prestamo rechazarPrestamo(Integer prestamoId, Integer gerenteId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new NoSuchElementException("Prestamo no encontrado: " + prestamoId));

        TipoEstadoPrestamo estadoRechazado = tipoEstadoPrestamoRepository.findByNombreEstado("rechazado")
                .orElseThrow(() -> new NoSuchElementException("No se encontro el estado 'rechazado'."));

        prestamo.setEstado(estadoRechazado);
        return prestamoRepository.save(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prestamo> findPrestamosPendientes() {
        TipoEstadoPrestamo estadoEnEspera = tipoEstadoPrestamoRepository.findByNombreEstado("en espera")
                .orElseThrow(() -> new NoSuchElementException("No se encontro el estado 'en espera'."));

        return prestamoRepository.findAll().stream()
                .filter(p -> p.getEstado().getId().equals(estadoEnEspera.getId()))
                .toList();
    }
}