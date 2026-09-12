package com.schibilinski.projeto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.schibilinski.projeto.dto.request.CadastrarUsuarioRequest;
import com.schibilinski.projeto.dto.response.UsuarioResponse;
import com.schibilinski.projeto.entity.Usuario;
import com.schibilinski.projeto.exception.EmailJaCadastradoException;
import com.schibilinski.projeto.mapper.UsuarioMapper;
import com.schibilinski.projeto.repository.UsuarioRepository;
import com.schibilinski.projeto.security.CodificadorSenha;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private CodificadorSenha codificadorSenha;

    private UsuarioService usuarioService;

    @BeforeEach
    @SuppressWarnings("unused")
    void configurar() {
        MockitoAnnotations.openMocks(this);

        usuarioService = new UsuarioService(
            usuarioRepository,
            usuarioMapper,
            codificadorSenha
        );
    }

    @Test
    void deveCadastrarUsuario() {
        CadastrarUsuarioRequest request =
            new CadastrarUsuarioRequest(
                "Evandro",
                "  EVANDRO@EMAIL.COM  ",
                "Senha123"
            );

        UsuarioResponse responseEsperado =
            new UsuarioResponse(
                null,
                "Evandro",
                "evandro@email.com",
                true,
                null
            );

        when(
            usuarioRepository.existsByEmail(
                "evandro@email.com"
            )
        ).thenReturn(false);

        when(
            codificadorSenha.codificar("Senha123")
        ).thenReturn("senha-protegida");

        when(
            usuarioRepository.save(any(Usuario.class))
        ).thenAnswer(invocacao ->
            invocacao.getArgument(0)
        );

        when(
            usuarioMapper.toResponse(any(Usuario.class))
        ).thenReturn(responseEsperado);

        UsuarioResponse resultado =
            usuarioService.cadastrar(request);

        assertEquals(responseEsperado, resultado);

        ArgumentCaptor<Usuario> captor =
            ArgumentCaptor.forClass(Usuario.class);

        verify(usuarioRepository).save(captor.capture());

        Usuario usuarioSalvo = captor.getValue();

        assertEquals(
            "evandro@email.com",
            usuarioSalvo.getEmail()
        );

        assertEquals(
            "senha-protegida",
            usuarioSalvo.getSenhaHash()
        );
    }

    @Test
    void deveImpedirEmailDuplicado() {
        CadastrarUsuarioRequest request =
            new CadastrarUsuarioRequest(
                "Evandro",
                "evandro@email.com",
                "Senha123"
            );

        when(
            usuarioRepository.existsByEmail(
                "evandro@email.com"
            )
        ).thenReturn(true);
        
        assertThrows(
            EmailJaCadastradoException.class,
            () -> usuarioService.cadastrar(request)
        );

        verify(codificadorSenha, never())
            .codificar(any());

        verify(usuarioRepository, never())
            .save(any());
    }
}