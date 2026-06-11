# Diagrama UML — Posto de Atendimento Bancário

Versão em **Mermaid** (renderiza direto no GitHub e no VS Code).
Há também a versão **PlantUML** em [`UML.puml`](UML.puml), que pode ser
renderizada em <https://www.plantuml.com/plantuml> ou pela extensão PlantUML do VS Code.

```mermaid
classDiagram
    direction LR

    class No~T~ {
        -T valor
        -No~T~ proximo
        +getValor() T
        +setProximo(No~T~) void
    }

    class Fila~T~ {
        -No~T~ inicio
        -No~T~ fim
        -int tamanho
        +enfileirar(T) void
        +desenfileirar() T
        +primeiro() T
        +estaVazia() boolean
        +tamanho() int
    }

    class Pilha~T~ {
        -No~T~ topo
        -int tamanho
        +empilhar(T) void
        +desempilhar() T
        +topo() T
        +estaVazia() boolean
        +paraLista() ListaDinamica~T~
    }

    class ListaDinamica~T~ {
        -Object[] dados
        -int tamanho
        +adicionar(T) void
        +get(int) T
        +set(int, T) void
        +trocar(int, int) void
    }

    class Comparador~T~ {
        <<interface>>
        +comparar(T, T) int
    }

    class Ordenacao {
        <<utility>>
        +quickSort(ListaDinamica~T~, Comparador~T~)$ void
        +mergeSort(ListaDinamica~T~, Comparador~T~)$ void
    }

    class TipoCliente {
        <<enumeration>>
        NORMAL
        PRIORIDADE
    }

    class TipoGuiche {
        <<enumeration>>
        PREFERENCIAL
        GERAL
    }

    class RegistroAtendimento {
        -int id
        -TipoCliente tipo
        -int horarioChegada
        -int tempoAtendimento
        -int horarioInicioAtendimento
        -int tempoEspera
        -String guicheAtendente
        +iniciarAtendimento(int, String) void
        +getHorarioFimAtendimento() int
        +isPrioritario() boolean
    }

    class Guiche {
        -String nome
        -TipoGuiche tipo
        -int totalAtendimentos
        -int totalNormais
        -int totalPrioritarios
        -int horarioLivre
        -boolean ultimoFoiPrioritario
        +estaLivre(int) boolean
        +atender(RegistroAtendimento, int) void
    }

    class PostoAtendimento {
        -int relogio
        -int proximoId
        +adicionarCliente(TipoCliente, int) RegistroAtendimento
        +chamarProximo(Guiche) RegistroAtendimento
        +simularAtendimentoAutomatico(int, int) void
        +gerarRelatorio() String
    }

    class Main {
        +main(String[])$ void
    }

    Fila "1" o-- "0..*" No : encadeia
    Pilha "1" o-- "0..*" No : encadeia
    Pilha ..> ListaDinamica : cria
    Ordenacao ..> ListaDinamica : ordena
    Ordenacao ..> Comparador : usa
    RegistroAtendimento --> TipoCliente
    Guiche --> TipoGuiche
    Guiche "1" *-- "1" Pilha : historico
    Guiche ..> RegistroAtendimento : atende
    PostoAtendimento "1" *-- "2" Fila : prioridade/normal
    PostoAtendimento "1" *-- "3" Guiche : preferencial/geral1/geral2
    PostoAtendimento ..> Ordenacao : usa
    PostoAtendimento ..> RegistroAtendimento : cria
    Main --> PostoAtendimento
```
