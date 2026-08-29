package com.schibilinski.projeto.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarefas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String titulo;

    @Size(max = 1000)
    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTarefa status;

    private LocalDate dataLimite;

    @Column(nullable = false, updatable = false)
    private LocalDate criadaEm;

    public Tarefa(
            String titulo,
            String descricao,
            LocalDate dataLimite
    ) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataLimite = dataLimite;
        this.status = StatusTarefa.PENDENTE;
    }

    @PrePersist
    private void definirDataCriacao() {
        if (this.criadaEm == null) {
            this.criadaEm = LocalDate.now();
        }
    }

    public void atualizarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException(
                    "O título é obrigatório"
            );
        }

        if (titulo.length() > 150) {
            throw new IllegalArgumentException(
                    "O título deve possuir no máximo 150 caracteres"
            );
        }

        this.titulo = titulo;
    }

    public void atualizarDescricao(String descricao) {
        if (descricao != null && descricao.length() > 1000) {
            throw new IllegalArgumentException(
                    "A descrição deve possuir no máximo 1000 caracteres"
            );
        }

        this.descricao = descricao;
    }

    public void atualizarDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public void iniciar() {
        if (this.status != StatusTarefa.PENDENTE) {
            throw new IllegalStateException(
                    "Apenas tarefas pendentes podem ser iniciadas"
            );
        }

        this.status = StatusTarefa.EM_ANDAMENTO;
    }

    public void concluir() {
        if (this.status != StatusTarefa.EM_ANDAMENTO) {
            throw new IllegalStateException(
                    "Apenas tarefas em andamento podem ser concluídas"
            );
        }

        this.status = StatusTarefa.CONCLUIDA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Tarefa outra)) {
            return false;
        }

        return id != null && id.equals(outra.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum StatusTarefa {
        PENDENTE,
        EM_ANDAMENTO,
        CONCLUIDA
    }
}