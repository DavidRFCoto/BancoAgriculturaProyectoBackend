package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    @Query("SELECT e FROM Empleado e JOIN FETCH e.rol WHERE e.id = :id")
    Optional<Empleado> findByIdWithRol(@Param("id") Integer id);
}