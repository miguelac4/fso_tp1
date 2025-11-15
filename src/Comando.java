public class Comando {

    public enum Tipo { RETA_FRENTE, RETA_TRAS, CURVA_ESQ, CURVA_DIR, PARAR, PARARFORCE }

    // Apenas dois parametros pois o movimento mais complexo tem no máximo 2 parametros
    public final Tipo tipo;
    public final int p1;  // Parametro para todos os movimentos ( Distancia ou Raio )
    public final int p2;  // Parametro para movimentos de rotacao ( Angulo )

    public Comando(Tipo t, int p1, int p2) {
        this.tipo = t;
        this.p1 = p1;
        this.p2 = p2;
    }

    public Comando(Comando comando) {
    	this.tipo = comando.tipo;
        this.p1 = comando.p1;
        this.p2 = comando.p2;
	}
    
    // Método ToString para retornar logs do conteudo de cada comando
    @Override
    public String toString() {
        return "Comando{" +
                "tipo=" + tipo +
                ", p1=" + p1 +
                ", p2=" + p2 +
                '}';
    }

	// Fábricas estáticas para maior clareza
    public static Comando retaFrente(int dist) {
        return new Comando(Tipo.RETA_FRENTE, dist, 0);
    }
    public static Comando retaTras(int dist) {
        return new Comando(Tipo.RETA_TRAS, dist, 0);
    }
    public static Comando curvaEsq(int raio, int ang) {
        return new Comando(Tipo.CURVA_ESQ, raio, ang);
    }
    public static Comando curvaDir(int raio, int ang) {
        return new Comando(Tipo.CURVA_DIR, raio, ang);
    }
    public static Comando parar() {
        return new Comando(Tipo.PARAR, 0, 0);
    }
    public static Comando pararForce() {
        return new Comando(Tipo.PARARFORCE, 0, 0);
    }
}
