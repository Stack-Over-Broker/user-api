# usuario-service

Este microsserviço é responsável pelo gerenciamento de usuários no ecossistema da plataforma da corretora. Ele é construído utilizando o framework Spring Boot e faz uso de tecnologias modernas como MongoDB, Redis e SQS da AWS para garantir performance, escalabilidade e desacoplamento.

## Funcionalidades

* Cadastro de novos usuários
* Consulta de usuários por e-mail
* Cache de usuários com Redis
* Processamento assíncrono de cadastros via fila SQS

## Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot Web**
* **MongoDB** como banco de dados principal
* **Redis** para cache de usuários
* **AWS SQS** para processamento assíncrono de cadastros
* **Lombok** para redução de boilerplate

## Estrutura de Cache

O serviço utiliza o `RedisService` para cachear os dados dos usuários, evitando consultas repetidas ao MongoDB. O cache é consultado primeiro e, em caso de *miss*, o dado é buscado no banco e inserido no cache.

```java
UsuarioDTO usuarioCacheado = redisService.buscarUsuarioCacheado(email);
```

## Cadastro de Usuário

A requisição de cadastro é encaminhada para uma fila SQS. Um consumidor interno do próprio serviço processa a mensagem, validando e persistindo o usuário no MongoDB:

```java
public Usuario criarUsuario(UsuarioDTO dto) {
    Optional<Usuario> email = repository.findByEmail(dto.getEmail());
    if (email.isPresent()) {
        throw new IllegalArgumentException("Email já cadastrado");
    }
    Usuario novoUsuario = Usuario.builder()
        .nome(dto.getNome())
        .email(dto.getEmail())
        .perfilInvestidor(dto.getPerfilInvestidor())
        .criadoEm(LocalDate.now())
        .build();
    return repository.save(novoUsuario);
}
```

## Endpoints Sugeridos

* `GET /usuarios/email/{email}` - Buscar usuário por e-mail
* `POST /usuarios` - Enviar requisição de cadastro para a fila SQS

## Dependências Necessárias

* Spring Web
* Spring Data MongoDB
* Spring Data Redis
* Lombok
* AWS SDK (SQS)

## Como Aplicar em Ambiente Local

1. Suba uma instância do MongoDB e Redis com Docker
2. Configure as variáveis de ambiente para conexão
3. Rode a aplicação com `mvn spring-boot:run` ou via container
4. Teste os endpoints com Postman ou Curl

## Considerações Finais

Esse serviço deve ser escalado horizontalmente conforme a demanda de cadastros cresce. O uso de cache e processamento assíncrono garante que ele mantenha boa performance mesmo com grandes volumes de requisições.
