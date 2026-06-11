package aed.estruturas;

/**
 * Criterio de comparacao generico (substitui o Comparator nativo do Java).
 *
 * Contrato: retorna um valor
 *   menor que 0  -> 'a' deve vir antes de 'b';
 *   igual a 0    -> 'a' e 'b' sao equivalentes na ordenacao;
 *   maior que 0  -> 'a' deve vir depois de 'b'.
 *
 * @param <T> tipo dos objetos comparados.
 */
public interface Comparador<T> {
    int comparar(T a, T b);
}
