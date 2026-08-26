# Frontend React — Minhas Tarefas

Frontend em React (Vite) integrado com a API REST Spring Boot do projeto
`projeto/` (endpoints em `/tarefas`).

## Estrutura

```
frontend/
├── index.html
├── vite.config.js        # proxy de /tarefas -> http://localhost:8080
├── src/
│   ├── main.jsx
│   ├── App.jsx            # estado e orquestração geral
│   ├── styles.css
│   ├── services/
│   │   └── tarefasApi.js  # todas as chamadas à API
│   └── components/
│       ├── Header.jsx
│       ├── FormularioTarefa.jsx
│       ├── Filtros.jsx
│       ├── ListaTarefas.jsx
│       ├── TarefaCard.jsx
│       ├── ModalEdicao.jsx
│       └── Toast.jsx
```

## Como rodar

1. Suba o backend Spring Boot (porta padrão 8080):
   ```bash
   cd ../projeto
   ./mvnw spring-boot:run
   ```

2. Em outro terminal, rode o frontend:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

3. Acesse `http://localhost:5173`.

O `vite.config.js` já faz proxy das chamadas `/tarefas` para
`http://localhost:8080`, então o frontend funciona sem configuração
adicional em desenvolvimento. Também foi adicionada uma configuração de
CORS no backend (`CorsConfig.java`) liberando `http://localhost:5173`,
caso você prefira chamar a API diretamente sem o proxy.

## Build de produção

```bash
npm run build
```

Gera a pasta `dist/`. Se quiser servir o React pelo próprio Spring Boot
(como fazia o `index.html` estático original), copie o conteúdo de
`dist/` para `projeto/src/main/resources/static/` no lugar do
`index.html` atual.

## Funcionalidades

- Listar tarefas (todas, por status, atrasadas)
- Criar tarefa
- Editar título, descrição, data limite e status
- Alternar status rapidamente pelo botão circular (Pendente → Em andamento → Concluída)
- Excluir tarefa (com confirmação)
- Notificações (toast) de sucesso e erro
