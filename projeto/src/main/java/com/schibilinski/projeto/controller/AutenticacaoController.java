package com.schibilinski.projeto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schibilinski.projeto.dto.request.LoginRequest;
import com.schibilinski.projeto.dto.response.LoginResponse;
import com.schibilinski.projeto.service.AutenticacaoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

   
    private final AutenticacaoService autenticacaoService;

    
    public AutenticacaoController(
        AutenticacaoService autenticacaoService
    ) {
        this.autenticacaoService = autenticacaoService;
    }

    // Endpoint de login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> autenticar(
        @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response =
            autenticacaoService.autenticar(request);

        return ResponseEntity.ok(response);
    }
}
