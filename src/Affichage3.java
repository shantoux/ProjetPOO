import javax.swing.*;
import java.awt.event.*;

public class Affichage3 extends JFrame {

    private JPanel mainPanel;
    private JRadioButton motifButton;
    private JRadioButton egalite2Button;
    private JRadioButton egaliteButton;
    private JTextArea resultatTextArea;
    private JButton comparaisonButton;
    private JTextField fileArn1;
    private JTextField fileArn2;
    private JTextField arn1SS;
    private JTextField arn2SS;
    private JLabel labelArn1;
    private JLabel labelArn2;
    private JRadioButton pgacButton;
    private JRadioButton afficherButton;
    private JTextArea infoFichier;

    public Affichage3() {
        //Initialisation du main Panel
        super("Manipulation de structures secondaires");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(900, 650);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);
        this.pack();
        this.resultatTextArea.setLineWrap(true);//Retour à la ligne automatique pour l'encart d'affichage de résultats

        //Focus Listener pour chaque JTextField pour gérer le fait que l'on ne puisse pas entrer des fichiers en plus
        // d'input manuels de structure. Et également pour faire un effet esthétique qui efface "entrer fichier" ou
        // "entrer structure secondaire" lorsqu'on clique sur les champs en question

        fileArn1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fileArn1.getText().equals("Entrer fichier")){
                    fileArn1.setText("");
                    arn1SS.setEditable(false);
                    arn2SS.setEditable(false);
                    infoFichier.setText("Veuillez entrer le nom du fichier sans l'extension '.stockholm.txt'" + "\n" +
                            "Le fichier doit être présent dans le répertoire local");
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fileArn1.getText().length() == 0) {
                    fileArn1.setText("Entrer fichier");
                    arn1SS.setEditable(true);
                    arn2SS.setEditable(true);
                    infoFichier.setText("");

                }

            }
        });

        fileArn2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fileArn2.getText().equals("Entrer fichier")){
                    fileArn2.setText("");
                    arn1SS.setEditable(false);
                    arn2SS.setEditable(false);
                    infoFichier.setText("Veuillez entrer le nom du fichier sans l'extension '.stockholm.txt'" +
                            "\n" + "Le fichier doit être présent dans le répertoire local");

                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fileArn2.getText().length() == 0) {
                    fileArn2.setText("Entrer fichier");
                    arn1SS.setEditable(true);
                    arn2SS.setEditable(true);
                    infoFichier.setText("");
                }

            }
        });

        arn1SS.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (arn1SS.getText().equals("Entrer la structure secondaire")){
                    arn1SS.setText("");
                    fileArn1.setEditable(false);
                    fileArn2.setEditable(false);
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (arn1SS.getText().length() == 0) {
                    arn1SS.setText("Entrer la structure secondaire");
                    fileArn1.setEditable(true);
                    fileArn2.setEditable(true);
                }
            }
        });

        arn2SS.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (arn2SS.getText().equals("Entrer la structure secondaire")){
                    arn2SS.setText("");
                    fileArn1.setEditable(false);
                    fileArn2.setEditable(false);
                }

            }

            @Override
            public void focusLost(FocusEvent e) {
                if (arn2SS.getText().length() == 0) {
                    arn2SS.setText("Entrer la structure secondaire");
                    fileArn1.setEditable(true);
                    fileArn2.setEditable(true);
                }
            }
        });

        //ActionListener si l'utilisateur appuie sur le bouton "comparaison"
        comparaisonButton.addActionListener(e -> {
            ARN arn1;
            ARN arn2;

            //Récupération des ARNs à partir des inputs de l'utilisateur :
            //Si il a renseigné des fichiers :
            if (!fileArn1.getText().contains("Entrer fichier") && !fileArn2.getText().contains("Entrer fichier")){
                //Si l'utilisateur a rentré quelque chose dans es champs fileArn1 et fileArn2
                arn1 = ARN.stockholmToARN(fileArn1.getText()+".stockholm.txt");//Construction de l'ARN1 à partir du fichier
                arn2 = ARN.stockholmToARN(fileArn2.getText()+".stockholm.txt");// Construction de l'ARN2 à partir du fichier
                if (arn1 == null){
                    resultatTextArea.setText("ERREUR : le fichier ARN1 n'a pas été trouvé dans le répertoire local");
                }
                else if (arn2 == null){
                    resultatTextArea.setText("ERREUR : le fichier ARN2 n'a pas été trouvé dans le répertoire local");
                }
            }
            else { //Sinon, c'est qu'il a renseigné manuellement une structure secondaire
                arn1 = new ARN(arn1SS.getText());
                arn2 = new ARN(arn2SS.getText());

            }

            //Différentes actions en fonction du RJButtons selectionné
            if (arn1 != null && arn2 != null && arn1.validiteSSentree() && arn2.validiteSSentree()) {//test nullité
                //et validité de structure secondaire en cas d'input manuel par l'utilisateur

                if (motifButton.isSelected()){

                    if (arn1.getSequence() != null && arn2.getSequence() != null){
                        resultatTextArea.setText("Recherche de motif structurel : "
                                + arn1.rechercheDeMotifs(arn2, "structure") + "\n\n"
                                + "Recherche de motif identique : " + arn1.rechercheDeMotifs(arn2,"sequence"));
                    }
                    else {
                        resultatTextArea.setText(arn1.rechercheDeMotifs(arn2, "structure"));
                    }
                }
                else if (egaliteButton.isSelected()){
                    if (arn1.getSequence() != null && arn2.getSequence() != null) {
                        resultatTextArea.setText("égalité de structure : "
                                + arn1.equals(arn2, "structure") + "\n\n"
                                + "égalité stricte : " + arn1.equals(arn2, "sequence"));
                    } else {
                        resultatTextArea.setText(Boolean.toString(arn1.equals(arn2, "structure")));

                    }
                }
                else if (egalite2Button.isSelected()){
                    if (arn1.getSequence() != null && arn2.getSequence() != null) {
                        resultatTextArea.setText("égalité de structure : " +
                                arn1.equalsSansTirets(arn2, "structure") + "\n\n"
                                + "égalité stricte : "+ arn1.equalsSansTirets(arn2, "sequence"));
                    } else {
                        resultatTextArea.setText(Boolean.toString(arn1.equalsSansTirets(arn2, "structure")));

                    }
                }
                else if (pgacButton.isSelected()&&fileArn1.getText().length()!=0){
                    if (arn1.getSequence() != null && arn2.getSequence() != null) {
                        resultatTextArea.setText("Plus grand motif structurel : " +
                                arn1.plusGrandMotifCommun(arn2, "structure").toString() + "\n\n"
                                + "plus grand motif strict : "+ arn1.plusGrandMotifCommun(arn2, "sequence"));
                    } else {
                        resultatTextArea.setText(arn1.plusGrandMotifCommun(arn2, "structure").toString());

                    }

                }
                else if (afficherButton.isSelected()){
                    String affichageDesARNs = "ARN1 :" + "\n" + arn1.toString() + "\n\n" +
                            "ARN2 :" + "\n" + arn2.toString() + "\n";
                    resultatTextArea.setText(affichageDesARNs);
                }
                else{
                    resultatTextArea.setText("ERREUR : Veuillez sélectionner une méthode de comparaison :)");
                }
            } else {
                if (arn1==null || arn2==null){
                    resultatTextArea.setText("ERREUR : Veuilez entrer deux ARNs à comparer :)");
                }
                if (!arn1.validiteSSentree()){//si le nombre de parenthèses ouvrantes et fermantes n'est pas le même
                    if (resultatTextArea.getText().contains("ERREUR")){
                        resultatTextArea.append("\nERREUR : La structure secondaire entrée pour l'ARN1 est incorrecte");
                    }
                    else{
                        resultatTextArea.setText("ERREUR : La structure secondaire entrée pour l'ARN1 est incorrecte");
                    }

                }
                if(!arn2.validiteSSentree()){
                    if (resultatTextArea.getText().contains("ERREUR")){
                        resultatTextArea.append("\nERREUR : La structure secondaire entrée pour l'ARN2 est incorrecte");
                    }
                    else{
                        resultatTextArea.setText("ERREUR : La structure secondaire entrée pour l'ARN2 est incorrecte");
                    }
                }

            }
        });
    }



    public static void main(String[] args) {

        JFrame affichage = new Affichage3();
        affichage.setVisible(true);


    }
}

