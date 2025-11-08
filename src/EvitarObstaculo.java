import java.util.function.Consumer;

public class EvitarObstaculo extends Tarefa{
	
	private RobotLegoEV3 robot;
	private final BaseDados bd;
	private final Consumer<String> log;
	
	public EvitarObstaculo(BaseDados bd, RobotLegoEV3 robot, Consumer<String> logger) {
		super();
		this.bd = bd;
		this.robot = robot;
		this.log = (logger != null) ? logger : System.out::println;
	}
	
	public void ExecutarEvitar() {
		log.accept("______EVITAR OBSTACULO______");
		robot.Parar(true);
		log.accept("Parar(imediato= true)");
		
		robot.Reta(-20);
		log.accept("Reta( -20 cm)");
		// Fazer sleep para executar o Reta, tempo calculado com as funções do tp1
		try { Thread.sleep(1100); } catch (InterruptedException ignored) {} 
		
		if (Math.random() < 0.5) {
		    robot.CurvarEsquerda(1, 90);
		    log.accept("CurvarEsquerda(raio= 1, ang= 90 ");
		} else {
			robot.CurvarDireita(1, 90);
			log.accept("CurvarDireita(raio= 1, ang= 90 ");
		}
		// Fazer sleep para executar o Curvar, tempo calculado com as funções do tp1
		try { Thread.sleep(183); } catch (InterruptedException ignored) {} 
		robot.Parar(false);
		log.accept("Parar(imediato= false)");
		log.accept("______PAROU EVITAR______");
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
        if (!bd.isRobotAberto()) { bloquear(); return; }

        boolean choque;
        synchronized (robot) {
        	// Verificar dentro da exclusao mutua se o robot colidiu
            choque = (robot.SensorToque(robot.S_1) == 1);
            if (choque) {
                // Editar da pd que o robot colidiu
                bd.prioridadeEvitar = true;
            }
            // Caso nao colida sai do syncronize para o robot poder usar outras threads
            if (!choque) return;
        }

        synchronized (robot) {
            try {
            	// verificar caso nao tenha alterado o estado de novo
                if (robot.SensorToque(robot.S_1) == 1) {
                    // enquanto evitar decorre, mantemos o lock
                    ExecutarEvitar();
                }
            } finally {
                // limpar prioridade e acordar as que estiverem à espera
                bd.prioridadeEvitar = false;
                robot.notifyAll();
            }
        }
    }
}
