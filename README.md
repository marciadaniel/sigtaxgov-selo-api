# 🖥️ SigTaxGov Backend – API de Validação de Selo Fiscal

Este projeto é uma API REST que permite validar um selo fiscal com base em seu código.

**Tecnologias Utilizadas**

- **Java 21**
- **Spring Boot 3.5**
- **Spring Data JPA**
- **Spring Validation**
- **Gradle**
- **PostgreSQL Database**
- **Spring Data Redis (Access+Driver)**

**⚙️ Como Executar**

```
git clone https://github.com/seu-usuario/sigtaxgov-selo-api.git
cd sigtaxgov-selo-api
./gradlew bootRun
```

A API utilizará um banco PostgreSQL local. Configure as credenciais no arquivo application.yml ou application.properties:

Para rodar esta aplicação, você precisará configurar um ambiente local com os seguintes serviços:

- **Banco de Dados PostgreSQL:**
    - É necessário ter uma instância do PostgreSQL instalada e rodando localmente.
    - Crie um banco de dados para a aplicação.
    - A API utilizará este banco PostgreSQL local. Configure as credenciais de conexão (URL, usuário e senha) no arquivo `src/main/resources/application.properties` (ou `application.yml`).
- **Servidor Redis:**
    - Instale e garanta que o Redis esteja em execução em sua máquina local.
    - As configurações de conexão para o Redis também devem ser especificadas no arquivo `src/main/resources/application.properties` (ou `application.yml`).

```
spring.datasource.url=jdbc:postgresql://localhost:5432/sigtaxgov
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.data.redis.host=localhost
spring.data.redis.port=6379
```

API disponível em:

```
http://localhost:8080/api/v1/selos/{codigo}
```
Podes testar a API usando esta coleção do Postman:
```
[https://www.postman.com/navigation-physicist-86609621/workspace/public/collection/26062719-9cd132d9-2ea7-41a9-b55d-09ce50828ffb?action=share&creator=26062719](https://www.postman.com/navigation-physicist-86609621/workspace/public/collection/26062719-9cd132d9-2ea7-41a9-b55d-09ce50828ffb?action=share&creator=26062719)
```
**Endpoints**

- `GET GET /api/v1/selos?status=VALIDO` que retorna os dados de um selo fiscal com base no tipo de status.
- `GET /api/v1/selos/{codigo}` que retorna os dados de um selo fiscal com base em seu código.
- `GET /api/v1/selos/recentes` que retorna os últimos códigos consultados.
- `POST /api/v1/selos/{codigo}` que cria um novo selo.
    
    Exemplo de entrada:
    
    ```
    {
      "tipo_produto": "tabaco",
      "fabricante": "Tabacos Angola Ltda",
      "status": "EXPIRADO"
    }
    ```
    

      Exemplo de saída:

```
{
    "codigo": "SEL-2025-0001",
    "tipoProduto": "tabaco",
    "fabricante": "Tabacos Angola Ltda",
    "dataEmissao": "2025-06-15",
    "status": "EXPIRADO"
}
```