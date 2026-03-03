# 📋 Sistema de Cadastro de Pessoas, Vagas e Recursos

Este projeto é uma aplicação Java com Spring Boot que permite o **cadastro de pessoas**, o controle de suas **vagas trabalhadas**, os **recursos utilizados** (como celulares e carros), e a **geração de documentos** com base nessas informações.

---

## 🧩 Funcionalidades

- 📌 Cadastro e atualização de **pessoas** por CPF
- 🅿️ Registro de **vagas** ocupadas por cada pessoa
- 🚗 Controle de **recursos utilizados**:
  - Celulares
  - Carros
- 📄 Geração de **documentos** personalizados
- 📥 Importação de dados a partir de planilhas Excel
- 🔄 Atualização automática de registros existentes durante a importação

---

## 🛠️ Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **Apache POI** – leitura de planilhas Excel
- **PostgreSQL** – banco de dados relacional
- **Lombok** – para reduzir boilerplate
- **Maven** – gerenciamento de dependências
- **Thymeleaf ou PDF Generator** – para geração de documentos
- **springdoc-openapi** – documentação da API (Swagger UI)
- **Liquibase** – versionamento do schema do banco de dados
- **Docker** – containerização da aplicação
- **JWT + OAuth2 (Google)** – autenticação e autorização

---

## 🗂️ Diagrama de Classes

```mermaid
classDiagram
    direction TB

    class Pessoa {
        +Long id
        +String cpf
        +String nome
        +String email
        +String telefone
        +LocalDate dataNascimento
        +String pis
        +String numeroRg
        +String categoriaCnh
        +String chavePix
        +byte[] foto
    }

    class Vaga {
        +Long id
        +String cidade
        +String uf
        +BigDecimal salario
        +TipoContrato tipoContrato
        +LocalDate dataAdmissao
        +LocalDate dataDemissao
        +TipoContratante contratante
        +Map atributosDinamicos
    }

    class Cliente {
        +Long id
        +String nome
        +String descricao
        +Boolean ativo
    }

    class TipoVaga {
        +Long id
        +String codigo
        +String nome
        +Boolean ativo
        +FieldSchema schema
        +List itensPadrao
    }

    class DadosBancarios {
        +Long id
        +String banco
        +String agencia
        +String conta
        +TipoConta tipoConta
    }

    class Recurso {
        <<abstract>>
        +Long id
        +LocalDate dataEntrega
        +LocalDate dataDevolucao
    }

    class RecursoDinamico {
        +Long id
        +LocalDate dataEntrega
        +LocalDate dataDevolucao
        +Map atributosSnapshot
        +List itensExtras
    }

    class ItemDinamico {
        +Long id
        +String identificador
        +Boolean ativo
        +Map atributos
    }

    class TipoRecurso {
        +Long id
        +String codigo
        +String nome
        +Boolean ativo
        +Boolean legado
        +FieldSchema schema
    }

    class TemplateDocumento {
        +Long id
        +String codigo
        +String nome
        +String conteudoHtml
        +FieldSchema schemaItens
        +List variaveisDisponiveis
        +Boolean ativo
    }

    class Usuario {
        +Long id
        +String email
        +String nome
        +Role role
        +Boolean ativo
        +LocalDateTime lastLogin
    }

    class RefreshToken {
        +Long id
        +String token
        +Instant expiryDate
    }

    class EmailPermitido {
        +Long id
        +String email
        +Role role
    }

    class FieldSchema {
        +List~FieldDefinition~ fields
        +getField(nome) FieldDefinition
        +hasField(nome) boolean
        +getCamposObrigatorios() List
    }

    class FieldDefinition {
        +String nome
        +String rotulo
        +FieldType tipo
        +Boolean obrigatorio
        +List~String~ opcoes
        +Double valorMinimo
        +Double valorMaximo
    }

    class TipoContrato {
        <<enumeration>>
    }

    class TipoContratante {
        <<enumeration>>
    }

    class TipoConta {
        <<enumeration>>
        CORRENTE
        POUPANCA
        SALARIO
    }

    class Role {
        <<enumeration>>
        USER
        ADMIN
    }

    class FieldType {
        <<enumeration>>
        STRING
        INTEGER
        DECIMAL
        DATE
        DATETIME
        BOOLEAN
        ENUM
    }

    Pessoa "1" o-- "*" Vaga : possui
    Pessoa "1" o-- "*" Recurso : utiliza
    Pessoa "1" o-- "0..1" DadosBancarios : tem
    Vaga "*" --> "1" TipoVaga : é do tipo
    Vaga "*" --> "0..1" Cliente : para o cliente
    Vaga --> TipoContrato
    Vaga --> TipoContratante
    RecursoDinamico "*" --> "1" Pessoa : emprestado a
    RecursoDinamico "*" --> "1" ItemDinamico : refere-se a
    ItemDinamico "*" --> "1" TipoRecurso : é do tipo
    TipoVaga --> FieldSchema : schema
    TipoRecurso --> FieldSchema : schema
    TemplateDocumento --> FieldSchema : schemaItens
    TemplateDocumento "*" <--> "*" TipoVaga : permitidos
    FieldSchema "1" o-- "*" FieldDefinition : campos
    FieldDefinition --> FieldType
    DadosBancarios --> TipoConta
    RefreshToken "*" --> "1" Usuario : pertence a
    EmailPermitido --> Role
    Usuario --> Role
```
