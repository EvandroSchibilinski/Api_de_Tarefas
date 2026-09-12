package com.schibilinski.projeto.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringCodificadorSenha
        implements CodificadorSenha {

    private final PasswordEncoder passwordEncoder;

    public SpringCodificadorSenha(
        PasswordEncoder passwordEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String codificar(String senha) {
        return passwordEncoder.encode(senha);
    }

    @Override
    public boolean corresponde(
        String senhaOriginal,
        String senhaHash
    ) {
        return passwordEncoder.matches(
            senhaOriginal,
            senhaHash
        );
    }
}