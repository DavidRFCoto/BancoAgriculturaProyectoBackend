package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.dto.CuentaDTO;
import com.bancoagricultura.bancobackend.dto.TransaccionDTO;
import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import com.bancoagricultura.bancobackend.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dependiente")
public class DependienteController {

    private final CuentaService cuentaService;

    @Autowired
    public DependienteController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
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
    public ResponseEntity<?> abonarEfectivo(@RequestBody TransaccionDTO transaccion) { // CAMBIO a @RequestBody
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
}