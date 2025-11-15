package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.AccionPersonal;
import com.bancoagricultura.bancobackend.entity.Empleado;
import com.bancoagricultura.bancobackend.repository.AccionPersonalRepository;
import com.bancoagricultura.bancobackend.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccionPersonalServiceImpl implements AccionPersonalService {

    private final AccionPersonalRepository accionPersonalRepository;
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public AccionPersonalServiceImpl(AccionPersonalRepository accionPersonalRepository,
                                     EmpleadoRepository empleadoRepository) {
        this.accionPersonalRepository = accionPersonalRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    @Transactional(readOnly = true)

    public List<AccionPersonal> findAccionesPendientes() {
        //  Usamos el metodo con JOIN FETCH que ya creamos
        return accionPersonalRepository.findByEstadoWithDetails("pendiente");
    }

    @Override
    @Transactional // Transaccion completa de lectura Y escritura

    public AccionPersonal aprobarAccion(Integer accionId, Integer gerenteGeneralId) {
        // Buscar la accion pendiente
        AccionPersonal accion = accionPersonalRepository.findById(accionId)
                .orElseThrow(() -> new NoSuchElementException("Accion de Personal no encontrada: " + accionId));

        if (!accion.getEstado().equals("pendiente")) {
            throw new IllegalArgumentException("La accion ya fue procesada.");
        }

        //  Crear el nuevo Empleado con los datos de la accion
        Empleado nuevoEmpleado = new Empleado();
        nuevoEmpleado.setNombre(accion.getNombreNuevoEmpleado());
        nuevoEmpleado.setPuesto(accion.getPuestoNuevoEmpleado());
        nuevoEmpleado.setSalario(accion.getSalarioNuevoEmpleado());
        nuevoEmpleado.setRol(accion.getRolNuevoEmpleado());
        nuevoEmpleado.setEstado("activo"); // Lo creamos activo

        empleadoRepository.save(nuevoEmpleado);

        // Actualizar la accion a "aprobado"

        accion.setEstado("aprobado");

        // accion.setGerenteAprobadorId(gerenteGeneralId);
        return accionPersonalRepository.save(accion);
    }

    @Override
    @Transactional
    public AccionPersonal rechazarAccion(Integer accionId, Integer gerenteGeneralId) {
        // Buscar la accion pendiente
        AccionPersonal accion = accionPersonalRepository.findById(accionId)
                .orElseThrow(() -> new NoSuchElementException("Accion de Personal no encontrada: " + accionId));

        if (!accion.getEstado().equals("pendiente")) {
            throw new IllegalArgumentException("La accion ya fue procesada.");
        }

        //  Actualizar la accion a "rechazado"
        accion.setEstado("rechazado");
        return accionPersonalRepository.save(accion);
    }
}