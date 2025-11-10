package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.dto.EmpleadoDTO;
import com.bancoagricultura.bancobackend.dto.EmpleadoRegistroDTO;
import com.bancoagricultura.bancobackend.dto.PrestamoDTO;
import com.bancoagricultura.bancobackend.dto.AccionPersonalRegistroDTO;
import com.bancoagricultura.bancobackend.dto.AccionPersonalDTO;
import com.bancoagricultura.bancobackend.entity.Empleado;
import com.bancoagricultura.bancobackend.entity.Prestamo;
import com.bancoagricultura.bancobackend.entity.Rol;
import com.bancoagricultura.bancobackend.entity.AccionPersonal;
import com.bancoagricultura.bancobackend.repository.EmpleadoRepository;
import com.bancoagricultura.bancobackend.repository.RolRepository;
import com.bancoagricultura.bancobackend.repository.AccionPersonalRepository;
import com.bancoagricultura.bancobackend.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/gerente") // URL base para el Gerente
public class GerenteController {

    private final EmpleadoRepository empleadoRepository;
    private final RolRepository rolRepository;

    private final AccionPersonalRepository accionPersonalRepository;
    private final PrestamoService prestamoService;

    @Autowired
    public GerenteController(EmpleadoRepository empleadoRepository,
                             RolRepository rolRepository,
                             AccionPersonalRepository accionPersonalRepository,
                             PrestamoService prestamoService) {
        this.empleadoRepository = empleadoRepository;
        this.rolRepository = rolRepository;
        this.accionPersonalRepository = accionPersonalRepository;
        this.prestamoService = prestamoService;
    }

    /**
     * Endpoint para solicitar Contratacion
     */
    @PostMapping("/solicitar-contratacion")
    public ResponseEntity<?> solicitarContratacion(@RequestBody AccionPersonalRegistroDTO dto) {
        try {
            // Buscar el Rol del nuevo empleado
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con ID: " + dto.getRolId()));

            // Buscar al Gerente que solicita
            Empleado gerente = empleadoRepository.findById(dto.getGerenteSolicitanteId())
                    .orElseThrow(() -> new NoSuchElementException("Gerente solicitante no encontrado con ID: " + dto.getGerenteSolicitanteId()));

            // Crear la Accion de Personal
            AccionPersonal ap = new AccionPersonal();
            ap.setTipoAccion("CONTRATACION");
            ap.setEstado("pendiente");
            ap.setNombreNuevoEmpleado(dto.getNombre());
            ap.setPuestoNuevoEmpleado(dto.getPuesto());
            ap.setSalarioNuevoEmpleado(dto.getSalario());
            ap.setRolNuevoEmpleado(rol);
            ap.setGerenteSolicitante(gerente);

            // Guardar la solicitud
            AccionPersonal apGuardada = accionPersonalRepository.save(ap);

            // Buscar con JOIN FETCH
            AccionPersonal apCompleta = accionPersonalRepository.findByIdWithDetails(apGuardada.getId())
                    .orElseThrow(() -> new NoSuchElementException("Error al recargar accion post-guardado."));

            // Devolver el DTO
            return ResponseEntity.status(201).body(new AccionPersonalDTO(apCompleta));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    // ENDPOINTS PARA GESTION DE PRESTAMOS
    @GetMapping("/prestamos/pendientes")
    public ResponseEntity<?> getPrestamosPendientes() {
        List<Prestamo> pendientes = prestamoService.findPrestamosPendientes();
        List<PrestamoDTO> dtos = pendientes.stream().map(PrestamoDTO::new).toList();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/prestamos/{id}/aprobar")
    public ResponseEntity<?> aprobarPrestamo(@PathVariable("id") Integer prestamoId) {
        try {
            Prestamo prestamo = prestamoService.aprobarPrestamo(prestamoId, 1);
            return ResponseEntity.ok(new PrestamoDTO(prestamo));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PostMapping("/prestamos/{id}/rechazar")
    public ResponseEntity<?> rechazarPrestamo(@PathVariable("id") Integer prestamoId) {
        try {
            Prestamo prestamo = prestamoService.rechazarPrestamo(prestamoId, 1);
            return ResponseEntity.ok(new PrestamoDTO(prestamo));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para Tarea del Gerente: "Dar de baja a un empleado"
     */
    @PutMapping("/empleados/{id}/desactivar")
    public ResponseEntity<?> desactivarEmpleado(@PathVariable("id") Integer empleadoId) {
        try {
            //  Buscar al empleado en la BD usando el metodo CON JOIN FETCH
            Empleado empleado = empleadoRepository.findByIdWithRol(empleadoId)
                    .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado con ID: " + empleadoId));

            // Cambiar el estado
            empleado.setEstado("inactivo");

            // Guardar el cambio
            empleadoRepository.save(empleado);

            // Devolver el empleado actualizado
            return ResponseEntity.ok(new EmpleadoDTO(empleado));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }


}