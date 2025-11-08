import java.util.function.Consumer;

public class RobotLegoEV3SIM extends RobotLegoEV3 {

    private final Consumer<String> log;
    private volatile boolean ligado = false;

    // atraso “realista” por cm / por grau (ajusta como quiseres)
    private static final int MS_POR_CM   = 40;   // ex.: 20–60
    private static final int MS_POR_GRAU = 15;   // ex.: 10–25

    public RobotLegoEV3SIM() {
        this(System.out::println);
    }

    public RobotLegoEV3SIM(Consumer<String> logger) {
        this.log = (logger != null) ? logger : System.out::println;
    }

    @Override
    public boolean OpenEV3(String nome) {
        ligado = true;
        // TODO Send the command to Console
        log.accept("OpenEV3(" + nome + ")");
        return true;
    }

    @Override
    public void CloseEV3() {
        ligado = false;
        log.accept("CloseEV3()");
     // TODO Send the command to Console
    }

    @Override
    public void Reta(int distancia) {
        if (!ligado) { 
            log.accept("O comando 'Reta' foi ignorado (robot OFF)"); 
         // TODO Send the command to Console
            return; 
        }
        //log.accept("Reta(Frente, " + distancia + " cm)");
        dormir(Math.abs(distancia) * MS_POR_CM);
    }

    public void Tras(int distancia) {
        if (!ligado) { 
            log.accept("O comando 'Tras' foi ignorado (robot OFF)"); 
            return; 
        }
        //log.accept("Reta(Trás, " + distancia + " cm)");
        dormir(Math.abs(distancia) * MS_POR_CM);
    }

    @Override
    public void CurvarEsquerda(int raio, int angulo) {
        if (!ligado) { log.accept("O comando 'CurvarEsquerda' foi ignorado (robot OFF)"); return; }
        //log.accept("CurvarEsquerda(raio=" + raio + ", ang=" + angulo + ")");
        dormir((int)(raio * MS_POR_CM * 0.5 + Math.abs(angulo) * MS_POR_GRAU));
    }

    @Override
    public void CurvarDireita(int raio, int angulo) {
        if (!ligado) { log.accept("O comando 'CurvarDireita' foi ignorado (robot OFF)"); return; }
        //log.accept("CurvarDireita(raio=" + raio + ", ang=" + angulo + ")");
        dormir((int)(raio * MS_POR_CM * 0.5 + Math.abs(angulo) * MS_POR_GRAU));
    }

    @Override
    public void Parar(boolean imediato) {
        if (!ligado) { log.accept("O comando 'Parar' foi ignorado (robot OFF)"); return; }
        //log.accept("Parar(imediato=" + imediato + ")");
        if (!imediato) dormir(1000);
    }
    
    @Override
    public int SensorToque(int sensor) {
        if (!ligado) {
            //log.accept("SensorToque(" + sensor + ") -> robot OFF, devolve 0");
            return 0;
        }

        // SIMULAÇÃO: por agora não há obstáculo
        // (podes depois ligar isto a outra lógica qualquer)
        return 0;
    }

    private void dormir(int ms) {
        try { Thread.sleep(Math.max(0, ms)); } 
        catch (InterruptedException ignored) {}
    }
}
