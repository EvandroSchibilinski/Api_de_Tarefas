const OPCOES = [
  { valor: 'TODAS', label: 'Todas' },
  { valor: 'PENDENTE', label: 'Pendente' },
  { valor: 'EM_ANDAMENTO', label: 'Em andamento' },
  { valor: 'CONCLUIDA', label: 'Concluída' },
  { valor: 'ATRASADAS', label: 'Atrasadas', perigo: true },
];

export default function Filtros({ filtroAtual, onFiltrar }) {
  return (
    <div className="filters">
      {OPCOES.map((opcao) => (
        <button
          key={opcao.valor}
          className={`filter-btn ${opcao.perigo ? 'danger-filter' : ''} ${filtroAtual === opcao.valor ? 'active' : ''}`}
          onClick={() => onFiltrar(opcao.valor)}
        >
          {opcao.label}
        </button>
      ))}
    </div>
  );
}
