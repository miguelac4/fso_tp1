import java.util.Arrays;
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
    
    // Helper
    public int ocupados() { return elementosOcupados.availablePermits(); }
    
    public BufferCircular() {
    	bufferCircular = new Comando[dimensaoBuffer];
    	getBuffer = 0;
    	putBuffer = 0;
    	
    	elementosLivres = new Semaphore(dimensaoBuffer);
    	elementosOcupados = new Semaphore(0);
    	acessoElemento = new Semaphore(1);
    }
    
    // Método ToString para retornar logs do conteudo do buffer
    @Override
    public String toString() {
        // isto imprime o array completo, incluindo nulls
        return Arrays.toString(bufferCircular);
    }
    
    // Função para limpar buffer e usar na GUI
    public void clearBuffer() {
        try {
            acessoElemento.acquire();        // para alterações

            // limpa o buffer
            Arrays.fill(bufferCircular, null);
            getBuffer = 0;
            putBuffer = 0;

            // reseta semaforos
            elementosOcupados.drainPermits();
            elementosLivres.drainPermits();
            elementosLivres.release(dimensaoBuffer);

            System.out.println("[BUFFER] Limpo (0/" + dimensaoBuffer + " ocupados).");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            acessoElemento.release();
        }
    }
    
    // ___________________PRODUTOR___________________
    public void inserirElemento(Comando comando){
    	try {
    		
    		if (elementosLivres.availablePermits() == 0) {
                System.out.println("[BUFFER] Buffer cheio (produtor vai aguardar espaço livre)");
            }
    		
    		elementosLivres.acquire();
    		acessoElemento.acquire();
    		
    		bufferCircular[putBuffer]= comando;
    		putBuffer = (putBuffer + 1) % dimensaoBuffer;
    		
    	} catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            acessoElemento.release();
            // primeiro liberta um "ocupado"
            elementosOcupados.release();
            System.out.println("[BUFFER] Inserido (" + comando.tipo + "). Ocupados: "
                    + elementosOcupados.availablePermits() + "/" + dimensaoBuffer);
        }
    }
    // ___________________CONSUMIDOR___________________
    public Comando removerElemento() {
    	 Comando comando= null;
    	 try {
    		 if (elementosOcupados.availablePermits() == 0) {
                 System.out.println("[BUFFER] Vazio (consumidor vai aguardar)");
             }
    		 
    		 elementosOcupados.acquire();
    		 acessoElemento.acquire();
    		 
    		 comando = bufferCircular[getBuffer];
    	     getBuffer = (getBuffer + 1) % dimensaoBuffer;
    		 
    	 } catch (InterruptedException e) {
    	        e.printStackTrace();
    	    } finally {
    	        acessoElemento.release();
    	        elementosLivres.release();
    	        //System.out.println("[BUFFER] Removido (" + comando.tipo + "). Ocupados: "
    	        //        + elementosOcupados.availablePermits() + "/" + dimensaoBuffer);

    	        if (elementosOcupados.availablePermits() == 0) {
    	            System.out.println("[BUFFER] (Vazio após consumir tudo)");
    	        }
    	    }
    	    return comando;
    }

}
