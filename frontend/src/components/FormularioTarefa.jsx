import { useState } from 'react';

export default function FormularioTarefa({ onCriar }) {
  const [titulo, setTitulo] = useState('');
  const [descricao, setDescricao] = useState('');
  const [dataLimite, setDataLimite] = useState('');
  const [enviando, setEnviando] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    if (!titulo.trim()) {
      onCriar(null, 'Informe um título');
      return;
    }

    setEnviando(true);
    const sucesso = await onCriar({
      titulo: titulo.trim(),
      descricao: descricao.trim(),
      dataLimite: dataLimite || null,
    });
    setEnviando(false);

    if (sucesso) {
      setTitulo('');
      setDescricao('');
      setDataLimite('');
    }
  }

  return (
    <div className="form-card">
      <h2>Nova tarefa</h2>
      <form className="form-row" onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Título da tarefa"
          value={titulo}
          onChange={(e) => setTitulo(e.target.value)}
        />
        <textarea
          placeholder="Descrição (opcional)"
          value={descricao}
          onChange={(e) => setDescricao(e.target.value)}
        />
        <input
          type="date"
          value={dataLimite}
          onChange={(e) => setDataLimite(e.target.value)}
        />
        <div className="form-actions">
          <button className="primary" type="submit" disabled={enviando}>
            <i className="ti ti-plus" aria-hidden="true"></i>
            {enviando ? 'Criando...' : 'Criar tarefa'}
          </button>
        </div>
      </form>
    </div>
  );
}
