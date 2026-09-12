package com.schibilinski.projeto.service;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schibilinski.projeto.dto.request.LoginRequest;
import com.schibilinski.projeto.dto.response.LoginResponse;
import com.schibilinski.projeto.dto.response.UsuarioResponse;
import com.schibilinski.projeto.entity.Usuario;
import com.schibilinski.projeto.exception.CredenciaisInvalidasException;
import com.schibilinski.projeto.mapper.UsuarioMapper;
import com.schibilinski.projeto.repository.UsuarioRepository;
import com.schibilinski.projeto.security.CodificadorSenha;
import com.schibilinski.projeto.security.GeradorToken;
import com.schibilinski.projeto.security.TokenAcesso;

@Service
public class AutenticacaoService {

    private static final String TIPO_TOKEN = "Bearer";

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final CodificadorSenha codificadorSenha;
    private final GeradorToken geradorToken;

    public AutenticacaoService(
        UsuarioRepository usuarioRepository,
        UsuarioMapper usuarioMapper,
        CodificadorSenha codificadorSenha,
        GeradorToken geradorToken
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.codificadorSenha = codificadorSenha;
        this.geradorToken = geradorToken;
    }

    @Transactional(readOnly = true)
    public LoginResponse autenticar(LoginRequest request) {
        String email = normalizarEmail(request.email());

        Usuario usuario = usuarioRepository
            .findByEmail(email)
            .orElseThrow(CredenciaisInvalidasException::new);

        validarUsuario(usuario, request.senha());

        TokenAcesso token = geradorToken.gerar(usuario);

        UsuarioResponse usuarioResponse =
            usuarioMapper.toResponse(usuario);

        return new LoginResponse(
            token.valor(),
            TIPO_TOKEN,
            token.expiraEm(),
            usuarioResponse
        );
    }

    private void validarUsuario(
        Usuario usuario,
        String senhaOriginal
    ) {
        if (!usuario.isAtivo()) {
            throw new CredenciaisInvalidasException();
        }

        boolean senhaCorreta =
            codificadorSenha.corresponde(
                senhaOriginal,
                usuario.getSenhaHash()
            );

        if (!senhaCorreta) {
            throw new CredenciaisInvalidasException();
        }
    }

    private String normalizarEmail(String email) {
        return email
            .trim()
            .toLowerCase(Locale.ROOT);
    }
}