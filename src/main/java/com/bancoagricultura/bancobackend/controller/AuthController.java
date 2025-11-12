package com.bancoagricultura.bancobackend.controller;

import com.bancoagricultura.bancobackend.dto.EmpleadoDTO;
import com.bancoagricultura.bancobackend.entity.Empleado;
import com.bancoagricultura.bancobackend.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    public static class LoginRequest {
        private String username;
        private String password;

        // Getters y Setters necesarios
        public String getUsername() {
            return username; }
        public void setUsername(String username) {
            this.username = username; }
        public String getPassword() {
            return password; }
        public void setPassword(String password) {
            this.password = password; }
    }

    /**
     * Endpoint para el Login de Empleados (Cajero, Gerentes)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Buscar al empleado por username
            Empleado empleado = empleadoRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

            // Validar la contrasena
            if (!empleado.getPassword().equals(loginRequest.getPassword())) {
                throw new IllegalArgumentException("Contrasena incorrecta");
            }

            // Validar que este "activo"
            if (!empleado.getEstado().equals("activo")) {
                throw new IllegalArgumentException("El usuario no esta activo. Contacte al Gerente General.");
            }

            //  Exito
            Empleado empleadoCompleto = empleadoRepository.findByIdWithRol(empleado.getId())
                    .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

            // Devolvemos el EmpleadoDTO que incluye el ROL
            return ResponseEntity.ok(new EmpleadoDTO(empleadoCompleto));

        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(java.util.Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(java.util.Collections.singletonMap("error", e.getMessage()));
        }
    }
}