package com.schibilinski.projeto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schibilinski.projeto.entity.Usuario;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
    
    boolean existsByEmail(String email); // Verifica se o e-mail já está cadastrado. Será usado no cadastro:
    
    Optional<Usuario> findByEmail(String email);

}
