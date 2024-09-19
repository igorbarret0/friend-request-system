<h1>Sistema de Solicitação de Amizade</h1>

Este projeto é uma aplicação que simula um sistema de solicitação de amizade, permitindo que os usuários se registrem, façam login e gerenciem suas conexões com outros usuários. A aplicação oferece funcionalidades como envio de pedidos de amizade, aceitação ou rejeição de solicitações e a visualização da lista de amigos.
## Tecnologias

- [Spring Boot](https://spring.io/projects/spring-boot)
- [MySQL](https://www.mysql.com/)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [JWT](https://jwt.io/)
- [Spring Security](https://spring.io/projects/spring-security)
- [JUnit5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)

## Como Executar

- Clonar repositório git
```
git clone git@github.com:igorbarret0/friend-request-system.git
```

- Construir o projeto:
```
./mvnw clean package
```

A API poderá ser acessada em [localhost:8080](http://localhost:8080).

## API Endpoints

Para fazer as requisições HTTP abaixo, foi utilizada a ferramenta [Postman](https://www.postman.com/)

-  Rotas Públicas
```
POST /user - Criar conta

{
    "username": "yourName",
    "profileVisibility": "PUBLIC",
    "password": "yourPassword"
}
```

```
POST /login - Realizar login na conta

{
    "username": "Alanzuka",
    "password": "123456asdfA"
}
```
- Rotas Protegidas (Requerem Autenticação)

```
POST /friendship/requester?friendId= - Enviar pedido de amizade
```

```
POST /friendship/accept?requesterId= - Aceitar pedido de amizade
```

```
POST /friendship/decline?requesterId= - Recusar pedido de amizade
```

```
GET /friendship - Obter lista de amizadeds
```


