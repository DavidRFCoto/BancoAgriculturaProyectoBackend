package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.Empleado;
import com.bancoagricultura.bancobackend.entity.Sucursal;
import com.bancoagricultura.bancobackend.repository.EmpleadoRepository;
import com.bancoagricultura.bancobackend.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- ¡MUY IMPORTANTE!

import java.util.NoSuchElementException;

@Service
public class SucursalServiceImpl implements SucursalService {

    private final SucursalRepository sucursalRepository;
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public SucursalServiceImpl(SucursalRepository sucursalRepository, EmpleadoRepository empleadoRepository) {
        this.sucursalRepository = sucursalRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    @Transactional // <-- ¡LA SOLUCIÓN!
    public Sucursal asignarGerenteASucursal(Integer sucursalId, Integer gerenteId) {
        // 1. Buscar la Sucursal. La sesión sigue ABIERTA.
        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new NoSuchElementException("Sucursal no encontrada: " + sucursalId));

        // 2. Buscar al Empleado (Gerente) CON SU ROL. La sesión sigue ABIERTA.
        Empleado gerente = empleadoRepository.findByIdWithRol(gerenteId)
                .orElseThrow(() -> new NoSuchElementException("Empleado (Gerente) no encontrado: " + gerenteId));

        // 3. VALIDACIÓN (Esto ahora funciona porque la sesión está abierta)
        if (!gerente.getRol().getNombre().equalsIgnoreCase("Gerente de Sucursal")) {
            throw new IllegalArgumentException("El empleado '" + gerente.getNombre() +
                    "' no tiene el rol de 'Gerente de Sucursal'.");
        }

        // 4. Asignar el gerente
        sucursal.setGerente(gerente);

        // 5. Guardar el cambio. La sesión se cierra SOLA al final del método.
        return sucursalRepository.save(sucursal);
    }
}