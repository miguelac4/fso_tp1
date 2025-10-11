import java.util.concurrent.Semaphore;

public class BufferCircular {
    final int dimensaoBuffer = 16;  // Capacidade de armazenamento do robot
    private final Comando[] bufferCircular;
    private int getBuffer; // índice de onde o consumidor vai retirar o próximo comando
    private int putBuffer; // índice onde o produtor vai colocar o próximo comando
    
    // NOTA: Não esquecer que para cada grupo de movimentos aleatorios é sempre contado mais 1 
    //instrução para contar com a instrução parar(FALSE) obrigatorio no final das instrucoes

    // criar semaforos
    private final Semaphore acessoElemento;
    private final Semaphore elementosLivres;
    private final Semaphore elementosOcupados;
    
    public BufferCircular() {
    	bufferCircular = new Comando[dimensaoBuffer];
    	getBuffer = 0;
    	putBuffer = 0;
    	
    	elementosLivres = new Semaphore(dimensaoBuffer);
    	elementosOcupados = new Semaphore(0);
    	acessoElemento = new Semaphore(1);
    }
    
    // ___________________PRODUTOR___________________
    public void inserirElemento(Comando comando){
    	try {
    		
    		if (elementosLivres.availablePermits() == 0) {
                System.out.println("[BUFFER] Buffer cheio — produtor vai aguardar espaço livre...");
            }
    		
    		elementosLivres.acquire();
    		acessoElemento.acquire();
    		
    		bufferCircular[putBuffer]= new Comando(comando);
    		putBuffer= ++putBuffer % dimensaoBuffer;
    		
    		int ocupados = dimensaoBuffer - elementosLivres.availablePermits();
            System.out.println("[BUFFER] Inserido comando (" + comando.tipo + "). Ocupados: " + ocupados + "/" + dimensaoBuffer);
    		
    		acessoElemento.release();
    	} catch (InterruptedException e) {}
    	 elementosOcupados.release();
    }
    // ___________________CONSUMIDOR___________________
    public Comando removerElemento() {
    	 Comando comando= null;
    	 try {
    		 if (elementosOcupados.availablePermits() == 0) {
                 System.out.println("[BUFFER] Nenhum comando disponível — consumidor vai aguardar...");
             }
    		 
    		 elementosOcupados.acquire();
    		 acessoElemento.acquire();
    		 
    	 } catch (InterruptedException e) {}
    	 comando = new Comando(bufferCircular[getBuffer]);
    	 getBuffer= ++getBuffer % dimensaoBuffer;
    	 
    	 int ocupados = dimensaoBuffer - elementosLivres.availablePermits();
         System.out.println("[BUFFER] Removido comando (" + comando.tipo + "). Ocupados: " + ocupados + "/" + dimensaoBuffer);

    	 
    	 
    	 
    	 acessoElemento.release();
    	 elementosLivres.release();
    	 
    	 if (elementosOcupados.availablePermits() == 0) {
             System.out.println("[BUFFER] Todas as tarefas foram consumidas — buffer está vazio.");
    	 }
    	 
    	 return comando;
    }

}
