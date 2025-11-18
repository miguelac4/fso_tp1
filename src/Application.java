public class Application {
	private GUI gui;
	private ConsoleSIMGUI guiSim;
	private GUIGravador guiGravador;
	private MovimentoAleatorio produtor;
	private Servidor consumidor;
    private BufferCircular buffer;
    private BufferCircular bufferGravador;
    private BaseDados bd;
    private BaseDadosGravador bdG;
    private EvitarObstaculo evitarObst;
   
	
	public Application() {
		gui = new GUI();
		guiSim = new ConsoleSIMGUI();
		
		bdG = new BaseDadosGravador();
		

        bd = gui.getBD();
        
        
        
        // DESCOMENTAR PARA USAR SIMULAÇÃO
        //RobotLegoEV3SIM simRobot = new RobotLegoEV3SIM(guiSim.getLogger());
        //guiSim.setSimularEvitarCallback(() -> evitarObst.simularEvitar());
        //bd.setRobot(simRobot);

        buffer = new BufferCircular();
        bufferGravador = new BufferCircular();
        produtor  = new MovimentoAleatorio(buffer, bd);
        
        guiGravador = new GUIGravador(bdG, bufferGravador);
        
        consumidor = new Servidor(buffer, bufferGravador, bd, bdG, bd.getRobot(), guiSim.getLogger());
        evitarObst = new EvitarObstaculo(bd, bdG, bufferGravador, bd.getRobot(), guiSim.getLogger());
        

        produtor.start();
        consumidor.start();
        evitarObst.start();
        
        gui.setTarefas(produtor, consumidor);
        gui.setBuffer(buffer);
	}
	
	public void run() {
		
		System.out.println("A aplicação começou.");
		while (!gui.getBD().isTerminar()) {
			
			// Log para ver conteudo do BufferGravador
			System.out.println(bufferGravador);
			
			// Log para ver boolean isRecording
			//System.out.println(bdG.getIsRecording());
			
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
