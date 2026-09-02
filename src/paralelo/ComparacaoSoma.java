package paralelo;

public class ComparacaoSoma {
    public static void main(String[] args) {
        long[] quantidades = {10_000L, 1_000_000L, 100_000_000L, 1_000_000_000L};

        for (long N : quantidades) {
            System.out.println("---- Quantidade de números: " + N + " ----");

            // ---------- Versão SERIAL ----------
            long inicioMillisSerial = System.currentTimeMillis();
            long inicioNanoSerial = System.nanoTime();

            long somaSerial = 0;
            for (long i = 1; i <= N; i++) {
                somaSerial += i;
            }

            long fimNanoSerial = System.nanoTime();
            long fimMillisSerial = System.currentTimeMillis();

            long tempoMillisSerial = fimMillisSerial - inicioMillisSerial;
            long tempoNanoSerial = fimNanoSerial - inicioNanoSerial;

            System.out.println("Soma Serial  : " + somaSerial);
            informarTempos("Serial", tempoMillisSerial, tempoNanoSerial);

            // ---------- Versão PARALELA (TAD Tarefa / 4 threads) ----------
            long inicioMillisParalelo = System.currentTimeMillis();
            long inicioNanoParalelo = System.nanoTime();

            long pedaco = N / 4;
            Tarefa t1 = new Tarefa(1, pedaco);
            Tarefa t2 = new Tarefa(pedaco + 1, pedaco * 2);
            Tarefa t3 = new Tarefa(pedaco * 2 + 1, pedaco * 3);
            Tarefa t4 = new Tarefa(pedaco * 3 + 1, N);

            t1.start();
            t2.start();
            t3.start();
            t4.start();

            try {
                t1.join();
                t2.join();
                t3.join();
                t4.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            long somaParalela = t1.getTotal() + t2.getTotal() + t3.getTotal() + t4.getTotal();

            long fimNanoParalelo = System.nanoTime();
            long fimMillisParalelo = System.currentTimeMillis();

            long tempoMillisParalelo = fimMillisParalelo - inicioMillisParalelo;
            long tempoNanoParalelo = fimNanoParalelo - inicioNanoParalelo;

            System.out.println("Soma Paralela: " + somaParalela);
            informarTempos("Paralelo", tempoMillisParalelo, tempoNanoParalelo);

            System.out.println("Somas iguais? " + (somaSerial == somaParalela));
            System.out.println();
        }
    }

    private static void informarTempos(String rotulo, long millis, long nanos) {
        System.out.println(rotulo + " | currentTimeMillis: " + millis + " ms");
        System.out.println(rotulo + " | nanoTime         : " + nanos + " ns ("
                + (nanos / 1_000_000.0) + " ms)");

        if (millis == 0 && nanos > 0) {
            System.out.println("Unidade mais precisa: nanoTime (millis deu 0 e não enxergou o intervalo).");
        } else {
            System.out.println("Unidade mais precisa: nanoTime (mede em ns; millis só conta milissegundos).");
        }
    }
}
