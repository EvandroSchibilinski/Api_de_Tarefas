import TarefaCard from './TarefaCard.jsx';

export default function ListaTarefas({ tarefas, carregando, onAlternarStatus, onEditar, onExcluir }) {
  if (carregando) {
    return <div className="carregando">Carregando tarefas...</div>;
  }

  if (tarefas.length === 0) {
    return (
      <div className="vazio">
        <i className="ti ti-clipboard-x" aria-hidden="true"></i>
        Nenhuma tarefa encontrada
      </div>
    );
  }

  return (
    <div className="lista">
      {tarefas.map((t) => (
        <TarefaCard
          key={t.id}
          tarefa={t}
          onAlternarStatus={onAlternarStatus}
          onEditar={onEditar}
          onExcluir={onExcluir}
        />
      ))}
    </div>
  );
}
