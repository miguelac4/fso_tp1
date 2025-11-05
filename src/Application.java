public class Application {
	private GUI gui;
	private ConsoleSIMGUI guiSim;
	private MovimentoAleatorio produtor;
	private Servidor consumidor;
    private BufferCircular buffer;
    private BaseDados bd;
    private EvitarObstaculo evitarObst;
   
	
	public Application() {
		gui = new GUI();
		guiSim = new ConsoleSIMGUI();
        bd = gui.getBD();
        
        // DESCOMENTAR PARA USAR SIMULAÇÃO
        RobotLegoEV3SIM simRobot = new RobotLegoEV3SIM(guiSim.getLogger());
        guiSim.setSimularEvitarCallback(() -> evitarObst.ExecutarEvitar());
        bd.setRobot(simRobot);

        buffer = new BufferCircular();
        produtor  = new MovimentoAleatorio(buffer, bd);
        
        consumidor = new Servidor(buffer, bd, bd.getRobot());
        evitarObst = new EvitarObstaculo(bd, bd.getRobot());
        

        produtor.start();
        consumidor.start();
        evitarObst.start();
        
        gui.setTarefas(produtor, consumidor);
        gui.setBuffer(buffer);
	}
	
	public void run() {
		
		System.out.println("A aplicação começou.");
		while (!gui.getBD().isTerminar()) {
			evitarObst.desbloquear();
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
