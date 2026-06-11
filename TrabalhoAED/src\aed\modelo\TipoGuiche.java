package aed.modelo;

/**
 * Tipo do guiche de atendimento.
 *
 * PREFERENCIAL - atende a fila de prioridade; so atende a normal se a de
 *                prioridade estiver vazia.
 * GERAL        - alterna entre as filas para equilibrar o tempo de espera.
 */
public enum TipoGuiche {
    PREFERENCIAL,
    GERAL
}
