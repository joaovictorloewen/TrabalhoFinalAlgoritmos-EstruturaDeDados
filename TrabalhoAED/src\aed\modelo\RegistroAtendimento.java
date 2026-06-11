package aed.modelo;

/**
 * Representa um cliente e o seu atendimento no posto bancario.
 *
 * Contem, no minimo, os dados exigidos pelo enunciado:
 *   - identificador do cliente;
 *   - horario de entrada na fila (chegada);
 *   - tempo de atendimento (valor entre 2 e 30 minutos);
 *   - horario de inicio do atendimento.
 *
 * Tambem armazena o tempo de espera e o guiche que realizou o atendimento.
 */
public class RegistroAtendimento {

    private final int id;
    private final TipoCliente tipo;
    private final int horarioChegada;     // minutos desde a meia-noite
    private final int tempoAtendimento;   // duracao do atendimento (2 a 30 min)

    private int horarioInicioAtendimento; // definido ao ser chamado
    private int tempoEspera;              // inicio - chegada
    private String guicheAtendente;       // nome do guiche que atendeu
    private boolean atendido;

    public RegistroAtendimento(int id, TipoCliente tipo, int horarioChegada, int tempoAtendimento) {
        this.id = id;
        this.tipo = tipo;
        this.horarioChegada = horarioChegada;
        this.tempoAtendimento = tempoAtendimento;
        this.horarioInicioAtendimento = -1;
        this.tempoEspera = -1;
        this.guicheAtendente = null;
        this.atendido = false;
    }

    /** Registra o inicio do atendimento e calcula o tempo de espera. */
    public void iniciarAtendimento(int horarioInicio, String nomeGuiche) {
        this.horarioInicioAtendimento = horarioInicio;
        this.tempoEspera = horarioInicio - horarioChegada;
        this.guicheAtendente = nomeGuiche;
        this.atendido = true;
    }

    /** Horario em que o atendimento termina. */
    public int getHorarioFimAtendimento() {
        return horarioInicioAtendimento + tempoAtendimento;
    }

    public boolean isPrioritario() {
        return tipo == TipoCliente.PRIORIDADE;
    }

    // ------------------------- Getters -------------------------

    public int getId() {
        return id;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public int getHorarioChegada() {
        return horarioChegada;
    }

    public int getTempoAtendimento() {
        return tempoAtendimento;
    }

    public int getHorarioInicioAtendimento() {
        return horarioInicioAtendimento;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public String getGuicheAtendente() {
        return guicheAtendente;
    }

    public boolean isAtendido() {
        return atendido;
    }

    @Override
    public String toString() {
        return String.format(
            "Cliente #%-3d | %-10s | chegada %s | inicio %s | espera %3d min | atend %2d min | %s",
            id,
            tipo,
            Relogio.formatar(horarioChegada),
            (atendido ? Relogio.formatar(horarioInicioAtendimento) : "--:--"),
            (atendido ? tempoEspera : 0),
            tempoAtendimento,
            (guicheAtendente == null ? "(aguardando)" : guicheAtendente));
    }
}
