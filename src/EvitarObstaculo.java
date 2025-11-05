
public class EvitarObstaculo extends Tarefa{
	
	private RobotLegoEV3 robot;
	private final BaseDados bd;
	
	public EvitarObstaculo(BaseDados bd, RobotLegoEV3 robot) {
		super();
		this.bd = bd;
		this.robot = robot;
	}
	
	public void ExecutarEvitar() {
		robot.Parar(true);
		robot.Reta(-20);
		// Fazer sleep para executar o Reta, tempo calculado com as funções do tp1
		try { Thread.sleep(1100); } catch (InterruptedException ignored) {} 
		if (Math.random() < 0.5) {
		    robot.CurvarEsquerda(1, 90);
		} else {
			robot.CurvarDireita(1, 90);
		}
		// Fazer sleep para executar o Curvar, tempo calculado com as funções do tp1
		try { Thread.sleep(183); } catch (InterruptedException ignored) {} 
		robot.Parar(false);
	}

	@Override
	protected void execucao() {
		if (!bd.isRobotAberto()) { bloquear(); return; }

	    if (robot.SensorToque(robot.S_1) == 1) {
	        // 1) ativar prioridade (não precisa de synchronized; a flag é volatile)
	        bd.prioridadeEvitar = true;

	        // 2) assumir posse quando o Servidor largar o lock (após o comando atual)
	        synchronized (robot) {
	            // 2.1) limpar prioridade ao entrar: evita bloquear o Servidor no próximo comando
	            bd.prioridadeEvitar = false;

	            // 3) DESVIO completo, sempre dentro do lock
	            robot.Parar(true);
	            robot.Reta(-20);
	            try { Thread.sleep(1100); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }

	            if (Math.random() < 0.5) robot.CurvarEsquerda(1, 90);
	            else                     robot.CurvarDireita(1, 90);
	            try { Thread.sleep(183); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }

	            robot.Parar(false);

	            // 4) acordar o Servidor (que pode estar em wait() por causa da flag)
	            robot.notifyAll();
	        }
	    }
	}
}
