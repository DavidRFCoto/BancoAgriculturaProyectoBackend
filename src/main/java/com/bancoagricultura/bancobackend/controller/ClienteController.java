package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.dto.CuentaCreacionRequestDTO;
import com.bancoagricultura.bancobackend.dto.CuentaDTO;
import com.bancoagricultura.bancobackend.dto.MovimientoDTO;
import com.bancoagricultura.bancobackend.repository.ClienteRepository;
import com.bancoagricultura.bancobackend.repository.CuentaRepository;
import com.bancoagricultura.bancobackend.repository.MovimientoRepository;
import com.bancoagricultura.bancobackend.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;

    @Autowired
    public ClienteController(ClienteRepository clienteRepository,
                             CuentaRepository cuentaRepository,
                             MovimientoRepository movimientoRepository,
                             CuentaService cuentaService) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
        this.cuentaService = cuentaService;
    }

    @GetMapping("/{clienteId}/cuentas")
    public ResponseEntity<?> obtenerCuentasPorCliente(@PathVariable Integer clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            return ResponseEntity.status(404)
                    .body(java.util.Collections.singletonMap("error", "Cliente no encontrado"));
        }

        List<CuentaDTO> cuentas = cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(CuentaDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{clienteId}/cuentas/{cuentaId}/movimientos")
    public ResponseEntity<?> obtenerMovimientosPorCuenta(@PathVariable Integer clienteId,
                                                         @PathVariable Integer cuentaId) {
        var cuenta = cuentaRepository.findByIdAndClienteId(cuentaId, clienteId);
        if (cuenta.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(java.util.Collections.singletonMap("error",
                            "Cuenta no encontrada para el cliente"));
        }

        List<MovimientoDTO> movimientos = movimientoRepository
                .findByCuentaIdOrderByFechaDesc(cuentaId)
                .stream()
                .map(MovimientoDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(movimientos);
    }

    @PostMapping("/{clienteId}/cuentas")
    public ResponseEntity<?> crearCuentaParaCliente(@PathVariable Integer clienteId,
                                                    @RequestBody CuentaCreacionRequestDTO request) {
        try {
            if (request == null) {
                return ResponseEntity.badRequest()
                        .body(java.util.Collections.singletonMap("error", "Debe especificar el tipo de cuenta."));
            }
            var cuenta = cuentaService.createCuenta(clienteId, request.getTipoCuenta());
            return ResponseEntity.status(201).body(new CuentaDTO(cuenta));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(java.util.Collections.singletonMap("error", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500)
                    .body(java.util.Collections.singletonMap("error", "No fue posible crear la cuenta."));
        }
    }
}
