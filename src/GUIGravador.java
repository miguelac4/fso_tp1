import javax.swing.filechooser.FileNameExtensionFilter;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.awt.event.ActionEvent;

public class GUIGravador extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final BaseDados bd;
    private final BaseDadosGravador bdG;
    private final BufferCircular bufferGravador;
	private final Gravar gravar;
    
    private JTextField txt_raio, txt_angulo, txt_dist, txt_robot, txt_ficheiro;
    private JCheckBox checkbox_ligar, checkbox_imprimir;
    private JButton button_frente, button_tras, button_esquerda, button_direita, button_parar, button_limpar;
    private JToggleButton toggle_gravar, toggle_reproduzir;
    private JTextArea console;

    // -- ESTADO DO GRAVADOR ---
    //private final List<String> gravacao = new ArrayList<>();
    //private volatile boolean aGravar = false;

    public GUIGravador(BaseDados bd, BaseDadosGravador bdG, BufferCircular bufferGravador, Gravar gravar, Reproduzir reproduzir) {
        setTitle("GUI Gravador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        this.bd = bd;
        this.bdG = bdG;
        this.bufferGravador = bufferGravador;        
        this.gravar = gravar;

        // -----labels
        JLabel lbl_robotTitulo = new JLabel("Robot");
        lbl_robotTitulo.setFont(new Font("Tahoma", Font.BOLD, 12));
        lbl_robotTitulo.setBounds(58, 11, 59, 15);
        getContentPane().add(lbl_robotTitulo);

        JLabel lbl_raio = new JLabel("Raio");
        lbl_raio.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_raio.setBounds(58, 34, 40, 27);
        getContentPane().add(lbl_raio);

        JLabel lbl_angulo = new JLabel("Ângulo");
        lbl_angulo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_angulo.setBounds(147, 34, 51, 27);
        getContentPane().add(lbl_angulo);

        JLabel lbl_dist = new JLabel("Distância");
        lbl_dist.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_dist.setBounds(257, 34, 67, 27);
        getContentPane().add(lbl_dist);

        txt_raio = new JTextField();
        txt_raio.addActionListener(e -> {
        	bd.setRaio(Integer.parseInt(txt_raio.getText()));
        });
        txt_raio.setBounds(100, 32, 37, 26);
        getContentPane().add(txt_raio);

        txt_angulo = new JTextField();
        txt_angulo.addActionListener(e -> {
        	bd.setAngulo(Integer.parseInt(txt_angulo.getText()));
        });
        txt_angulo.setBounds(203, 32, 37, 26);
        getContentPane().add(txt_angulo);

        txt_dist = new JTextField();
        txt_dist.addActionListener(e -> {
        	bd.setDistancia(Integer.parseInt(txt_dist.getText()));
        });
        txt_dist.setBounds(325, 32, 37, 26);
        getContentPane().add(txt_dist);

        JLabel lbl_robot = new JLabel("Robot");
        lbl_robot.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_robot.setBounds(446, 30, 47, 27);
        getContentPane().add(lbl_robot);

        txt_robot = new JTextField();
        txt_robot.addActionListener(e -> {
            String nome = txt_robot.getText();
            bd.setNomeRobot(nome);
            console.append("Nome do robot definido: " + nome + "\n");
        });
        txt_robot.setBounds(501, 32, 90, 26);
        getContentPane().add(txt_robot);
        
        

        // ----- botões dos comandos
        button_frente = new JButton("FRENTE");
        button_frente.setBackground(new Color(0, 255, 0));
        button_frente.setFont(new Font("Tahoma", Font.PLAIN, 15));
        button_frente.setBounds(273, 85, 89, 33);
        getContentPane().add(button_frente);

        button_parar = new JButton("PARAR");
        button_parar.setBackground(new Color(255, 0, 0));
        button_parar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        button_parar.setBounds(273, 119, 89, 33);
        getContentPane().add(button_parar);

        button_esquerda = new JButton("ESQUERDA");
        button_esquerda.setBackground(new Color(255, 0, 255));
        button_esquerda.setFont(new Font("Tahoma", Font.PLAIN, 15));
        button_esquerda.setBounds(167, 119, 105, 33);
        getContentPane().add(button_esquerda);

        button_tras = new JButton("TRÁS");
        button_tras.setBackground(new Color(255, 128, 0));
        button_tras.setFont(new Font("Tahoma", Font.PLAIN, 15));
        button_tras.setBounds(273, 153, 89, 33);
        getContentPane().add(button_tras);

        button_direita = new JButton("DIREITA");
        button_direita.setBackground(new Color(0, 0, 255));
        button_direita.setFont(new Font("Tahoma", Font.PLAIN, 15));
        button_direita.setBounds(363, 119, 105, 33);
        getContentPane().add(button_direita);
   
        //
        button_frente.addActionListener(e -> {

            if (!bd.isRobotAberto()) {
                console.append("Necessita de ligar o robot primeiro!\n");
                return;
            }

            int distancia = bd.getDistancia();
            if (distancia >= 10 && distancia <= 50) {

                gravar.enviarComandoManual(Comando.retaFrente(distancia));
                gravar.enviarComandoManual(Comando.parar());

                console.append("Fazer Reta | Distância = " + distancia + "\n");
            } else {
                console.append("Distância precisa de estar entre 10 e 50 cm\n");
            }
        });

        button_tras.addActionListener(e -> {
            if (!bd.isRobotAberto()) {
                console.append("Necessita de ligar o robot primeiro!\n");
                return;
            }

            int distancia = bd.getDistancia();
            if (distancia >= 10 && distancia <= 50) {

                gravar.enviarComandoManual(Comando.retaTras(distancia));
                gravar.enviarComandoManual(Comando.parar());

                console.append("Fazer Marcha-atrás | Distância = " + distancia + "\n");
            } else {
                console.append("Distância precisa de estar entre 10 e 50 cm\n");
            }
        });

        button_esquerda.addActionListener(e -> {
            if (!bd.isRobotAberto()) {
                console.append("Necessita de ligar o robot primeiro!\n");
                return;
            }

            int raio   = bd.getRaio();
            int angulo = bd.getAngulo();

            if (raio >= 0 && raio <= 30 && angulo >= 20 && angulo <= 90) {

                gravar.enviarComandoManual(Comando.curvaEsq(raio, angulo));
                gravar.enviarComandoManual(Comando.parar());

                console.append("Virar à esquerda | Raio = " + raio +
                               "; Ângulo = " + angulo + "\n");
            } else {
                console.append("Raio necessita de estar entre 0 e 30 cm | " +
                               "Ângulo necessita de estar entre 20 e 90 graus.\n");
            }
        });

        button_direita.addActionListener(e -> {
            if (!bd.isRobotAberto()) {
                console.append("Necessita de ligar o robot primeiro!\n");
                return;
            }

            int raio   = bd.getRaio();
            int angulo = bd.getAngulo();

            if (raio >= 0 && raio <= 30 && angulo >= 20 && angulo <= 90) {

                gravar.enviarComandoManual(Comando.curvaDir(raio, angulo));
                gravar.enviarComandoManual(Comando.parar());

                console.append("Virar à direita | Raio = " + raio +
                               "; Ângulo = " + angulo + "\n");
            } else {
                console.append("Raio necessita de estar entre 0 e 30 cm | " +
                               "Ângulo necessita de estar entre 20 e 90 graus.\n");
            }
        });

        
        button_parar.addActionListener(e -> {
            if (!bd.isRobotAberto()) {
                console.append("Necessita de ligar o robot primeiro!\n");
                return;
            }

            try {
                gravar.enviarComandoManual(Comando.parar());
                console.append("A parar o robot.\n");
            } catch (Exception ex) {
                console.append("Erro detetado ao tentar parar: " +
                               ex.getMessage() + "\n");
            }
        });

        
        
        // ---------------------------------------
        
        checkbox_ligar = new JCheckBox("Ligar");
        checkbox_ligar.addActionListener(e -> {
            if (!bd.isRobotAberto()) {
                // LIGAR
                String nome = bd.getNomeRobot();      // vem do txt_robot ou do default "EVA"
                boolean ok = bd.getRobot().OpenEV3(nome);
                bd.setRobotAberto(ok);
                checkbox_ligar.setSelected(ok);
                console.append(ok
                        ? "Robot conectado: " + nome + "\n"
                        : "Falha na ligação: " + nome + "\n");
            } else {
                // DESLIGAR
                if (bd.isRobotAberto()) {
                    RobotLegoEV3 robot = bd.getRobot();
                    synchronized (robot) {
                        try { robot.Parar(true); } catch (Exception ignore) {}
                        robot.CloseEV3();
                        robot.notifyAll();
                    }
                    bd.setRobotAberto(false);
                }
                console.append("Robot desligado.\n");
            }
        });

        checkbox_ligar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        checkbox_ligar.setBounds(478, 64, 97, 23);
        getContentPane().add(checkbox_ligar);

        // ----- gravador -----
        JLabel lbl_gravador = new JLabel("Gravador");
        lbl_gravador.setFont(new Font("Tahoma", Font.BOLD, 12));
        lbl_gravador.setBounds(58, 197, 59, 15);
        getContentPane().add(lbl_gravador);

        JLabel lbl_ficheiro = new JLabel("Ficheiro");
        lbl_ficheiro.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_ficheiro.setBounds(58, 211, 67, 27);
        getContentPane().add(lbl_ficheiro);

        txt_ficheiro = new JTextField();
        txt_ficheiro.setBounds(124, 215, 381, 23);
        getContentPane().add(txt_ficheiro);
        
        txt_ficheiro.addActionListener(e -> {
        	String nome = txt_ficheiro.getText().trim();
        	bdG.setPathFicheiro(nome);
        });
        
        JButton button_selecionar = new JButton("...");  
        button_selecionar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JFileChooser chooser = new JFileChooser();
        		
        		boolean estaAGravar = toggle_gravar.isSelected();
        		
        		if (estaAGravar) {
        			chooser.setDialogTitle("Selecione onde deseja gravar o ficheiro");
        			int resultado = chooser.showSaveDialog(GUIGravador.this);
        			
            		if (resultado == JFileChooser.APPROVE_OPTION) {
            			File f = chooser.getSelectedFile();
            			
            			if (!f.getName().contains(".")) {
            				f = new File(f.getAbsolutePath() + ".txt");
            			}
            			
            			String path = f.getAbsolutePath();
            			
            			//mostra a respetiva diretoria na GUI
            			txt_ficheiro.setText(path);
            			//guarda na base de dados do gravar
            			bdG.setPathFicheiro(path);
            		}
        		} else {
        			
        			chooser.setDialogTitle("Selecione o ficheiro que pretende reproduzir");
        			int resultado = chooser.showOpenDialog(GUIGravador.this);
        			
        			if (resultado == JFileChooser.APPROVE_OPTION) {
        				File f = chooser.getSelectedFile();
        				
        				String path = f.getAbsolutePath();
        				txt_ficheiro.setText(path);
        				bdG.setPathFicheiro(path);
        			}
        		}
  
        		//debug
        		System.out.println("toggle_gravar=" + toggle_gravar.isSelected()
                + "  bdG.isRecording=" + bdG.getIsRecording());
     
             	}
        });
        button_selecionar.setFont(new Font("Tahoma", Font.BOLD, 13));
        button_selecionar.setBounds(515, 215, 67, 23);
        getContentPane().add(button_selecionar);

        toggle_gravar = new JToggleButton("Gravar");
        toggle_gravar.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		boolean selected = toggle_gravar.isSelected();
        		
        		bdG.setIsRecording(selected);
        		
        		if (selected) {
        			toggle_gravar.setText("● Gravar");
        			toggle_gravar.setForeground(Color.RED);
        			
        			gravar.desbloquear();
        		} else {
        			toggle_gravar.setText("Gravar");
        			toggle_gravar.setForeground(Color.BLACK);
        		}
        	}
        });
        toggle_gravar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        toggle_gravar.setBounds(192, 249, 121, 23);
        getContentPane().add(toggle_gravar);
        

        toggle_reproduzir = new JToggleButton("Reproduzir");
        toggle_reproduzir.addActionListener(e -> {
            boolean selected = toggle_reproduzir.isSelected();

            if (selected) {
                toggle_reproduzir.setText("● Reproduzir");
                toggle_reproduzir.setForeground(Color.RED);

                // Impedir que gravar em simultâneo
                toggle_gravar.setSelected(false);
                bdG.setIsRecording(false);
                
                bdG.setIsReproducing(true);
                reproduzir.desbloquear();

            } else {
                toggle_reproduzir.setText("Reproduzir");
                toggle_reproduzir.setForeground(Color.BLACK);
                
                bdG.setIsReproducing(false);
            }
        });
        toggle_reproduzir.setFont(new Font("Tahoma", Font.PLAIN, 14));
        toggle_reproduzir.setBounds(318, 249, 121, 23);
        getContentPane().add(toggle_reproduzir);

        // ----- consola -----
        JLabel lbl_consola = new JLabel("Consola");
        lbl_consola.setFont(new Font("Tahoma", Font.BOLD, 12));
        lbl_consola.setBounds(58, 291, 59, 15);
        getContentPane().add(lbl_consola);

        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(51, 310, 534, 102);
        getContentPane().add(scroll);

        console = new JTextArea();
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        scroll.setViewportView(console);

        button_limpar = new JButton("Limpar");
        button_limpar.setFont(new Font("Tahoma", Font.PLAIN, 15));
        button_limpar.setBounds(207, 423, 105, 27);
        getContentPane().add(button_limpar);

        checkbox_imprimir = new JCheckBox("Imprimir");
        checkbox_imprimir.setFont(new Font("Tahoma", Font.PLAIN, 14));
        checkbox_imprimir.setBounds(325, 425, 97, 23);
        getContentPane().add(checkbox_imprimir);
        

        setSize(660, 520);
        setLocationRelativeTo(null);
        
        setVisible(true);
    }

  
    
    // -------- getters para o "controller" usar --------

    public JTextField getTxtRaio()        { return txt_raio; }
    public JTextField getTxtAngulo()      { return txt_angulo; }
    public JTextField getTxtDist()        { return txt_dist; }
    public JTextField getTxtRobot()       { return txt_robot; }
    public JTextField getTxtFicheiro()    { return txt_ficheiro; }

    public JCheckBox getCheckboxLigar()       { return checkbox_ligar; }
    public JCheckBox getCheckboxImprimir()    { return checkbox_imprimir; }

    public JButton getButtonFrente()      { return button_frente; }
    public JButton getButtonTras()        { return button_tras; }
    public JButton getButtonEsquerda()    { return button_esquerda; }
    public JButton getButtonDireita()     { return button_direita; }
    public JButton getButtonParar()       { return button_parar; }
    public JButton getButtonLimpar()      { return button_limpar; }

    public JToggleButton getToggleGravar()     { return toggle_gravar; }
    public JToggleButton getToggleReproduzir() { return toggle_reproduzir; }

    public JTextArea getConsole()         { return console; }
}


