package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.AccionPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccionPersonalRepository extends JpaRepository<AccionPersonal, Integer> {

    // Metodo para que el Gerente General vea las pendientes
    @Query("SELECT ap FROM AccionPersonal ap JOIN FETCH ap.rolNuevoEmpleado JOIN FETCH ap.gerenteSolicitante WHERE ap.estado = :estado")
    List<AccionPersonal> findByEstadoWithDetails(String estado);

    //Para buscar una sola accion
    @Query("SELECT ap FROM AccionPersonal ap JOIN FETCH ap.rolNuevoEmpleado JOIN FETCH ap.gerenteSolicitante WHERE ap.id = :id")
    Optional<AccionPersonal> findByIdWithDetails(@Param("id") Integer id);
}