# APGAR-TECHINCAL-TEST

API REST para gerenciamento de reservas de salas de reunião, feita para o desafio técnico da APGAR. Dados armazenados em memória (sem banco de dados), conforme exigido pelo enunciado.

## Requisitos

- Java 25

## Como executar

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

Alternativamente, gerando o `.jar`:

```bash
./mvnw clean package
java -jar target/techinical-test-0.0.1-SNAPSHOT.jar
```

## Como executar os testes

```bash
./mvnw test
```

Teste unitarios com (JUnit 5 + Mockito + AssertJ), Testes de integração (`*IntegrationTest`) usam MockMvc para validar a "relação" entre Controller, Service, Validator e o tratamento de erros, sem repetir as regras de negócio já cobertas nos testes unitários.

JaCoCo roda automaticamente junto com `./mvnw test` (relatório em `target/site/jacoco/index.html`).

## Endpoints

| Método | Rota            | Descrição                                |
| ------ | --------------- | ---------------------------------------- |
| POST   | `/reservas`     | Cria uma reserva                         |
| GET    | `/reservas`     | Lista todas as reservas                  |
| DELETE | `/reservas`     | Remove todas as reservas                 |
| GET    | `/estatisticas` | Estatísticas de ocupação do dia corrente |
| GET    | `/health`       | Health check simples (`{"status":"Tudo Funcionando aqui"}`) |

Regras de validação da reserva (sala, formato de data, duração, janela de horário, sobreposição etc.) conforme solicitado no enunciado.

Documentação (Swagger UI): `http://localhost:8080/swagger-ui/index.html`. JSON OpenAPI puro: `http://localhost:8080/v3/api-docs`.

## Padrões de código adotados

- **Camadas**: `Controller` (sem lógica) → `Service` (orquestra) → `Validator`/`Repository`. Validações de negócio ficam isoladas em `ReservaValidator`, nunca no Controller ou no Repository.
- **Fail-fast**: cada regra de negócio é um método privado que lança exceção assim que encontra o problema, na ordem do enunciado.
- **Injeção via construtor**: nenhuma classe usa `@Autowired` em campo.
- **Records para dados imutáveis**: `Reserva`, `ReservaRequest`, `ReservaResponse`, `EstatisticasResponse`.
- **Sem strings/números "mágicos"**: salas válidas são um `enum` (`Sala`), limites de horário e duração são constantes nomeadas.
- **Exceções de negócio dedicadas**: `ReservaInvalidaException`, tratada em um único `@RestControllerAdvice` (`TratadorDeExcecoes`).
- **Nunca retorna `null`**: listas vazias em vez de `null` quando não há dados.
- **Concorrência**: lock `synchronized` no fluxo de criação (`ReservaService.criar`) e nos métodos do `InMemoryReservaRepository`, para evitar duas reservas conflitantes serem aceitas ao mesmo tempo.
- **Testes Given-When-Then**: Dividindo todos os testes em três passos.

## Git flow e commits

- Branch por task: `feat/APGAR-<numero-da-task>` (ex.: `feat/APGAR-1.2`), integradas na branch `dev`.
- Commits seguem o padrão [Conventional Commits](https://www.conventionalcommits.org/): `feat(escopo): resumo curto`, corpo com uma frase descrevendo a mudança, e `Refs: <numero-da-task>` referenciando a task correspondente.
- Utilização do trello para criação das tasks do projeto: https://trello.com/b/6W0gSQHS/apgar-techinical-test
