import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;

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
        super("Manipulation de structures secondaires");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(900, 650);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);
        this.pack();
        this.resultatTextArea.setLineWrap(true);

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
                else {
                    File f = new File(fileArn1.getText()+".stockholm.txt");
                    if(!f.exists() || f.isDirectory()) {
                        resultatTextArea.setText("/!\\le fichier ARN1 n'a pas été trouvé dans le répertoire local." +
                                " Veuillez entrer un fichier présent dans le répertoire local");
                    }
                    else {
                        resultatTextArea.setText("Résultat de la comparaison...");
                    }
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
                else {
                    File f = new File(fileArn2.getText()+".stockholm.txt");
                    if(!f.exists() || f.isDirectory()) {
                        resultatTextArea.setText("/!\\le fichier ARN2 n'a pas été trouvé dans le répertoire local." +
                                " Veuillez entrer un fichier présent dans le répertoire local");
                    }
                    else{
                        resultatTextArea.setText("Résultat de la comparaison...");
                    }
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


        comparaisonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ARN arn1 = new ARN();
                ARN arn2 = new ARN();
                if (!fileArn1.getText().contains("Entrer fichier") && !fileArn2.getText().contains("Entrer fichier")){
                    try {
                        arn1 = ARN.stockholmToARN(fileArn1.getText()+".stockholm.txt");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        arn2 = ARN.stockholmToARN(fileArn2.getText()+".stockholm.txt");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else {
                    arn1 = new ARN(arn1SS.getText());
                    arn2 = new ARN(arn2SS.getText());
                }

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
                                Boolean.toString(arn1.equalsSansTirets(arn2, "structure")) + "\n\n"
                                + "égalité stricte : "+Boolean.toString(arn1.equalsSansTirets(arn2, "sequence")));
                    } else {
                        resultatTextArea.setText(Boolean.toString(arn1.equalsSansTirets(arn2, "structure")));

                    }
                }
                else if (pgacButton.isSelected()&&fileArn1.getText().length()!=0){
                    try {
                        resultatTextArea.setText(arn1.plusGrandARNCommun(arn2).toString());
                    } catch (Exception exception) {
                        resultatTextArea.setText("pas de motif commun trouvé");
                    }
                }
                else if (afficherButton.isSelected()){
                    String affichageDesARNs = "ARN1 :" + "\n" + arn1.toString() + "\n\n" +
                            "ARN2 :" + "\n" + arn2.toString() + "\n";
                    resultatTextArea.setText(affichageDesARNs);
                }
                else{
                    resultatTextArea.setText("Veuillez sélectionner une méthode de comparaison :)");
                }
            }
        });
    }

    public static void main(String[] args) {

        JFrame affichage = new Affichage3();
        affichage.setVisible(true);


    }
}

