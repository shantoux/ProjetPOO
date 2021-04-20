package Affichage_test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Affichage2 {

    public static void main (String[] args){

        //Definir le frame et on place le commentaire de logiciel
        JFrame fenetre = new JFrame("Manipulation de structures secondaires");
        JLabel description = new JLabel("Ce programme vous propose de manipuler des séquences ADN et de les comparer entre elles au niveau de leurs structures secondaires", JLabel.CENTER);

        final JTextField Arn1 = new JTextField("Entrez ici le premier ARN choisi");
        final JTextField Arn2 = new JTextField("Entrez ici le second ARN choisi");
        final JTextField resultat = new JTextField();
        Arn1.setBounds(20,40,800,880);
        Arn2.setBounds(500,40,600,880);

        //Definir le panel
        JPanel caseArn = new JPanel();
        caseArn.setBounds(150,50,600,800);
        caseArn.setBackground(Color.lightGray);

        //Ajouter un bouton "comparer"
        JButton comparer = new JButton("Comparer les séquences");
        comparer.setBackground(Color.GREEN);
        comparer.setBounds(320,850,300,30);

        //Ajouter les zones de text des deux ARN à comparer
        caseArn.add(Arn1);
        caseArn.add(Arn2);

        //Ajouter les commentaires de text à l'interieur des zones de textes

        comparer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                resultat.setText("Voilà ce que donne la comparaison !");
            }
        });

        //fenetre.setLayout(new GridLayout(10, 10)); ????

        //Ajouter les elements au frame
        fenetre.add(caseArn);
        fenetre.add(comparer);
        fenetre.add(description);
        fenetre.pack();
        fenetre.setSize(900, 950);
        fenetre.setLayout(null);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setVisible(true);

    }
}
