package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    List<Movimiento> findByCuentaIdOrderByFechaDesc(Integer cuentaId);
}
