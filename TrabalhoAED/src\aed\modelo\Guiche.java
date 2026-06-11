package aed.modelo;

import aed.estruturas.Pilha;

/**
 * Representa um guiche de atendimento.
 *
 * Cada guiche mantem uma Pilha com o historico dos clientes que atendeu
 * (o ultimo atendido fica no topo) e estatisticas proprias. O atributo
 * horarioLivre indica em que minuto o guiche volta a ficar disponivel,
 * e ultimoFoiPrioritario apoia a logica de alternancia dos guiches gerais.
 */
public class Guiche {

    private final String nome;
    private final TipoGuiche tipo;
    private final Pilha<RegistroAtendimento> historico;

    private int totalAtendimentos;
    private int totalNormais;
    private int totalPrioritarios;

    private int horarioLivre;             // minuto em que o guiche fica livre
    private boolean ultimoFoiPrioritario; // controla a alternancia (guiches gerais)

    public Guiche(String nome, TipoGuiche tipo) {
        this.nome = nome;
        this.tipo = tipo;
        this.historico = new Pilha<RegistroAtendimento>();
        this.totalAtendimentos = 0;
        this.totalNormais = 0;
        this.totalPrioritarios = 0;
        this.horarioLivre = 0;
        this.ultimoFoiPrioritario = false;
    }

    /** O guiche esta livre quando o relogio ja alcancou o minuto em que ele liberou. */
    public boolean estaLivre(int relogio) {
        return relogio >= horarioLivre;
    }

    /** Efetiva o atendimento de um cliente neste guiche. */
    public void atender(RegistroAtendimento cliente, int horarioInicio) {
        cliente.iniciarAtendimento(horarioInicio, nome);
        historico.empilhar(cliente);
        totalAtendimentos++;
        if (cliente.isPrioritario()) {
            totalPrioritarios++;
            ultimoFoiPrioritario = true;
        } else {
            totalNormais++;
            ultimoFoiPrioritario = false;
        }
        horarioLivre = cliente.getHorarioFimAtendimento();
    }

    // ------------------------- Getters -------------------------

    public String getNome() {
        return nome;
    }

    public TipoGuiche getTipo() {
        return tipo;
    }

    public Pilha<RegistroAtendimento> getHistorico() {
        return historico;
    }

    public int getTotalAtendimentos() {
        return totalAtendimentos;
    }

    public int getTotalNormais() {
        return totalNormais;
    }

    public int getTotalPrioritarios() {
        return totalPrioritarios;
    }

    public int getHorarioLivre() {
        return horarioLivre;
    }

    public boolean isUltimoFoiPrioritario() {
        return ultimoFoiPrioritario;
    }
}
