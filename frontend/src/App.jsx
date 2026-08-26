import { useEffect, useMemo, useRef, useState } from 'react';
import Header from './components/Header.jsx';
import FormularioTarefa from './components/FormularioTarefa.jsx';
import Filtros from './components/Filtros.jsx';
import ListaTarefas from './components/ListaTarefas.jsx';
import ModalEdicao from './components/ModalEdicao.jsx';
import Toast from './components/Toast.jsx';
import { tarefasApi } from './services/tarefasApi.js';

export default function App() {
  const [tarefas, setTarefas] = useState([]);
  const [carregando, setCarregando] = useState(true);
  const [filtroAtual, setFiltroAtual] = useState('TODAS');
  const [tarefaEditando, setTarefaEditando] = useState(null);
  const [toast, setToast] = useState({ mensagem: '', erro: false, visivel: false });

  const toastTimer = useRef(null);

  function mostrarToast(mensagem, erro = false) {
    setToast({ mensagem, erro, visivel: true });
    clearTimeout(toastTimer.current);
    toastTimer.current = setTimeout(() => {
      setToast((t) => ({ ...t, visivel: false }));
    }, 2500);
  }

  async function carregar() {
    setCarregando(true);
    try {
      const dados = await tarefasApi.listarTodas();
      setTarefas(dados || []);
    } catch (err) {
      mostrarToast(err.message || 'Erro ao conectar com a API', true);
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    carregar();
  }, []);

  async function handleCriar(payload, erroValidacao) {
    if (erroValidacao) {
      mostrarToast(erroValidacao, true);
      return false;
    }
    try {
      await tarefasApi.criar(payload);
      mostrarToast('Tarefa criada');
      await carregar();
      return true;
    } catch (err) {
      mostrarToast(err.message || 'Erro ao criar', true);
      return false;
    }
  }

  async function handleAlternarStatus(tarefa) {
    const proximo =
      tarefa.status === 'PENDENTE' ? 'EM_ANDAMENTO'
      : tarefa.status === 'EM_ANDAMENTO' ? 'CONCLUIDA'
      : 'PENDENTE';
    try {
      await tarefasApi.atualizarStatus(tarefa.id, proximo);
      await carregar();
    } catch (err) {
      mostrarToast(err.message || 'Erro ao atualizar status', true);
    }
  }

  async function handleExcluir(id) {
    if (!confirm('Excluir esta tarefa?')) return;
    try {
      await tarefasApi.excluir(id);
      mostrarToast('Tarefa excluída');
      await carregar();
    } catch (err) {
      mostrarToast(err.message || 'Erro ao excluir', true);
    }
  }

  async function handleSalvarEdicao(id, dados) {
    try {
      await tarefasApi.atualizar(id, {
        titulo: dados.titulo,
        descricao: dados.descricao,
        dataLimite: dados.dataLimite,
      });
      if (dados.statusAlterado) {
        await tarefasApi.atualizarStatus(id, dados.status);
      }
      setTarefaEditando(null);
      mostrarToast('Tarefa atualizada');
      await carregar();
    } catch (err) {
      mostrarToast(err.message || 'Erro ao salvar', true);
    }
  }

  const tarefasFiltradas = useMemo(() => {
    const hoje = new Date().toISOString().split('T')[0];
    if (filtroAtual === 'ATRASADAS') {
      return tarefas.filter((t) => t.dataLimite && t.dataLimite < hoje && t.status !== 'CONCLUIDA');
    }
    if (filtroAtual !== 'TODAS') {
      return tarefas.filter((t) => t.status === filtroAtual);
    }
    return tarefas;
  }, [tarefas, filtroAtual]);

  return (
    <>
      <Header />
      <main>
        <FormularioTarefa onCriar={handleCriar} />

        <Filtros filtroAtual={filtroAtual} onFiltrar={setFiltroAtual} />

        <ListaTarefas
          tarefas={tarefasFiltradas}
          carregando={carregando}
          onAlternarStatus={handleAlternarStatus}
          onEditar={setTarefaEditando}
          onExcluir={handleExcluir}
        />
      </main>

      <Toast mensagem={toast.mensagem} erro={toast.erro} visivel={toast.visivel} />

      <ModalEdicao
        tarefa={tarefaEditando}
        onSalvar={handleSalvarEdicao}
        onFechar={() => setTarefaEditando(null)}
      />
    </>
  );
}
