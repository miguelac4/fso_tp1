
public class MonitorRobot {
    private boolean ocupado = false;
    private boolean prioridade = false;

    public synchronized void pedirAcesso() throws InterruptedException {
        while (ocupado || prioridade) wait();
        ocupado = true;
    }

    public synchronized void libertarAcesso() {
        ocupado = false;
        notifyAll();
    }

    // Usado pelo EvitarObstaculo para se meter a frente dos comandos
    public synchronized void pedirPrioridade() {
        prioridade = true;
       
        // EvitarObstaculo.
    }

    // Após parar o robô, EvitarObstaculo aguarda posse exclusiva
    public synchronized void assumirPossePrioritaria() throws InterruptedException {
        while (ocupado) wait();
        ocupado = true;
        prioridade = false; // posse adquirida, limpar sinal de prioridade
    }
}

