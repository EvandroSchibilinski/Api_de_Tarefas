package com.schibilinski.projeto.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.schibilinski.projeto.dto.TarefaDto;
import com.schibilinski.projeto.entity.Tarefa.StatusTarefa;
import com.schibilinski.projeto.service.TarefaService;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<TarefaDto> criar(@RequestBody TarefaDto dto) {
        TarefaDto criada = TarefaDto.fromEntity(tarefaService.criar(dto.toEntity()));
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<TarefaDto>> listarTodas() {
        List<TarefaDto> lista = tarefaService.listarTodas()
                .stream()
                .map(TarefaDto::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(TarefaDto.fromEntity(tarefaService.buscarPorId(id)));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TarefaDto>> listarPorStatus(@PathVariable StatusTarefa status) {
        List<TarefaDto> lista = tarefaService.listarPorStatus(status)
                .stream()
                .map(TarefaDto::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/atrasadas")
    public ResponseEntity<List<TarefaDto>> listarAtrasadas() {
        List<TarefaDto> lista = tarefaService.listarAtrasadas()
                .stream()
                .map(TarefaDto::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TarefaDto>> buscarPorTitulo(@RequestParam String titulo) {
        List<TarefaDto> lista = tarefaService.buscarPorTitulo(titulo)
                .stream()
                .map(TarefaDto::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaDto> atualizar(@PathVariable Long id, @RequestBody TarefaDto dto) {
        return ResponseEntity.ok(TarefaDto.fromEntity(tarefaService.atualizar(id, dto.toEntity())));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaDto> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusTarefa status) {
        return ResponseEntity.ok(TarefaDto.fromEntity(tarefaService.atualizarStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
