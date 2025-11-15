package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.dto.CuentaDTO;
import com.bancoagricultura.bancobackend.dto.ClienteDTO;
import com.bancoagricultura.bancobackend.dto.ClienteRegistroDTO;
import com.bancoagricultura.bancobackend.dto.DependienteResumenDTO;
import com.bancoagricultura.bancobackend.dto.TransaccionDTO;
import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import com.bancoagricultura.bancobackend.entity.Dependiente;
import com.bancoagricultura.bancobackend.service.CuentaService;
import com.bancoagricultura.bancobackend.repository.ClienteRepository;
import com.bancoagricultura.bancobackend.repository.DependienteRepository;
import com.bancoagricultura.bancobackend.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dependiente")
public class DependienteController {

    private final CuentaService cuentaService;
    private final ClienteRepository clienteRepository;
    private final DependienteRepository dependienteRepository;
    private final MovimientoRepository movimientoRepository;

    @Autowired
    public DependienteController(CuentaService cuentaService,
                                 ClienteRepository clienteRepository,
                                 DependienteRepository dependienteRepository,
                                 MovimientoRepository movimientoRepository) {
        this.cuentaService = cuentaService;
        this.clienteRepository = clienteRepository;
        this.dependienteRepository = dependienteRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @GetMapping("/cuentas/{dui}")
    public ResponseEntity<?> getCuentasPorDui(@PathVariable("dui") String dui) {
        List<CuentaBancaria> cuentas = cuentaService.findCuentasByDui(dui);
        if (cuentas == null || cuentas.isEmpty()) {

            return ResponseEntity.status(404)
                    .body(java.util.Collections.singletonMap("error", "No se encontraron cuentas para el DUI: " + dui));
        }
        List<CuentaDTO> cuentasDTO = cuentas.stream()
                .map(CuentaDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(cuentasDTO);
    }

    @PostMapping("/abonarefectivo") // (Comentario: CAMBIO de @POST y @Path)
    public ResponseEntity<?> abonarEfectivo(@RequestBody TransaccionDTO transaccion) { // (Comentario: CAMBIO a @RequestBody)
        try {
            cuentaService.abonarEfectivo(
                    transaccion.getNumeroCuenta(),
                    transaccion.getMonto(),
                    transaccion.getDependienteId()
            );
            // (Comentario: CAMBIO a ResponseEntity)
            return ResponseEntity.ok(java.util.Collections.singletonMap("mensaje", "Abono exitoso"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/clientes")
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteRegistroDTO clienteDTO) {
        try {
            var cliente = new com.bancoagricultura.bancobackend.entity.Cliente();
            cliente.setNombre(clienteDTO.getNombre());
            cliente.setApellidos(clienteDTO.getApellidos());
            cliente.setGenero(clienteDTO.getGenero());
            cliente.setDui(clienteDTO.getDui());
            cliente.setSalario(clienteDTO.getSalario());
            var guardado = clienteRepository.save(cliente);
            return ResponseEntity.status(201).body(new ClienteDTO(guardado));
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/retirarefectivo") // (Comentario: CAMBIO de @POST y @Path)
    public ResponseEntity<?> retirarEfectivo(@RequestBody TransaccionDTO transaccion) { // (Comentario: CAMBIO a @RequestBody)
        try {
            cuentaService.retirarEfectivo(
                    transaccion.getNumeroCuenta(),
                    transaccion.getMonto(),
                    transaccion.getDependienteId()
            );
            return ResponseEntity.ok(java.util.Collections.singletonMap("mensaje", "Retiro exitoso"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/{dependienteId}/resumen")
    public ResponseEntity<?> obtenerResumen(@PathVariable Integer dependienteId) {
        Dependiente dependiente = dependienteRepository.findById(dependienteId)
                .orElse(null);
        if (dependiente == null) {
            return ResponseEntity.status(404)
                    .body(java.util.Collections.singletonMap("error", "Dependiente no encontrado"));
        }

        BigDecimal comisionAcumulada = movimientoRepository.sumComisionByDependienteId(dependienteId);
        if (comisionAcumulada == null) {
            comisionAcumulada = BigDecimal.ZERO;
        }
        DependienteResumenDTO dto = new DependienteResumenDTO(
                dependiente.getId(),
                dependiente.getNombre(),
                comisionAcumulada,
                dependiente.getComision()
        );
        return ResponseEntity.ok(dto);
    }
}