package com.schibilinski.projeto.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.schibilinski.projeto.exception.RegraDeNegocioException;
import com.schibilinski.projeto.exception.TransicaoStatusInvalidaException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tarefas")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tarefa {

    private static final int TAMANHO_MAXIMO_TITULO = 150;
    private static final int TAMANHO_MAXIMO_DESCRICAO = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        nullable = false,
        length = TAMANHO_MAXIMO_TITULO
    )
    private String titulo;

    @Column(length = TAMANHO_MAXIMO_DESCRICAO)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusTarefa status;

    /*
     * Data em que a tarefa ficará disponível para execução.
     * Se estiver no futuro, a tarefa começa como AGENDADA.
     */
    private LocalDate dataInicio;

    /*
     * Prazo máximo para conclusão.
     */
    private LocalDate dataLimite;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm;

    @Column(nullable = false)
    private LocalDateTime atualizadaEm;

    /*
     * Evita que duas atualizações concorrentes sobrescrevam
     * silenciosamente os dados uma da outra.
     */
    @Version
    private Long versao;

    /*
     * Construtor utilizado para criar uma nova tarefa.
     *
     * O parâmetro "hoje" é recebido para que o comportamento
     * possa ser testado sem depender diretamente de LocalDate.now().
     */
    public Tarefa(
        String titulo,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataLimite,
        LocalDate hoje
    ) {
        validarHoje(hoje);
        validarTitulo(titulo);
        validarDescricao(descricao);
        validarDatas(dataInicio, dataLimite);

        this.titulo = normalizarTitulo(titulo);
        this.descricao = normalizarDescricao(descricao);
        this.dataInicio = dataInicio;
        this.dataLimite = dataLimite;
        this.status = determinarStatusInicial(dataInicio, hoje);
    }

    /*
     * Atualiza os dados editáveis da tarefa.
     *
     * Não recebe status porque a alteração de status possui
     * métodos específicos: iniciar(), concluir(), agendar() etc.
     */
    public void atualizarInformacoes(
        String titulo,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataLimite,
        LocalDate hoje
    ) {
        validarHoje(hoje);
        garantirQuePodeSerEditada();
        validarTitulo(titulo);
        validarDescricao(descricao);
        validarDatas(dataInicio, dataLimite);
        validarDataInicioDuranteExecucao(dataInicio, hoje);

        this.titulo = normalizarTitulo(titulo);
        this.descricao = normalizarDescricao(descricao);
        this.dataInicio = dataInicio;
        this.dataLimite = dataLimite;

        atualizarAgendamentoSeNecessario(hoje);
    }

    /*
     * Agenda ou reagenda uma tarefa.
     */
    public void agendar(
        LocalDate novaDataInicio,
        LocalDate novaDataLimite,
        LocalDate hoje
    ) {
        validarHoje(hoje);
        garantirQuePodeSerEditada();
        validarDataDeAgendamento(novaDataInicio, hoje);
        validarDatas(novaDataInicio, novaDataLimite);

        this.dataInicio = novaDataInicio;
        this.dataLimite = novaDataLimite;
        this.status = StatusTarefa.AGENDADA;
    }

    /*
     * Libera uma tarefa quando sua data de início chega.
     */
    public void liberarParaExecucao(LocalDate hoje) {
        validarHoje(hoje);

        if (status != StatusTarefa.AGENDADA) {
            throw new TransicaoStatusInvalidaException(
                "Somente tarefas agendadas podem ser liberadas"
            );
        }

        if (dataInicio != null && dataInicio.isAfter(hoje)) {
            throw new TransicaoStatusInvalidaException(
                "A tarefa ainda está agendada para uma data futura"
            );
        }

        this.status = StatusTarefa.PENDENTE;
    }

    /*
     * Inicia uma tarefa pendente.
     */
    public void iniciar() {
        if (status != StatusTarefa.PENDENTE) {
            throw new TransicaoStatusInvalidaException(
                "Somente tarefas pendentes podem ser iniciadas"
            );
        }

        this.status = StatusTarefa.EM_ANDAMENTO;
    }

    /*
     * Conclui uma tarefa que esteja em andamento.
     */
    public void concluir() {
        if (status != StatusTarefa.EM_ANDAMENTO) {
            throw new TransicaoStatusInvalidaException(
                "Somente tarefas em andamento podem ser concluídas"
            );
        }

        this.status = StatusTarefa.CONCLUIDA;
    }

    /*
     * Regra de consulta do domínio.
     * Não precisa ser persistida no banco.
     */
    public boolean estaAtrasada(LocalDate hoje) {
        validarHoje(hoje);

        return dataLimite != null
            && dataLimite.isBefore(hoje)
            && status != StatusTarefa.CONCLUIDA;
    }

    /*
     * Indica se a tarefa já chegou a um estado final.
     */
    public boolean estaConcluida() {
        return status == StatusTarefa.CONCLUIDA;
    }

    /*
     * Define AGENDADA quando a data de início está no futuro.
     * Caso contrário, a tarefa começa como PENDENTE.
     */
    private StatusTarefa determinarStatusInicial(
        LocalDate dataInicio,
        LocalDate hoje
    ) {
        if (dataInicio != null && dataInicio.isAfter(hoje)) {
            return StatusTarefa.AGENDADA;
        }

        return StatusTarefa.PENDENTE;
    }

    /*
     * Quando uma tarefa AGENDADA ou PENDENTE é editada,
     * recalcula o estado a partir da data de início.
     *
     * Não altera tarefas que já estão em andamento.
     */
    private void atualizarAgendamentoSeNecessario(LocalDate hoje) {
        if (
            status == StatusTarefa.AGENDADA
                || status == StatusTarefa.PENDENTE
        ) {
            this.status = determinarStatusInicial(dataInicio, hoje);
        }
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new RegraDeNegocioException(
                "O título é obrigatório"
            );
        }

        if (titulo.trim().length() > TAMANHO_MAXIMO_TITULO) {
            throw new RegraDeNegocioException(
                "O título deve possuir no máximo "
                    + TAMANHO_MAXIMO_TITULO
                    + " caracteres"
            );
        }
    }

    private void validarDescricao(String descricao) {
        if (
            descricao != null
                && descricao.trim().length()
                > TAMANHO_MAXIMO_DESCRICAO
        ) {
            throw new RegraDeNegocioException(
                "A descrição deve possuir no máximo "
                    + TAMANHO_MAXIMO_DESCRICAO
                    + " caracteres"
            );
        }
    }

    private void validarDatas(
        LocalDate dataInicio,
        LocalDate dataLimite
    ) {
        if (
            dataInicio != null
                && dataLimite != null
                && dataLimite.isBefore(dataInicio)
        ) {
            throw new RegraDeNegocioException(
                "A data limite não pode ser anterior à data de início"
            );
        }
    }

    private void validarDataDeAgendamento(
        LocalDate dataInicio,
        LocalDate hoje
    ) {
        if (dataInicio == null) {
            throw new RegraDeNegocioException(
                "A data de início é obrigatória para agendar uma tarefa"
            );
        }

        if (!dataInicio.isAfter(hoje)) {
            throw new RegraDeNegocioException(
                "A data de início do agendamento deve estar no futuro"
            );
        }
    }

    private void validarDataInicioDuranteExecucao(
        LocalDate novaDataInicio,
        LocalDate hoje
    ) {
        if (
            status == StatusTarefa.EM_ANDAMENTO
                && novaDataInicio != null
                && novaDataInicio.isAfter(hoje)
        ) {
            throw new RegraDeNegocioException(
                "Uma tarefa em andamento não pode ter início no futuro"
            );
        }
    }

    private void validarHoje(LocalDate hoje) {
        if (hoje == null) {
            throw new IllegalArgumentException(
                "A data atual é obrigatória"
            );
        }
    }

    private void garantirQuePodeSerEditada() {
        if (status == StatusTarefa.CONCLUIDA) {
            throw new RegraDeNegocioException(
                "Uma tarefa concluída não pode ser editada"
            );
        }
    }

    private String normalizarTitulo(String titulo) {
        return titulo.trim();
    }

    private String normalizarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return null;
        }

        return descricao.trim();
    }

    @PrePersist
    @SuppressWarnings("unused")
    private void antesDePersistir() {
        LocalDateTime agora = LocalDateTime.now();

        this.criadaEm = agora;
        this.atualizadaEm = agora;
    }

    @PreUpdate
    @SuppressWarnings("unused")
    private void antesDeAtualizar() {
        this.atualizadaEm = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Tarefa outra)) {
            return false;
        }

        return id != null && id.equals(outra.id);
    }

    @Override
    public int hashCode() {
        /*
         * O hash não depende do ID, pois ele é nulo antes da
         * persistência e recebe um valor depois do save().
         */
        return getClass().hashCode();
    }
}
