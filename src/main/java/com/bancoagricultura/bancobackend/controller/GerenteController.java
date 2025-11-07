package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.dto.EmpleadoDTO;
import com.bancoagricultura.bancobackend.dto.EmpleadoRegistroDTO;
import com.bancoagricultura.bancobackend.dto.PrestamoDTO;
import com.bancoagricultura.bancobackend.entity.Empleado;
import com.bancoagricultura.bancobackend.entity.Prestamo;
import com.bancoagricultura.bancobackend.entity.Rol;
import com.bancoagricultura.bancobackend.repository.EmpleadoRepository;
import com.bancoagricultura.bancobackend.repository.RolRepository;
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
    private final PrestamoService prestamoService;

    @Autowired
    public GerenteController(EmpleadoRepository empleadoRepository,
                             RolRepository rolRepository,
                             PrestamoService prestamoService) {
        this.empleadoRepository = empleadoRepository;
        this.rolRepository = rolRepository;
        this.prestamoService = prestamoService;
    }

    /**
     * Endpoint para la Tarea del Gerente: "Ingresar nuevos empleados"
     */
    @PostMapping("/empleados")
    public ResponseEntity<?> registrarEmpleado(@RequestBody EmpleadoRegistroDTO dto) {
        try {
            // ... (código para buscar Rol y crear Empleado se queda igual) ...
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con ID: " + dto.getRolId()));

            Empleado nuevoEmpleado = new Empleado();
            // ... (setear nombre, puesto, salario, rol) ...
            nuevoEmpleado.setEstado("activo");

            // 4. Guardar en la BD
            Empleado empleadoGuardado = empleadoRepository.save(nuevoEmpleado);

            // 5. ¡ARREGLO! Volver a buscar el empleado guardado USANDO EL JOIN FETCH
            // para tener el objeto Rol completo antes de crear el DTO.
            Empleado empleadoCompleto = empleadoRepository.findByIdWithRol(empleadoGuardado.getId())
                    .orElseThrow(() -> new NoSuchElementException("Error al recuperar empleado post-guardado."));

            // 6. Convertir a DTO y devolver
            EmpleadoDTO empleadoDTO = new EmpleadoDTO(empleadoCompleto);
            return ResponseEntity.status(201).body(empleadoDTO);

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