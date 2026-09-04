# Minhas Tarefas

Aplicação full stack para gerenciamento de tarefas, com API REST em Spring Boot e interface em React.

O projeto permite criar, consultar, editar e excluir tarefas, controlar o fluxo de status, definir datas de início e limite, pesquisar por título e identificar tarefas atrasadas.

## Funcionalidades

- Criação e edição de tarefas com título, descrição, data de início e data limite
- Estados `AGENDADA`, `PENDENTE`, `EM_ANDAMENTO` e `CONCLUIDA`
- Transições de status protegidas por regras de domínio
- Listagem paginada e ordenada
- Filtros por status e tarefas atrasadas
- Pesquisa por parte do título
- Validação dos dados recebidos pela API
- Respostas de erro padronizadas
- Controle de concorrência por versão da entidade

## Tecnologias

### Backend

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- PostgreSQL
- MapStruct 1.6.3
- Lombok
- Maven
- JUnit

### Frontend

- React 18
- Vite 5
- JavaScript
- CSS

## Arquitetura

```text
frontend
   ↓ HTTP/JSON
controller
   ↓ DTOs de entrada
service
   ↓ regras de domínio
entity
   ↓ persistência
repository
   ↓
PostgreSQL
```

Responsabilidades principais:

- `controller`: recebe requisições HTTP, valida dados e define códigos de resposta
- `dto/request`: representa os dados aceitos em cada operação
- `dto/response`: representa os contratos devolvidos ao cliente
- `mapper`: converte entidades em respostas e requisições de criação em entidades
- `service`: coordena casos de uso e transações
- `entity`: protege as regras e transições de estado da tarefa
- `repository`: executa consultas e operações de persistência
- `exception`: padroniza erros de validação, domínio e recursos inexistentes

## Estrutura

```text
.
├── projeto/
│   └── src/
│       ├── main/java/com/schibilinski/projeto/
│       │   ├── config/
│       │   ├── controller/
│       │   ├── dto/
│       │   │   ├── request/
│       │   │   └── response/
│       │   ├── entity/
│       │   ├── exception/
│       │   ├── mapper/
│       │   ├── repository/
│       │   └── service/
│       └── test/java/
└── frontend/
    └── src/
        ├── components/
        ├── services/
        ├── App.jsx
        └── styles.css
```

## Pré-requisitos

- JDK 21
- Maven 3.9 ou Maven Wrapper
- Node.js 18 ou superior
- PostgreSQL

## Configuração do banco

Crie um banco PostgreSQL chamado `projeto`:

```sql
CREATE DATABASE projeto;
```

Confira as credenciais em:

```text
projeto/src/main/resources/application.yaml
```

Para uso fora do ambiente local, não mantenha credenciais reais no repositório. Use variáveis de ambiente ou perfis do Spring.

## Executando o backend

No diretório raiz do repositório:

```powershell
cd projeto
.\mvnw.cmd spring-boot:run
```

Se o Maven Wrapper não funcionar e o Maven estiver instalado:

```powershell
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

O backend também contém uma versão estática da interface em:

```text
http://localhost:8080/
```

## Executando o frontend React

Em outro terminal:

```powershell
cd frontend
npm install
npm run dev
```

A interface ficará disponível em:

```text
http://localhost:5173
```

O Vite encaminha as chamadas iniciadas por `/tarefas` para o backend em `http://localhost:8080`.

## Acesso pelo celular

O celular e o computador devem estar na mesma rede Wi-Fi. Descubra o IPv4 do computador:

```powershell
ipconfig
```

Para acessar a interface servida pelo backend, use no celular:

```text
http://IP_DO_COMPUTADOR:8080
```

Exemplo:

```text
http://192.168.18.53:8080
```

Use explicitamente `http://`. O servidor local não está configurado com certificado HTTPS.

Para disponibilizar o Vite na rede local:

```powershell
npm run dev -- --host 0.0.0.0
```

Depois acesse:

```text
http://IP_DO_COMPUTADOR:5173
```

## Estados e transições

```text
AGENDADA → PENDENTE → EM_ANDAMENTO → CONCLUIDA
```

- Uma tarefa com data de início futura é criada como `AGENDADA`
- Uma tarefa sem início futuro é criada como `PENDENTE`
- Somente uma tarefa pendente pode ser iniciada
- Somente uma tarefa em andamento pode ser concluída
- Uma tarefa concluída não pode ser editada
- Uma tarefa é atrasada quando a data limite passou e ela ainda não foi concluída

## Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/tarefas` | Cria uma tarefa |
| `GET` | `/tarefas` | Lista tarefas com paginação |
| `GET` | `/tarefas/{id}` | Busca uma tarefa por ID |
| `GET` | `/tarefas/status/{status}` | Lista tarefas por status |
| `GET` | `/tarefas/atrasadas` | Lista tarefas atrasadas |
| `GET` | `/tarefas/buscar?titulo=texto` | Pesquisa por título |
| `PUT` | `/tarefas/{id}` | Atualiza os dados da tarefa |
| `PATCH` | `/tarefas/{id}/status` | Avança o status da tarefa |
| `DELETE` | `/tarefas/{id}` | Exclui uma tarefa |

Os endpoints de listagem aceitam os parâmetros `page`, `size` e `sort`:

```text
GET /tarefas?page=0&size=20&sort=criadaEm,desc
```

## Exemplos da API

### Criar tarefa

```http
POST /tarefas
Content-Type: application/json
```

```json
{
  "titulo": "Estudar Spring Boot",
  "descricao": "Revisar entidades, DTOs e services",
  "dataInicio": "2026-09-05",
  "dataLimite": "2026-09-10"
}
```

### Atualizar dados

```http
PUT /tarefas/1
Content-Type: application/json
```

```json
{
  "titulo": "Estudar Spring Boot e JPA",
  "descricao": "Revisar domínio e persistência",
  "dataInicio": "2026-09-05",
  "dataLimite": "2026-09-12"
}
```

### Atualizar status

```http
PATCH /tarefas/1/status
Content-Type: application/json
```

```json
{
  "status": "EM_ANDAMENTO"
}
```

## Validação e erros

A API utiliza os seguintes códigos principais:

- `400 Bad Request`: dados ou parâmetros inválidos
- `404 Not Found`: tarefa inexistente
- `409 Conflict`: transição de status inválida
- `500 Internal Server Error`: falha inesperada

Exemplo de erro de validação:

```json
{
  "timestamp": "2026-09-04T10:30:00",
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Existem campos inválidos",
  "caminho": "/tarefas",
  "campos": {
    "titulo": "O título é obrigatório"
  }
}
```

## Testes

Execute os testes do backend:

```powershell
cd projeto
.\mvnw.cmd test
```

Ou, com Maven instalado:

```powershell
mvn test
```

## Build de produção

Backend:

```powershell
cd projeto
mvn clean package
java -jar target/*.jar
```

Frontend:

```powershell
cd frontend
npm run build
```

O build do frontend será criado em `frontend/dist`.

## Licença

Distribuído sob a licença MIT.
