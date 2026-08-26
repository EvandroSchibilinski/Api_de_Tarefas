package com.schibilinski.projeto.dto;

import java.time.LocalDate;

import com.schibilinski.projeto.entity.Tarefa;
import com.schibilinski.projeto.entity.Tarefa.StatusTarefa;

public class TarefaDto {

    private Long id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private LocalDate dataLimite;
    private LocalDate criadaEm;

    public TarefaDto() {}

    public TarefaDto(Long id, String titulo, String descricao, StatusTarefa status,
                     LocalDate dataLimite, LocalDate criadaEm) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.dataLimite = dataLimite;
        this.criadaEm = criadaEm;
    }

    public static TarefaDto fromEntity(Tarefa tarefa) {
        if (tarefa == null) return null;
        return new TarefaDto(
            tarefa.getId(),
            tarefa.getTitulo(),
            tarefa.getDescricao(),
            tarefa.getStatus(),
            tarefa.getDataLimite(),
            tarefa.getCriadaEm()
        );
    }

    public Tarefa toEntity() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(this.titulo);
        tarefa.setDescricao(this.descricao);
        tarefa.setDataLimite(this.dataLimite);
        return tarefa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }

    public LocalDate getDataLimite() { return dataLimite; }
    public void setDataLimite(LocalDate dataLimite) { this.dataLimite = dataLimite; }

    public LocalDate getCriadaEm() { return criadaEm; }
    public void setCriadaEm(LocalDate criadaEm) { this.criadaEm = criadaEm; }
}
