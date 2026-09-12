package com.schibilinski.projeto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.schibilinski.projeto.dto.response.UsuarioResponse;
import com.schibilinski.projeto.entity.Usuario;

@Mapper(
    componentModel ="spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR  
)

public interface UsuarioMapper {
    UsuarioResponse toResponse(Usuario usuario);

}
