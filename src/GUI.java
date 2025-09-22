import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private BaseDados bd;

    public GUI() {
        setTitle("EV3 Demo");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 450, 220);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(8, 8, 8, 8));
        setContentPane(contentPane);

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

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
