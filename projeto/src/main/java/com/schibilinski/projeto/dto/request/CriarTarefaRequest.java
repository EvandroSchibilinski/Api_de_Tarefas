package com.schibilinski.projeto.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarTarefaRequest(

    @NotBlank(message = "O título é obrigatório")
    @Size(
        max = 150,
        message = "O título deve possuir no máximo 150 caracteres"
    )
    String titulo,

    @Size(
        max = 1000,
        message = "A descrição deve possuir no máximo 1000 caracteres"
    )
    String descricao,

    LocalDate dataInicio,

    LocalDate dataLimite

) {
}
