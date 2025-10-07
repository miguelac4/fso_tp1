import java.util.concurrent.Semaphore;

public class BufferCircular {
    final int dimensaoBuffer = 16;  // Capacidade de armazenamento do robot
    Comando[] bufferCircular;
    int getBuffer; // índice de onde o consumidor vai retirar o próximo comando
    int putBuffer; // índice onde o produtor vai colocar o próximo comando
    
    // NOTA: Não esquecer que para cada grupo de movimentos aleatorios é sempre contado mais 1 
    //instrução para contar com a instrução parar(FALSE) obrigatorio no final das instrucoes

    // criar semaforos
    Semaphore acessoElemento;
    Semaphore elementosLivres;
    Semaphore elementosOcupados;
    
    public BufferCircular() {
    	bufferCircular = new Comando[dimensaoBuffer];
    	getBuffer = 0;
    	putBuffer = 0;
    	
    	elementosLivres = new Semaphore(dimensaoBuffer);
    	elementosOcupados = new Semaphore(0);
    	acessoElemento = new Semaphore(1);
    }
    
    public void inserirElemento(Comando comando){
    	try {
    		elementosLivres.acquire();
    		acessoElemento.acquire();
    		bufferCircular[putBuffer]= new Comando(comando);
    		putBuffer= ++putBuffer % dimensaoBuffer;
    		acessoElemento.release();
    	} catch (InterruptedException e) {}
    	 elementosOcupados.release();
    }
    
    public Comando removerElemento() {
    	 Comando comando= null;
    	 try {
    		 elementosOcupados.acquire();
    		 acessoElemento.acquire();
    	 } catch (InterruptedException e) {}
    	 comando = new Comando(bufferCircular[getBuffer]);
    	 getBuffer= ++getBuffer % dimensaoBuffer;
    	 acessoElemento.release();
    	 elementosLivres.release();
    	 return comando;
    }

}
