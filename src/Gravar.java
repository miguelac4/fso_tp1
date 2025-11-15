import java.util.function.Consumer;

public class Gravar extends Tarefa{
	
	private RobotLegoEV3 robot;
	private final BaseDados bd;
	private final Consumer<String> log;
	private GUIGravador guiGravador;
	private BufferCircular buffer;
	
	public Gravar(BufferCircular buffer, BaseDados bd, RobotLegoEV3 robot, Consumer<String> logger) {
		super();
		this.bd = bd;
		this.buffer = buffer;
		this.robot = robot;
		this.log = (logger != null) ? logger : System.out::println;
	}
	
	// Usar inserirElemento e removerElemento da classe BufferCircular

	@Override
	protected void execucao() {
		// TODO Auto-generated method stub
		synchronized (robot) {
			
		}
		
	}
	

}
