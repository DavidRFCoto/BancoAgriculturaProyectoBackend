package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.TipoEstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoEstadoPrestamoRepository extends JpaRepository<TipoEstadoPrestamo, Integer> {
    Optional<TipoEstadoPrestamo> findByNombreEstado(String nombreEstado);
}