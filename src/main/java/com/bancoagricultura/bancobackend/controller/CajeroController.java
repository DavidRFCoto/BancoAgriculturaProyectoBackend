package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.dto.CajeroTransaccionDTO;
import com.bancoagricultura.bancobackend.dto.ClienteDTO;
import com.bancoagricultura.bancobackend.dto.ClienteRegistroDTO;
import com.bancoagricultura.bancobackend.dto.DependienteDTO;
import com.bancoagricultura.bancobackend.dto.DependienteRegistroDTO;
import com.bancoagricultura.bancobackend.dto.PrestamoDTO;
import com.bancoagricultura.bancobackend.dto.PrestamoSolicitudDTO;
import com.bancoagricultura.bancobackend.entity.Cliente;
import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import com.bancoagricultura.bancobackend.entity.Dependiente;
import com.bancoagricultura.bancobackend.entity.Prestamo;
import com.bancoagricultura.bancobackend.entity.Rol;
import com.bancoagricultura.bancobackend.repository.ClienteRepository;
import com.bancoagricultura.bancobackend.repository.CuentaRepository;
import com.bancoagricultura.bancobackend.repository.DependienteRepository;
import com.bancoagricultura.bancobackend.repository.RolRepository;
import com.bancoagricultura.bancobackend.service.CuentaService;
import com.bancoagricultura.bancobackend.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cajero")
public class CajeroController {

    // servicios y repositorios
    private final PrestamoService prestamoService;
    private final CuentaService cuentaService;
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final DependienteRepository dependienteRepository;
    private final RolRepository rolRepository;

    @Autowired
    public CajeroController(PrestamoService prestamoService,
                            CuentaService cuentaService,
                            CuentaRepository cuentaRepository,
                            ClienteRepository clienteRepository,
                            DependienteRepository dependienteRepository,
                            RolRepository rolRepository) {
        this.prestamoService = prestamoService;
        this.cuentaService = cuentaService;
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.dependienteRepository = dependienteRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Endpoint para la Tarea del Cajero: "Apertura un prestamo a un cliente"
     */
    @PostMapping("/solicitar-prestamo")
    public ResponseEntity<?> solicitarPrestamo(@RequestBody PrestamoSolicitudDTO solicitud) {
        // ... (Tu codigo existente)
        try {
            Prestamo prestamoGuardado = prestamoService.solicitarPrestamo(
                    solicitud.getClienteId(),
                    solicitud.getCajeroId(),
                    solicitud.getMonto()
            );
            PrestamoDTO prestamoDTO = new PrestamoDTO(prestamoGuardado);
            return ResponseEntity.status(201).body(prestamoDTO);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para Tarea del Cajero: "Ingresar dinero validando DUI"

     */
    @PostMapping("/abonar")
    public ResponseEntity<?> abonarConValidacion(@RequestBody CajeroTransaccionDTO dto) {
        try {
            validarDuiYCuenta(dto.getNumeroCuenta(), dto.getDuiCliente());

            //  Anadimos 'null' porque el Cajero no genera comision
            cuentaService.abonarEfectivo(dto.getNumeroCuenta(), dto.getMonto(), null);

            return ResponseEntity.ok(java.util.Collections.singletonMap("mensaje", "Abono exitoso (validado por cajero)"));
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para Tarea del Cajero: "Retirar dinero validando DUI"
     */
    @PostMapping("/retirar")
    public ResponseEntity<?> retirarConValidacion(@RequestBody CajeroTransaccionDTO dto) {
        try {
            validarDuiYCuenta(dto.getNumeroCuenta(), dto.getDuiCliente());

            cuentaService.retirarEfectivo(dto.getNumeroCuenta(), dto.getMonto(), null);

            return ResponseEntity.ok(java.util.Collections.singletonMap("mensaje", "Retiro exitoso (validado por cajero)"));
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Metodo de ayuda para la logica de validacion del Cajero
     */
    private void validarDuiYCuenta(String numeroCuenta, String dui) {
        CuentaBancaria cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada: " + numeroCuenta));
        if (!cuenta.getCliente().getDui().equals(dui)) {
            throw new IllegalArgumentException("Error de validación: El DUI no coincide con el propietario de la cuenta.");
        }
    }

    /**
     * Endpoint para Tarea del Cajero: "Ingresar nuevos clientes o prestamistas"
     */
    @PostMapping("/clientes")
    public ResponseEntity<?> registrarCliente(@RequestBody ClienteRegistroDTO clienteDTO) {
        try {
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre(clienteDTO.getNombre());
            nuevoCliente.setApellidos(clienteDTO.getApellidos());
            nuevoCliente.setGenero(clienteDTO.getGenero());
            nuevoCliente.setDui(clienteDTO.getDui());
            nuevoCliente.setDireccion(clienteDTO.getDireccion());
            nuevoCliente.setSalario(clienteDTO.getSalario());
            if (clienteDTO.getPassword() != null && !clienteDTO.getPassword().trim().isEmpty()) {
                nuevoCliente.setPassword(clienteDTO.getPassword());
            }
            Cliente clienteGuardado = clienteRepository.save(nuevoCliente);
            ClienteDTO respuestaDTO = new ClienteDTO(clienteGuardado);
            return ResponseEntity.status(201).body(respuestaDTO);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para Tarea del Cajero: "Agregar dependientes del banco"
     */
    @PostMapping("/dependientes")
    public ResponseEntity<?> registrarDependiente(@RequestBody DependienteRegistroDTO dto) {
        try {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new NoSuchElementException("Rol no encontrado con ID: " + dto.getRolId()));
            Dependiente nuevoDependiente = new Dependiente();
            nuevoDependiente.setNombre(dto.getNombre());
            nuevoDependiente.setRol(rol);
            Dependiente dependienteGuardado = dependienteRepository.save(nuevoDependiente);
            DependienteDTO dependienteDTO = new DependienteDTO(dependienteGuardado);
            return ResponseEntity.status(201).body(dependienteDTO);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }
}