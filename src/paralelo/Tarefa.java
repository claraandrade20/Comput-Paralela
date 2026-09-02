package paralelo;

public class Tarefa extends Thread {

    private final long valorInicial;
    private final long valorFinal;
    private long total = 0;

    // método construtor que receberá os parametros da tarefa;
    public Tarefa(long valorInicial, long valorFinal) {
        this.valorInicial = valorInicial;
        this.valorFinal = valorFinal;
    }

    // método que retorna o total calculado
    public long getTotal() {
        return total;
    }

    @Override
    // Este método se faz necessário para que possamos dar start() na Thread e iniciar a tarefa em paralelo
    public void run() {
        for (long i = valorInicial; i <= valorFinal; i++) {
            total += i;
        }
    }
}
/* A forma clássica de se criar uma thread é estendendo a classe Thread, assim
como no código, onde temos a classe Tarefa que estende a classe Thread,
que por sua vez implementa a interface Runnable. Neste caso
sobrescrevemos o método run() da nova classe, que fica encarregado de
executar nossa tarefa. */
