package com.schibilinski.projeto.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

class SpringCodificadorSenhaTest {

    private final SpringCodificadorSenha codificador =
        new SpringCodificadorSenha(
            PasswordEncoderFactories
                .createDelegatingPasswordEncoder()
        );

    @Test
    void deveCodificarECompararSenha() {
        String senhaOriginal = "SenhaSegura123";

        String senhaHash =
            codificador.codificar(senhaOriginal);

        assertNotEquals(senhaOriginal, senhaHash);
        assertTrue(
            codificador.corresponde(
                senhaOriginal,
                senhaHash
            )
        );
        assertFalse(
            codificador.corresponde(
                "SenhaIncorreta",
                senhaHash
            )
        );
    }
}