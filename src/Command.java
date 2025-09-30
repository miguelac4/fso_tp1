
public class Command {

	public enum Type { RETA_FRENTE, RETA_TRAS, CURVA_ESQ, CURVA_DIR, PARAR }
	
	
	public final Type type;
	public final int distancia;
	public final int raio;
	public final int angulo;
	
	private Command(Type t, int d, int r, int a) { 
		type=t; 
		distancia=d; 
		raio=r; 
		angulo=a; 
	}
	
	public static Command retaFrente(int dist){ 
		return new Command(Type.RETA_FRENTE, dist, 0, 0); 
	}
    public static Command retaTras(int dist)  { 
    	return new Command(Type.RETA_TRAS,   dist, 0, 0); 
    }
    public static Command curvaEsq(int r,int a){
    	return new Command(Type.CURVA_ESQ,  0, r, a);
    }
    public static Command curvaDir(int r,int a){
    	return new Command(Type.CURVA_DIR,  0, r, a);
    }
    public static Command parar(){ 
    	return new Command(Type.PARAR, 0, 0, 0); 
    }
}
