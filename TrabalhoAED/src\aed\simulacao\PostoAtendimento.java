package aed.simulacao;

import aed.estruturas.Comparador;
import aed.estruturas.Fila;
import aed.estruturas.ListaDinamica;
import aed.estruturas.Ordenacao;
import aed.estruturas.Pilha;
import aed.modelo.Guiche;
import aed.modelo.RegistroAtendimento;
import aed.modelo.Relogio;
import aed.modelo.TipoCliente;
import aed.modelo.TipoGuiche;

import java.util.Random;

/**
 * Nucleo da aplicacao: gerencia as duas filas de clientes (prioridade e normal),
 * os tres guiches (um preferencial e dois gerais), a logica de prioridade entre
 * eles, o relogio da simulacao e a geracao do relatorio.
 */
public class PostoAtendimento {

    /** Horario de abertura do posto: 08:00 (em minutos desde a meia-noite). */
    public static final int ABERTURA = 8 * 60;

    private final Fila<RegistroAtendimento> filaPrioridade;
    private final Fila<RegistroAtendimento> filaNormal;

    private final Guiche preferencial;
    private final Guiche geral1;
    private final Guiche geral2;

    private int relogio;      // minuto atual da simulacao
    private int proximoId;    // gerador sequencial de identificadores
    private final Random sorteio;

    public PostoAtendimento() {
        this.filaPrioridade = new Fila<RegistroAtendimento>();
        this.filaNormal = new Fila<RegistroAtendimento>();
        this.preferencial = new Guiche("Guiche Preferencial", TipoGuiche.PREFERENCIAL);
        this.geral1 = new Guiche("Guiche Geral 1", TipoGuiche.GERAL);
        this.geral2 = new Guiche("Guiche Geral 2", TipoGuiche.GERAL);
        this.relogio = ABERTURA;
        this.proximoId = 1;
        this.sorteio = new Random();
    }

    // ===================== Cadastro de clientes =====================

    /**
     * Adiciona um cliente na fila correspondente, com chegada no horario informado.
     * O tempo de atendimento e sorteado entre 2 e 30 minutos.
     */
    public RegistroAtendimento adicionarCliente(TipoCliente tipo, int horarioChegada) {
        int tempoAtendimento = sortearTempoAtendimento();
        RegistroAtendimento cliente =
                new RegistroAtendimento(proximoId++, tipo, horarioChegada, tempoAtendimento);
        enfileirar(cliente);
        return cliente;
    }

    /** Adiciona um cliente que chega no minuto atual do relogio. */
    public RegistroAtendimento adicionarClienteAgora(TipoCliente tipo) {
        return adicionarCliente(tipo, relogio);
    }

    private void enfileirar(RegistroAtendimento cliente) {
        if (cliente.isPrioritario()) {
            filaPrioridade.enfileirar(cliente);
        } else {
            filaNormal.enfileirar(cliente);
        }
    }

    private int sortearTempoAtendimento() {
        // 2 + [0..28] => intervalo fechado de 2 a 30 minutos.
        return 2 + sorteio.nextInt(29);
    }

    // ===================== Logica de atendimento =====================

    /**
     * Seleciona o proximo cliente para o GUICHE PREFERENCIAL:
     * atende a FilaPrioridade; somente caso esta esteja vazia atende a FilaNormal.
     */
    private RegistroAtendimento selecionarParaPreferencial() {
        if (!filaPrioridade.estaVazia()) {
            return filaPrioridade.desenfileirar();
        }
        if (!filaNormal.estaVazia()) {
            return filaNormal.desenfileirar();
        }
        return null;
    }

    /**
     * Seleciona o proximo cliente para um GUICHE GERAL, alternando as filas:
     * se o ultimo cliente atendido por este guiche foi prioritario, tenta a
     * FilaNormal primeiro (equilibrando o tempo de espera); caso contrario,
     * prioriza a FilaPrioridade. Se a fila preferida estiver vazia, usa a outra.
     */
    private RegistroAtendimento selecionarParaGeral(Guiche guiche) {
        boolean preferirNormal = guiche.isUltimoFoiPrioritario();
        if (preferirNormal) {
            if (!filaNormal.estaVazia()) {
                return filaNormal.desenfileirar();
            }
            if (!filaPrioridade.estaVazia()) {
                return filaPrioridade.desenfileirar();
            }
        } else {
            if (!filaPrioridade.estaVazia()) {
                return filaPrioridade.desenfileirar();
            }
            if (!filaNormal.estaVazia()) {
                return filaNormal.desenfileirar();
            }
        }
        return null;
    }

    /**
     * Chama o proximo cliente para um guiche especifico, caso ele esteja livre.
     * Calcula o tempo de espera (inicio do atendimento - chegada) e registra
     * o cliente no historico do guiche.
     *
     * @return o registro atendido, ou null se nao houve atendimento.
     */
    public RegistroAtendimento chamarProximo(Guiche guiche) {
        if (!guiche.estaLivre(relogio)) {
            return null;
        }
        RegistroAtendimento cliente;
        if (guiche.getTipo() == TipoGuiche.PREFERENCIAL) {
            cliente = selecionarParaPreferencial();
        } else {
            cliente = selecionarParaGeral(guiche);
        }
        if (cliente == null) {
            return null;
        }
        guiche.atender(cliente, relogio);
        return cliente;
    }

    /** Tenta acionar os tres guiches no minuto atual. */
    public void chamarProximoTodos() {
        chamarProximo(preferencial);
        chamarProximo(geral1);
        chamarProximo(geral2);
    }

    public boolean haClientesEsperando() {
        return !filaPrioridade.estaVazia() || !filaNormal.estaVazia();
    }

    private boolean algumGuicheOcupado() {
        return !preferencial.estaLivre(relogio)
                || !geral1.estaLivre(relogio)
                || !geral2.estaLivre(relogio);
    }

    // ===================== Controle do relogio =====================

    public void avancarRelogio() {
        relogio++;
    }

    public void avancarRelogioPara(int minuto) {
        if (minuto > relogio) {
            relogio = minuto;
        }
    }

    /**
     * Modo interativo: se houver clientes esperando mas todos os guiches
     * estiverem ocupados, avanca o relogio ate o primeiro guiche liberar.
     */
    public void avancarSeNecessario() {
        if (haClientesEsperando() && !preferencial.estaLivre(relogio)
                && !geral1.estaLivre(relogio) && !geral2.estaLivre(relogio)) {
            int menor = Math.min(preferencial.getHorarioLivre(),
                    Math.min(geral1.getHorarioLivre(), geral2.getHorarioLivre()));
            avancarRelogioPara(menor);
        }
    }

    // ===================== Simulacao automatica =====================

    /**
     * Executa uma simulacao automatica de ponta a ponta: gera 'quantidade'
     * clientes com chegadas aleatorias ao longo do expediente e processa
     * todos os atendimentos respeitando a logica de prioridade.
     *
     * @param quantidade          numero de clientes a gerar.
     * @param chanceDePrioridade  probabilidade (0 a 100) de um cliente ser prioritario.
     */
    public void simularAtendimentoAutomatico(int quantidade, int chanceDePrioridade) {
        ListaDinamica<RegistroAtendimento> pendentes =
                gerarClientesPendentes(quantidade, chanceDePrioridade);
        int indicePendente = 0;

        while (indicePendente < pendentes.tamanho() || haClientesEsperando() || algumGuicheOcupado()) {
            // 1) Enfileira os clientes que ja chegaram ate o minuto atual.
            while (indicePendente < pendentes.tamanho()
                    && pendentes.get(indicePendente).getHorarioChegada() <= relogio) {
                enfileirar(pendentes.get(indicePendente));
                indicePendente++;
            }

            // 2) Aciona os guiches que estiverem livres.
            chamarProximoTodos();

            // 3) Avanca o relogio para o proximo evento relevante.
            int proximoEvento = proximoMinutoRelevante(pendentes, indicePendente);
            if (proximoEvento == Integer.MAX_VALUE) {
                break; // nao ha mais nada a processar
            }
            if (proximoEvento <= relogio) {
                relogio++;
            } else {
                relogio = proximoEvento;
            }
        }
    }

    private ListaDinamica<RegistroAtendimento> gerarClientesPendentes(int quantidade, int chanceDePrioridade) {
        ListaDinamica<RegistroAtendimento> pendentes = new ListaDinamica<RegistroAtendimento>();
        int chegada = relogio;
        for (int i = 0; i < quantidade; i++) {
            chegada += sorteio.nextInt(6); // 0 a 5 minutos entre chegadas
            TipoCliente tipo = (sorteio.nextInt(100) < chanceDePrioridade)
                    ? TipoCliente.PRIORIDADE : TipoCliente.NORMAL;
            int tempoAtendimento = sortearTempoAtendimento();
            pendentes.adicionar(new RegistroAtendimento(proximoId++, tipo, chegada, tempoAtendimento));
        }
        return pendentes;
    }

    /** Menor minuto futuro relevante: proxima chegada ou liberacao de guiche. */
    private int proximoMinutoRelevante(ListaDinamica<RegistroAtendimento> pendentes, int indicePendente) {
        int proximo = Integer.MAX_VALUE;
        if (indicePendente < pendentes.tamanho()) {
            proximo = Math.min(proximo, pendentes.get(indicePendente).getHorarioChegada());
        }
        proximo = Math.min(proximo, candidatoLiberacao(preferencial));
        proximo = Math.min(proximo, candidatoLiberacao(geral1));
        proximo = Math.min(proximo, candidatoLiberacao(geral2));
        return proximo;
    }

    private int candidatoLiberacao(Guiche g) {
        return g.getHorarioLivre() > relogio ? g.getHorarioLivre() : Integer.MAX_VALUE;
    }

    // ===================== Coleta e relatorio =====================

    /** Reune, em uma unica lista, todos os clientes atendidos pelos tres guiches. */
    public ListaDinamica<RegistroAtendimento> coletarAtendimentos() {
        ListaDinamica<RegistroAtendimento> todos = new ListaDinamica<RegistroAtendimento>();
        copiarPilha(preferencial.getHistorico(), todos);
        copiarPilha(geral1.getHistorico(), todos);
        copiarPilha(geral2.getHistorico(), todos);
        return todos;
    }

    private void copiarPilha(Pilha<RegistroAtendimento> pilha, ListaDinamica<RegistroAtendimento> destino) {
        ListaDinamica<RegistroAtendimento> temp = pilha.paraLista();
        for (int i = 0; i < temp.tamanho(); i++) {
            destino.adicionar(temp.get(i));
        }
    }

    private ListaDinamica<RegistroAtendimento> copiar(ListaDinamica<RegistroAtendimento> origem) {
        ListaDinamica<RegistroAtendimento> nova = new ListaDinamica<RegistroAtendimento>();
        for (int i = 0; i < origem.tamanho(); i++) {
            nova.adicionar(origem.get(i));
        }
        return nova;
    }

    /**
     * Monta o relatorio consolidado de atendimentos exigido pelo enunciado.
     */
    public String gerarRelatorio() {
        StringBuilder sb = new StringBuilder();
        ListaDinamica<RegistroAtendimento> todos = coletarAtendimentos();
        int total = todos.tamanho();

        sb.append("==================================================================\n");
        sb.append("                  RELATORIO DE ATENDIMENTOS\n");
        sb.append("==================================================================\n");
        sb.append(String.format("Horario atual da simulacao: %s%n", Relogio.formatar(relogio)));
        sb.append(String.format("Total de atendimentos realizados: %d%n", total));

        sb.append("\n--- Atendimentos por guiche ---\n");
        sb.append(linhaGuiche(preferencial));
        sb.append(linhaGuiche(geral1));
        sb.append(linhaGuiche(geral2));

        // Metricas de desempenho.
        int somaTotal = 0, somaPri = 0, somaNor = 0, qtdPri = 0, qtdNor = 0;
        for (int i = 0; i < total; i++) {
            RegistroAtendimento r = todos.get(i);
            somaTotal += r.getTempoEspera();
            if (r.isPrioritario()) {
                somaPri += r.getTempoEspera();
                qtdPri++;
            } else {
                somaNor += r.getTempoEspera();
                qtdNor++;
            }
        }

        sb.append("\n--- Metricas de desempenho (tempo de espera) ---\n");
        sb.append(String.format("  Tempo medio de espera (total):       %s min%n", media(somaTotal, total)));
        sb.append(String.format("  Tempo medio de espera (prioritario): %s min%n", media(somaPri, qtdPri)));
        sb.append(String.format("  Tempo medio de espera (normal):      %s min%n", media(somaNor, qtdNor)));

        if (total == 0) {
            sb.append("\n(Sem atendimentos registrados ainda.)\n");
            sb.append("==================================================================\n");
            return sb.toString();
        }

        // Relacao 1: ordem crescente de tempo de espera (QuickSort).
        ListaDinamica<RegistroAtendimento> porEspera = copiar(todos);
        Ordenacao.quickSort(porEspera, new Comparador<RegistroAtendimento>() {
            public int comparar(RegistroAtendimento a, RegistroAtendimento b) {
                return Integer.compare(a.getTempoEspera(), b.getTempoEspera());
            }
        });

        sb.append("\n--- Relacao de atendimentos: ORDEM CRESCENTE DE TEMPO DE ESPERA (QuickSort) ---\n");
        sb.append(listar(porEspera));

        // Relacao 2: ordem cronologica pelo horario de inicio do atendimento (MergeSort).
        ListaDinamica<RegistroAtendimento> porInicio = copiar(todos);
        Ordenacao.mergeSort(porInicio, new Comparador<RegistroAtendimento>() {
            public int comparar(RegistroAtendimento a, RegistroAtendimento b) {
                return Integer.compare(a.getHorarioInicioAtendimento(), b.getHorarioInicioAtendimento());
            }
        });

        sb.append("\n--- Relacao de atendimentos: ORDEM CRONOLOGICA DE ATENDIMENTO (MergeSort) ---\n");
        sb.append(listar(porInicio));

        sb.append("==================================================================\n");
        return sb.toString();
    }

    /** Historico (pilha) de um guiche, do mais recente ao mais antigo. */
    public String historicoGuiche(Guiche g) {
        StringBuilder sb = new StringBuilder();
        sb.append("Historico de ").append(g.getNome())
          .append(" (do mais recente ao mais antigo):\n");
        ListaDinamica<RegistroAtendimento> lista = g.getHistorico().paraLista();
        if (lista.estaVazia()) {
            sb.append("  (nenhum atendimento)\n");
        } else {
            sb.append(listar(lista));
        }
        return sb.toString();
    }

    private String listar(ListaDinamica<RegistroAtendimento> lista) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.tamanho(); i++) {
            sb.append("  ").append(lista.get(i)).append("\n");
        }
        return sb.toString();
    }

    private String linhaGuiche(Guiche g) {
        return String.format("  %-20s | total: %2d | normais: %2d | prioritarios: %2d%n",
                g.getNome(), g.getTotalAtendimentos(), g.getTotalNormais(), g.getTotalPrioritarios());
    }

    private String media(int soma, int qtd) {
        if (qtd == 0) {
            return "0,00";
        }
        return String.format("%.2f", (double) soma / qtd);
    }

    // ===================== Getters de estado =====================

    public Guiche getPreferencial() {
        return preferencial;
    }

    public Guiche getGeral1() {
        return geral1;
    }

    public Guiche getGeral2() {
        return geral2;
    }

    public int getRelogio() {
        return relogio;
    }

    public int getQtdFilaPrioridade() {
        return filaPrioridade.tamanho();
    }

    public int getQtdFilaNormal() {
        return filaNormal.tamanho();
    }
}
