public class Servidor extends Tarefa {
    
	private BufferCircular buffer;
    private RobotLegoEV3 robot;
    private final BaseDados bd;
    private final MonitorRobot monitor;

    /*
     * Classe Consumidora
     * Executa os comandos no Robot
     */
    public Servidor(BufferCircular buffer, BaseDados bd, RobotLegoEV3 robot, MonitorRobot monitor) {
    	super();
    	this.bd = bd;
        this.buffer = buffer;
        this.robot = robot;
        this.monitor = monitor;
    }

    // Métodos separados (bons para depuração e modularidade)
    public void Reta(int distancia) {
        robot.Reta(distancia);
    }

    public void CurvarDireita(int raio, int angulo) {
        robot.CurvarDireita(raio, angulo);
    }

    public void CurvarEsquerda(int raio, int angulo) {
        robot.CurvarEsquerda(raio, angulo);
    }

    public void Parar() {
        robot.Parar(false);
    }

    // Método principal do consumidor (executa o comando retirado do buffer)
    public void executar(Comando comando) {
    	
        switch (comando.tipo) {
            case RETA_FRENTE:
                Reta(comando.p1);
                break;
            case RETA_TRAS:
            	Reta(-comando.p1);
                break;
            case CURVA_DIR:
                CurvarDireita(comando.p1, comando.p2);
                break;
            case CURVA_ESQ:
                CurvarEsquerda(comando.p1, comando.p2);
                break;
            case PARAR:
                Parar();
                break;
        }
    }
    
    public int getTempoEspera(Comando c) {
    	double vel = 20;
        int tempo_com = 100;
        int dist = 0;
        int raio = 0;
        int angulo = 0;
        int tempo_espera = 0;
        
	   switch (c.tipo) {
	        case RETA_FRENTE:
	        case RETA_TRAS:
	        	dist = Math.abs(c.p1); // usar modulo porque em caso de reta para tras nao queremos uma distancia negativa
	        	tempo_espera = (int) ((dist / vel) + tempo_com);
	        	break;
	        case CURVA_DIR:
	        case CURVA_ESQ:
	        	raio = c.p1;
	        	//System.out.println("c.p2 = " + c.p2);
	        	angulo = c.p2;
	        	//System.out.println("angulo = " + angulo);
	        	tempo_espera = (int) ((raio * angulo * 1000 * (2*Math.PI/360) / vel) + tempo_com);
	        	//System.out.println("Raio = " + raio + "\n Angulo = " + angulo);
	            break;
	        case PARAR:
	            tempo_espera = tempo_com;
	            break;
	    }
	   return tempo_espera;
    }
    
    // Exclusão Mutua entre Servidor - EvitarObstaculo
    private void executarComExclusao(Comando c) {
        try {
            monitor.pedirAcesso();       // bloqueia se Evitar pediu preempção
            executar(c);                 // envia comando ao robô
            Thread.sleep(getTempoEspera(c));  // aguarda duração do comando
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        } finally {
            monitor.libertarAcesso();    // permite Evitar assumir (se preempção ativa)
        }
    }


    @Override
    protected void execucao() {
    	/*
    	if (!bd.isRobotAberto()) { bloquear(); return; }

        // 1) aguarda 1 comando (bloqueante)
        Comando c = buffer.removerElemento();
        
        executar(c);
        System.out.println("[EXEC] " + c.tipo + " | P1: " + c.p1 + " | P2: " + c.p2);
        System.out.println("[c/] Tempo de Execução: " + getTempoEspera(c));
        try { Thread.sleep(getTempoEspera(c)); } catch (InterruptedException ignored) {}

        // 2) drena os restantes que já estejam disponíveis (sem dormir)
        while (buffer.ocupados() > 0) {
            c = buffer.removerElemento();
            
            executar(c);
            System.out.println("[EXEC] " + c.tipo + " | P1: " + c.p1 + " | P2: " + c.p2);
            System.out.println("[c/] Tempo de Execução: " + getTempoEspera(c));
            try { Thread.sleep(getTempoEspera(c)); } catch (InterruptedException ignored) {}
        }
        */
    	
    	if (!bd.isRobotAberto()) { bloquear(); return; }

        // 1) um comando (bloqueante)
        Comando c = buffer.removerElemento();
        System.out.println("[EXEC] " + c.tipo + " | P1: " + c.p1 + " | P2: " + c.p2);
        System.out.println("[c/] Tempo de Execução: " + getTempoEspera(c));
        executarComExclusao(c);

        // 2) drena restantes disponíveis (cada um protegido pelo monitor)
        while (buffer.ocupados() > 0) {
            c = buffer.removerElemento();
            System.out.println("[EXEC] " + c.tipo + " | P1: " + c.p1 + " | P2: " + c.p2);
            System.out.println("[c/] Tempo de Execução: " + getTempoEspera(c));
            executarComExclusao(c);
        }
    }

    @Override
    protected void dormir() {
        //try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
    }
}
