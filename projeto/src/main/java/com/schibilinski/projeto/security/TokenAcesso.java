package com.schibilinski.projeto.security;

import java.time.Instant;

public record TokenAcesso(

    String valor,

    Instant expiraEm

) {
}