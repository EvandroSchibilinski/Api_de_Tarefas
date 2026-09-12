package com.schibilinski.projeto.service;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schibilinski.projeto.dto.request.CadastrarUsuarioRequest;
import com.schibilinski.projeto.dto.response.UsuarioResponse;
import com.schibilinski.projeto.entity.Usuario;
import com.schibilinski.projeto.exception.EmailJaCadastradoException;
import com.schibilinski.projeto.mapper.UsuarioMapper;
import com.schibilinski.projeto.repository.UsuarioRepository;
import com.schibilinski.projeto.security.CodificadorSenha;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final CodificadorSenha codificadorSenha;

    public UsuarioService(
        UsuarioRepository usuarioRepository,
        UsuarioMapper usuarioMapper,
        CodificadorSenha codificadorSenha
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.codificadorSenha = codificadorSenha;
    }

    @Transactional
    public UsuarioResponse cadastrar(
        CadastrarUsuarioRequest request
    ) {
        String emailNormalizado =
            normalizarEmail(request.email());

        verificarEmailDisponivel(emailNormalizado);

        String senhaHash = codificadorSenha.codificar(
            request.senha()
        );

        Usuario usuario = new Usuario(
            request.nome(),
            emailNormalizado,
            senhaHash
        );

        Usuario usuarioSalvo =
            usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuarioSalvo);
    }

    private void verificarEmailDisponivel(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailJaCadastradoException(
                "Este e-mail já está cadastrado"
            );
        }
    }

    private String normalizarEmail(String email) {
        return email
            .trim()
            .toLowerCase(Locale.ROOT);
    }
}