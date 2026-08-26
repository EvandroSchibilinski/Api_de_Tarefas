const LABELS = {
  PENDENTE: 'Pendente',
  EM_ANDAMENTO: 'Em andamento',
  CONCLUIDA: 'Concluída',
};

function formatarData(data) {
  if (!data) return '';
  const [a, m, d] = data.split('-');
  return `${d}/${m}/${a}`;
}

export default function TarefaCard({ tarefa, onAlternarStatus, onEditar, onExcluir }) {
  const hoje = new Date().toISOString().split('T')[0];
  const atrasada = tarefa.dataLimite && tarefa.dataLimite < hoje && tarefa.status !== 'CONCLUIDA';
  const concluida = tarefa.status === 'CONCLUIDA';

  return (
    <div className={`tarefa-card ${concluida ? 'concluida' : ''}`}>
      <button
        className={`check-btn ${concluida ? 'done' : ''}`}
        onClick={() => onAlternarStatus(tarefa)}
        title="Alternar status"
      >
        {concluida && <i className="ti ti-check" aria-hidden="true"></i>}
      </button>

      <div className="tarefa-body">
        <div className={`tarefa-titulo ${concluida ? 'riscado' : ''}`}>{tarefa.titulo}</div>
        {tarefa.descricao && <div className="tarefa-desc">{tarefa.descricao}</div>}
        <div className="tarefa-meta">
          <span className={`badge ${tarefa.status}`}>{LABELS[tarefa.status]}</span>
          {tarefa.dataLimite && (
            <span className={`data-badge ${atrasada ? 'atrasada' : ''}`}>
              <i className="ti ti-calendar" aria-hidden="true"></i>
              {formatarData(tarefa.dataLimite)}
              {atrasada ? ' · atrasada' : ''}
            </span>
          )}
        </div>
      </div>

      <div className="tarefa-actions">
        <button className="icon-btn" onClick={() => onEditar(tarefa)} title="Editar">
          <i className="ti ti-edit" aria-hidden="true"></i>
        </button>
        <button className="icon-btn danger" onClick={() => onExcluir(tarefa.id)} title="Excluir">
          <i className="ti ti-trash" aria-hidden="true"></i>
        </button>
      </div>
    </div>
  );
}
