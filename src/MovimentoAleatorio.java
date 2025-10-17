import java.util.Random;

public class MovimentoAleatorio extends Tarefa{
	
    private final BufferCircular buffer;
    private final BaseDados bd;
    private final Random rnd = new Random();
    
    private volatile int pendentes = 0; // Tamanho do nr de comandos pedidos na GUI
	
	public MovimentoAleatorio(BufferCircular buffer, BaseDados bd) {
        super(null);
        this.buffer = buffer;
        this.bd = bd;
	}
	
	public void iniciarComandos(int n) {
        pendentes = Math.max(1, n);
        desbloquear();                 // acorda a tarefa para produzir o lote
    }
	
	private Comando comandoAleatorio() {
		int tipo = rnd.nextInt(4); // 0=RETA_FRENTE, 1=CURVA_ESQ, 2=CURVA_DIR
		
		// Não é permitido os valores de distancia, raio e angulo serem menores que 10, 10, 30, respetivamente
        switch (tipo) {
            case 0: { // RETA_FRENTE
                int dist = 10 + rnd.nextInt(41); // 10..50 cm
                return Comando.retaFrente(dist);
            }
            case 1: { // CURVA_ESQ
                int raio = 10 + rnd.nextInt(21); // 10..30
                int ang  = 30 + rnd.nextInt(61); // 30..90
                return Comando.curvaEsq(raio, ang);
            }
            case 3:{
            	int dist = 10 + rnd.nextInt(41); // 10..50 cm
                return Comando.retaTras(dist);
            }
            default: { // CURVA_DIR
                int raio = 10 + rnd.nextInt(21);
                int ang  = 30 + rnd.nextInt(61);
                return Comando.curvaDir(raio, ang);
            }
        }
	}

    // -------- Máquina de estados herdada de Tarefa --------
    @Override
    protected void execucao() {
    	// só produz se o robot estiver ligado e houve pedido
        if (!bd.isRobotAberto() || pendentes <= 0) { bloquear(); return; }
        System.out.println("___Lote Nova____");
        // produzir exatamente 'pendentes' comandos
        for (int i = 0; i < pendentes; i++) {
            buffer.inserirElemento(comandoAleatorio());
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
        buffer.inserirElemento(Comando.parar());  // fecha o lote
        System.out.println("________________");

        pendentes = 0;     // esgota o pedido
        bloquear();        // volta a BLOQUEADO até novo clique da GUI
    }

    @Override
    protected void dormir() {
        // reduz a cadência quando "desativado"
        try { Thread.sleep(200); } catch (InterruptedException ignored) {}
    }

}
