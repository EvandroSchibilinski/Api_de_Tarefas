package com.schibilinski.projeto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schibilinski.projeto.entity.Tarefa;
import com.schibilinski.projeto.entity.Tarefa.StatusTarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);

    List<Tarefa> findByDataLimiteBefore(LocalDate data);

    List<Tarefa> findByStatus(StatusTarefa status);
}
