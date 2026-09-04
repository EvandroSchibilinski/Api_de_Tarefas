package com.schibilinski.projeto.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.schibilinski.projeto.exception.RegraDeNegocioException;
import com.schibilinski.projeto.exception.TransicaoStatusInvalidaException;

class TarefaTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 9, 4);

    @Test
    void deveCriarTarefaPendenteSemDataDeInicioFutura() {
        Tarefa tarefa = novaTarefa(null, HOJE.plusDays(2));

        assertEquals(StatusTarefa.PENDENTE, tarefa.getStatus());
        assertEquals("Estudar Spring", tarefa.getTitulo());
    }

    @Test
    void deveCriarTarefaAgendadaComInicioFuturo() {
        Tarefa tarefa = novaTarefa(HOJE.plusDays(1), HOJE.plusDays(2));

        assertEquals(StatusTarefa.AGENDADA, tarefa.getStatus());
    }

    @Test
    void deveImpedirDataLimiteAnteriorAoInicio() {
        assertThrows(
            RegraDeNegocioException.class,
            () -> novaTarefa(HOJE.plusDays(2), HOJE.plusDays(1))
        );
    }

    @Test
    void deveExecutarFluxoPendenteAteConcluida() {
        Tarefa tarefa = novaTarefa(null, HOJE.plusDays(2));

        tarefa.iniciar();
        assertEquals(StatusTarefa.EM_ANDAMENTO, tarefa.getStatus());

        tarefa.concluir();
        assertEquals(StatusTarefa.CONCLUIDA, tarefa.getStatus());
    }

    @Test
    void deveImpedirConclusaoDiretaDeTarefaPendente() {
        Tarefa tarefa = novaTarefa(null, HOJE.plusDays(2));

        assertThrows(TransicaoStatusInvalidaException.class, tarefa::concluir);
    }

    @Test
    void deveIdentificarAtrasoSomenteEnquantoNaoConcluida() {
        Tarefa tarefa = novaTarefa(null, HOJE.minusDays(1));

        assertTrue(tarefa.estaAtrasada(HOJE));

        tarefa.iniciar();
        tarefa.concluir();

        assertFalse(tarefa.estaAtrasada(HOJE));
    }

    private Tarefa novaTarefa(LocalDate inicio, LocalDate limite) {
        return new Tarefa(
            "  Estudar Spring  ",
            "Domínio e persistência",
            inicio,
            limite,
            HOJE
        );
    }
}
