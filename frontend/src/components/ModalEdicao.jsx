import { useEffect, useState } from 'react';

export default function ModalEdicao({ tarefa, onSalvar, onFechar }) {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [dataInicio, setDataInicio] = useState('');
  const [dataLimite, setDataLimite] = useState('');
  const [salvando, setSalvando] = useState(false);

  useEffect(() => {
    if (tarefa) {
      setTitulo(tarefa.titulo || '');
      setDescricao(tarefa.descricao || '');
      setDataInicio(tarefa.dataInicio || '');
      setDataLimite(tarefa.dataLimite || '');
    }
  }, [tarefa]);

  if (!tarefa) return null;

  async function handleSalvar() {
    if (!titulo.trim()) return;
    setSalvando(true);
    await onSalvar(tarefa.id, {
      titulo: titulo.trim(),
      descricao: descricao.trim(),
      dataInicio: dataInicio || null,
      dataLimite: dataLimite || null,
    });
    setSalvando(false);
  }

  return (
    <div
      className="modal-overlay open"
      onClick={(e) => {
        if (e.target === e.currentTarget) onFechar();
      }}
    >
      <div className="modal">
        <h2>Editar tarefa</h2>
        <div className="form-row">
          <div>
            <label>Título</label>
            <input type="text" value={titulo} onChange={(e) => setTitulo(e.target.value)} />
          </div>
          <div>
            <label>Descrição</label>
            <textarea value={descricao} onChange={(e) => setDescricao(e.target.value)} />
          </div>
          <div>
            <label htmlFor="edicao-data-inicio">Data de início</label>
            <input
              id="edicao-data-inicio"
              type="date"
              value={dataInicio}
              onChange={(e) => setDataInicio(e.target.value)}
            />
          </div>
          <div>
            <label>Data limite</label>
            <input type="date" value={dataLimite} onChange={(e) => setDataLimite(e.target.value)} />
          </div>
        </div>
        <div className="form-actions">
          <button className="primary" onClick={handleSalvar} disabled={salvando}>
            <i className="ti ti-check" aria-hidden="true"></i>
            {salvando ? 'Salvando...' : 'Salvar'}
          </button>
          <button onClick={onFechar}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}
