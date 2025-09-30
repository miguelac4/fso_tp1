public class Comando {

    public enum Tipo { RETA_FRENTE, RETA_TRAS, CURVA_ESQ, CURVA_DIR, PARAR }

    // Apenas dois parametros pois o movimento mais complexo tem no máximo 2 parametros
    public final Tipo tipo;
    public final int p1;  // Parametro para todos os movimentos ( Distancia ou Raio )
    public final int p2;  // Parametro para movimentos de rotacao ( Angulo )

    private Comando(Tipo t, int p1, int p2) {
        this.tipo = t;
        this.p1 = p1;
        this.p2 = p2;
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
}
