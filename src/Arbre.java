import java.util.ArrayList;
import java.util.Objects;


/**
 * La classe Arbre représente la structure secondaire d'un ARN sous forme d'arbre général
 * L'objet "Arbre" représente un noeud interne (bases appariées) ou une feuille (bases non appariées)
 * L'Arraylist "enfants" représente les bases successives, jusqu'aux prochaines bases appariées
 * le String "base" contient le type de base "A,C,G,U", lorsqu'il s'agit d'un noeud repré
 * sentant des bases appariées, l'attribut "base" contient les deux bases (ex : GC)
 */
public class Arbre {
    private ArrayList<Arbre> enfants = new ArrayList();
    private Arbre lienVersLePere = null;
    private String base;


    //CONSTRUCTEURS

    public Arbre() { //constructeur pour la création d'un Arbre vide
    }

    public Arbre(String base, Arbre pere) { //constructeur pour les noeuds au sein de l'arbre
        this.base = base;
        this.lienVersLePere = pere;
    }

    public ArrayList<Arbre> getEnfants() {
        return enfants;
    }

    public Arbre getLienVersLePere() {
        return lienVersLePere;
    }

    //Retourne la dernière feuille ajoutée à un ancêtre dans l'arbre
    public Arbre getDernierEnfantAjoute() {
        int indexLastEnfant = this.enfants.size() - 1;
        return this.enfants.get(indexLastEnfant);
    }

    //Méthodes

    public String toString() {
        return this.base.toString();
    }

    //Ajoute un enfant à un arbre
    public void addEnfant(String base) {
        Arbre enfant = new Arbre(base, this);
        this.enfants.add(enfant);
        enfant.base = base;
    }



    //Ajoute une paire appariée au noeud (this.base est nécessairement non null)
    public void addPaire(String base) {
        this.base += base;
    }


    //Retourne la base appariée précédente
    public Arbre retournerAlAscendant() {
        return this.lienVersLePere;
    }

    /**
     * Méthode qui permet d'obtenir un Arbre représentant la structure secondaire d'un ARN
     *
     * @param arn l'ARN dont on veut l'arbre
     * @return un Arbre raciné dont les noeuds internes sont les bases appariées et les feuilles des bases non appariées
     */
    public static Arbre arnVersArbre(ARN arn) {
        String appariements = arn.appariements;
        String sequence = arn.sequence;
        Arbre racine = new Arbre();
        Arbre pere = racine; //buffer
        for (int i = 0; i < appariements.length(); i++) {
            if (appariements.charAt(i) == '-') { //tiret = base non appariée
                pere.addEnfant(Character.toString(sequence.charAt(i))); //ajout d'un enfant au buffer
            } else if (appariements.charAt(i) == '(') { //( = première base d'un couple de bases appariées
                pere.addEnfant(Character.toString(sequence.charAt(i))); //ajout d'un enfant au buffer
                pere = pere.getDernierEnfantAjoute();//l'enfant ajouté devient le nouveau buffer
            } else if (appariements.charAt(i) == ')') { //) = deuxième base d'un couple de bases appariées
                pere.addPaire(Character.toString(sequence.charAt(i)));//ajout d'une base au buffer
                // qui contient désormais une paire de bases
                pere = pere.getLienVersLePere();//l'ancêtre du buffer devient le nouveau buffer
            }
        }
        return racine;//retour de l'arbre créé
    }


    //Affichage sommaire de l'arbre pour vérification de la méthode parentheseVersArbre
    public void affichageArbre() {
        for (Arbre noeud : this.enfants) {
            if (noeud.enfants == null) {
                System.out.println(noeud.base);
            } else {
                System.out.println(noeud.base);
                noeud.affichageArbre();
            }
        }
    }

    public ARN arbreVersARN() {
        String parenthese = new String("");
        String sequence = new String("");
        if (this.enfants != null) {
            for (Arbre noeud : this.enfants) {
                if (noeud.enfants.size() == 0) {
                    parenthese += "-";
                    sequence += noeud.base;
                } else {
                    parenthese += "(";
                    sequence += noeud.base.charAt(0);
                    parenthese += noeud.arbreVersARN().appariements;
                    sequence += noeud.arbreVersARN().sequence;
                    parenthese += ")";
                    sequence += noeud.base.charAt(1);
                }
            }
        }
        return new ARN(sequence, parenthese);
    }


    public static Arbre plusGrandArbreCommun(Arbre a1, Arbre a2) {
        ARN arn1 = a1.arbreVersARN();
        ARN arn2 = a2.arbreVersARN();
        System.out.println(ARN.plusGrandARNCommun(arn1, arn2));
        return arnVersArbre(ARN.plusGrandARNCommun(arn1, arn2));
    }

    public void addNoeud(Arbre noeud){
        this.enfants.add(noeud);
        noeud.lienVersLePere = this;
    }

    public Arbre getProchainNoeudInterne(){
        for (int i = 0; i < this.enfants.size(); i++){
            if (this.enfants.get(i).enfants != null){
                return this.enfants.get(i);
            }

        }
        return null;
    }


    public Arbre plusGrandArbreCommun(Arbre a1){
        Arbre plusGrandArbreCommun = new Arbre();
        int hauteurArbreMax = 0;
        int hauteurArbre = 0;
        if (this.enfants != null && a1.enfants != null){
            for (Arbre noeud1 : this.enfants){
                Arbre buffer = new Arbre();
                hauteurArbre = 0;
                for (Arbre noeud2 : a1.enfants){
                    System.out.println(noeud1.equals(noeud2));
                    while (noeud1.equals(noeud2)){
                        buffer.addNoeud(noeud1);
                        noeud1 = noeud1.getProchainNoeudInterne();
                        noeud2 = noeud2.getProchainNoeudInterne();
                        hauteurArbre+=1;
                        System.out.println(hauteurArbre);
                        buffer.affichageArbre();
                    }
                }
                if (hauteurArbre > hauteurArbreMax){
                    hauteurArbreMax = hauteurArbre;
                    plusGrandArbreCommun = buffer;
                }
            }
        }
        return plusGrandArbreCommun;
    }
}


