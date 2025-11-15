package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

// ... (otros imports)
import com.bancoagricultura.bancobackend.dto.ClienteDTO; // <-- NUEVO IMPORT
import com.bancoagricultura.bancobackend.dto.ClienteRegistroDTO; // <-- NUEVO IMPORT
import com.bancoagricultura.bancobackend.entity.Cliente; // <-- NUEVO IMPORT
import com.bancoagricultura.bancobackend.repository.ClienteRepository; // <-- NUEVO IMPORT
import com.bancoagricultura.bancobackend.repository.CuentaRepository;
import com.bancoagricultura.bancobackend.service.CuentaService;
import com.bancoagricultura.bancobackend.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDui(String dui);

    Optional<Cliente> findByUsuarioAndActivoTrue(String usuario);
}

