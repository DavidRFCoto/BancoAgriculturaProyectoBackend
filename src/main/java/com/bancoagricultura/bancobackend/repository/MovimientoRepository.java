package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    List<Movimiento> findByCuentaIdOrderByFechaDesc(Integer cuentaId);
    @Query("SELECT m FROM Movimiento m JOIN FETCH m.cuenta c JOIN FETCH c.cliente cl ORDER BY m.fecha DESC")
    List<Movimiento> findAllWithCuentaAndCliente();
}
