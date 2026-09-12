package com.schibilinski.projeto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schibilinski.projeto.dto.request.CadastrarUsuarioRequest;
import com.schibilinski.projeto.dto.response.UsuarioResponse;
import com.schibilinski.projeto.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(
        @Valid @RequestBody CadastrarUsuarioRequest request
    ) {
        UsuarioResponse response = usuarioService.cadastrar(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}






