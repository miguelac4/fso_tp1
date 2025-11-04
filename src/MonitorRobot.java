
// MonitorAcessoRobot.java
public class MonitorRobot {
    private boolean ocupado = false;
    private boolean preempt = false;

    public synchronized void pedirAcesso() throws InterruptedException {
        while (ocupado || preempt) wait();
        ocupado = true;
    }

    public synchronized void libertarAcesso() {
        ocupado = false;
        notifyAll();
    }

    // Sinalização de emergência: EvitarObstaculo quer assumir o controlo
    public synchronized void pedirPreempcao() {
        preempt = true;
        // não bloqueia aqui; serve para travar novas entradas de Servidor/GUI
        // EvitarObstaculo deve parar o robô já de seguida e depois tomar posse.
    }

    // Após parar o robô, EvitarObstaculo aguarda posse exclusiva
    public synchronized void assumirPossePreemptiva() throws InterruptedException {
        while (ocupado) wait();
        ocupado = true;
        preempt = false; // posse adquirida, limpar sinal de preempção
    }
}

