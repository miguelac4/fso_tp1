public class Application {
	private GUI gui;
	private MovimentoAleatorio ma;
	private boolean terminar = false;
	
	public Application() {
		gui = new GUI();
		ma = new MovimentoAleatorio();
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
