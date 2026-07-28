# 💊 FarmaControl

Sistema corporativo voltado para o setor farmacêutico, focado no controle de estoque, gerenciamento de medicamentos e rastreabilidade de lotes. Desenvolvido para fins de estudo das especificações **Jakarta EE** e arquitetura distribuída.

---

## 🏛️ Arquitetura e Módulos

O projeto foi estruturado utilizando uma arquitetura multi-módulos gerenciada pelo **Maven**, garantindo o desacoplamento de responsabilidades:

* **`farma-control-shared`**: Módulo comum/compartilhado que contém os DTOs (*Data Transfer Objects*), exceções de domínio customizadas e as interfaces remotas (`@Remote`).
* **`farma-control-ejb`**: Módulo EJB responsável pelas regras de negócio da aplicação (`@Stateless`), mapeamento das entidades JPA/Hibernate, consultas JPQL e persistência via DAOs.
* **`farma-control-swing`** *(Em desenvolvimento)*: Interface gráfica desktop desenvolvida com Java Swing para interação do usuário e comunicação remota com a camada EJB.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 21+
* **Plataforma:** Jakarta EE 10 (EJB 4.0 / JPA 3.1)
* **Servidor de Aplicação:** WildFly
* **Banco de Dados:** PostgreSQL
* **Gerenciador de Dependências:** Apache Maven
* **Interface Gráfica:** Java Swing

---

## ⚙️ Regras de Negócio Implementadas

- [x] **Unicidade de Produto:** Validação para impedir o cadastro de produtos com o mesmo nome ou código de barras.
- [x] **Unicidade do Número do Lote por Escopo:** Restrição que permite o reuso do número do lote em produtos diferentes, garantindo unicidade apenas dentro do mesmo produto (`numero_lote` + `produto_id`).
- [x] **Baixa de Estoque com Validação de Validade:** Lotes vencidos não permitem a operação de baixa.
- [x] **Controle de Estoque Consolidado:** O estoque total do produto é calculado dinamicamente com base na soma dos seus lotes ativos.
- [x] **Persistência em Cascata:** Suporte à criação de produtos com inclusão opcional de lote inicial em transação única (`CascadeType.ALL`).

---

## 🚀 Como Executar o Projeto

*(Instruções detalhadas e passo a passo de deployment no WildFly serão adicionadas após a finalização do módulo Swing)*