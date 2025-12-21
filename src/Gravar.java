import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.function.Consumer;

import javax.swing.JTextArea;

public class Gravar extends Tarefa {
	
	private RobotLegoEV3 robot;
	private final BaseDados bd;
	private final BaseDadosGravador bdG;
	private final Consumer<String> log;
	
	private GUIGravador guiGravador;
	private BufferCircular buffer; //buffer servidor -> robot
	private BufferCircular bufferGravador; //buffer para gravar o ficheiro
	
	private static final String NOME_BLOCO = "bloco_gravador.txt";
	private JTextArea console;
	
	public Gravar(BufferCircular buffer, BufferCircular bufferGravador, BaseDados bd, 
				BaseDadosGravador bdG, RobotLegoEV3 robot, Consumer<String> logger) {
		super();
		this.bd = bd;
		this.bdG = bdG;
		this.buffer = buffer;
		this.robot = robot;
		this.log = (logger != null) ? logger : System.out::println;
		this.bufferGravador = bufferGravador;
	}
	
	public void enviarComandoManual(Comando c) {
		buffer.inserirElemento(c);
		
		if (bdG.getIsRecording()) {
			bufferGravador.inserirElemento(new Comando(c));
			
		}
	}
	
	// CLASSES AUXILIARES A gravarComandoNoBloco()___________________________
	private String comandoParaLinha(Comando c) {
        switch (c.tipo) {
            case RETA_FRENTE:
                return "RETA_FRENTE " + c.p1;
            case RETA_TRAS:
                return "RETA_TRAS " + c.p1;
            case CURVA_ESQ:
                return "CURVA_ESQ " + c.p1 + " " + c.p2;
            case CURVA_DIR:
                return "CURVA_DIR " + c.p1 + " " + c.p2;
            case PARAR:
                return "PARAR";
            case PARARFORCE:
                return "PARARFORCE";
            default:
                return c.toString();
        }
    }
	
	public boolean isTxtPath(String path) {
	    return path != null && path.endsWith(".txt");
	}
	
	/*
	public boolean txtFileExists(String path) {
	    // verifica se a string não é null e termina em ".txt"
	    if (path == null || !isTxtPath(path)) {
	        return false;
	    }

	    File ficheiro = new File(path);
	    // exists() verifica se existe; isFile() garante que é ficheiro e não pasta
	    return ficheiro.exists() && ficheiro.isFile();
	}
	*/

	//________________________________________________________________________
	
	public void gravarComandoNoBloco() {
	    // verificar se há comandos no buffer
	    if (bufferGravador.ocupados() == 0) {
	    	//System.out.println("TESTE1");
	        return;
	    }

	    // vai buscar o primeiro comando do buffer
	    Comando c = bufferGravador.removerElemento();
	    if (c == null) {
	    	//System.out.println("TESTE2");
	    	return;
	    }

	    String linha = comandoParaLinha(c);
	    // path definido na BaseDadosGravador (vindo da GUI)
	    String path = bdG.getPathFicheiro();

	    // 1) verifica se termina em .txt
	    if (!isTxtPath(path)) {
	        log.accept("Path inválido (não termina em .txt): " + path);
	        //System.out.println("TESTE3");
	        return;
	    }
	    
	    File ficheiro = new File(path);
	    try {
	    	File parent = ficheiro.getParentFile();
	    	
	    	if (parent != null && !parent.exists()) {
	    		parent.mkdirs();
	    	}
	    
	    	if (!ficheiro.exists()) {
	    		ficheiro.createNewFile();
	    	}
	    
	    } catch (IOException e) {
	    	log.accept("Erro ao criar o ficheiro: " + e.getMessage());
	    	return;
	    }

	    /*
	    // 2) verifica se o ficheiro existe
	    if (!isTxtPath(path)) {
	        log.accept("Ficheiro .txt não existe: " + path);
	        //System.out.println("TESTE4");
	        return;
	    }
	     */
	    
	    
	    
	    // 3) abre em modo append e escreve a linha
	    try (FileWriter fw = new FileWriter(path, true);
	         PrintWriter out = new PrintWriter(fw)) {

	        out.println(linha);
	        log.accept("Gravado no ficheiro [" + path + "]: " + linha);

	    } catch (IOException ex) {
	    	//System.out.println("TESTE5");
	        log.accept("Erro a escrever no ficheiro [" + path + "]: " + ex.getMessage());
	    }
	}

	
	
	// Usar inserirElemento e removerElemento da classe BufferCircular

	@Override
	protected void execucao() {
		// TODO Auto-generated method stub
		//System.out.print("FUNFA");
		if (!bdG.getIsRecording()) {
            bloquear();
            return;
        }
		
		gravarComandoNoBloco();
		
	}
	
	@Override
    protected void dormir() {
        try {
            Thread.sleep(10);  // pequena pausa entre execuções
        } catch (InterruptedException ignored) {}
    }
	
	

}
