package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.service.SucursalService;
import com.bancoagricultura.bancobackend.service.AccionPersonalService;
import com.bancoagricultura.bancobackend.dto.*;
import com.bancoagricultura.bancobackend.entity.*;
import com.bancoagricultura.bancobackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gerente-general")
public class GerenteGeneralController {


    private final MovimientoRepository movimientoRepository;
    private final SucursalRepository sucursalRepository;
    private final SucursalService sucursalService;
    private final AccionPersonalService accionPersonalService;
    private final AccionPersonalRepository accionPersonalRepository;

    @Autowired
    public GerenteGeneralController(MovimientoRepository movimientoRepository,
                                    SucursalRepository sucursalRepository,
                                    SucursalService sucursalService,
                                    AccionPersonalService accionPersonalService,
                                    AccionPersonalRepository accionPersonalRepository) {

        this.movimientoRepository = movimientoRepository;
        this.sucursalRepository = sucursalRepository;
        this.sucursalService = sucursalService;
        this.accionPersonalService = accionPersonalService;
        this.accionPersonalRepository = accionPersonalRepository;
    }

    /**
     * Endpoint para Tarea del Gerente General: "Ver todos los movimientos"
     */
    @GetMapping("/movimientos")
    public ResponseEntity<?> verTodosLosMovimientos() {
        List<Movimiento> movimientos = movimientoRepository.findAllWithCuentaAndCliente();
        List<MovimientoDTO> dtos = movimientos.stream()
                .map(MovimientoDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint para Tarea del Gerente General: "Crear el registro de una nueva sucursal"
     */
    @PostMapping("/sucursales")
    public ResponseEntity<?> crearSucursal(@RequestBody SucursalRegistroDTO dto) {
        try {
            Sucursal nuevaSucursal = new Sucursal();
            nuevaSucursal.setNombre(dto.getNombre());
            nuevaSucursal.setDireccion(dto.getDireccion());
            Sucursal sucursalGuardada = sucursalRepository.save(nuevaSucursal);
            return ResponseEntity.status(201).body(new SucursalDTO(sucursalGuardada));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Endpoint Tarea: "Asignar a ella un nuevo gerente de sucursal"
     */
    @PostMapping("/sucursales/{id}/asignar-gerente")
    public ResponseEntity<?> asignarGerente(@PathVariable("id") Integer sucursalId,
                                            @RequestBody AsignacionGerenteDTO dto) {
        try {
            Sucursal sucursalGuardada = sucursalService.asignarGerenteASucursal(sucursalId, dto.getGerenteId());
            Sucursal sucursalParaDTO = sucursalRepository.findByIdWithGerente(sucursalGuardada.getId())
                    .orElseThrow(() -> new NoSuchElementException("Error al recargar sucursal para DTO."));
            return ResponseEntity.ok(new SucursalDTO(sucursalParaDTO));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Endpoint Tarea: "Aceptar o rechazar las acciones de personal" (VER PENDIENTES)
     */
    @GetMapping("/acciones-personal/pendientes")
    public ResponseEntity<?> getAccionesPendientes() {
        List<AccionPersonal> pendientes = accionPersonalService.findAccionesPendientes();
        List<AccionPersonalDTO> dtos = pendientes.stream()
                .map(AccionPersonalDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Endpoint Tarea: "Aceptar o rechazar las acciones de personal" (APROBAR)
     */
    @PostMapping("/acciones-personal/{id}/aprobar")
    public ResponseEntity<?> aprobarAccionPersonal(@PathVariable("id") Integer accionId) {
        try {
            AccionPersonal accionAprobada = accionPersonalService.aprobarAccion(accionId, 1);

            // (Comentario: Ahora esto funciona porque el repositorio esta inyectado)
            AccionPersonal apCompleta = accionPersonalRepository.findByIdWithDetails(accionAprobada.getId())
                    .orElseThrow(() -> new NoSuchElementException("Error al recargar accion post-aprobacion."));

            return ResponseEntity.ok(new AccionPersonalDTO(apCompleta));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Endpoint Tarea: "Aceptar o rechazar las acciones de personal" (RECHAZAR)
     */
    @PostMapping("/acciones-personal/{id}/rechazar")
    public ResponseEntity<?> rechazarAccionPersonal(@PathVariable("id") Integer accionId) {
        try {
            AccionPersonal accionRechazada = accionPersonalService.rechazarAccion(accionId, 1);

            AccionPersonal apCompleta = accionPersonalRepository.findByIdWithDetails(accionRechazada.getId())
                    .orElseThrow(() -> new NoSuchElementException("Error al recargar accion post-rechazo."));

            return ResponseEntity.ok(new AccionPersonalDTO(apCompleta));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }
}