package paralelo;

public class ExemploUso {
    public static void main(String[] args) {
        long tempoInicial = System.currentTimeMillis(); // Guarda o tempo atual

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

        long tempoFinal = System.currentTimeMillis(); // Captura o tempo após a execução
        long tempoExecucao = tempoFinal - tempoInicial; // Calcula o tempo total em milissegundos

        // Exibimos o somatório dos totalizadores de cada Thread
        System.out.println("Total: " + (t1.getTotal() + t2.getTotal() + t3.getTotal()));
        System.out.println("Tempo de execução: " + tempoExecucao + " milissegundos");

        /* Exercício:
         - Tome o código anterior.
         Acrescente o cálculo do tempo inicial e do tempo final para
         calcular o tempo de execução
         *** Use System.currentTimeMillis(); */
    }
}
