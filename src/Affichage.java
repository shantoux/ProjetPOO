import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Affichage extends JFrame {

    public Affichage() {
        super("Manipulation de structures secondaires");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(900, 650);
        this.setLocationRelativeTo(null);
        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new FlowLayout());
        contentPane.add(new JButton("Comparer les séquences"));
        contentPane.add(new JButton("Youhou"));


    }

    public static void main(String[] args) {

        Affichage affichage = new Affichage();
        affichage.setVisible(true);

    }

}
