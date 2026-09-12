package com.schibilinski.projeto.dto.response;

import java.time.LocalDateTime;

public record UsuarioResponse(

    Long id,

    String nome,

    String email,

    boolean ativo,

    LocalDateTime criadoEm

) {
}