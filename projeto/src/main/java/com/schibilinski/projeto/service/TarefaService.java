package com.schibilinski.projeto.service;

import java.time.Clock;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schibilinski.projeto.dto.request.AlterarStatusRequest;
import com.schibilinski.projeto.dto.request.AtualizarTarefaRequest;
import com.schibilinski.projeto.dto.request.CriarTarefaRequest;
import com.schibilinski.projeto.dto.response.PaginaResponse;
import com.schibilinski.projeto.dto.response.TarefaResponse;
import com.schibilinski.projeto.entity.StatusTarefa;
import com.schibilinski.projeto.entity.Tarefa;
import com.schibilinski.projeto.exception.RegraDeNegocioException;
import com.schibilinski.projeto.exception.TransicaoStatusInvalidaException;
import com.schibilinski.projeto.mapper.TarefaMapper;
import com.schibilinski.projeto.repository.TarefaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TarefaMapper tarefaMapper;
    private final Clock clock;

    public TarefaService(
        TarefaRepository tarefaRepository,
        TarefaMapper tarefaMapper,
        Clock clock
    ) {
        this.tarefaRepository = tarefaRepository;
        this.tarefaMapper = tarefaMapper;
        this.clock = clock;
    }

    @Transactional
    public TarefaResponse criar(CriarTarefaRequest request) {
        LocalDate hoje = hoje();
        Tarefa tarefa = tarefaMapper.toEntity(request, hoje);
        Tarefa salva = tarefaRepository.save(tarefa);
        return tarefaMapper.toResponse(salva, hoje);
    }

    @Transactional(readOnly = true)
    public PaginaResponse<TarefaResponse> listarTodas(Pageable pageable) {
        return mapearPagina(tarefaRepository.findAll(pageable));
    }

    @Transactional(readOnly = true)
    public TarefaResponse buscarPorId(Long id) {
        return tarefaMapper.toResponse(buscarEntidade(id), hoje());
    }

    @Transactional(readOnly = true)
    public PaginaResponse<TarefaResponse> listarPorStatus(
        StatusTarefa status,
        Pageable pageable
    ) {
        return mapearPagina(tarefaRepository.findByStatus(status, pageable));
    }

    @Transactional(readOnly = true)
    public PaginaResponse<TarefaResponse> listarAtrasadas(Pageable pageable) {
        Page<Tarefa> pagina = tarefaRepository.findByDataLimiteBeforeAndStatusNot(
            hoje(),
            StatusTarefa.CONCLUIDA,
            pageable
        );
        return mapearPagina(pagina);
    }

    @Transactional(readOnly = true)
    public PaginaResponse<TarefaResponse> buscarPorTitulo(
        String titulo,
        Pageable pageable
    ) {
        String termo = titulo == null ? "" : titulo.trim();
        if (termo.isBlank()) {
            throw new RegraDeNegocioException("O título da busca é obrigatório");
        }
        return mapearPagina(
            tarefaRepository.findByTituloContainingIgnoreCase(termo, pageable)
        );
    }

    @Transactional
    public TarefaResponse atualizar(Long id, AtualizarTarefaRequest request) {
        LocalDate hoje = hoje();
        Tarefa tarefa = buscarEntidade(id);
        tarefa.atualizarInformacoes(
            request.titulo(),
            request.descricao(),
            request.dataInicio(),
            request.dataLimite(),
            hoje
        );
        return tarefaMapper.toResponse(tarefa, hoje);
    }

    @Transactional
    public TarefaResponse atualizarStatus(Long id, AlterarStatusRequest request) {
        LocalDate hoje = hoje();
        Tarefa tarefa = buscarEntidade(id);

        if (request.status() == tarefa.getStatus()) {
            return tarefaMapper.toResponse(tarefa, hoje);
        }

        switch (request.status()) {
            case PENDENTE -> tarefa.liberarParaExecucao(hoje);
            case EM_ANDAMENTO -> tarefa.iniciar();
            case CONCLUIDA -> tarefa.concluir();
            case AGENDADA -> throw new TransicaoStatusInvalidaException(
                "Use a edição das datas para agendar uma tarefa"
            );
        }

        return tarefaMapper.toResponse(tarefa, hoje);
    }

    @Transactional
    public void excluir(Long id) {
        Tarefa tarefa = buscarEntidade(id);
        tarefaRepository.delete(tarefa);
    }

    private Tarefa buscarEntidade(Long id) {
        return tarefaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Tarefa não encontrada com id: " + id
            ));
    }

    private PaginaResponse<TarefaResponse> mapearPagina(Page<Tarefa> pagina) {
        LocalDate hoje = hoje();
        return PaginaResponse.from(
            pagina.map(tarefa -> tarefaMapper.toResponse(tarefa, hoje))
        );
    }

    private LocalDate hoje() {
        return LocalDate.now(clock);
    }
}
