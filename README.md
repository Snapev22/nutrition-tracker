# Nutrition Tracker

Aplicação desktop em Java/JavaFX para acompanhamento nutricional. Permite cadastrar alunos e alimentos, montar planos alimentares diários e acompanhar o consumo calórico em relação à meta de cada aluno através de um dashboard visual.

O projeto foi desenvolvido como trabalho acadêmico e vem passando por um processo contínuo de refatoração com foco em boas práticas de arquitetura, persistência e organização de código — como preparação para uma futura migração para Spring Boot.

![Plano Diário](./docs/screenshots/plano.png)

<details>
<summary>Ver mais telas</summary>

| Alunos | Alimentos |
|---|---|
| ![Alunos](./docs/screenshots/aluno.png) | ![Alimentos](./docs/screenshots/alimento.png) |

| Dashboard |
|---|
| ![Dashboard](./docs/screenshots/dashboard.png) |

</details>

---

## Funcionalidades

* Cadastro, edição e exclusão de alunos, com cálculo automático da meta calórica diária (baseado em TMB, fator de atividade e objetivo — emagrecimento, manutenção ou hipertrofia)
* Cadastro, edição e exclusão de alimentos, com informações nutricionais (proteína, carboidrato, gordura, calorias)
* Montagem de plano alimentar diário por aluno e data, com adição e remoção de itens
* Validação automática de limite calórico ao adicionar um item ao plano
* Dashboard com resumo visual do consumo do dia (meta, consumido, restante) e barra de progresso colorida conforme o percentual da meta utilizada
* Confirmação de exclusão antes de remover qualquer cadastro
* Operações de banco de dados executadas em background (`javafx.concurrent.Task`), mantendo a interface responsiva

---

## Arquitetura

O projeto segue uma separação em camadas inspirada no padrão usado em aplicações Spring, como preparação para a migração futura:

```
Controller  →  Service  →  DAO  →  Banco de Dados (MySQL)
   (UI)        (regras de     (persistência,
              negócio)         JDBC puro)
```

* **Controller** — controla os componentes JavaFX (FXML) e delega toda regra de negócio para a camada de Service. Nenhum controller acessa DAO diretamente.
* **Service** — concentra as regras de negócio (cálculo de meta calórica, validação de limite calórico, orquestração entre entidades relacionadas).
* **DAO** — acesso a dados via JDBC puro (sem ORM), com queries parametrizadas (`PreparedStatement`) para evitar SQL Injection.
* **Model** — entidades simples (POJOs), com boilerplate reduzido via Lombok (`@Getter`, `@Setter`, `@EqualsAndHashCode` baseado em identidade).

Algumas decisões técnicas ao longo do desenvolvimento, documentadas via commits:
* Agregações (como soma de calorias consumidas) são calculadas via `SUM()` no banco em vez de carregadas por completo para a memória, reduzindo volume de dados trafegado.
* Credenciais de banco de dados são externalizadas em `application.properties` (fora do controle de versão).

---

## Tecnologias

* Java 17
* JavaFX
* JDBC (MySQL)
* Maven
* Lombok
* JUnit 5
* SLF4J + Logback

---

## Como executar

### Pré-requisitos

* JDK 17 ou superior
* Apache Maven 3.6.0 ou superior
* MySQL 8.0 ou superior (rodando localmente ou acessível via rede)

### Instalação

1. Clone o repositório:
   ```bash
   git clone https://github.com/<seu-usuario>/nutrition-tracker-java.git
   cd nutrition-tracker-java
   ```

2. Crie o schema do banco de dados executando o script SQL disponível em [`/sql/schema.sql`](./sql/schema.sql) (ajuste conforme o nome do seu banco).

3. Configure as credenciais do banco:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
   Edite o arquivo `application.properties` com a URL, usuário e senha do seu banco local.

4. Instale as dependências e compile o projeto:
   ```bash
   mvn clean install
   ```

5. Execute a aplicação:
   ```bash
   mvn javafx:run
   ```

### Rodando os testes

```bash
mvn test
```

---

## Uso

Ao abrir a aplicação, a navegação entre as telas é feita pela barra superior:

1. **Alunos** — cadastro de alunos com dados físicos (peso, altura, idade, sexo), fator de atividade e objetivo. A meta calórica diária é calculada automaticamente ao salvar.
2. **Alimentos** — cadastro de alimentos com valores nutricionais por unidade/porção.
3. **Plano Diário** — seleção de um aluno e uma data para montar (ou carregar) o plano alimentar do dia, adicionando alimentos com a quantidade desejada. O sistema bloqueia a adição de itens que ultrapassem a meta calórica do aluno.
4. **Dashboard** — visão consolidada do consumo do dia selecionado, com meta, consumido, restante e barra de progresso (verde até 80% da meta, amarelo até 100%, vermelho acima disso).

---

## Limitações conhecidas

* As tabelas (`TableView`) carregam todos os registros de uma vez, sem paginação — adequado para o volume de dados atual, mas seria o próximo ponto de otimização em caso de crescimento da base.
* Aplicação single-user, sem autenticação — fora do escopo original do projeto.

---

## Roadmap

* [ ] Migração da camada de persistência para Spring Data JPA / Hibernate
* [ ] Exposição das funcionalidades como API REST via Spring Boot
* [ ] Testes de integração de repositório com banco em memória (H2)
* [ ] Pipeline de CI (GitHub Actions) rodando os testes a cada push

---

## Contribuindo

Este é um projeto de portfólio pessoal, mas sugestões são bem-vindas:

1. Faça um fork do repositório
2. Crie uma branch para sua feature ou correção (`git checkout -b feature/nome-da-feature`)
3. Faça commit das suas alterações (`git commit -m 'feat: descrição da mudança'`)
4. Envie para o seu fork (`git push origin feature/nome-da-feature`)
5. Abra um Pull Request

---

## Autor

Desenvolvido por **[Vitor Henrique]** como projeto de estudo em Java/JavaFX, com foco em arquitetura em camadas e boas práticas de desenvolvimento backend.

[LinkedIn](https://www.linkedin.com/in/vitor-henrique-deva61412/) · [GitHub](https://github.com/Snapev22)
