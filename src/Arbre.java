import java.util.ArrayList;
import org.javatuples.Pair;

public class Arbre {
    private ArrayList<Arbre> enfants = new ArrayList();
    private Arbre lienVersLePere = null;
    private String base;


    //CONSTRUCTEURS
    public Arbre(String base, Arbre pere){
        this.base = base;
        this.lienVersLePere = pere;
    }
    public Arbre(){
    }

    public Arbre(Arbre pere){
        this.lienVersLePere = pere;
    }

    //Méthodes

    public String toString() {
        return this.base.toString();
    }

    //Ajoute un enfant à un arbre
    public void addEnfant(String base){
        Arbre enfant = new Arbre(this);
        this.enfants.add(enfant);
        enfant.base = base;
    }

    //Ajoute une paire appariée au noeud
    public void addPaire(String base){
        if (this.base != null){
            this.base += base;
        }
        else {
            this.base = base;
        }
    }

    public Arbre passerAuNoeudSuivant(){
        return this.enfants.get(this.enfants.size()-1);
    }

    public Arbre retournerAuNoeudPrecedent(){
        return this.lienVersLePere;
    }

    public static Arbre parentheseVersArbre(String appariements, String sequence){
        Arbre racine = new Arbre();
        Arbre pere = racine;
        for (int i=0; i<appariements.length();i++){
            if (appariements.charAt(i) == '-'){
                pere.addEnfant(Character.toString(sequence.charAt(i)));
            }
            else if (appariements.charAt(i) == '('){
                pere.addEnfant(Character.toString(sequence.charAt(i)));
                pere = pere.passerAuNoeudSuivant();
                pere.addPaire(Character.toString(sequence.charAt(i)));
            }
            else if (appariements.charAt(i) == ')'){
                pere = pere.retournerAuNoeudPrecedent();
                pere.addPaire(Character.toString(sequence.charAt(i)));
            }
        }
        return racine;
    }

    public String arbreVersParenthese(Arbre racine) {
        String parenthese = null;
        Arbre buffer = racine;
        if (racine != null) {
            int i = 0;
            buffer = buffer.enfants.get(i);
            do{

            }
        }
        return parenthese;
    }










}
