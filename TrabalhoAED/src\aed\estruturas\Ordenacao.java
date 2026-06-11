package aed.estruturas;

/**
 * Algoritmos de ordenacao implementados do zero: QuickSort e MergeSort.
 * Ambos operam sobre uma ListaDinamica utilizando um Comparador para
 * definir o criterio de ordem.
 */
public final class Ordenacao {

    private Ordenacao() {
        // Classe utilitaria: nao deve ser instanciada.
    }

    // ----------------------------- QuickSort -----------------------------

    /** Ordena a lista inteira usando QuickSort. */
    public static <T> void quickSort(ListaDinamica<T> lista, Comparador<T> c) {
        quickSort(lista, 0, lista.tamanho() - 1, c);
    }

    private static <T> void quickSort(ListaDinamica<T> lista, int inicio, int fim, Comparador<T> c) {
        if (inicio < fim) {
            int posPivo = particionar(lista, inicio, fim, c);
            quickSort(lista, inicio, posPivo - 1, c);
            quickSort(lista, posPivo + 1, fim, c);
        }
    }

    private static <T> int particionar(ListaDinamica<T> lista, int inicio, int fim, Comparador<T> c) {
        T pivo = lista.get(fim);
        int i = inicio - 1;
        for (int j = inicio; j < fim; j++) {
            if (c.comparar(lista.get(j), pivo) <= 0) {
                i++;
                lista.trocar(i, j);
            }
        }
        lista.trocar(i + 1, fim);
        return i + 1;
    }

    // ----------------------------- MergeSort -----------------------------

    /** Ordena a lista inteira usando MergeSort. */
    public static <T> void mergeSort(ListaDinamica<T> lista, Comparador<T> c) {
        if (lista.tamanho() > 1) {
            Object[] auxiliar = new Object[lista.tamanho()];
            mergeSort(lista, 0, lista.tamanho() - 1, c, auxiliar);
        }
    }

    private static <T> void mergeSort(ListaDinamica<T> lista, int inicio, int fim,
                                      Comparador<T> c, Object[] aux) {
        if (inicio < fim) {
            int meio = (inicio + fim) / 2;
            mergeSort(lista, inicio, meio, c, aux);
            mergeSort(lista, meio + 1, fim, c, aux);
            merge(lista, inicio, meio, fim, c, aux);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> void merge(ListaDinamica<T> lista, int inicio, int meio, int fim,
                                  Comparador<T> c, Object[] aux) {
        // Copia o trecho para o vetor auxiliar.
        for (int k = inicio; k <= fim; k++) {
            aux[k] = lista.get(k);
        }
        int i = inicio;       // indice na metade esquerda
        int j = meio + 1;     // indice na metade direita
        for (int k = inicio; k <= fim; k++) {
            if (i > meio) {
                lista.set(k, (T) aux[j++]);
            } else if (j > fim) {
                lista.set(k, (T) aux[i++]);
            } else if (c.comparar((T) aux[i], (T) aux[j]) <= 0) {
                lista.set(k, (T) aux[i++]);
            } else {
                lista.set(k, (T) aux[j++]);
            }
        }
    }
}
