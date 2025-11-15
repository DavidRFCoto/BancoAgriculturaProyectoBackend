package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.CasoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasoPrestamoRepository extends JpaRepository<CasoPrestamo, Integer> {
}