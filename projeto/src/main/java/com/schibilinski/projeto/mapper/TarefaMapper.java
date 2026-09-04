package com.schibilinski.projeto.mapper;

import java.time.LocalDate;
import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.schibilinski.projeto.dto.request.CriarTarefaRequest;
import com.schibilinski.projeto.dto.response.TarefaResponse;
import com.schibilinski.projeto.entity.Tarefa;

@Mapper(componentModel = "spring")
public interface TarefaMapper {

    default Tarefa toEntity(CriarTarefaRequest request, @Context LocalDate hoje) {
        if (request == null) {
            return null;
        }

        return new Tarefa(
            request.titulo(),
            request.descricao(),
            request.dataInicio(),
            request.dataLimite(),
            hoje
        );
    }

    @Mapping(target = "atrasada", expression = "java(tarefa.estaAtrasada(hoje))")
    TarefaResponse toResponse(Tarefa tarefa, @Context LocalDate hoje);

    List<TarefaResponse> toResponseList(List<Tarefa> tarefas, @Context LocalDate hoje);
}
