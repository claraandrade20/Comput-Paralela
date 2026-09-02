package paralelo;

public class ParallelSumExample {

    public static void main(String[] args) {
        // n maior para o tempo de execução ficar mensurável
        int n = 50_000_000;
        int p = 4; // número de processadores (núcleos)

        //versão sequencial
        long inicioMillisSeq = System.currentTimeMillis();
        long inicioNanoSeq = System.nanoTime();

        long somaSequencial = somarSequencial(n);

        long fimNanoSeq = System.nanoTime();
        long fimMillisSeq = System.currentTimeMillis();

        long tempoMillisSeq = fimMillisSeq - inicioMillisSeq;
        long tempoNanoSeq = fimNanoSeq - inicioNanoSeq;

        System.out.println("Versão SEQUENCIAL");
        System.out.println("Soma total = " + somaSequencial);
        System.out.println("Tempo (currentTimeMillis) = " + tempoMillisSeq + " ms");
        System.out.println("Tempo (nanoTime) = " + tempoNanoSeq + " ns ("
                + (tempoNanoSeq / 1_000_000.0) + " ms)");
        System.out.println();

        // ---------- Versão PARALELA (partição por rank + Threads) ----------
        long inicioMillisPar = System.currentTimeMillis();
        long inicioNanoPar = System.nanoTime();

        long somaParalela = somarParalelo(n, p);

        long fimNanoPar = System.nanoTime();
        long fimMillisPar = System.currentTimeMillis();

        long tempoMillisPar = fimMillisPar - inicioMillisPar;
        long tempoNanoPar = fimNanoPar - inicioNanoPar;

        System.out.println("Versão PARALELA (p = " + p + ")");
        System.out.println("Soma total = " + somaParalela);
        System.out.println("Tempo (currentTimeMillis) = " + tempoMillisPar + " ms");
        System.out.println("Tempo (nanoTime) = " + tempoNanoPar + " ns ("
                + (tempoNanoPar / 1_000_000.0) + " ms)");
        System.out.println();

        // ---------- Comparação ----------
        System.out.println("Comparação");
        System.out.println("Somas iguais? " + (somaSequencial == somaParalela));
        System.out.println("Diferença de tempo (millis): sequencial - paralelo = "
                + (tempoMillisSeq - tempoMillisPar) + " ms");
        System.out.println("Diferença de tempo (nano): sequencial - paralelo = "
                + (tempoNanoSeq - tempoNanoPar) + " ns");
        System.out.println();
        System.out.println("Observação sobre currentTimeMillis() vs nanoTime():");
        System.out.println("- currentTimeMillis(): mede o relógio do sistema (ms). Pode");
        System.out.println("  saltar se o relógio for ajustado; resolução costuma ser menor.");
        System.out.println("- nanoTime(): relógio monotônico de alta resolução, melhor para");
        System.out.println("  medir intervalos curtos de execução (não é 'hora do dia').");
    }

    /* Nesse trecho, cada processador (ou núcleo) executa o loop for de forma
     independente. Cada processador calcula my_first_i e my_last_i com base
     nas fórmulas discutidas anteriormente. Em seguida, ele itera sobre os
     elementos entre esses índices, computando o valor de my_x e acumulando-o em my_sum.
     O paralelismo ocorre porque vários processadores podem executar esse
     loop simultaneamente, processando diferentes partes dos dados. Cada
     processador trabalha de forma independente, aproveitando os recursos da
     CPU de maneira eficiente para calcular a soma total dos valores. */

    /** Soma sequencial: um único loop sobre todos os elementos. */
    public static long somarSequencial(int n) {
        long soma = 0;
        for (int i = 0; i < n; i++) {
            soma += computeNextValue(i);
        }
        return soma;
    }

    /**
     * Soma paralela: cada rank (thread) calcula sua fatia [myFirstI, myLastI)
     * e no final as somas parciais são reunidas.
     */

    public static long somarParalelo(int n, int p) {
        Thread[] threads = new Thread[p];
        long[] somasParciais = new long[p];

        for (int rank = 0; rank < p; rank++) {
            final int r = rank;
            threads[rank] = new Thread(() -> {
                int myFirstI = calcularMyFirstI(n, p, r);
                int myLastI = calcularMyLastI(n, p, r);
                long mySum = 0;
                for (int my_i = myFirstI; my_i < myLastI; my_i++) {
                    int my_x = computeNextValue(my_i);
                    mySum += my_x;
                }
                somasParciais[r] = mySum;
                System.out.println("Para o rank " + r + ": my_first_i = " + myFirstI
                        + ", my_last_i = " + myLastI + ", soma parcial = " + mySum);
            });
            threads[rank].start();
        }

        long somaTotal = 0;
        for (int rank = 0; rank < p; rank++) {
            try {
                threads[rank].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            somaTotal += somasParciais[rank];
        }
        return somaTotal;
    }

    public static int calcularMyFirstI(int n, int p, int rank) {
        // Calcula o índice inicial (my_first_i) para um dado processador (rank)
        int elementosPorNucleo = n / p; // Assumindo que n é divisível por p
        return rank * elementosPorNucleo;
    }

    public static int calcularMyLastI(int n, int p, int rank) {
        // Calcula o índice final (my_last_i) para um dado processador (rank)
        int elementosPorNucleo = n / p; // Assumindo que n é divisível por p
        return (rank + 1) * elementosPorNucleo;
    }

    public static int computeNextValue(int my_i) {
        // Função fictícia para calcular o próximo valor com base no índice my_i
        return my_i * 2;
    }

    /* Exercício:
     - Tome o código sequencial e o código anterior.
     Acrescente o cálculo do tempo inicial e do tempo final para calcular o tempo
     de execução dos dois códigos e compare.
     *** Use System.currentTimeMillis() e verifiquem com System.nanoTime()
         – o que acontece de diferente? */
}
