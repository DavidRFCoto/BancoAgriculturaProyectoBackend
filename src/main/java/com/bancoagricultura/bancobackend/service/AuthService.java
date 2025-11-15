package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.dto.LoginRequestDTO;
import com.bancoagricultura.bancobackend.dto.LoginResponseDTO;
import com.bancoagricultura.bancobackend.entity.Empleado;
import com.bancoagricultura.bancobackend.entity.Dependiente;
import com.bancoagricultura.bancobackend.entity.Cliente;
import com.bancoagricultura.bancobackend.repository.EmpleadoRepository;
import com.bancoagricultura.bancobackend.repository.DependienteRepository;
import com.bancoagricultura.bancobackend.repository.ClienteRepository;
import com.bancoagricultura.bancobackend.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final EmpleadoRepository empleadoRepository;
    private final DependienteRepository dependienteRepository;
    private final SucursalRepository sucursalRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    public AuthService(EmpleadoRepository empleadoRepository,
                      DependienteRepository dependienteRepository,
                      SucursalRepository sucursalRepository,
                      ClienteRepository clienteRepository) {
        this.empleadoRepository = empleadoRepository;
        this.dependienteRepository = dependienteRepository;
        this.sucursalRepository = sucursalRepository;
        this.clienteRepository = clienteRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        String usuario = request.getUsuario();
        String password = request.getPassword();

        // Primero intentar buscar en Empleados
        Optional<Empleado> empleadoOpt = empleadoRepository.findByUsuario(usuario);
        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            if (password != null && password.equals(empleado.getPassword())) {
                Integer sucursalId = findSucursalIdByEmpleado(empleado.getId());
                return new LoginResponseDTO(
                    empleado.getId(),
                    empleado.getNombre(),
                    empleado.getRol().getNombre(),
                    empleado.getRol().getId(),
                    sucursalId
                );
            }
        }

        // Si no se encuentra en Empleados, buscar en Dependientes
        Optional<Dependiente> dependienteOpt = dependienteRepository.findByUsuario(usuario);
        if (dependienteOpt.isPresent()) {
            Dependiente dependiente = dependienteOpt.get();
            if (password != null && password.equals(dependiente.getPassword())) {
                return new LoginResponseDTO(
                    dependiente.getId(),
                    dependiente.getNombre(),
                    dependiente.getRol().getNombre(),
                    dependiente.getRol().getId(),
                    null // Dependientes no tienen sucursal
                );
            }
        }

        // Intentar autenticar un cliente
        Optional<Cliente> clienteOpt = clienteRepository.findByUsuarioAndActivoTrue(usuario);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            if (password != null && password.equals(cliente.getPassword())) {
                LoginResponseDTO response = new LoginResponseDTO(
                        cliente.getId(),
                        cliente.getNombre(),
                        "Cliente",
                        null,
                        null
                );
                response.setClienteId(cliente.getId());
                return response;
            }
        }

        throw new RuntimeException("Usuario o contraseña incorrectos");
    }

    private Integer findSucursalIdByEmpleado(Integer empleadoId) {
        return sucursalRepository.findAll().stream()
            .filter(s -> s.getGerente() != null && s.getGerente().getId().equals(empleadoId))
            .findFirst()
            .map(s -> s.getId())
            .orElse(null);
    }
}

