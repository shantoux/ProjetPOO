import java.util.ArrayList;


/**
 * La classe Arbre représente la structure secondaire d'un ARN sous forme d'un arbre général
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
    public Arbre(String base, Arbre pere){
        this.base = base;
        this.lienVersLePere = pere;
    }
    public Arbre(){
        this.base = "racine";
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

    //Retourne la dernière feuille ajoutée à un ancêtre dans l'arbre
    public Arbre dernierEnfantAjoute(){
        int indexLastEnfant = this.enfants.size() - 1;
        return this.enfants.get(indexLastEnfant);
    }

    //Retourne la base appariée précédente
    public Arbre retournerAlAscendant(){
        return this.lienVersLePere;
    }

    /**
     *Méthode qui permet d'obtenir un Arbre représentant la structure secondaire d'un ARN
     * @param appariements structure secondaire en format parenthèses-tirets
     * @param sequence de l'ARN que l'on veut représenter en Arbre
     * @return un Arbre raciné dont les noeuds internes sont les bases appariées et les feuilles des bases non appariées
     */
    public static Arbre parentheseVersArbre(String appariements, String sequence){
        Arbre racine = new Arbre();
        Arbre pere = racine; //buffer
        for (int i=0; i<appariements.length();i++){
            if (appariements.charAt(i) == '-'){ //tiret = base non appariée
                pere.addEnfant(Character.toString(sequence.charAt(i))); //ajout d'un enfant au buffer
            }
            else if (appariements.charAt(i) == '('){ //( = première base d'un couple de bases appariées
                pere.addEnfant(Character.toString(sequence.charAt(i))); //ajout d'un enfant au buffer
                pere = pere.dernierEnfantAjoute();//l'enfant ajouté devient le nouveau buffer
            }
            else if (appariements.charAt(i) == ')'){ //) = deuxième base d'un couple de bases appariées
                pere.addPaire(Character.toString(sequence.charAt(i)));//ajout d'une base au buffer
                                                                      // qui contient désormais une paire de bases
                pere = pere.retournerAlAscendant();//l'ancêtre du buffer devient le nouveau buffer
            }
        }
        return racine;//retour de l'arbre créé
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

//    public String arbreVersParenthese(Arbre racine) {
//        String parenthese = null;
//        Arbre buffer = racine;
//        if (racine != null) {
//            int i = 0;
//            buffer = buffer.enfants.get(i);
//            do{
//
//            }
//        }
//        return parenthese;
//    }

}
