package Affichage_test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Affichage2 {

    public static void main (String[] args){

        //Definir le frame et on place le commentaire de logiciel
        JFrame fenetre = new JFrame("Manipulation de structures secondaires");
        JLabel description = new JLabel("Ce programme vous propose de manipuler des séquences ADN et de les comparer entre elles au niveau de leurs structures secondaires", JLabel.CENTER);

        final JTextArea Arn1 = new JTextArea("Entrez ici le premier ARN choisi");
        final JTextArea Arn2 = new JTextArea("Entrez ici le second ARN choisi");
        Arn1.setBounds(10, 50, 180, 20);
        Arn2.setBounds(200, 240, 180, 20);

        //Definir le panel
        JPanel caseArn = new JPanel();
        caseArn.setBounds(40,50,150,150);
        caseArn.setBackground(Color.lightGray);

        //Ajouter un bouton "comparer"
        JButton comparer = new JButton("Comparer les séquences");
        comparer.setBackground(Color.GREEN);
        comparer.setBounds(50,100,80,30);

        //Ajouter les zones de text des deux ARN à comparer


        //Ajouter les commentaires de text à l'interieur des zones de textes

        //Ajouter le bouton et les commentaires au panel
        fenetre.setLayout(new GridLayout(10, 10));



        //Ajouter le panel au frame
        fenetre.add(caseArn);
        fenetre.add(comparer);
        fenetre.add(description);
        fenetre.add(coms);
        fenetre.pack();
        fenetre.setSize(600, 600);
        fenetre.setLayout(null);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setVisible(true);

    }
}
