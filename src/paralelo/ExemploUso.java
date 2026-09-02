package paralelo;

public class ExemploUso {
    public static void main(String[] args) {
        long tempoInicialMillis = System.currentTimeMillis(); // Guarda o tempo atual
        long tempoInicialNano = System.nanoTime();

        // cria 3 tarefas
        Tarefa t1 = new Tarefa(0, 1000);
        t1.setName("Tarefa1");
        Tarefa t2 = new Tarefa(1001, 2000);
        t2.setName("Tarefa2");
        Tarefa t3 = new Tarefa(2001, 3000);
        t3.setName("Tarefa3");

        // inicia a execução paralela das 3 tarefas, iniciando 3 novas threads no programa
        t1.start();
        System.out.println("Executando T1");
        t2.start();
        System.out.println("Executando T2");
        t3.start();
        System.out.println("Executando T3");

        // aguarda a finalização das tarefas
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        /* Para testarmos o paralelismo em nosso programa, criamos a classe
         ExemploUso com o método main para executar o programa,
         conforme o código. Por uma questão de melhor identificação, toda
         Thread tem um nome, mesmo não sendo fornecido. */

        long tempoFinalNano = System.nanoTime();
        long tempoFinalMillis = System.currentTimeMillis(); // Captura o tempo após a execução
        long tempoExecucaoMillis = tempoFinalMillis - tempoInicialMillis;
        long tempoExecucaoNano = tempoFinalNano - tempoInicialNano;

        // Exibimos o somatório dos totalizadores de cada Thread
        System.out.println("Total: " + (t1.getTotal() + t2.getTotal() + t3.getTotal()));
        System.out.println("Tempo de execução (currentTimeMillis): " + tempoExecucaoMillis + " milissegundos");
        System.out.println("Tempo de execução (nanoTime): " + tempoExecucaoNano + " ns ("
                + (tempoExecucaoNano / 1_000_000.0) + " ms)");
        if (tempoExecucaoMillis == 0 && tempoExecucaoNano > 0) {
            System.out.println("Unidade mais precisa: nanoTime (millis deu 0 e não enxergou o intervalo).");
        } else {
            System.out.println("Unidade mais precisa: nanoTime (mede em ns; millis só conta milissegundos).");
        }

        /* Exercício:
         - Tome o código anterior.
         Acrescente o cálculo do tempo inicial e do tempo final para
         calcular o tempo de execução
         *** Use System.currentTimeMillis(); */
    }
}
