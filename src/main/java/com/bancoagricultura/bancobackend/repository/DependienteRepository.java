package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Dependiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DependienteRepository extends JpaRepository<Dependiente, Integer> {
    
    @Query("SELECT d FROM Dependiente d JOIN FETCH d.rol WHERE d.usuario = :usuario AND d.estado = 'activo'")
    Optional<Dependiente> findByUsuario(@Param("usuario") String usuario);
}
