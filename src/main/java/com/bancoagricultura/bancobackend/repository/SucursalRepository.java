package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {

    @Query("SELECT s FROM Sucursal s LEFT JOIN FETCH s.gerente WHERE s.id = :id")
    Optional<Sucursal> findByIdWithGerente(@Param("id") Integer id);
}