package com.schibilinski.projeto.security;

public interface CodificadorSenha {
    String codificar(String senha);

    boolean corresponde(
        String senhaOriginal,
        String senhaHash
    );

}
