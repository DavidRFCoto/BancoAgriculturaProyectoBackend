package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.Cliente;
import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import com.bancoagricultura.bancobackend.entity.Movimiento;
import com.bancoagricultura.bancobackend.repository.ClienteRepository;
import com.bancoagricultura.bancobackend.repository.CuentaRepository;
import com.bancoagricultura.bancobackend.repository.MovimientoRepository;
import com.bancoagricultura.bancobackend.service.CuentaService;
import com.bancoagricultura.bancobackend.util.NumeroCuentaGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementacion del servicio de cuentas.
 */
@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimientoRepository movimientoRepository;
    private final NumeroCuentaGenerator numeroCuentaGenerator;

    public CuentaServiceImpl(CuentaRepository cuentaRepository,
                             ClienteRepository clienteRepository,
                             MovimientoRepository movimientoRepository,
                             NumeroCuentaGenerator numeroCuentaGenerator) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.movimientoRepository = movimientoRepository;
        this.numeroCuentaGenerator = numeroCuentaGenerator;
    }

    @Override
    @Transactional
    public CuentaBancaria createCuenta(Integer clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));

        List<CuentaBancaria> cuentasExistentes = cuentaRepository.findByClienteId(cliente.getId());
        if (cuentasExistentes.size() >= 3) {
            throw new IllegalArgumentException("El cliente ya tiene 3 cuentas. No se puede crear mas.");
        }

        // Genera numero de cuenta unico (se asegura en el util)
        String numeroCuenta = numeroCuentaGenerator.generarNumeroCuentaUnico();

        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setCliente(cliente);
        cuenta.setSaldo(BigDecimal.ZERO);

        CuentaBancaria guardada = cuentaRepository.save(cuenta);

        // Mantener la referencia bidireccional en memoria
        cliente.getCuentas().add(guardada);

        return guardada;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaBancaria> findCuentasByDui(String dui) {
        return clienteRepository.findByDui(dui)
                .map(cliente -> cuentaRepository.findByClienteId(cliente.getId()))
                .orElseGet(List::of);
    }

    @Override
    @Transactional
    public void abonarEfectivo(String numeroCuenta, BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a abonar debe ser mayor que cero.");
        }

        CuentaBancaria cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));

        // actualizar saldo
        BigDecimal nuevoSaldo = cuenta.getSaldo().add(monto);
        cuenta.setSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);

        // registrar movimiento
        Movimiento movimiento = new Movimiento("DEPOSITO", monto, "Abono realizado");
        movimiento.setCuenta(cuenta);
        movimientoRepository.save(movimiento);
    }

    @Override
    @Transactional
    public void retirarEfectivo(String numeroCuenta, BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor que cero.");
        }

        CuentaBancaria cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada: " + numeroCuenta));

        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
        }

        // actualizar saldo
        BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(monto);
        cuenta.setSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);

        // registrar movimiento
        Movimiento movimiento = new Movimiento("RETIRO", monto, "Retiro realizado");
        movimiento.setCuenta(cuenta);
        movimientoRepository.save(movimiento);
    }
}

