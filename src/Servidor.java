import java.util.function.Consumer;

public class Servidor extends Tarefa {
    
	private BufferCircular buffer;
	private BufferCircular bufferGravador;
    private RobotLegoEV3 robot;
    private final BaseDados bd;
    private final BaseDadosGravador bdG;
    
    private final Consumer<String> log;

    /*
     * Classe Consumidora
     * Executa os comandos no Robot
     */
    public Servidor(BufferCircular buffer, BufferCircular bufferGravador, BaseDados bd, BaseDadosGravador bdG, RobotLegoEV3 robot, Consumer<String> logger) {
    	super();
    	this.bd = bd;
    	this.bdG = bdG;
        this.buffer = buffer;
        this.bufferGravador = bufferGravador;
        this.robot = robot;
        this.log = (logger != null) ? logger : System.out::println;
    }

    // Métodos separados (bons para depuração e modularidade)
    public void Reta(int distancia) {
        robot.Reta(distancia);
        
        if(bdG.getIsRecording() == true) {
			// Gravar no buffer de gravação
        	bufferGravador.inserirElemento(Comando.retaFrente(distancia));
		}
		
        log.accept("Reta(" + distancia + " cm)");
    }

    public void CurvarDireita(int raio, int angulo) {
        robot.CurvarDireita(raio, angulo);
        
        if(bdG.getIsRecording() == true) {
			// Gravar no buffer de gravação
        	bufferGravador.inserirElemento(Comando.curvaDir(raio, angulo));
		}
		
        log.accept("CurvarDireita(raio=" + raio + ", ang=" + angulo + ")");
    }

    public void CurvarEsquerda(int raio, int angulo) {
        robot.CurvarEsquerda(raio, angulo);
        
        if(bdG.getIsRecording() == true) {
			// Gravar no buffer de gravação
        	bufferGravador.inserirElemento(Comando.curvaEsq(raio, angulo));
		}
		
        log.accept("CurvarEsquerda(raio=" + raio + ", ang=" + angulo + ")");
    }

    public void Parar() {
        robot.Parar(false);
        
        if(bdG.getIsRecording() == true) {
        	// Gravar no buffer de gravação
        	bufferGravador.inserirElemento(Comando.parar());
		}
		
        log.accept("Parar(imediato= false)");
    }
    
    public void PararForce() {
    	robot.Parar(true);
    	
    	if(bdG.getIsRecording() == true) {
    		// Gravar no buffer de gravação
        	bufferGravador.inserirElemento(Comando.pararForce());
		}
			
    	log.accept("Parar(imediato= true)");
    }

    // Método principal do consumidor (executa o comando retirado do buffer)
    public void executar(Comando comando) {
    	
        switch (comando.tipo) {
            case RETA_FRENTE:
                Reta(comando.p1);
                break;
            case RETA_TRAS:
            	Reta(-comando.p1);
                break;
            case CURVA_DIR:
                CurvarDireita(comando.p1, comando.p2);
                break;
            case CURVA_ESQ:
                CurvarEsquerda(comando.p1, comando.p2);
                break;
            case PARAR:
                Parar();
                break;
            case PARARFORCE:
            	PararForce();
            	break;
        }
    }
    
    public int getTempoEspera(Comando c) {
    	double vel = 20;
        int tempo_com = 100;
        int dist = 0;
        int raio = 0;
        int angulo = 0;
        int tempo_espera = 0;
        
	   switch (c.tipo) {
	        case RETA_FRENTE:
	        case RETA_TRAS:
	        	dist = Math.abs(c.p1); // usar modulo porque em caso de reta para tras nao queremos uma distancia negativa
	        	tempo_espera = (int) ((dist / vel) + tempo_com);
	        	break;
	        case CURVA_DIR:
	        case CURVA_ESQ:
	        	raio = c.p1;
	        	//System.out.println("c.p2 = " + c.p2);
	        	angulo = c.p2;
	        	//System.out.println("angulo = " + angulo);
	        	tempo_espera = (int) ((raio * angulo * 1000 * (2*Math.PI/360) / vel) + tempo_com);
	        	//System.out.println("Raio = " + raio + "\n Angulo = " + angulo);
	            break;
	        case PARAR:
	            tempo_espera = tempo_com;
	            break;
	    }
	   return tempo_espera;
    }
    
    // Exclusão Mutua entre Servidor - EvitarObstaculo
    private void executarComExclusao(Comando c) {
        synchronized (this.robot) {

        	// Executar comandos do buffer a partir da tarefa de consumo
            executar(c);

        }
    }


    @Override
    protected void execucao() {
    	// Verificar se o robot está aberto
    	if (!bd.isRobotAberto()) { bloquear(); return; }

        Comando c = buffer.removerElemento();
        executarComExclusao(c);

        // 2) drenar restantes (cada um protegido)
        while (buffer.ocupados() > 0) {
            c = buffer.removerElemento();
            executarComExclusao(c);
        }
    }

    @Override
    protected void dormir() {
        //try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
    }
}
