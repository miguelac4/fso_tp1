import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {

	private Timer timerAleatorio;
    private BaseDados bd;
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField text_Distancia, text_Angulo, text_Raio, text_Robot;
    private JCheckBox chckbxNewCheckBox;
    private JButton btnFrente, btnEsquerda, btnTras, btnDireita, btnParar;
    private JSpinner spinner;
    private JRadioButton rdbtnNewRadioButton;
    private JTextArea console;
    private MovimentoAleatorio produtor;
    private Servidor consumidor;
    private BufferCircular buffer;
    
    private void Consola(String s) {
    	console.append(s+"\n");
    }
    
    public void setBuffer(BufferCircular buffer) {
        this.buffer = buffer;
    }

    public GUI() {
        setTitle("EV3 Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBorder(new javax.swing.border.EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        bd = new BaseDados();
        

        // Fecho seguro da app
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                if (bd.isRobotAberto()) {
                    bd.getRobot().CloseEV3();
                    bd.setRobotAberto(false);
                }
                bd.setTerminar(true);
                dispose();
            }
        });
        

        // GRELHA DO SISTEMA
        JPanel panelTop = new JPanel();
        contentPane.add(panelTop);
        GridBagLayout gbl_panelTop = new GridBagLayout();
        gbl_panelTop.columnWidths = new int[]{0,0,0,0,0,0,0,0,0};
        gbl_panelTop.rowHeights  = new int[]{0,0,0,0,0, 0, 0, 0, 0, 0, 0,0,0};
        gbl_panelTop.columnWeights = new double[]{1.0,1.0,1.0,1.0,0.0,1.0,0.0,1.0,Double.MIN_VALUE};
        gbl_panelTop.rowWeights    = new double[]{0.0,0.0,0.0,0.0,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0,Double.MIN_VALUE};
        panelTop.setLayout(gbl_panelTop);

             
        // CAMPO --> RAIO
        JLabel lbl_Raio = new JLabel("Raio");
        GridBagConstraints gbc_lbl_Raio = new GridBagConstraints();
        gbc_lbl_Raio.anchor = GridBagConstraints.EAST;
        gbc_lbl_Raio.insets = new Insets(0,0,5,5);
        gbc_lbl_Raio.gridx = 0; gbc_lbl_Raio.gridy = 0;
        panelTop.add(lbl_Raio, gbc_lbl_Raio);
        text_Raio = new JTextField(); 
        text_Raio.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		bd.setRaio(Integer.parseInt(text_Raio.getText()));
        	}
        });text_Raio.setColumns(5);
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0,0,5,5);
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 1; gbc_textField.gridy = 0;
        panelTop.add(text_Raio, gbc_textField);
        
        
        
        // CAMPO --> ÂNGULO
        JLabel lbl_Angulo = new JLabel("Ângulo");
        GridBagConstraints gbc_lbl_Angulo = new GridBagConstraints();
        gbc_lbl_Angulo.anchor = GridBagConstraints.EAST;
        gbc_lbl_Angulo.insets = new Insets(0,0,5,5);
        gbc_lbl_Angulo.gridx = 2; gbc_lbl_Angulo.gridy = 0;
        panelTop.add(lbl_Angulo, gbc_lbl_Angulo);

        text_Angulo = new JTextField(); 
        text_Angulo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		bd.setAngulo(Integer.parseInt(text_Angulo.getText()));
        	}
        });text_Angulo.setColumns(1);
        GridBagConstraints gbc_text_Angulo = new GridBagConstraints();
        gbc_text_Angulo.insets = new Insets(0,0,5,5);
        gbc_text_Angulo.fill = GridBagConstraints.HORIZONTAL;
        gbc_text_Angulo.gridx = 3; gbc_text_Angulo.gridy = 0;
        panelTop.add(text_Angulo, gbc_text_Angulo);
        
        
                
        // CAMPO --> DISTÂNCIA
        JLabel lbl_Distancia = new JLabel("Distancia");
        GridBagConstraints gbc_lbl_Distancia = new GridBagConstraints();
        gbc_lbl_Distancia.anchor = GridBagConstraints.EAST;
        gbc_lbl_Distancia.insets = new Insets(0,0,5,5);
        gbc_lbl_Distancia.gridx = 4; gbc_lbl_Distancia.gridy = 0;
        panelTop.add(lbl_Distancia, gbc_lbl_Distancia);

        text_Distancia = new JTextField(); 
        text_Distancia.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		bd.setDistancia(Integer.parseInt(text_Distancia.getText()));
        	}
        });
        text_Distancia.setColumns(1);
        GridBagConstraints gbc_text_Distancia = new GridBagConstraints();
        gbc_text_Distancia.insets = new Insets(0,0,5,5);
        gbc_text_Distancia.fill = GridBagConstraints.HORIZONTAL;
        gbc_text_Distancia.gridx = 5; gbc_text_Distancia.gridy = 0;
        panelTop.add(text_Distancia, gbc_text_Distancia);
        
        
                
        // CAMPO --> NOME DO ROBOT
        JLabel lbl_Robot = new JLabel("Robot");
        GridBagConstraints gbc_lbl_Robot = new GridBagConstraints();
        gbc_lbl_Robot.anchor = GridBagConstraints.EAST;
        gbc_lbl_Robot.insets = new Insets(0,0,5,5);
        gbc_lbl_Robot.gridx = 6; gbc_lbl_Robot.gridy = 0;
        panelTop.add(lbl_Robot, gbc_lbl_Robot);

        text_Robot = new JTextField(); 
        text_Robot.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String nome = text_Robot.getText();
        		bd.setNomeRobot(text_Robot.getText());
        		Consola("Nome do robot definido:" + nome);
        	}
        });text_Robot.setColumns(1);
        GridBagConstraints gbc_text_Robot = new GridBagConstraints();
        gbc_text_Robot.insets = new Insets(0,0,5,0);
        gbc_text_Robot.fill = GridBagConstraints.HORIZONTAL;
        gbc_text_Robot.gridx = 7; gbc_text_Robot.gridy = 0;
        panelTop.add(text_Robot, gbc_text_Robot);
        
        
                
        // CAIXA --> ESTABELECER LIGAÇÃO COM O ROBOT --EV10--
        chckbxNewCheckBox = new JCheckBox("Ligar");
        chckbxNewCheckBox.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (!bd.isRobotAberto()) {
        			String nome = bd.getNomeRobot();
        			boolean ok = bd.getRobot().OpenEV3(nome);
        			bd.setRobotAberto(ok);
        			chckbxNewCheckBox.setSelected(ok);
        			Consola(ok ? "Robot conectado: " + nome 
        					: " Falha na ligação: " + nome);
        			
        			if (ok && produtor != null && consumidor != null) {
                        produtor.desbloquear();
                        consumidor.desbloquear();
        			}
        		} else {
        			if (bd.isRobotAberto()) {
        				bd.getRobot().CloseEV3();
        				bd.setRobotAberto(false);
        			}
        			Consola("Robot desligado.");
        			
        			if (produtor != null && consumidor != null) {
                        produtor.bloquear();
                        consumidor.bloquear();
                    }
        		}	
        	}
        });
        GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
        gbc_chckbxNewCheckBox.insets = new Insets(0,0,5,5);
        gbc_chckbxNewCheckBox.gridx = 6; gbc_chckbxNewCheckBox.gridy = 1;
        panelTop.add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
        
        
           
        // BOTÃO --> FRENTE
        btnFrente = new JButton("FRENTE");
        btnFrente.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		if (!bd.isRobotAberto()) {
        			Consola("Necessita de ligar o robot primeiro!");
        			return;
        		}
        		
        		int distancia = bd.getDistancia();
        		if (distancia >= 10 && distancia <= 50) {
        			
        			buffer.inserirElemento(Comando.retaFrente(distancia));
        			buffer.inserirElemento(Comando.parar());
        			
        			//bd.getRobot().Reta(distancia);
        			//bd.getRobot().Parar(false);
        			
            		Consola("Fazer Reta | Distância = " + distancia);
        		} else {
        			Consola("Distância precisa de estar entre 10 e 50 cm");
        		}     		
        	}
        });
        btnFrente.setBackground(Color.GREEN);
        btnFrente.setOpaque(true);
        btnFrente.setContentAreaFilled(true);
        GridBagConstraints gbc_btnFrente = new GridBagConstraints();
        gbc_btnFrente.insets = new Insets(0,0,5,5);
        gbc_btnFrente.gridx = 4; gbc_btnFrente.gridy = 2;
        panelTop.add(btnFrente, gbc_btnFrente);

            
        
        // BOTÃO --> ESQUERDA
        btnEsquerda = new JButton("ESQUERDA");
        btnEsquerda.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (!bd.isRobotAberto()) {
        			Consola("Necessita de ligar o robot primeiro!");
        			return;
        		}
        		int raio = bd.getRaio();
        		int angulo = bd.getAngulo();
        		if (raio >= 10 && raio <= 30 && angulo >= 20 && angulo <= 90) {
        			
        			buffer.inserirElemento(Comando.curvaEsq(raio, angulo));
        			buffer.inserirElemento(Comando.parar());
        			
        			//bd.getRobot().CurvarEsquerda(raio, angulo);
        			//bd.getRobot().Parar(false);
        			
        			Consola("Virar à esquerda | Raio = " + raio + "; Ângulo = " + angulo);
        		} else {
        			Consola("Raio necessita de estar entre 10 e 30 cm | "
        					+ "Ângulo necessita de estar entre 20 e 90 graus.");
        		}
        	}
        });
        btnEsquerda.setBackground(Color.MAGENTA);
        btnEsquerda.setOpaque(true);
        btnEsquerda.setContentAreaFilled(true);
        GridBagConstraints gbc_btnEsquerda = new GridBagConstraints();
        gbc_btnEsquerda.insets = new Insets(0,0,5,5);
        gbc_btnEsquerda.gridx = 3; gbc_btnEsquerda.gridy = 3;
        panelTop.add(btnEsquerda, gbc_btnEsquerda);
          
        
        
        // BOTÃO --> PARAR
        btnParar = new JButton("PARAR");
        btnParar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (!bd.isRobotAberto()) {
        			Consola("Necessita de ligar o robot primeiro!");
        			return;
        		}
        		try {
        			
        			buffer.inserirElemento(Comando.parar());
        			
        			//bd.getRobot().Parar(true); 
        			
        			Consola("A parar o robot.");
        		} catch (Exception ex) {
        			Consola("Erro detetado ao tentar parar." + ex.getMessage());	
		}
        	}
        });
        btnParar.setBackground(Color.RED);
        btnParar.setOpaque(true);
        btnParar.setContentAreaFilled(true);
        GridBagConstraints gbc_btnParar = new GridBagConstraints();
        gbc_btnParar.insets = new Insets(0,0,5,5);
        gbc_btnParar.gridx = 4; gbc_btnParar.gridy = 3;
        panelTop.add(btnParar, gbc_btnParar);
          
        
        
        // BOTÃO --> DIREITA
        btnDireita = new JButton("DIREITA");
        btnDireita.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if (!bd.isRobotAberto()) {
        			Consola("Necessita de ligar o robot primeiro!");
        			return;
        		}
        		int raio = bd.getRaio();
        		int angulo = bd.getAngulo();
        		if (raio >= 10 && raio <= 30 && angulo >= 20 && angulo <= 90) {
        			
        			buffer.inserirElemento(Comando.curvaDir(raio, angulo));
        			buffer.inserirElemento(Comando.parar());
        			
        			//bd.getRobot().CurvarDireita(raio, angulo);
        			//bd.getRobot().Parar(false);
        			
        			Consola("Virar à direita | Raio = " + raio + "; Ângulo = " + angulo);
        		} else {
        			Consola("Raio necessita de estar entre 10 e 30 cm | "
        					+ "Ângulo necessita de estar entre 20 e 90 graus.");
        		}
        	}
        });
        btnDireita.setBackground(Color.BLUE);
        btnDireita.setOpaque(true);
        btnDireita.setContentAreaFilled(true);
        GridBagConstraints gbc_btnDireita = new GridBagConstraints();
        gbc_btnDireita.insets = new Insets(0,0,5,5);
        gbc_btnDireita.gridx = 5; gbc_btnDireita.gridy = 3;
        panelTop.add(btnDireita, gbc_btnDireita);

        
        
        //BOTÃO --> TRÁS
        btnTras = new JButton("TRÁS");
        btnTras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!bd.isRobotAberto()) {
                    Consola("Necessita de ligar o robot primeiro!");
                    return;
                }

                int distancia = bd.getDistancia();
                if (distancia >= 10 && distancia <= 50) {
                	
                	buffer.inserirElemento(Comando.retaTras(distancia)); // <<<<< desta forma o sinal negativo fica na classe comando
        			buffer.inserirElemento(Comando.parar());
                	
                    //bd.getRobot().Reta(-distancia);  // <<<<< nota o sinal negativo
                    //bd.getRobot().Parar(false);
                    
                    
                    Consola("Fazer Marcha-atrás | Distância = " + distancia);
                } else {
                    Consola("Distância precisa de estar entre 10 e 50 cm");
                }
            }
        });
        btnTras.setBackground(Color.PINK);
        btnTras.setOpaque(true);
        btnTras.setContentAreaFilled(true);
        GridBagConstraints gbc_btnTras = new GridBagConstraints();
        gbc_btnTras.insets = new Insets(0,0,5,5);
        gbc_btnTras.gridx = 4; gbc_btnTras.gridy = 4;
        panelTop.add(btnTras, gbc_btnTras);
        
        
        
        // TO-DO --> NÚMERO
        JLabel lbl_Numero = new JLabel("Número");
        GridBagConstraints gbc_lbl_Numero = new GridBagConstraints();
        gbc_lbl_Numero.anchor = GridBagConstraints.EAST;
        gbc_lbl_Numero.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_Numero.gridx = 4;
        gbc_lbl_Numero.gridy = 5;
        panelTop.add(lbl_Numero, gbc_lbl_Numero);
        
        // cria o spinner já com modelo
        spinner = new JSpinner(new SpinnerNumberModel(3, 0, 100, 1));
        // mantém a BD sincronizada
        spinner.addChangeListener(e -> bd.setSpinnerNum((Integer) spinner.getValue()));
        // inicializa a BD com o valor atual
        bd.setSpinnerNum((Integer) spinner.getValue());

        GridBagConstraints gbc_spinner = new GridBagConstraints();
        gbc_spinner.anchor = GridBagConstraints.WEST;
        gbc_spinner.insets = new Insets(0, 0, 5, 5);
        gbc_spinner.gridx = 5;
        gbc_spinner.gridy = 5;
        panelTop.add(spinner, gbc_spinner);
        
        
        //TO-DO --> MOVIMENTOS ALEATÓRIOS
        rdbtnNewRadioButton = new JRadioButton("Movimentos Aleatórios");
        rdbtnNewRadioButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		// se não há robot, não liga
        	    if (!bd.isRobotAberto()) {
        	        rdbtnNewRadioButton.setSelected(false);
        	        Consola("Necessita de ligar o robot primeiro!");
        	        return;
        	    }
        	    if (produtor == null) {
        	        rdbtnNewRadioButton.setSelected(false);
        	        Consola("Produtor não disponível.");
        	        return;
        	    }

        	    if (rdbtnNewRadioButton.isSelected()) {
        	        // cria o timer só uma vez
        	        if (timerAleatorio == null) {
        	            timerAleatorio = new Timer(2000, ev -> {
        	                // segurança: se desligar robot, pára tudo
        	                if (!bd.isRobotAberto()) {
        	                    timerAleatorio.stop();
        	                    rdbtnNewRadioButton.setSelected(false);
        	                    Consola("Robot desligado. A parar movimentos aleatórios.");
        	                    return;
        	                }
        	                try { spinner.commitEdit(); } catch (java.text.ParseException ignored) {}
        	                int n = (Integer) spinner.getValue();  // lê sempre o valor atual
        	                produtor.iniciarComandos(n);
        	                Consola("Pedido de lote aleatório: " + n + " comandos (+ PARAR).");
        	            });
        	            timerAleatorio.setRepeats(true);
        	        }
        	        timerAleatorio.start();
        	        Consola("Movimentos aleatórios: ATIVADOS (a cada 2s).");
        	    } else {
        	    	buffer.clearBuffer();
        	    	buffer.inserirElemento(Comando.pararForce());
        	        if (timerAleatorio != null) timerAleatorio.stop();
        	        Consola("Movimentos aleatórios: DESATIVADOS.");
        	    }
        	}
        });
        GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
        gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 5);
        gbc_rdbtnNewRadioButton.gridx = 6;
        gbc_rdbtnNewRadioButton.gridy = 5;
        panelTop.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
        
        
        // CONSOLA --> HISTÓRICO DE AÇÕES
        JLabel lbl_Consola = new JLabel("Consola");
        GridBagConstraints gbc_lbl_Consola = new GridBagConstraints();
        gbc_lbl_Consola.insets = new Insets(0, 0, 5, 5);
        gbc_lbl_Consola.gridx = 2;
        gbc_lbl_Consola.gridy = 6;
        panelTop.add(lbl_Consola, gbc_lbl_Consola);
        
        console = new JTextArea();
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(console);

        GridBagConstraints gbc_console = new GridBagConstraints();
        gbc_console.gridx = 1;
        gbc_console.gridy = 7;
        gbc_console.gridwidth = 6;
        gbc_console.gridheight = 5;
        gbc_console.insets = new Insets(0, 0, 5, 0);
        gbc_console.fill = GridBagConstraints.BOTH;
        gbc_console.weightx = 1.0;   // << faz expandir na horizontal
        gbc_console.weighty = 1.0;   // << faz expandir na vertical
        panelTop.add(scroll, gbc_console);
        setPreferredSize(new Dimension(600, 400));  // tamanho desejado da janela
        pack();                                      // calcula layout com base nos preferred sizes
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void setTarefas(MovimentoAleatorio produtor, Servidor consumidor) {
        this.produtor = produtor;
        this.consumidor = consumidor;
    }

    // <<< O GETTER TEM QUE FICAR AQUI, FORA DO CONSTRUTOR >>>
    public BaseDados getBD() {
        return bd;
    }
}