import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Affichage extends JFrame implements ActionListener {
    private JButton traitement;
    private JLabel arn1;
    private JTextField fichierArn1;
    private JTextField fichierArn2;
    private JLabel arn2;
    private JCheckBox equals;
    private JCheckBox motifs;

    public Affichage(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Structures Secondaires");
        setSize(600,600);

        Container conteneur = getContentPane();

        arn1 = new JLabel("Entrer ARN1 ici");
        arn2 = new JLabel("Entrer ARN2 ici");
        fichierArn1 = new JTextField(20);
        fichierArn2 = new JTextField(20);

        conteneur.add(arn1, BorderLayout.NORTH);
        conteneur.add(fichierArn1, BorderLayout.WEST);

//        conteneur.add(arn2, BorderLayout.NORTH);
//        conteneur.add(fichierArn2, BorderLayout.EAST);

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Affichage().setVisible(true);
            }
        });
    }
}
