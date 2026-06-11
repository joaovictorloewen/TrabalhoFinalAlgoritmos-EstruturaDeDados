package aed.modelo;

/**
 * Utilitario para tratar o tempo da simulacao.
 * O tempo e representado internamente como minutos desde a meia-noite
 * (ex.: 480 = 08:00) e pode ser formatado como "HH:MM".
 */
public final class Relogio {

    private Relogio() {
        // Classe utilitaria: nao deve ser instanciada.
    }

    public static String formatar(int minutosDesdeMeiaNoite) {
        int horas = (minutosDesdeMeiaNoite / 60) % 24;
        int minutos = minutosDesdeMeiaNoite % 60;
        return String.format("%02d:%02d", horas, minutos);
    }
}
