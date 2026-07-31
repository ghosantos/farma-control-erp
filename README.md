# 💊 FarmaControl ERP — Backend Core & Tests

Sistema ERP corporativo voltado para o setor farmacêutico, focado no controle de estoque, gerenciamento de medicamentos e rastreabilidade de lotes. Desenvolvido para fins de estudo e demonstração prática das especificações **Jakarta EE 10**, arquitetura em camadas e **cobertura de testes de unidade automatizados**.

---

# 🏛️ Arquitetura e Módulos

O projeto foi estruturado utilizando uma arquitetura multi-módulos gerenciada pelo **Apache Maven**, garantindo forte desacoplamento, reuso de código e isolamento de responsabilidades.

```text
farma-control-erp/
├── farma-control-shared/    # DTOs, Interfaces Remotas do EJB e Exceções de Domínio
└── farma-control-ejb/       # Entidades JPA (Hibernate), DAOs, EJB Stateless Facades e Testes
```

### 📦 farma-control-shared

Módulo leve (JAR) focado em transferir dados e contratos de interface entre a camada de negócio e os futuros clientes.

Contém:

- DTOs (Data Transfer Objects)
- Exceções de domínio customizadas
- Interfaces remotas (`@Remote`)

### 🏗️ farma-control-ejb

Módulo principal de negócio (WAR), responsável por:

- Regras de negócio da aplicação (`@Stateless`)
- Mapeamento das entidades JPA/Hibernate
- Consultas JPQL
- Persistência via DAOs
- Suíte de testes unitários

---

# 🛠️ Tecnologias Utilizadas

| Tecnologia | Utilização |
|------------|------------|
| **Linguagem** | Java 21 |
| **Plataforma** | Jakarta EE 10 (EJB 4.0 / JPA 3.1) |
| **Servidor de Aplicação** | WildFly |
| **Banco de Dados** | PostgreSQL |
| **Gerenciador de Dependências** | Apache Maven |
| **Testes Automatizados** | JUnit 5 & Mockito |
| **Logging** | `java.util.logging` integrado à infraestrutura do WildFly |

---

# ⚙️ Regras de Negócio Implementadas

- ✅ **Unicidade de Produto:** Validação para impedir o cadastro de produtos com o mesmo nome ou código de barras.
- ✅ **Unicidade do Número do Lote por Escopo:** Restrição que permite o reuso do número do lote em produtos diferentes, garantindo unicidade apenas dentro do mesmo produto (`numero_lote` + `produto_id`).
- ✅ **Baixa de Estoque com Validação de Validade:** Lotes vencidos impedem a operação de baixa de estoque.
- ✅ **Controle de Estoque Consolidado:** O estoque total do produto é calculado dinamicamente com base na soma dos seus lotes ativos.
- ✅ **Persistência em Cascata:** Suporte à criação de produtos com inclusão opcional de lote inicial em transação única (`CascadeType.ALL`).
- ✅ **Logs de Auditoria:** Registro de violações de regras de negócio (WARNING) diretamente nos logs do servidor.

---

# 🧪 Cobertura de Testes

O core do sistema conta com testes de unidade cobrindo as regras de negócio, operações de CRUD e exceções de domínio sem dependência de um servidor de aplicação ou banco de dados ativo.

## Cenários Cobertos

### ProdutoFacadeBeanTest

- Inclusão
- Atualização
- Exclusão
- Busca
- Validações de unicidade (nome e código de barras)

### LoteFacadeBeanTest

- Vinculação a produtos existentes
- Validação de número de lote duplicado por produto
- Atualização
- Remoção
- Baixa de estoque segura (bloqueio de baixa em lote vencido ou com estoque insuficiente)

---

# 🚀 Como Executar o Projeto

## Pré-requisitos

- JDK 21 instalado e configurado na variável de ambiente `JAVA_HOME`
- Apache Maven 3.9+ instalado

---

## 1️⃣ Clonar o Repositório

```text
git clone https://github.com/ghosantos/farma-control-erp.git
cd farma-control-erp
```

---

## 2️⃣ Executar a Suíte de Testes

Como se trata de um projeto multi-módulos, execute o comando a partir da raiz do projeto.
```text
mvn clean test
```
> 💡 **Nota:** Todos os testes de unidade são executados em memória via Mockito, garantindo rápida validação do código sem necessidade de subir o PostgreSQL e o WildFly.

---

## 3️⃣ Empacotar a Aplicação (.war)

Para gerar o artefato compilado para deploy.
```text
mvn clean package
```
O arquivo **`farma-control-ejb.war`** será gerado dentro da pasta:

```text
farma-control-ejb/target/
```