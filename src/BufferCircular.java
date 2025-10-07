import java.util.concurrent.Semaphore;

public class BufferCircular {
    final int dimensaoBuffer = 16;  // Capacidade de armazenamento do robot
    Comando[] bufferCircular;
    int getBuffer; // índice de onde o consumidor vai retirar o próximo comando
    int putBuffer; // índice onde o produtor vai colocar o próximo comando
    
    // NOTA: Não esquecer que para cada grupo de movimentos aleatorios é sempre contado mais 1 
    //instrução para contar com a instrução parar(FALSE) obrigatorio no final das instrucoes

    // criar semaforos
    Semaphore acessoElementos;
    Semaphore elementosLivres;
    Semaphore elementosOcupados;
    
    public BufferCircular() {
    	bufferCircular = new Comando[dimensaoBuffer];
    	getBuffer = 0;
    	putBuffer = 0;
    	
    	acessoElementos = new Semaphore(dimensaoBuffer);
    	elementosLivres = new Semaphore(0);
    	elementosOcupados = new Semaphore(1);
    }
    
    public void inserirElemento(String s){
    	try {
    		elementosLivres.acquire();
    		acessoElementos.acquire();
    		bufferCircular[putBuffer]= new String(s);
    		putBuffer= ++putBuffer % dimensaoBuffer;
    		acessoElementos.release();
    	} catch (InterruptedException e) {}
    	 elementosOcupados.release();
    }
    
    public String removerElemento() {
    	 String s= null;
    	 try {
    		 elementosOcupados.acquire();
    		 acessoElementos.acquire();
    	 } catch (InterruptedException e) {}
    	 s = new String(bufferCircular[getBuffer]);
    	 getBuffer= ++getBuffer % dimensaoBuffer;
    	 acessoElementos.release();
    	 elementosLivres.release();
    	 return s;
    }

}
