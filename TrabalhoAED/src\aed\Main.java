package aed;

import aed.modelo.Guiche;
import aed.modelo.RegistroAtendimento;
import aed.modelo.Relogio;
import aed.modelo.TipoCliente;
import aed.simulacao.PostoAtendimento;

import java.util.Scanner;

/**
 * Ponto de entrada da aplicacao. Apresenta um menu interativo para
 * demonstrar o funcionamento do posto de atendimento bancario.
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PostoAtendimento posto = new PostoAtendimento();

        System.out.println("===========================================================");
        System.out.println("     SIMULADOR DE POSTO DE ATENDIMENTO BANCARIO");
        System.out.println("     Algoritmos e Estrutura de Dados - 2026/1");
        System.out.println("===========================================================");

        boolean executando = true;
        while (executando) {
            exibirMenu(posto);
            String opcao = sc.nextLine().trim();
            switch (opcao) {
                case "1":
                    adicionarCliente(sc, posto);
                    break;
                case "2":
                    chamarProximo(posto);
                    break;
                case "3":
                    System.out.println();
                    System.out.println(posto.gerarRelatorio());
                    break;
                case "4":
                    simular(sc, posto);
                    break;
                case "5":
                    verHistorico(sc, posto);
                    break;
                case "0":
                    executando = false;
                    break;
                default:
                    System.out.println(">> Opcao invalida.\n");
            }
        }

        System.out.println("Programa encerrado.");
        sc.close();
    }

    private static void exibirMenu(PostoAtendimento posto) {
        System.out.println("-----------------------------------------------------------");
        System.out.printf("Relogio: %s | Fila prioridade: %d | Fila normal: %d%n",
                Relogio.formatar(posto.getRelogio()),
                posto.getQtdFilaPrioridade(),
                posto.getQtdFilaNormal());
        System.out.println("1 - Adicionar cliente");
        System.out.println("2 - Chamar proximo (aciona os guiches livres)");
        System.out.println("3 - Imprimir relatorio de atendimentos");
        System.out.println("4 - Simulacao automatica");
        System.out.println("5 - Ver historico de um guiche");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opcao: ");
    }

    private static void adicionarCliente(Scanner sc, PostoAtendimento posto) {
        System.out.print("Tipo do cliente (1 = Normal, 2 = Prioridade): ");
        String tipo = sc.nextLine().trim();
        TipoCliente t;
        if ("2".equals(tipo)) {
            t = TipoCliente.PRIORIDADE;
        } else if ("1".equals(tipo)) {
            t = TipoCliente.NORMAL;
        } else {
            System.out.println(">> Tipo invalido. Cliente nao adicionado.\n");
            return;
        }
        RegistroAtendimento c = posto.adicionarClienteAgora(t);
        System.out.println(">> Adicionado: " + c + "\n");
    }

    private static void chamarProximo(PostoAtendimento posto) {
        posto.avancarSeNecessario();
        RegistroAtendimento a = posto.chamarProximo(posto.getPreferencial());
        RegistroAtendimento b = posto.chamarProximo(posto.getGeral1());
        RegistroAtendimento c = posto.chamarProximo(posto.getGeral2());

        imprimirChamada(posto.getPreferencial().getNome(), a);
        imprimirChamada(posto.getGeral1().getNome(), b);
        imprimirChamada(posto.getGeral2().getNome(), c);

        if (a == null && b == null && c == null) {
            System.out.println(">> Nenhum cliente chamado (filas vazias ou guiches ocupados).");
        }
        System.out.println();
    }

    private static void imprimirChamada(String guiche, RegistroAtendimento r) {
        if (r != null) {
            System.out.printf(">> %s atendeu: %s%n", guiche, r);
        }
    }

    private static void simular(Scanner sc, PostoAtendimento posto) {
        int quantidade = lerInteiro(sc, "Quantos clientes gerar? ", 1, 1000, 20);
        int chance = lerInteiro(sc, "Chance de prioridade (0 a 100%%)? ", 0, 100, 30);
        posto.simularAtendimentoAutomatico(quantidade, chance);
        System.out.println(">> Simulacao concluida.\n");
        System.out.println(posto.gerarRelatorio());
    }

    private static void verHistorico(Scanner sc, PostoAtendimento posto) {
        System.out.print("Guiche (1 = Preferencial, 2 = Geral 1, 3 = Geral 2): ");
        String op = sc.nextLine().trim();
        Guiche g;
        if ("1".equals(op)) {
            g = posto.getPreferencial();
        } else if ("2".equals(op)) {
            g = posto.getGeral1();
        } else if ("3".equals(op)) {
            g = posto.getGeral2();
        } else {
            System.out.println(">> Guiche invalido.\n");
            return;
        }
        System.out.println();
        System.out.println(posto.historicoGuiche(g));
    }

    /** Le um inteiro do teclado com validacao de intervalo e valor padrao. */
    private static int lerInteiro(Scanner sc, String mensagem, int min, int max, int padrao) {
        System.out.printf(mensagem);
        String entrada = sc.nextLine().trim();
        if (entrada.isEmpty()) {
            return padrao;
        }
        try {
            int valor = Integer.parseInt(entrada);
            if (valor < min || valor > max) {
                System.out.printf(">> Valor fora do intervalo [%d, %d]. Usando %d.%n", min, max, padrao);
                return padrao;
            }
            return valor;
        } catch (NumberFormatException e) {
            System.out.printf(">> Entrada invalida. Usando %d.%n", padrao);
            return padrao;
        }
    }
}
