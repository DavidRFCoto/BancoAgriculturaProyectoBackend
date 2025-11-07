package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.Dependiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DependienteRepository extends JpaRepository<Dependiente, Integer> {
}
