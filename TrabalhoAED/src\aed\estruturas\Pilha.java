package aed.estruturas;

/**
 * Pilha (LIFO - Last In, First Out) encadeada, implementada do zero.
 * Cada guiche utiliza uma pilha para guardar o historico de clientes atendidos.
 *
 * @param <T> tipo dos elementos da pilha.
 */
public class Pilha<T> {

    private No<T> topo;
    private int tamanho;

    public Pilha() {
        this.topo = null;
        this.tamanho = 0;
    }

    /** Empilha um elemento no topo. */
    public void empilhar(T valor) {
        No<T> novo = new No<T>(valor);
        novo.setProximo(topo);
        topo = novo;
        tamanho++;
    }

    /** Desempilha e retorna o elemento do topo. */
    public T desempilhar() {
        if (estaVazia()) {
            throw new IllegalStateException("Pilha vazia.");
        }
        No<T> removido = topo;
        topo = topo.getProximo();
        removido.setProximo(null);
        tamanho--;
        return removido.getValor();
    }

    /** Consulta (sem remover) o elemento do topo. */
    public T topo() {
        if (estaVazia()) {
            throw new IllegalStateException("Pilha vazia.");
        }
        return topo.getValor();
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }

    /**
     * Copia os elementos da pilha para uma ListaDinamica, do topo para a base,
     * SEM destruir a pilha. Util para gerar relatorios e consultar o historico.
     */
    public ListaDinamica<T> paraLista() {
        ListaDinamica<T> lista = new ListaDinamica<T>();
        No<T> atual = topo;
        while (atual != null) {
            lista.adicionar(atual.getValor());
            atual = atual.getProximo();
        }
        return lista;
    }
}
