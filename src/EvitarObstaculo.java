
public class EvitarObstaculo extends Tarefa{
	
	private RobotLegoEV3 robot;
	private final BaseDados bd;
	
	public EvitarObstaculo(BaseDados bd, RobotLegoEV3 robot) {
		super();
		this.bd = bd;
		this.robot = robot;
	}
	
	public void ExecutarEvitar() {
		robot.Parar(false);
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
}
