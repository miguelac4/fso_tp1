import java.util.concurrent.Semaphore;

public class BufferCircular {
    private final Comando[] buffer;
    private int pos_top = 0; // índice de onde o consumidor vai retirar o próximo comando
    private int pos_bottom = 0; // índice onde o produtor vai colocar o próximo comando
    private int bufferCapacity = 16; // Capacidade de armazenamento do robot
    
    // NOTA: Não esquecer que para cada grupo de movimentos aleatorios é sempre contado mais 1 
    //instrução para contar com a instrução parar(FALSE) obrigatorio no final das instrucoes

    // criar semaforos
    private final Semaphore semaforo_decisao = new Semaphore(1, true);
    private final Semaphore semaforo_adicionar;
    private final Semaphore semaforo_consumir;
    
    public BufferCircular() {
    	buffer = new Comando[bufferCapacity];
    	semaforo_adicionar = new Semaphore(bufferCapacity, true);
    	semaforo_consumir = new Semaphore(0, true);
    }
    
    public void put(Comando c) throws InterruptedException {
        semaforo_adicionar.acquire();       // espera espaço livre
        semaforo_decisao.acquire();    // entra na secção crítica
        try {
            buffer[pos_bottom] = c;
            pos_bottom = (pos_bottom + 1) % buffer.length; //TODO TENHO DE PERCEBER MELHOR AQUI
        } finally {
            semaforo_decisao.release();
            semaforo_consumir.release(); // sinaliza item disponível
        }
    }
}
