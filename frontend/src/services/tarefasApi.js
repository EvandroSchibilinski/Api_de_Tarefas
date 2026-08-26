// Camada de integração com a API REST do backend Spring Boot (/tarefas).
// Em dev, o Vite faz proxy de /tarefas para http://localhost:8080 (ver vite.config.js).
const BASE_URL = '/tarefas';

async function tratarResposta(res) {
  if (res.status === 204) return null;

  if (!res.ok) {
    let mensagem = `Erro na requisição (HTTP ${res.status})`;
    try {
      const corpo = await res.json();
      mensagem = corpo.mensagem || corpo.message || mensagem;
    } catch {
      // corpo vazio ou não-JSON, mantém mensagem padrão
    }
    throw new Error(mensagem);
  }

  const texto = await res.text();
  return texto ? JSON.parse(texto) : null;
}

export const tarefasApi = {
  listarTodas() {
    return fetch(BASE_URL).then(tratarResposta);
  },

  buscarPorId(id) {
    return fetch(`${BASE_URL}/${id}`).then(tratarResposta);
  },

  listarPorStatus(status) {
    return fetch(`${BASE_URL}/status/${status}`).then(tratarResposta);
  },

  listarAtrasadas() {
    return fetch(`${BASE_URL}/atrasadas`).then(tratarResposta);
  },

  buscarPorTitulo(titulo) {
    return fetch(`${BASE_URL}/buscar?titulo=${encodeURIComponent(titulo)}`).then(tratarResposta);
  },

  criar(tarefa) {
    return fetch(BASE_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(tarefa),
    }).then(tratarResposta);
  },

  atualizar(id, tarefa) {
    return fetch(`${BASE_URL}/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(tarefa),
    }).then(tratarResposta);
  },

  atualizarStatus(id, status) {
    return fetch(`${BASE_URL}/${id}/status?status=${status}`, {
      method: 'PATCH',
    }).then(tratarResposta);
  },

  excluir(id) {
    return fetch(`${BASE_URL}/${id}`, { method: 'DELETE' }).then(tratarResposta);
  },
};
