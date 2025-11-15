package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.Prestamo;
import java.math.BigDecimal;
import java.util.List;

public interface PrestamoService {


    Prestamo solicitarPrestamo(Integer clienteId, Integer cajeroId, BigDecimal monto);


    Prestamo aprobarPrestamo(Integer prestamoId, Integer gerenteId);


    Prestamo rechazarPrestamo(Integer prestamoId, Integer gerenteId);

    List<Prestamo> findPrestamosPendientes();
}