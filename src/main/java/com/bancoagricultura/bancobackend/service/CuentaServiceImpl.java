package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.Cliente;
import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import com.bancoagricultura.bancobackend.entity.Movimiento;
import com.bancoagricultura.bancobackend.repository.ClienteRepository;
import com.bancoagricultura.bancobackend.repository.CuentaRepository;
import com.bancoagricultura.bancobackend.repository.MovimientoRepository;
import com.bancoagricultura.bancobackend.repository.DependienteRepository;
import com.bancoagricultura.bancobackend.util.NumeroCuentaGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.math.RoundingMode;

@Service
public class CuentaServiceImpl implements CuentaService {

    private static final BigDecimal PORCENTAJE_COMISION = new BigDecimal("0.05"); // 5%

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimientoRepository movimientoRepository;
    private final NumeroCuentaGenerator numeroCuentaGenerator;
    private final DependienteRepository dependienteRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository,
                             ClienteRepository clienteRepository,
                             MovimientoRepository movimientoRepository,
                             NumeroCuentaGenerator numeroCuentaGenerator,
                             DependienteRepository dependienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.movimientoRepository = movimientoRepository;
        this.numeroCuentaGenerator = numeroCuentaGenerator;
        this.dependienteRepository = dependienteRepository;
    }

    private String normalizarTipoCuenta(String tipoCuenta) {
        if (tipoCuenta == null || tipoCuenta.trim().isEmpty()) {
            return "Cuenta Corriente";
        }

        String valor = tipoCuenta.trim().toLowerCase();
        if (valor.contains("ahorro")) {
            return "Cuenta de Ahorro";
        }
        if (valor.contains("corriente")) {
            return "Cuenta Corriente";
        }

        throw new IllegalArgumentException("Tipo de cuenta no válido. Use 'corriente' o 'ahorro'.");
    }

    // El metodo crear Cuenta se queda igua
    @Override
    @Transactional
    public CuentaBancaria createCuenta(Integer clienteId, String tipoCuenta) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));

        List<CuentaBancaria> cuentasExistentes = cuentaRepository.findByClienteId(cliente.getId());
        if (cuentasExistentes.size() >= 3) {
            throw new IllegalArgumentException("El cliente ya tiene 3 cuentas. No se puede crear mas.");
        }

        String numeroCuenta = numeroCuentaGenerator.generarNumeroCuentaUnico();
        String tipoNormalizado = normalizarTipoCuenta(tipoCuenta);

        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setCliente(cliente);
        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setTipoCuenta(tipoNormalizado);

        CuentaBancaria guardada = cuentaRepository.save(cuenta);

        cliente.getCuentas().add(guardada);

        return guardada;
    }

    //  metodo findCuentasByDui
    @Override
    @Transactional(readOnly = true)
    public List<CuentaBancaria> findCuentasByDui(String dui) {
        return clienteRepository.findByDui(dui)
                .map(cliente -> cuentaRepository.findByClienteId(cliente.getId()))
                .orElseGet(List::of);
    }


    // METODO ABONAR
    @Override
    @Transactional
    public void abonarEfectivo(String numeroCuenta, BigDecimal monto, Integer dependienteId) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a abonar debe ser mayor que cero.");
        }

        CuentaBancaria cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));

        // (Comentario: actualizar saldo)
        BigDecimal nuevoSaldo = cuenta.getSaldo().add(monto);
        cuenta.setSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);

        // registrar movimiento
        Movimiento movimiento = new Movimiento("DEPOSITO", monto, "Abono realizado");
        movimiento.setCuenta(cuenta);

        //  LOGICA DE COMISION
        if (dependienteId != null) {
            // Verificamos que el dependiente exista)
            if (!dependienteRepository.existsById(dependienteId)) {
                throw new NoSuchElementException("Dependiente no encontrado: " + dependienteId);
            }
            //  Calculamos la comision
            BigDecimal comision = monto.multiply(PORCENTAJE_COMISION).setScale(2, RoundingMode.HALF_UP);
            movimiento.setMontoComision(comision);
            movimiento.setIdDependienteComision(dependienteId);
        }

        movimientoRepository.save(movimiento);
    }

    // METODO RETIRAR
    @Override
    @Transactional
    public void retirarEfectivo(String numeroCuenta, BigDecimal monto, Integer dependienteId) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor que cero.");
        }

        CuentaBancaria cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));

        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
        }

        //  actualizar saldo
        BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(monto);
        cuenta.setSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);

        //  registrar movimiento
        Movimiento movimiento = new Movimiento("RETIRO", monto, "Retiro realizado");
        movimiento.setCuenta(cuenta);

        //  LOGICA DE COMISION
        if (dependienteId != null) {
            if (!dependienteRepository.existsById(dependienteId)) {
                throw new NoSuchElementException("Dependiente no encontrado: " + dependienteId);
            }
            BigDecimal comision = monto.multiply(PORCENTAJE_COMISION).setScale(2, RoundingMode.HALF_UP);
            movimiento.setMontoComision(comision);
            movimiento.setIdDependienteComision(dependienteId);
        }


        movimientoRepository.save(movimiento);
    }
}