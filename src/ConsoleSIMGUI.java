import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ConsoleSIMGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JTextArea console;
    private final JButton btnSimularEvitar;

    // callback que a Application vai definir
    private Runnable simularEvitarCallback;

    public ConsoleSIMGUI() {
        setTitle("Consola Robot EV3 SIM");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        console = new JTextArea();
        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(console);
        add(scroll, BorderLayout.CENTER);

        // Painel de botões em baixo
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSimularEvitar = new JButton("Simular Evitar");
        panelBotoes.add(btnSimularEvitar);
        add(panelBotoes, BorderLayout.SOUTH);

        // Ação do botão
        btnSimularEvitar.addActionListener(e -> {
            if (simularEvitarCallback != null) {
                // corre noutra thread para não bloquear a GUI,
                // porque ExecutarEvitar tem Thread.sleep()
            	appendLine("Encontrou Obstaculo (simulado)");
                new Thread(simularEvitarCallback, "SimularEvitarThread").start();
            }
        });

        setSize(500, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Logger a passar ao RobotLegoEV3SIM */
    public Consumer<String> getLogger() {
        return msg -> SwingUtilities.invokeLater(() -> {
            console.append(msg + "\n");
            console.setCaretPosition(console.getDocument().getLength());
        });
    }

    /** Permite à Application dizer o que acontece quando se carrega em "Simular Evitar" */
    public void setSimularEvitarCallback(Runnable callback) {
        this.simularEvitarCallback = callback;
    }

    private void appendLine(String msg) {
        console.append(msg + "\n");
        console.setCaretPosition(console.getDocument().getLength());
    }
}
