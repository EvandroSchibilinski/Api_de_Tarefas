# Minhas Tarefas

Aplicação full stack para gerenciamento de tarefas, desenvolvida com **Spring Boot**, **React** e **PostgreSQL**.

## Funcionalidades

- Criar, editar e excluir tarefas
- Definir data de início e data limite
- Pesquisar e filtrar tarefas
- Identificar tarefas atrasadas
- Controlar o status das tarefas

Fluxo dos status:

```text
AGENDADA → PENDENTE → EM ANDAMENTO → CONCLUÍDA
```

## Tecnologias

### Backend

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- PostgreSQL
- Bean Validation
- MapStruct
- Lombok
- JUnit

### Frontend

- React 18
- Vite 5
- JavaScript
- CSS

## Como executar

### Banco de dados

Crie um banco PostgreSQL chamado `projeto`:

```sql
CREATE DATABASE projeto;
```

Depois, confira as credenciais em:

```text
projeto/src/main/resources/application.yaml
```

### Backend

```powershell
cd projeto
mvn spring-boot:run
```

A aplicação estará disponível em:

```text
http://localhost:8080
```

### Frontend React

```powershell
cd frontend
npm install
npm run dev
```

Acesse:

```text
http://localhost:5173
```

## Principais endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/tarefas` | Criar tarefa |
| `GET` | `/tarefas` | Listar tarefas |
| `GET` | `/tarefas/{id}` | Buscar por ID |
| `GET` | `/tarefas/atrasadas` | Listar atrasadas |
| `GET` | `/tarefas/buscar?titulo=` | Buscar por título |
| `PUT` | `/tarefas/{id}` | Editar tarefa |
| `PATCH` | `/tarefas/{id}/status` | Atualizar status |
| `DELETE` | `/tarefas/{id}` | Excluir tarefa |

## Exemplo de criação

```json
{
  "titulo": "Estudar Spring Boot",
  "descricao": "Revisar entidades e DTOs",
  "dataInicio": "2026-09-05",
  "dataLimite": "2026-09-10"
}
```

## Testes

```powershell
cd projeto
mvn test
```

## Acesso pelo celular

Com o celular e o computador na mesma rede Wi-Fi, acesse:

```text
http://IP_DO_COMPUTADOR:8080
```

Use `http://`, pois o ambiente local não possui certificado HTTPS.
