package aed.estruturas;

/**
 * Lista dinamica baseada em vetor, implementada do zero (sem ArrayList).
 * Permite acesso por indice, sendo usada para acumular os atendimentos
 * e aplicar os algoritmos de ordenacao.
 *
 * @param <T> tipo dos elementos.
 */
public class ListaDinamica<T> {

    private Object[] dados;
    private int tamanho;

    public ListaDinamica() {
        this.dados = new Object[16];
        this.tamanho = 0;
    }

    /** Adiciona um elemento ao final da lista, redimensionando se necessario. */
    public void adicionar(T valor) {
        if (tamanho == dados.length) {
            redimensionar();
        }
        dados[tamanho] = valor;
        tamanho++;
    }

    @SuppressWarnings("unchecked")
    public T get(int indice) {
        verificarIndice(indice);
        return (T) dados[indice];
    }

    public void set(int indice, T valor) {
        verificarIndice(indice);
        dados[indice] = valor;
    }

    /** Troca os elementos das posicoes i e j (usado nos algoritmos de ordenacao). */
    public void trocar(int i, int j) {
        verificarIndice(i);
        verificarIndice(j);
        Object temp = dados[i];
        dados[i] = dados[j];
        dados[j] = temp;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    private void verificarIndice(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Indice invalido: " + indice);
        }
    }

    private void redimensionar() {
        Object[] novo = new Object[dados.length * 2];
        for (int i = 0; i < tamanho; i++) {
            novo[i] = dados[i];
        }
        dados = novo;
    }
}
