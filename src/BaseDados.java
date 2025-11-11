public class BaseDados {
    private boolean terminar;
    private boolean robotAberto;
    private RobotLegoEV3 robot;
    private int distancia;
    private int angulo;
    private int raio;
    private int spinnerNum;
    private String nomeRobot = "EVA";
    public volatile boolean prioridadeEvitar = false; // flag de prioridade

    public BaseDados() {
        terminar = false;
        robotAberto = false;
        
        // DESCOMENTAR PARA USAR SIMULAÇÃO
        robot = new RobotLegoEV3();
        //robot = new RobotLegoEV3SIM(mensagem -> System.out.println(mensagem));
        
        spinnerNum = 0;
        //distancia = 0;
        //angulo = 0;
        //raio = 0;
    }
  

    public RobotLegoEV3 getRobot() { return robot; }
    
    public void setRobot(RobotLegoEV3 robot) {
        this.robot = robot;
    }
    
    public boolean isTerminar() { return terminar; }
    public void setTerminar(boolean terminar) { this.terminar = terminar; }
    public boolean isRobotAberto() { return robotAberto; }
    public void setRobotAberto(boolean robotAberto) { this.robotAberto = robotAberto; }
    
    public void setDistancia(int distancia ) { this.distancia = distancia; }
    public int getDistancia() { return distancia;}
    
    public void setAngulo(int angulo) { this.angulo = angulo; }
    public int getAngulo() { return angulo;}
    
    public void setRaio(int raio) { this.raio = raio; }
    public int getRaio() { return raio;}
   
    public void setNomeRobot(String nomeRobot) { this.nomeRobot = nomeRobot; }
    public String getNomeRobot() { return nomeRobot; }
    
    public void setSpinnerNum(int spinnerNum) {
    	this.spinnerNum = spinnerNum;
    }
    
    public int getSpinnerNum() {
    	return spinnerNum;
    }	
}
