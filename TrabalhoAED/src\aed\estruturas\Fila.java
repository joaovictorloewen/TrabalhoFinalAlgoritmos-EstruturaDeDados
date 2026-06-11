package aed.estruturas;

/**
 * Fila (FIFO - First In, First Out) encadeada, implementada do zero,
 * sem utilizar colecoes nativas do Java.
 *
 * @param <T> tipo dos elementos da fila.
 */
public class Fila<T> {

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    public Fila() {
        this.inicio = null;
        this.fim = null;
        this.tamanho = 0;
    }

    /** Insere um elemento no fim da fila. */
    public void enfileirar(T valor) {
        No<T> novo = new No<T>(valor);
        if (estaVazia()) {
            inicio = novo;
        } else {
            fim.setProximo(novo);
        }
        fim = novo;
        tamanho++;
    }

    /** Remove e retorna o elemento que esta no inicio da fila. */
    public T desenfileirar() {
        if (estaVazia()) {
            throw new IllegalStateException("Fila vazia: nao ha elemento para desenfileirar.");
        }
        No<T> removido = inicio;
        inicio = inicio.getProximo();
        if (inicio == null) {
            fim = null;
        }
        removido.setProximo(null);
        tamanho--;
        return removido.getValor();
    }

    /** Consulta (sem remover) o elemento do inicio da fila. */
    public T primeiro() {
        if (estaVazia()) {
            throw new IllegalStateException("Fila vazia.");
        }
        return inicio.getValor();
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }
}
