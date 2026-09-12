package com.schibilinski.projeto.entity;

import java.time.LocalDateTime;
import java.util.Locale;

import com.schibilinski.projeto.exception.RegraDeNegocioException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(
    name = "usuarios",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_usuarios_email",
            columnNames = "email"
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String senhaHash;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Version
    private Long versao;

    public Usuario(
        String nome,
        String email,
        String senhaHash
    ) {
        validarNome(nome);
        validarEmail(email);
        validarSenhaHash(senhaHash);

        this.nome = nome.trim(); // remove espaços no começo e no final.
        this.email = normalizarEmail(email);
        this.senhaHash = senhaHash;
        this.ativo = true;
    }

    @PrePersist //executa automaticamente antes do primeiro salvamento no banco
    @SuppressWarnings("unused")
    private void antesDePersistir() {
        this.criadoEm = LocalDateTime.now();
    }

    private void validarNome(String nome) { //impede nome nulo, vazio ou maior que 100 caracteres.
        if (nome == null || nome.isBlank()) {
            throw new RegraDeNegocioException(
                "O nome é obrigatório"
            );
        }

        if (nome.trim().length() > 100) { 
            throw new RegraDeNegocioException(
                "O nome deve possuir no máximo 100 caracteres"
            );
        }
    }

    private void validarEmail(String email) { //impede e-mail nulo, vazio ou maior que 150 caracteres.
        if (email == null || email.isBlank()) {
            throw new RegraDeNegocioException(
                "O e-mail é obrigatório"
            );
        }

        if (email.trim().length() > 150) {
            throw new RegraDeNegocioException(
                "O e-mail deve possuir no máximo 150 caracteres"
            );
        }
    }

    private void validarSenhaHash(String senhaHash) { //impede salvar usuário sem senha protegida.
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new RegraDeNegocioException(
                "A senha protegida é obrigatória"
            );
        }
    }

    private String normalizarEmail(String email) { //remove espaços e transforma o e-mail em minúsculas.
        return email.trim().toLowerCase(Locale.ROOT);
    }
}