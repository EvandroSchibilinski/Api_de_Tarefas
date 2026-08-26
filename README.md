# 📋 Minhas Tarefas

Aplicação full stack de gerenciamento de tarefas (To-Do List), com backend em **Spring Boot** e frontend em **React**.

Permite criar, listar, filtrar, editar, atualizar status e excluir tarefas, com controle de data limite e destaque para tarefas atrasadas.

## ✨ Funcionalidades

- Criar tarefas com título, descrição e data limite
- Listar todas as tarefas
- Filtrar por status (Pendente, Em andamento, Concluída) ou por atrasadas
- Buscar tarefas por título
- Alternar status rapidamente (Pendente → Em andamento → Concluída)
- Editar título, descrição, data limite e status
- Excluir tarefas
- Identificação visual de tarefas atrasadas

## 🛠️ Tecnologias

**Backend**
- Java 25
- Spring Boot 3.5
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Maven

**Frontend**
- React 18
- Vite

## 📁 Estrutura do projeto

```
.
├── projeto/          # Backend Spring Boot (API REST)
│   └── src/main/java/com/schibilinski/projeto/
│       ├── controller/    # Endpoints REST
│       ├── service/       # Regras de negócio
│       ├── repository/    # Acesso a dados (JPA)
│       ├── entity/        # Entidades (Tarefa)
│       ├── dto/           # Objetos de transferência
│       ├── exception/     # Tratamento global de exceções
│       └── config/        # Configuração de CORS
│
└── frontend/         # Frontend React (Vite)
    └── src/
        ├── components/    # Componentes de UI
        ├── services/      # Integração com a API
        └── App.jsx
```

## 🚀 Como rodar o projeto

### Pré-requisitos

- [Java 25 (JDK)](https://adoptium.net/)
- [Node.js](https://nodejs.org/) (versão 18 ou superior)
- [PostgreSQL](https://www.postgresql.org/download/) rodando localmente

### 1. Configurar o banco de dados

Crie um banco chamado `projeto` no PostgreSQL. As credenciais padrão usadas em `projeto/src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/projeto
    username: postgres
    password: Admin
```

> Ajuste usuário/senha conforme sua instalação local. O Hibernate cria as tabelas automaticamente (`ddl-auto: update`).

### 2. Rodar o backend

```bash
cd projeto
./mvnw spring-boot:run        # Linux/Mac
mvnw.cmd spring-boot:run      # Windows
```

A API sobe em `http://localhost:8080`.

### 3. Rodar o frontend

Em outro terminal:

```bash
cd frontend
npm install
npm run dev
```

A aplicação sobe em `http://localhost:5173`.

> Em desenvolvimento, o Vite faz proxy das chamadas `/tarefas` para `http://localhost:8080` (configurado em `vite.config.js`), então o frontend funciona sem configuração adicional. Também há uma configuração de CORS no backend liberando `localhost:5173`, para o caso de consumir a API diretamente.

## 📡 Endpoints da API

| Método | Endpoint                     | Descrição                              |
|--------|-------------------------------|-----------------------------------------|
| GET    | `/tarefas`                    | Lista todas as tarefas                  |
| GET    | `/tarefas/{id}`                | Busca uma tarefa por ID                 |
| GET    | `/tarefas/status/{status}`     | Lista tarefas por status                |
| GET    | `/tarefas/atrasadas`           | Lista tarefas atrasadas                 |
| GET    | `/tarefas/buscar?titulo=`      | Busca tarefas por título                |
| POST   | `/tarefas`                    | Cria uma nova tarefa                    |
| PUT    | `/tarefas/{id}`                | Atualiza uma tarefa                     |
| PATCH  | `/tarefas/{id}/status?status=` | Atualiza apenas o status                |
| DELETE | `/tarefas/{id}`                | Exclui uma tarefa                       |

Status possíveis: `PENDENTE`, `EM_ANDAMENTO`, `CONCLUIDA`.

## 📦 Build de produção

**Backend:**
```bash
cd projeto
./mvnw clean package
java -jar target/*.jar
```

**Frontend:**
```bash
cd frontend
npm run build
```

Gera a pasta `dist/`. Para servir o React pelo próprio Spring Boot, copie o conteúdo de `frontend/dist` para `projeto/src/main/resources/static/`.

## 📄 Licença

Este projeto está sob a licença MIT — sinta-se livre para usar e modificar.
