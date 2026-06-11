# 🏦 Simulador de Posto de Atendimento Bancário

Projeto desenvolvido para a disciplina de **Algoritmos e Estruturas de Dados (AED)** com o objetivo de simular o funcionamento de um posto de atendimento bancário utilizando estruturas de dados implementadas manualmente em Java.

## 📋 Sobre o Projeto

O sistema simula o atendimento de clientes em uma agência bancária, considerando:

* Clientes normais e prioritários;
* Filas de atendimento;
* Guichês gerais e preferenciais;
* Controle de horários de chegada e atendimento;
* Histórico de atendimentos;
* Relatórios estatísticos;
* Algoritmos de ordenação para análise dos dados.

O principal objetivo do projeto é aplicar na prática os conceitos estudados em Estruturas de Dados, sem utilizar coleções prontas da linguagem Java.

---

## 🚀 Funcionalidades

* Cadastro de clientes normais e prioritários;
* Gerenciamento de filas de espera;
* Simulação de atendimento em múltiplos guichês;
* Controle de tempo de espera;
* Histórico de atendimentos;
* Relatórios estatísticos;
* Ordenação de registros utilizando algoritmos clássicos.

---

## 🧱 Estruturas de Dados Utilizadas

### Fila (FIFO)

Utilizada para controlar a ordem de atendimento dos clientes.

**Aplicação:**

* Fila de clientes normais;
* Fila de clientes prioritários.

### Pilha (LIFO)

Utilizada para armazenar o histórico de atendimentos realizados pelos guichês.

### Lista Dinâmica

Utilizada para armazenar registros e permitir operações de busca e ordenação.

---

## 🔄 Algoritmos de Ordenação

O projeto implementa manualmente:

### QuickSort

Utilizado para ordenar registros por critérios como tempo de espera.

### MergeSort

Utilizado para ordenação cronológica dos atendimentos.

---

## 📁 Estrutura do Projeto

```text
src/
│
├── Main.java
│
├── estruturas/
│   ├── No.java
│   ├── Fila.java
│   ├── Pilha.java
│   ├── ListaDinamica.java
│   ├── Comparador.java
│   └── Ordenacao.java
│
├── modelo/
│   ├── TipoCliente.java
│   ├── TipoGuiche.java
│   ├── Relogio.java
│   ├── RegistroAtendimento.java
│   └── Guiche.java
│
└── simulacao/
    └── PostoAtendimento.java
```

---

## 🏛️ Regras de Atendimento

### Guichê Preferencial

Atende prioritariamente:

1. Clientes prioritários;
2. Clientes normais somente quando não houver prioritários aguardando.

### Guichês Gerais

Realizam atendimento alternado entre:

* Clientes prioritários;
* Clientes normais;

Essa estratégia evita que algum grupo fique muito tempo aguardando.

---

## 📊 Relatórios Gerados

O sistema permite visualizar informações como:

* Total de atendimentos realizados;
* Quantidade de clientes normais;
* Quantidade de clientes prioritários;
* Tempo médio de espera;
* Histórico de cada guichê;
* Registros ordenados por diferentes critérios.

---

## 💻 Tecnologias Utilizadas

* Java
* Programação Orientada a Objetos (POO)
* Estruturas de Dados
* Algoritmos de Ordenação

---

## 🎯 Objetivos Acadêmicos

Este projeto foi desenvolvido para demonstrar conhecimentos em:

* Filas;
* Pilhas;
* Listas Dinâmicas;
* Algoritmos de Ordenação;
* Programação Orientada a Objetos;
* Simulação de Processos Reais.

---

## 👨‍💻 Autores

Projeto desenvolvido como atividade da disciplina de **Algoritmos e Estruturas de Dados (AED)**.

---

## 📄 Licença

Este projeto possui fins exclusivamente acadêmicos e educacionais.
