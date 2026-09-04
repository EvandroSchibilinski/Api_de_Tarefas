package com.schibilinski.projeto.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schibilinski.projeto.dto.request.AlterarStatusRequest;
import com.schibilinski.projeto.dto.request.AtualizarTarefaRequest;
import com.schibilinski.projeto.dto.request.CriarTarefaRequest;
import com.schibilinski.projeto.dto.response.PaginaResponse;
import com.schibilinski.projeto.dto.response.TarefaResponse;
import com.schibilinski.projeto.entity.StatusTarefa;
import com.schibilinski.projeto.service.TarefaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaResponse> criar(
        @Valid @RequestBody CriarTarefaRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaService.criar(request));
    }

    @GetMapping
    public ResponseEntity<PaginaResponse<TarefaResponse>> listarTodas(
        @PageableDefault(size = 20, sort = "criadaEm", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(tarefaService.listarTodas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> buscarPorId(
        @PathVariable @Positive(message = "O ID deve ser positivo") Long id
    ) {
        return ResponseEntity.ok(tarefaService.buscarPorId(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<PaginaResponse<TarefaResponse>> listarPorStatus(
        @PathVariable StatusTarefa status,
        @PageableDefault(size = 20, sort = "criadaEm", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(tarefaService.listarPorStatus(status, pageable));
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<PaginaResponse<TarefaResponse>> listarAtrasadas(
        @PageableDefault(size = 20, sort = "dataLimite", direction = Sort.Direction.ASC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(tarefaService.listarAtrasadas(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<PaginaResponse<TarefaResponse>> buscarPorTitulo(
        @RequestParam
        @NotBlank(message = "O título da busca é obrigatório")
        @Size(max = 150, message = "A busca deve possuir no máximo 150 caracteres")
        String titulo,
        @PageableDefault(size = 20, sort = "criadaEm", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(tarefaService.buscarPorTitulo(titulo, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizar(
        @PathVariable @Positive(message = "O ID deve ser positivo") Long id,
        @Valid @RequestBody AtualizarTarefaRequest request
    ) {
        return ResponseEntity.ok(tarefaService.atualizar(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaResponse> atualizarStatus(
        @PathVariable @Positive(message = "O ID deve ser positivo") Long id,
        @Valid @RequestBody AlterarStatusRequest request
    ) {
        return ResponseEntity.ok(tarefaService.atualizarStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
        @PathVariable @Positive(message = "O ID deve ser positivo") Long id
    ) {
        tarefaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
