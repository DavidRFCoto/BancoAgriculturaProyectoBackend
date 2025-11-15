package com.bancoagricultura.bancobackend.service;
import com.bancoagricultura.bancobackend.entity.Sucursal;

public interface SucursalService{
    Sucursal asignarGerenteASucursal(Integer sucursalId, Integer gerenteId);
}
