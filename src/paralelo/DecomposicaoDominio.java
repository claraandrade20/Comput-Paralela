package paralelo;

public class DecomposicaoDominio {
    public static void main(String[] args) throws InterruptedException {
        int limite = 10_000_000;
        int numThreads = Runtime.getRuntime().availableProcessors(); // Detecta núcleos (ex: 8)
        int tamanhoPedaco = limite / numThreads;

        System.out.println("Limite = " + limite);
        System.out.println("Núcleos detectados (numThreads) = " + numThreads);
        System.out.println("Tamanho do pedaço = " + tamanhoPedaco);
        System.out.println();

        //versão serial (um único núcleo)
        long inicioMillisSerial = System.currentTimeMillis();
        long inicioNanoSerial = System.nanoTime();

        double somaSerial = 0;
        for (int j = 0; j < limite; j++) {
            somaSerial += Math.sqrt(j * Math.PI);
        }

        long fimNanoSerial = System.nanoTime();
        long fimMillisSerial = System.currentTimeMillis();

        System.out.println("Versão SERIAL");
        System.out.println("Soma final: " + somaSerial);
        informarTempos(fimMillisSerial - inicioMillisSerial, fimNanoSerial - inicioNanoSerial);
        System.out.println();

        //versão paralela (decomposição de domínio)
        // Dividimos o intervalo em pedaços e damos um pedaço para cada trabalhador (Thread).
        long inicioMillisParalelo = System.currentTimeMillis();
        long inicioNanoParalelo = System.nanoTime();

        Thread[] trabalhadores = new Thread[numThreads];
        double[] resultadosParciais = new double[numThreads];

        for (int i = 0; i < numThreads; i++) {
            final int id = i;
            final int inicio = i * tamanhoPedaco;
            // O último trabalhador pega o resto se a divisão não for exata
            final int fim = (i == numThreads - 1) ? limite : (i + 1) * tamanhoPedaco;

            trabalhadores[i] = new Thread(() -> {
                double somaLocal = 0;
                for (int j = inicio; j < fim; j++) {
                    somaLocal += Math.sqrt(j * Math.PI); // Computação pesada
                }
                resultadosParciais[id] = somaLocal; // Guarda o resultado daquele núcleo
            });
            trabalhadores[i].start(); // Inicia o núcleo
        }

        // Espera todos terminarem (Sincronização)
        double somaTotal = 0;
        for (int i = 0; i < numThreads; i++) {
            trabalhadores[i].join();
            somaTotal += resultadosParciais[i]; // Consolida os resultados
        }

        long fimNanoParalelo = System.nanoTime();
        long fimMillisParalelo = System.currentTimeMillis();

        System.out.println("Versão PARALELA");
        System.out.println("Soma final: " + somaTotal);
        informarTempos(fimMillisParalelo - inicioMillisParalelo, fimNanoParalelo - inicioNanoParalelo);
        System.out.println("Somas iguais? " + (somaSerial == somaTotal));
        System.out.println();
        System.out.println("Isso é paralelismo de dados: cada núcleo faz a mesma operação");
        System.out.println("em um pedaço diferente do intervalo (decomposição de domínio).");
    }

    private static void informarTempos(long millis, long nanos) {
        System.out.println("Tempo (currentTimeMillis): " + millis + " ms");
        System.out.println("Tempo (nanoTime)         : " + nanos + " ns ("
                + (nanos / 1_000_000.0) + " ms)");
        if (millis == 0 && nanos > 0) {
            System.out.println("Unidade mais precisa: nanoTime (millis deu 0).");
        } else {
            System.out.println("Unidade mais precisa: nanoTime (mede em ns; millis só conta milissegundos).");
        }
    }
}
