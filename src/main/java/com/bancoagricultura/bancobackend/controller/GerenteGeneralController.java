package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.service.SucursalService;
import com.bancoagricultura.bancobackend.dto.MovimientoDTO;
import com.bancoagricultura.bancobackend.entity.Movimiento;
import com.bancoagricultura.bancobackend.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bancoagricultura.bancobackend.repository.SucursalRepository;
import com.bancoagricultura.bancobackend.entity.Sucursal;
import com.bancoagricultura.bancobackend.dto.SucursalRegistroDTO;
import com.bancoagricultura.bancobackend.dto.SucursalDTO;
import com.bancoagricultura.bancobackend.entity.Empleado;
import com.bancoagricultura.bancobackend.dto.AsignacionGerenteDTO;
import com.bancoagricultura.bancobackend.repository.EmpleadoRepository;
import java.util.NoSuchElementException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gerente-general") // URL base para el Gerente General
public class GerenteGeneralController {

    private final MovimientoRepository movimientoRepository;
    private final SucursalRepository sucursalRepository;
    private final SucursalService sucursalService;

    @Autowired
    public GerenteGeneralController(MovimientoRepository movimientoRepository,
                                    SucursalRepository sucursalRepository,
                                    EmpleadoRepository empleadoRepository,
                                    SucursalService sucursalService) {
        this.movimientoRepository = movimientoRepository;
        this.sucursalRepository = sucursalRepository;
        this.sucursalService = sucursalService;

    }

    /**
     * Endpoint para Tarea del Gerente General: "Ver todos los movimientos"
     */
    @GetMapping("/movimientos")
    public ResponseEntity<?> verTodosLosMovimientos() {

        // Usamos el nuevo metodo del repositorio que trae todo
        List<Movimiento> movimientos = movimientoRepository.findAllWithCuentaAndCliente();

        // Convertimos a DTOs
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
            // Crear la nueva entidad
            Sucursal nuevaSucursal = new Sucursal();
            nuevaSucursal.setNombre(dto.getNombre());
            nuevaSucursal.setDireccion(dto.getDireccion());

            // Guardar en la BD
            Sucursal sucursalGuardada = sucursalRepository.save(nuevaSucursal);

            // Devolver el DTO
            return ResponseEntity.status(201).body(new SucursalDTO(sucursalGuardada));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }
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
        } catch (IllegalArgumentException e) { // <-- Capturamos la validación de rol
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }
}