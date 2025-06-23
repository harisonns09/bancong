# 💳 Bancong - API de Banco Digital

Sistema de gestão bancária desenvolvido com Spring Boot, utilizando arquitetura RESTful e persistência com JPA. Ideal para estudos de lógica de negócios bancários, como depósitos, pagamentos com taxas e transferências entre contas.

---

## 🔧 Tecnologias Utilizadas

- **Java 11**
- **Spring Boot 2.7.13**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **DevTools** (Hot Reload)
- **Maven**

---

## 📦 Endpoints

### 🔹 Conta

| Método | Endpoint           | Descrição                         |
|--------|--------------------|-----------------------------------|
| POST   | `/conta`           | Criar uma nova conta              |
| GET    | `/conta`           | Consultar saldo de uma conta      |
| GET    | `/conta/listar`    | Listar todas as contas            |

### 🔹 Transação

| Método | Endpoint                  | Descrição                         |
|--------|---------------------------|-----------------------------------|
| POST   | `/transacao`              | Realizar pagamento com taxa       |
| POST   | `/transacao/deposito`     | Realizar depósito em conta        |
| POST   | `/transacao/transferencia`| Transferência entre contas        |

---

## 💼 Regras de Negócio

- **Pagamentos** com taxas:
  - Débito (`D`) → 3%
  - Crédito (`C`) → 5%
  - PIX (`P`) → 0%
- **Transferência** exige saldo suficiente e contas diferentes.
- **Depósito** não aceita valores negativos ou zero.

---

## 🧪 Executando o Projeto

### Pré-requisitos

- Java 11
- Maven 3.8+
- PostgreSQL (ou H2 para testes)

### Passos para rodar:

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/bancong.git
cd bancong

# Compile e rode o projeto
./mvnw spring-boot:run
```

Certifique-se de configurar o banco de dados em src/main/resources/application.properties:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/bancong
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```
