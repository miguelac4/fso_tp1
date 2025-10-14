public class Servidor extends Tarefa {
    
	private BufferCircular buffer;
    private RobotLegoEV3 robot;

    /*
     * Classe Consumidora
     * Executa os comandos no Robot
     */
    public Servidor(BufferCircular buffer, RobotLegoEV3 robot, Tarefa proxima) {
    	super(proxima);
        this.buffer = buffer;
        this.robot = robot;
    }

    // Métodos separados (bons para depuração e modularidade)
    public void Reta(int distancia) {
    	System.out.print("TEST");
        robot.Reta(distancia);
    }

    public void CurvarDireita(int raio, int angulo) {
    	System.out.print("TEST");
        robot.CurvarDireita(raio, angulo);
    }

    public void CurvarEsquerda(int raio, int angulo) {
    	System.out.print("TEST");
        robot.CurvarEsquerda(raio, angulo);
    }

    public void Parar() {
        robot.Parar(false);
    }

    // Método principal do consumidor (executa o comando retirado do buffer)
    public void executar() {
        Comando comando = buffer.removerElemento();
        switch (comando.tipo) {
            case RETA_FRENTE:
                Reta(comando.p1);
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
        executar();
    }

    @Override
    protected void dormir() {
        try { Thread.sleep(20000); } catch (InterruptedException ignored) {}
    }
}
