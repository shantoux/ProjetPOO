import java.util.ArrayList;

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
        this.base = "racine";
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
        Arbre enfant = new Arbre(base,this);
        this.enfants.add(enfant);
        enfant.base = base;
    }

    //Ajoute une paire appariée au noeud (this.base est nécessairement non null)
    public void addPaire(String base){
        this.base += base;
    }

    public Arbre dernierEnfantAjoute(){
        int indexLastEnfant = this.enfants.size() - 1;
        return this.enfants.get(indexLastEnfant);
    }

    public Arbre passerAuNoeudSuivant(){
        return this.enfants.get(this.enfants.size()-1);
    }

    public Arbre retournerAlAscendant(){
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
                pere = pere.dernierEnfantAjoute();
            }
            else if (appariements.charAt(i) == ')'){
                pere.addPaire(Character.toString(sequence.charAt(i)));
                pere = pere.retournerAlAscendant();

            }
        }
        return racine;
    }
    //Affichage sommaire de l'arbre pour vérification de la méthode parentheseVersArbre
    public void affichageArbre(){
        for (Arbre noeud : this.enfants){
            if (noeud.enfants == null){
                System.out.println(noeud.base);
            }
            else {
                System.out.println(noeud.base);
                noeud.affichageArbre();
            }
        }
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
