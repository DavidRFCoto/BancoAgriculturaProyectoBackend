package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByDui(String dui);
}

