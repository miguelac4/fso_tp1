
public class EvitarObstaculo extends Tarefa{
	
	private RobotLegoEV3 robot;
	private final BaseDados bd;
	private final MonitorRobot monitor;
	
	public EvitarObstaculo(BaseDados bd, RobotLegoEV3 robot, MonitorRobot monitor) {
		super();
		this.bd = bd;
		this.robot = robot;
		this.monitor = monitor;
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
		//System.out.print("TESTE");

        if (robot.SensorToque(robot.S_1) == 1) {
            // 1) sinaliza preempção e pára já o robô
            monitor.pedirPrioridade();
            //robot.Parar(false);	

            try {
                // 2) assume posse exclusiva antes de desviar
                monitor.assumirPossePrioritaria();
                
                // 3) faz o desvio (mantendo a posse do monitor!)
                ExecutarEvitar();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                // liberta a posse para o servidor voltar a consumir
                monitor.libertarAcesso();
            }
        }
		
	}
}
