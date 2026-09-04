package com.schibilinski.projeto.dto.request;

import com.schibilinski.projeto.entity.StatusTarefa;

import jakarta.validation.constraints.NotNull;

public record AlterarStatusRequest(

    @NotNull(message = "O novo status é obrigatório")
    StatusTarefa status

) {
}