import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;

public class GUIGravador extends JFrame {

    private static final long serialVersionUID = 1L;

    // Componentes da GUI
    private JTextField txt_raio, txt_angulo, txt_dist, txt_robot, txt_ficheiro;
    private JCheckBox checkbox_ligar, checkbox_imprimir;
    private JButton button_frente, button_tras, button_esquerda, button_direita, button_parar, button_limpar;
    private JToggleButton toggle_gravar, toggle_reproduzir;
    private JTextArea console;
    
    private BaseDadosGravador bdG;
    private BufferCircular bufferGravador;

    public GUIGravador(BaseDadosGravador bdG, BufferCircular bufferGravador) {
    	this.bdG = bdG; 
    	this.bufferGravador = bufferGravador;
    	
        setTitle("GUI Gravador");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        // ----- labels -----
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
        txt_raio.setBounds(100, 32, 37, 26);
        getContentPane().add(txt_raio);

        txt_angulo = new JTextField();
        txt_angulo.setBounds(203, 32, 37, 26);
        getContentPane().add(txt_angulo);

        txt_dist = new JTextField();
        txt_dist.setBounds(325, 32, 37, 26);
        getContentPane().add(txt_dist);

        JLabel lbl_robot = new JLabel("Robot");
        lbl_robot.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lbl_robot.setBounds(446, 30, 47, 27);
        getContentPane().add(lbl_robot);

        txt_robot = new JTextField();
        txt_robot.setBounds(501, 32, 90, 26);
        getContentPane().add(txt_robot);

        // ----- botões dos comandos -----
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

        checkbox_ligar = new JCheckBox("Ligar");
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

        toggle_gravar = new JToggleButton("Gravar");
        toggle_gravar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                boolean selected = toggle_gravar.isSelected();

                bdG.setIsRecording(selected);

                if (selected) {
                    toggle_gravar.setText("● Gravar");
                    toggle_gravar.setForeground(Color.RED);
                } else {
                    toggle_gravar.setText("Gravar");
                    toggle_gravar.setForeground(Color.BLACK);
                    
                    gravarBufferParaFicheiro();
                }
            }
        });
        toggle_gravar.setFont(new Font("Tahoma", Font.PLAIN, 14));
        toggle_gravar.setBounds(192, 249, 121, 23);
        getContentPane().add(toggle_gravar);

        toggle_reproduzir = new JToggleButton("Reproduzir");
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
    
    private void gravarBufferParaFicheiro() {
        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String nomeFicheiro = fmt.format(agora) + ".txt";

        try (PrintWriter out = new PrintWriter(new FileWriter(nomeFicheiro))) {

            int n = bufferGravador.ocupados();   // snapshot na altura do clique

            for (int i = 0; i < n; i++) {
                Comando c = bufferGravador.removerElemento();
                String linha = comandoParaLinha(c);
                out.println(linha);
            }

            appendToConsole("Gravação guardada em: " + nomeFicheiro);

        } catch (IOException ex) {
            appendToConsole("Erro a escrever ficheiro: " + ex.getMessage());
        } finally {
            // garante que tudo fica mesmo limpo e semáforos resetados
            bufferGravador.clearBuffer();
        }
    }

    
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
    
    public void appendToConsole(String s) {
        console.append(s + "\n");
        console.setCaretPosition(console.getDocument().getLength());
    }
    



    
}
