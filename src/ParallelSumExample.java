public class ParallelSumExample {
public static void main(String[] args) {
int n = 100; // Número total de elementos
int p = 4; // Número total de processadores (núcleos)
int rank = 2; // Número do processador (rank) atual
int myFirstI = calcularMyFirstI(n, p, rank);
int myLastI = calcularMyLastI(n, p, rank);
int mySum = 0;
for (int my_i = myFirstI; my_i < myLastI; my_i++) {
int my_x = computeNextValue(my_i); // Acumula a soma - cada núcleo
mySum += my_x;
}
System.out.println("Para o rank " + rank + ":");
System.out.println("my_first_i = " + myFirstI);
System.out.println("my_last_i = " + myLastI);
System.out.println("Soma parcial = " + mySum);
  }
/* Nesse trecho, cada processador (ou núcleo) executa o loop for de forma
 independente. Cada processador calcula my_first_i e my_last_i com base
nas fórmulas discutidas anteriormente. Em seguida, ele itera sobre os
elementos entre esses índices, computando o valor de my_x e acumulandoo em my_sum.
O paralelismo ocorre porque vários processadores podem executar esse
loop simultaneamente, processando diferentes partes dos dados. Cada
processador trabalha de forma independente, aproveitando os recursos da
CPU de maneira eficiente para calcular a soma total dos valores. */

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
// Substitua esta função pela lógica específica do seu problema
return my_i * 2;
  }
/*Exercício:
- Tome o código sequencial e o código anterior.
Acrescente o cálculo do tempo inicial e do tempo final para calcular o tempo de execução dos dois códigos e compare.
*** Use System.currentTimeMillis() e verifiquem com System.nanoTime() – o que acontece de
diferente?;  */
}







