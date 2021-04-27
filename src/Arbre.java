import java.util.ArrayList;


/**
 * La classe Arbre représente la structure secondaire d'un ARN sous forme d'arbre général
 * L'objet "Arbre" représente un noeud interne (bases appariées) ou une feuille (bases non appariées)
 * L'Arraylist "enfants" représente les bases successives, jusqu'aux prochaines bases appariées
 * le String "base" contient le type de base "A,C,G,U", lorsqu'il s'agit d'un noeud repré
 * sentant des bases appariées, l'attribut "base" contient les deux bases (ex : GC)
 */
public class Arbre {
    private final ArrayList<Arbre> enfants = new ArrayList();
    private Arbre lienVersLePere = null;
    private String base;


    //CONSTRUCTEURS

    public Arbre() { //constructeur pour la création d'un Arbre vide = racine
    }

    public Arbre(String base, Arbre pere) { //constructeur pour les noeuds au sein de l'arbre
        this.base = base;
        this.lienVersLePere = pere;
    }

    //getter lienVersLePere
    public Arbre getLienVersLePere() {
        return lienVersLePere;
    }

    //Retourne la dernière feuille ajoutée à un ancêtre dans l'arbre
    public Arbre getDernierEnfantAjoute() {
        int indexLastEnfant = this.enfants.size() - 1;
        return this.enfants.get(indexLastEnfant);
    }

    //Méthodes

    //Ajoute un enfant à un arbre qui appelle la méthode
    public void addEnfant(String base) {
        Arbre enfant = new Arbre(base, this);
        this.enfants.add(enfant);
        enfant.base = base;
    }


    //Ajoute une paire appariée au noeud qui appelle la méthode(this.base est nécessairement non null)
    public void addPaire(String base) {
        this.base += base;
    }


    //Affichage sommaire de l'arbre pour vérification de la méthode parentheseVersArbre, par récursivité
    public void affichageArbre() {
        for (Arbre noeud : this.enfants) {
            if (noeud.enfants.size() == 0) {
                System.out.print(noeud.base);
            } else {
                System.out.println("["+noeud.base+"]");
                noeud.affichageArbre();
            }
        }
    }

    /**
     * Méthode récursive pour obtenir un ARN à partir d'un Arbre
     *
     * @return Retourne l'ARN correspond à l'Arbre appelant la méthode
     */
    public ARN arbreVersARN() {
        StringBuilder parenthese = new StringBuilder(); //Initialisation du StringBuilder parenthese
        StringBuilder sequence = new StringBuilder(); //Initialisation du StringBuilder sequence
        if (this.enfants.size() != 0) { //si l'arbre ne se limite pas qu'à un racine
            for (Arbre noeud : this.enfants) { //On parcourt les enfants
                if (noeud.enfants.size() == 0) { //Si on tombe sur une feuille = une base non appariée
                    parenthese.append("-"); //on ajoute un "tiret" au String parenthese
                    sequence.append(noeud.base); //Et on ajoute la base correspondant à ce noeud au String sequence
                } else { //Si on tombe sur un noeud interne = une base appariée
                    parenthese.append("("); //On ajoute une parenthèse ouvrante au String parenthese
                    sequence.append(noeud.base.charAt(0)); //On ajoute la base correspondante au String sequence
                    parenthese.append(noeud.arbreVersARN().getAppariements()); //On appelle la méthode pour le
                    // noeud traité, par récursivité, on récupère le String appariement de l'ARN généré
                    sequence.append(noeud.arbreVersARN().getSequence());//Idem pour la séquence
                    parenthese.append(")");//On effectue le post-traitement : ajout d'une parenthèse fermante
                    sequence.append(noeud.base.charAt(1)); // récupération de la base en deuxième position du
                    // String dans l'attribut "base"
                }
            }
        }
        return new ARN(sequence.toString(), parenthese.toString());//On retourne un nouvel ARN construit à
        // partir du String parenthese et du String sequence
    }


    /**
     * @param a2 Arbre passé en paramètre à comparer à l'instance
     * @return renvoie le plus grand Arbre commun entre a1 et a2 passés en paramètres
     */
    public Arbre plusGrandArbreCommun(Arbre a2) {
        ARN arn1 = this.arbreVersARN(); //Conversion en ARN
        ARN arn2 = a2.arbreVersARN(); // Conversion en ARN
        if (arn1.plusGrandMotifCommun(arn2, "sequence") != null) { //Seulement si la méthode plusGrandARNCommun renvoie bien
            // quelque chose
            return arn1.plusGrandMotifCommun(arn2, "sequence").arnVersArbre(); //On convertit en Arbre le résultat de
            // la méthode plusGrandARNCommun
        } else {
            return null; //sinon on retourne null
        }
    }

    //ESSAI INFRUCTEUX DE PARCOURIR LES ARBRES AFIN DE RETOURNER LE PLUS GRAND SOUS ARBRE COMMUN

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Arbre arbre = (Arbre) o;
//        return Objects.equals(enfants, arbre.enfants) && Objects.equals(base, arbre.base);
//    }


    //    public void addNoeud(Arbre noeud){
//        this.enfants.add(noeud);
//        noeud.lienVersLePere = this;
//    }
//
//    public Arbre getProchainNoeudInterne(){
//        for (int i = 0; i < this.enfants.size(); i++){
//            if (this.enfants.get(i).enfants != null){
//                return this.enfants.get(i);
//            }
//
//        }
//        return null;
//    }
//
//
//    public Arbre plusGrandArbreCommun(Arbre a1){
//        Arbre plusGrandArbreCommun = new Arbre();
//        int hauteurArbreMax = 0;
//        int hauteurArbre = 0;
//        if (this.enfants != null && a1.enfants != null){
//            for (Arbre noeud1 : this.enfants){
//                Arbre buffer = new Arbre();
//                hauteurArbre = 0;
//                for (Arbre noeud2 : a1.enfants){
//                    System.out.println(noeud1.equals(noeud2));
//                    while (noeud1.equals(noeud2)){
//                        buffer.addNoeud(noeud1);
//                        noeud1 = noeud1.getProchainNoeudInterne();
//                        noeud2 = noeud2.getProchainNoeudInterne();
//                        hauteurArbre+=1;
//                        System.out.println(hauteurArbre);
//                        buffer.affichageArbre();
//                    }
//                }
//                if (hauteurArbre > hauteurArbreMax){
//                    hauteurArbreMax = hauteurArbre;
//                    plusGrandArbreCommun = buffer;
//                }
//            }
//        }
//        return plusGrandArbreCommun;
//    }
}


