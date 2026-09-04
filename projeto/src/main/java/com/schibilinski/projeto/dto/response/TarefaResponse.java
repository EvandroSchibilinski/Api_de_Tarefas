package com.schibilinski.projeto.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.schibilinski.projeto.entity.StatusTarefa;

public record TarefaResponse(

    Long id,

    String titulo,

    String descricao,

    StatusTarefa status,

    LocalDate dataInicio,

    LocalDate dataLimite,

    LocalDateTime criadaEm,

    LocalDateTime atualizadaEm,

    Long versao,

    boolean atrasada

) {
}