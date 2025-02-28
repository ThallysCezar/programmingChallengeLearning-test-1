# Projeto de Desafio de Aprendizado, Desafio 1

Este projeto é uma plataforma de E-commerce abrangente, construída usando Quarkus e arquitetura de microserviços. O principal objetivo deste projeto foi aprofundar meu entendimento sobre o Quarkus e suas capacidades em lidar com a comunicação entre microserviços, migrações de banco de dados com Flyway, e observabilidade usando Grafana e Prometheus.

## Stack utilizada

**Back-end:** Java, SpringBoot, H2, JPA/Hibernate, Spring Security Com JWT Token para autenticação e Swagger OpenAPI para documentação.

## Configuração

- Certifique-se de ter o Java(versão 17) e o Maven instalados em seu sistema antes de prosseguir.

```bash
  git clone https://github.com/ThallysCezar/programmingChallengeLearning-test-1
  cd programmingChallengeLearning-test-1
```
- No application.properties do projeto, lembre-se de configurar o banco de dados H2, o qual será usado. Por exemplo:
```bash
 spring.datasource.url=jdbc:h2:mem:testdb
 spring.datasource.username=sa
 spring.datasource.password=
```
- E para rodar o projeto, basta apenas rodar o comando no cmd da pasta:
```cmd
 mvn spring-boot:run
```
## Documentação da APIs

A documentação completa da API pode ser encontrada no Swagger. Para acessar a documentação, siga as etapas abaixo:

1. Certifique-se de que o projeto esteja em execução.

2. Abra um navegador da web e vá para a seguinte URL:

    - [Swagger API Documentation - API](http://localhost:8080/q/swagger-ui)

3. Isso abrirá a interface do Swagger, onde você pode explorar e testar os endpoints da API, apenas se tiver autenticado.

Divirta-se explorando a API!

## Como a API funciona
Para começar, basicamente o projeto consiste em: API RESTful de criação de usuários e carros com login. Logo, teremos:

### Endpoints para Usuário, o qual não precisa de autenticação para acessar:

  ```http
    GET /api/users
  ```

- Esta requisição retornará todos os usuários já cadastrados.

    ```http
        GET /api/users/{id}
    ```
- Esta requisição retornará um usuário pelo id.

    ```http
        POST /api/users
    ```
- Esta requisição cadastra um usuário, o qual logo abaixo estará um JSON de exemplo:

    ```json
          {
            "firstName": "Hello",
            "lastName": "World",
            "email": "exemplo3@email.com",
            "birthday": "1990-05-01",
            "login": "helloWorldu",
            "password": "123456T",
            "phone": "988888888",
            "role":"CLIENTE",
            "cars": [
              {
                "years": "2020",
                "licensePlate": "ABC-1234",
                "model": "Honda Civic",
                "color": "Preto"
              }
            ]
          }
    ```

    ```http
        PUT /api/users/{id}
    ```
- Esta requisição atualiza um usuário pelo id.

    ```http
        DELETE /api/users/{id}
    ```
- Esta requisição remove um usuário pelo id.

### Endpoints para Carros, o qual precisará de autenticação para acessar:

  ```http
    GET /api/cars
  ```

- Esta requisição retornará todos os carros já cadastrados.

    ```http
        GET /api/cars/{id}
    ```
- Esta requisição retornará um carro pelo id.

    ```http
        POST /api/cars
    ```
- Esta requisição cadastra um carro, o qual logo abaixo estará um JSON de exemplo:

    ```json
          {
            "years": "2020",
            "licensePlate": "DEF-9101",
            "model": "Ford Mustang",
            "color": "Vermelho"
          }
    ```

    ```http
        PUT /api/cars/{id}
    ```
- Esta requisição atualiza um carro pelo id.

    ```http
        DELETE /api/cars/{id}
    ```
- Esta requisição remove um carro pelo id.

### Endpoint para Authenticaçãoo qual precisará de um usuário cadastrado, para dessa forma pegar o token de acesso:

  ```http
      POST /api/cars
  ```
- Esta requisição realiza login para ter acesso ao token de segurança, o qual logo abaixo estará um JSON de exemplo:

    ```json
          {
            "login": "helloWorldu",
            "password": "123456T"
          }
    ```

### Endpoint que retornará as informações do usuário logado:

  ```http
      GET /api/me
  ```

Observação: Caso tenha dificuldades para acessar os endpoints fornecidos, consulte como deve ser usado cada endpoint na documentação da API via Swagger.

Mas, de fato, como a API RESTful funciona? Então, esta API permite gerenciar usuários com autenticação e proteção de rotas. Aqui estão os passos básicos para utilizar a API:

1. Cadastro de Usuário: Para cadastrar um novo usuário, envie uma requisição POST no endpoint mencionado acima(cadastrar usuários). Se o cadastro for bem-sucedido, a API retornará um código #201 Created.
2. Login do Usuário: Para obter um token de autenticação, envie uma requisição POST no endpoint mencionado acima(login). Se as credenciais forem corretas, a API retornará um token JWT, por exemplo:

  ```http
        {
          "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        }
  ```
3. Acessando Rotas Protegidas: Agora que você possui um token JWT, pode utilizá-lo para acessar rotas protegidas. Para isso, adicione o token ao cabeçalho Authorization de suas requisições.
# Validações
Algumas validações são necessárias, logo abaixo teremos:

### E-mail
- Deve conter caracteres alfanuméricos antes do @.
- Pode conter ., _, +, e - antes do @.
- Deve conter @.
- Deve conter um domínio válido após o @ (ex: gmail.com, empresa.org).
- O domínio pode conter . (ex: co.uk, gov.br).

Exemplos válidos:
- exemplo3@email.com.
- meu.email123@example.com.
- usuario+teste@email.co.uk.
- nome_sobrenome@email.org.

Exemplos inválidos:
- email@com (domínio incompleto).
- @example.com (não pode começar com @).
- email.example.com (falta o @).
- email@.com (falta o domínio)
- email@example..com (não pode ter dois pontos seguidos)

### Login
- Deve conter apenas letras (a-z, A-Z), números (0-9) e underscore (_).
- Deve ter no mínimo 5 caracteres.

Exemplos válidos:
- helloWorldu.
- meu_login.
- user01.
- log_in.

Exemplos inválidos:
- ab12 (menos de 5 caracteres).
- user!@# (contém caracteres especiais não permitidos).
- log in (espaço não é permitido).
- 12345 (não pode ser apenas números, precisa de pelo menos uma letra).

### Password
- Deve conter pelo menos 1 letra.
- Deve conter pelo menos 1 número.
- Deve ter no mínimo 6 caracteres.

Exemplos válidos:
- 123456T.
- senha1.
- senha1.
- meuPass123.
- a1b2c3d4.

Exemplos inválidos:
- abcdef (falta número).
- 123456 (falta letra).
- abc12 (menos de 6 caracteres).
- senha_123 (contém caractere especial _ não permitido).
# Rodando os testes

Para rodar os testes, rode o seguinte comando

```bash
  mvn test
```

Os testes foram apenas feitos para:
- Services: Os testes de serviço garantem que as regras de negócio funcionem corretamente. Aqui são validadas operações como cálculos, validações de dados e interações com repositórios.
- Controllers: Os testes de controller verificam se as requisições HTTP são processadas corretamente, garantindo que as respostas tenham os códigos de status adequados (200 OK, 400 Bad Request, etc.) e os dados retornados sejam os esperados.
- Utils: Testamos os métodos auxiliares da aplicação, como formatação de dados, conversões e funções reutilizáveis. Isso ajuda a evitar falhas em partes críticas do código.
- Mappers: Os testes de mappers garantem que a conversão entre entidades e DTOs (Data Transfer Objects) seja feita corretamente, preservando a integridade dos dados ao trafegar entre as camadas do sistema.

# Agendamento de Tarefas no Projeto

No projeto, há uma tarefa agendada que remove usuários inativos e seus carros associados do banco de dados. Um usuário é considerado inativo se ele não fez login há mais de 30 dias.

- Manter o banco de dados limpo: Usuários inativos ocupam espaço desnecessário no banco de dados.

- Melhorar o desempenho: Menos dados no banco de dados significa consultas mais rápidas.

- Garantir a integridade dos dados: A tarefa remove não apenas os usuários, mas também os carros associados a eles, evitando problemas de integridade.

## Como Funciona o Agendamento?

A tarefa é executada automaticamente 5 minutos após a aplicação ser iniciada. Ela segue os seguintes passos:

1. Identifica usuários inativos:
- Verifica a data do último login de cada usuário.
- Se o último login foi há mais de 30 dias, o usuário é considerado inativo.

2. Remove os carros associados:
- Antes de remover um usuário, a tarefa exclui todos os carros associados a ele.
- Isso evita erros no banco de dados, já que os carros dependem dos usuários.

3. Remove os usuários inativos:
- Após remover os carros, a tarefa exclui os usuários inativos.

## Como Testar a Funcionalidade?
Para testar a funcionalidade, siga os passos abaixo:

1. Inicie a aplicação:
- A aplicação será iniciada e o banco de dados será populado com dados de exemplo.

2.  Aguarde 5 minutos:
- Após 5 minutos, a tarefa agendada será executada automaticamente.

3. Verifique o console:
- A mensagem "Usuarios inativos removidos com sucesso." será exibida no console.

4. Verifique o banco de dados:
- Os usuários inativos e seus carros associados serão removidos do banco de dados.
# Contribuindo

Contribuições são sempre bem-vindas!

Se você encontrar problemas ou tiver sugestões de melhorias, sinta-se à vontade para abrir um problema ou enviar uma solicitação pull.