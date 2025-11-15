import java.util.function.Consumer;

public class EvitarObstaculo extends Tarefa{
	
	private RobotLegoEV3 robot;
	private final BaseDados bd;
	private final BaseDadosGravador bdG;
	private final Consumer<String> log;
	private BufferCircular bufferGravador;
	
	public EvitarObstaculo(BaseDados bd, BaseDadosGravador bdG, BufferCircular bufferGravador, RobotLegoEV3 robot, Consumer<String> logger) {
		super();
		this.bd = bd;
		this.bdG = bdG;
		this.bufferGravador = bufferGravador;
		this.robot = robot;
		this.log = (logger != null) ? logger : System.out::println;
	}
	
	private void ExecutarEvitar() {
		log.accept("______EVITAR OBSTACULO______");
		robot.Parar(true);
		
		if(bdG.getIsRecording() == true) {
			// Gravar no buffer de gravação
        	bufferGravador.inserirElemento(Comando.pararForce());
		}
		
		log.accept("Parar(imediato= true)");
		
		robot.Reta(-20);
		
		if(bdG.getIsRecording() == true) {
			// Gravar no buffer de gravação
        	bufferGravador.inserirElemento(Comando.retaFrente(-20));
		}
		
		log.accept("Reta( -20 cm)");
		// Fazer sleep para executar o Reta, tempo calculado com as funções do tp1
		try { Thread.sleep(1100); } catch (InterruptedException ignored) {} 
		
		if (Math.random() < 0.5) {
		    robot.CurvarEsquerda(0, 90);
		    
		    if(bdG.getIsRecording() == true) {
		    	// Gravar no buffer de gravação
		    	bufferGravador.inserirElemento(Comando.curvaEsq(0, 90));
			}
			
		    log.accept("CurvarEsquerda(raio= 0, ang= 90)");
		} else {
			robot.CurvarDireita(0, 90);
			
			if(bdG.getIsRecording() == true) {
				bufferGravador.inserirElemento(Comando.curvaDir(0, 90));
			}
			
			log.accept("CurvarDireita(raio= 0, ang= 90)");
		}
		// Fazer sleep para executar o Curvar, tempo calculado com as funções do tp1
		try { Thread.sleep(183); } catch (InterruptedException ignored) {} 
		robot.Parar(false);
		
		if(bdG.getIsRecording() == true) {
			bufferGravador.inserirElemento(Comando.parar());
		}
		
		log.accept("Parar(imediato= false)");
		log.accept("______PAROU EVITAR______");
	}
	
	// chamado pelo botão "Simular Evitar"
	public void simularEvitar() {
	    synchronized (robot) {
	        //bd.prioridadeEvitar = true;
	        try {
	            ExecutarEvitar();
	        } finally {
	            //bd.prioridadeEvitar = false;
	            robot.notifyAll();
	        }
	    }
	}

	/*
	@Override
	protected void execucao() {
		if (!bd.isRobotAberto()) { bloquear(); return; }

	    if (robot.SensorToque(robot.S_1) == 1) {
	        // Ativar prioridade
	        bd.prioridadeEvitar = true;

	        // Assumir posse quando o Servidor largar o lock (após o comando atual)
	        synchronized (robot) {
	            // Limpar prioridade ao entrar
	            bd.prioridadeEvitar = false;
	            
	            // Efetuar Evitar
	            ExecutarEvitar();

	            // Acordar o Servidor (que pode estar em wait() por causa da flag)
	            robot.notifyAll();
	        }
	    }
	}
	*/
	
	// Nova versao com o sensor toque dentro da exlusao mutua tambem
	@Override
    protected void execucao() {
		// Verificar se o robot está aberto
        if (!bd.isRobotAberto()) { bloquear(); return; }
        
        // Dormir 50 milisegundos para poder aceder a outra tarefa
        dormir();

        synchronized (robot) {
        	
            // verificar caso nao tenha alterado o estado
            if (robot.SensorToque(robot.S_1) == 1) {
                	
            	// executar evitar
            	ExecutarEvitar();
            }
        }
	}
	
	@Override
    protected void dormir() {
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
    }
}

