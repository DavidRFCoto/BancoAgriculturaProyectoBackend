package com.bancoagricultura.bancobackend.util;

import com.bancoagricultura.bancobackend.repository.CuentaRepository;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Generador de numeros de cuenta unicos.
 * - Prefijo configurable.
 * - Genera hasta encontrar numero no usado
 */
@Component
public class NumeroCuentaGenerator {

    private static final String PREFIJO = "BAC-";
    private static final Random RANDOM = new Random();
    private final CuentaRepository cuentaRepository;

    public NumeroCuentaGenerator(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Genera un numero de cuenta unico consultando la BD.
     *
     * @return numero de cuenta (ej: BAC-123456)
     */
    public String generarNumeroCuentaUnico() {
        String numero;
        boolean existe;
        do {
            int valor = 100000 + RANDOM.nextInt(900000); // 6 dígitos
            numero = PREFIJO + valor;
            existe = cuentaRepository.findByNumeroCuenta(numero).isPresent();
        } while (existe);
        return numero;
    }
}

