package com.schibilinski.projeto.security;

import com.schibilinski.projeto.entity.Usuario;

public interface GeradorToken {

    TokenAcesso gerar(Usuario usuario);
}