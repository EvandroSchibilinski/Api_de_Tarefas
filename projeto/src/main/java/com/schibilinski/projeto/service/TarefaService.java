package com.schibilinski.projeto.service;

import com.schibilinski.projeto.entity.Tarefa;
import com.schibilinski.projeto.entity.Tarefa.StatusTarefa;
import com.schibilinski.projeto.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public Tarefa criar(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().isBlank()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }

        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setCriadaEm(LocalDate.now());

        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Tarefa não encontrada com id: " + id));
    }

    public List<Tarefa> listarPorStatus(StatusTarefa status) {
        return tarefaRepository.findByStatus(status);
    }

    public List<Tarefa> listarAtrasadas() {
        return tarefaRepository.findByDataLimiteBefore(LocalDate.now());
    }

    public List<Tarefa> buscarPorTitulo(String titulo) {
        return tarefaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public Tarefa atualizar(Long id, Tarefa dadosAtualizados) {
        Tarefa tarefa = buscarPorId(id);

        tarefa.setTitulo(dadosAtualizados.getTitulo());
        tarefa.setDescricao(dadosAtualizados.getDescricao());
        tarefa.setDataLimite(dadosAtualizados.getDataLimite());

        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizarStatus(Long id, StatusTarefa novoStatus) {
        Tarefa tarefa = buscarPorId(id);

        if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
            throw new IllegalStateException("Tarefa já concluída não pode mudar de status");
        }

        tarefa.setStatus(novoStatus);
        return tarefaRepository.save(tarefa);
    }

    public void excluir(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefaRepository.delete(tarefa);
    }
}
