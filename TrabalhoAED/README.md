# Trabalho Prático — Algoritmos e Estrutura de Dados (2026/1)

Simulador de **posto de atendimento bancário** em Java, aplicando **fila**, **pilha**
e **algoritmos de ordenação** (QuickSort e MergeSort), todos implementados do zero,
**sem usar coleções nativas do Java** (ArrayList, LinkedList, Stack, Queue, etc.).

---

## Como compilar e executar

> Requer um **JDK** instalado (com `javac` no PATH). Java 8 ou superior.

### Opção A — script (Windows / PowerShell)
```powershell
.\compilar-e-executar.ps1
```

### Opção B — manual
```powershell
# A partir da pasta do projeto:
javac -encoding UTF-8 -d bin (Get-ChildItem -Recurse src\*.java).FullName
java -cp bin aed.Main
```

No Linux/macOS:
```bash
javac -encoding UTF-8 -d bin $(find src -name "*.java")
java -cp bin aed.Main
```

---

## Estrutura do projeto

```
TrabalhoAED/
├─ src/aed/
│  ├─ estruturas/        # estruturas e algoritmos genéricos (feitos do zero)
│  │  ├─ No.java            # nó genérico das estruturas encadeadas
│  │  ├─ Fila.java          # fila FIFO encadeada
│  │  ├─ Pilha.java         # pilha LIFO encadeada (histórico dos guichês)
│  │  ├─ ListaDinamica.java # lista por vetor (acesso por índice p/ ordenar)
│  │  ├─ Comparador.java    # interface de comparação (substitui Comparator)
│  │  └─ Ordenacao.java     # QuickSort e MergeSort
│  ├─ modelo/            # domínio do problema
│  │  ├─ TipoCliente.java
│  │  ├─ TipoGuiche.java
│  │  ├─ Relogio.java       # formata minutos como HH:MM
│  │  ├─ RegistroAtendimento.java
│  │  └─ Guiche.java
│  ├─ simulacao/
│  │  └─ PostoAtendimento.java  # núcleo: filas, guichês, lógica e relatório
│  └─ Main.java          # menu interativo
├─ docs/
│  ├─ UML.puml           # diagrama de classes (PlantUML)
│  └─ diagrama-uml.md    # diagrama de classes (Mermaid)
├─ compilar-e-executar.ps1
└─ README.md
```

---

## Menu da aplicação

1. **Adicionar cliente** — insere na fila de prioridade ou normal (chegada = relógio atual).
2. **Chamar próximo** — aciona os guichês livres seguindo a lógica de prioridade e
   calcula o tempo de espera. Se todos estiverem ocupados, o relógio avança até o
   primeiro liberar.
3. **Imprimir relatório** — totais, contagens por guichê, métricas de espera e as
   duas listagens ordenadas.
4. **Simulação automática** — gera N clientes com chegadas aleatórias e processa
   todo o expediente de ponta a ponta (boa opção para a demonstração).
5. **Ver histórico de um guichê** — desempilha (em cópia) a pilha do guichê escolhido.

---

## Como o enunciado foi atendido

| Requisito do enunciado | Onde está |
| --- | --- |
| Duas filas: `FilaPrioridade` e `FilaNormal` | `PostoAtendimento` (dois objetos `Fila`) |
| Classe `RegistroAtendimento` (id, chegada, tempo de atendimento 2–30, início) | `modelo/RegistroAtendimento.java` |
| Guichê preferencial (prioridade; normal só se prioridade vazia) | `selecionarParaPreferencial()` |
| Dois guichês gerais com alternância das filas | `selecionarParaGeral()` + flag `ultimoFoiPrioritario` |
| Pilha de histórico por guichê | `Guiche.historico : Pilha<RegistroAtendimento>` |
| Adicionar cliente | `adicionarCliente(...)` |
| Chamar próximo + cálculo do tempo de espera | `chamarProximo(...)` / `RegistroAtendimento.iniciarAtendimento(...)` |
| Relatório: totais, por guichê, normal vs. prioritário | `gerarRelatorio()` |
| Métricas: média total, por prioridade e por normal | `gerarRelatorio()` |
| Ordenação por tempo de espera (crescente) | **QuickSort** em `Ordenacao` |
| Ordenação cronológica (horário de atendimento) | **MergeSort** em `Ordenacao` |
| Não usar estruturas nativas do Java | tudo em `aed/estruturas` é implementado do zero |

> Observação sobre as restrições: `java.util.Random` (sorteio de tempos/tipos) e
> `java.util.Scanner` (leitura do menu) **não são estruturas de dados** — são apenas
> utilitários de I/O e geração de números. Nenhuma coleção nativa (ArrayList, etc.)
> foi utilizada para armazenar os dados.

---

## Lógica de atendimento (resumo)

- **Guichê Preferencial:** sempre tenta a `FilaPrioridade`; só atende a `FilaNormal`
  quando a de prioridade está vazia.
- **Guichês Gerais:** se o último cliente atendido naquele guichê foi prioritário,
  o próximo a ser chamado vem preferencialmente da `FilaNormal` (equilibra a espera);
  caso contrário, prioriza a `FilaPrioridade`. Se a fila preferida estiver vazia,
  usa a outra.
- O **tempo de espera** de cada cliente = horário de início do atendimento − horário
  de chegada.

---

## Diagrama UML

Veja [`docs/diagrama-uml.md`](docs/diagrama-uml.md) (Mermaid) ou
[`docs/UML.puml`](docs/UML.puml) (PlantUML).
