public class Servidor extends Tarefa {
    
	private BufferCircular buffer;
    private RobotLegoEV3 robot;
    private final BaseDados bd;

    /*
     * Classe Consumidora
     * Executa os comandos no Robot
     */
    public Servidor(BufferCircular buffer, BaseDados bd, RobotLegoEV3 robot, Tarefa proxima) {
    	super(proxima);
    	this.bd = bd;
        this.buffer = buffer;
        this.robot = robot;
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

    @Override
    protected void execucao() {
    	if (!bd.isRobotAberto()) { bloquear(); return; }

        // 1) aguarda 1 comando (bloqueante)
        Comando c = buffer.removerElemento();
        executar(c);

        // 2) drena os restantes que já estejam disponíveis (sem dormir)
        while (buffer.ocupados() > 0) {
            c = buffer.removerElemento();
            executar(c);
        }
    }

    @Override
    protected void dormir() {
        //try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
    }
}
