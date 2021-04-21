import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class Affichage3 extends JFrame {

    private JPanel mainPanel;
    private JRadioButton motifButton;
    private JRadioButton egalite2Button;
    private JRadioButton egaliteButton;
    private JTextArea resultatTextField;
    private JButton comparaisonButton;
    private JTextField fileArn1;
    private JTextField fileArn2;
    private JTextField arn1SS;
    private JTextField arn2SS;
    private JLabel labelArn1;
    private JLabel labelArn2;
    private JRadioButton pgacButton;

    public Affichage3() {
        super("Manipulation de structures secondaires");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(900, 650);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);
        this.pack();

        fileArn1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                fileArn1.setText("");
                arn1SS.setEditable(false);
                arn2SS.setEditable(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fileArn1.getText().length() == 0) {
                    fileArn1.setText("Entrer fichier");
                    arn1SS.setEditable(true);
                    arn2SS.setEditable(true);
                }
            }
        });

        fileArn2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                fileArn2.setText("");
                arn1SS.setEditable(false);
                arn2SS.setEditable(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (fileArn2.getText().length() == 0) {
                    fileArn2.setText("Entrer fichier");
                    arn1SS.setEditable(true);
                    arn2SS.setEditable(true);
                }
            }
        });

        arn1SS.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                arn1SS.setText("");
                fileArn1.setEditable(false);
                fileArn2.setEditable(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (arn1SS.getText().length() == 0) {
                    arn1SS.setText("Entrer structure secondaire");
                    fileArn1.setEditable(true);
                    fileArn2.setEditable(true);
                }
            }
        });

        arn2SS.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                arn2SS.setText("");
                fileArn1.setEditable(false);
                fileArn2.setEditable(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (arn2SS.getText().length() == 0) {
                    arn2SS.setText("Entrer structure secondaire");
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
                if (fileArn1.getText().contains("txt") && fileArn2.getText().contains("txt")){
                    try {
                        arn1 = ARN.stockholmARN(fileArn1.getText());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    try {
                        arn2 = ARN.stockholmARN(fileArn2.getText());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else {
                    arn1 = new ARN(arn1SS.getText());
                    arn2 = new ARN(arn2SS.getText());
                }
                if (motifButton.isSelected()){
                    resultatTextField.setText(Boolean.toString(arn1.rechercheDeMotifs(arn2, "structure")));
                }
                else if (egaliteButton.isSelected()){
                    resultatTextField.setText(Boolean.toString(arn1.equals(arn2, "structure")));
                }
                else if (egalite2Button.isSelected()){
                    resultatTextField.setText(Boolean.toString(arn1.equalsSansTirets(arn2, "structure")));
                }
                else if (pgacButton.isSelected()&&fileArn1.getText().length()!=0){
                    resultatTextField.setText(ARN.plusGrandARNCommun(arn1, arn2).toString());
                }
            }
        });
    }

    public static void main(String[] args) {

        JFrame affichage = new Affichage3();
        affichage.setVisible(true);


    }
}

