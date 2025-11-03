public class Application {
	private GUI gui;
	private MovimentoAleatorio produtor;
	private Servidor consumidor;
    private BufferCircular buffer;
    private BaseDados bd;
    
    private MonitorRobot monitor;
	
	public Application() {
		gui = new GUI();
        bd = gui.getBD();

        buffer = new BufferCircular();
        produtor  = new MovimentoAleatorio(buffer, bd);
        monitor = new MonitorRobot();
        
        consumidor = new Servidor(buffer, bd, bd.getRobot(), monitor);

        produtor.start();
        consumidor.start();
        
        gui.setTarefas(produtor, consumidor);
        gui.setBuffer(buffer);
	}
	
	public void run() {
		
		System.out.println("A aplicação começou.");
		while (!gui.getBD().isTerminar()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		System.out.println("A aplicação terminou.");
	}
	
	public static void main (String[] args) {
		Application app = new Application();
		app.run();
	}

}
