package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.AccionPersonal;
import java.util.List;

// Interfaz para la logica de negocio de Acciones de Personal
public interface AccionPersonalService {

    // Metodo para que el Gerente General vea las pendientes
    List<AccionPersonal> findAccionesPendientes();

    //  Metodo para aprobar una solicitud
    AccionPersonal aprobarAccion(Integer accionId, Integer gerenteGeneralId);

    // Metodo para rechazar una solicitud
    AccionPersonal rechazarAccion(Integer accionId, Integer gerenteGeneralId);
}