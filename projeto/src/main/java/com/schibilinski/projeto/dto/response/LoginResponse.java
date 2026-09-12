package com.schibilinski.projeto.dto.response;

import java.time.Instant;

public record LoginResponse(

    String token,

    String tipo,

    Instant expiraEm,

    UsuarioResponse usuario

) {
}