package com.schibilinski.projeto.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.schibilinski.projeto.entity.StatusTarefa;
import com.schibilinski.projeto.entity.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    Page<Tarefa> findByStatus(
        StatusTarefa status,
        Pageable pageable
    );

    Page<Tarefa> findByTituloContainingIgnoreCase(
        String titulo,
        Pageable pageable
    );

    Page<Tarefa> findByDataLimiteBeforeAndStatusNot(
        LocalDate data,
        StatusTarefa status,
        Pageable pageable
    );

    Page<Tarefa> findByDataInicioLessThanEqualAndStatus(
        LocalDate dataInicio,
        StatusTarefa status,
        Pageable pageable
    );
}