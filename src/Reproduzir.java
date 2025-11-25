import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Consumer;

public class Reproduzir extends Tarefa {

    private final BufferCircular buffer;      // buffer principal -> servidor
    private final BaseDadosGravador bdG;
    private final Consumer<String> log;

    public Reproduzir(BufferCircular buffer, BaseDadosGravador bdG, Consumer<String> logger) {
        this.buffer = buffer;
        this.bdG = bdG;
        this.log = (logger != null) ? logger : System.out::println;
    }

    @Override
    protected void execucao() {
        if (!bdG.getIsReproducing()) {
            bloquear();   // não está a reproduzir → dormir
            return;
        }

        String path = bdG.getPathFicheiro();
        if (path == null || path.isEmpty()) {
            log.accept("Nenhum ficheiro selecionado para reprodução.");
            bdG.setIsReproducing(false);
            bloquear();
            return;
        }

        File f = new File(path);
        if (!f.exists()) {
            log.accept("Ficheiro não encontrado: " + path);
            bdG.setIsReproducing(false);
            bloquear();
            return;
        }

        log.accept("-> A REPRODUZIR: " + path + " ---> ");

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {

            String linha;
            while ((linha = br.readLine()) != null) {

                Comando cmd = parseLinhaParaComando(linha);

                if (cmd != null) {
                    buffer.inserirElemento(cmd);
                }
            }

            log.accept(" ----- FIM DA REPRODUÇÃO ----- ");

        } catch (IOException e) {
            log.accept("Erro ao ler o ficheiro: " + e.getMessage());
        }

        // a reprodução do ficheiro chegou ao fim
        bdG.setIsReproducing(false);
        bloquear(); 
    }


    @Override
    protected void dormir() {
        try { Thread.sleep(10); } catch (InterruptedException ignored) {}
    }


    // ---------------------------- PARSER ----------------------------
    private Comando parseLinhaParaComando(String linha) {

        String[] t = linha.trim().split(" ");

        try {
            switch (t[0]) {
                case "RETA_FRENTE": return Comando.retaFrente(Integer.parseInt(t[1]));
                case "RETA_TRAS":   return Comando.retaTras(Integer.parseInt(t[1]));
                case "CURVA_ESQ":   return Comando.curvaEsq(Integer.parseInt(t[1]), Integer.parseInt(t[2]));
                case "CURVA_DIR":   return Comando.curvaDir(Integer.parseInt(t[1]), Integer.parseInt(t[2]));
                case "PARAR":       return Comando.parar();
                case "PARARFORCE":  return Comando.pararForce();
            }
        } catch (Exception e) {
            log.accept("Linha inválida no ficheiro: " + linha);
        }

        return null;
    }
}
