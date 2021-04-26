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

    public Arbre() { //constructeur pour la création d'un Arbre vide
    }

    public Arbre(String base, Arbre pere) { //constructeur pour les noeuds au sein de l'arbre
        this.base = base;
        this.lienVersLePere = pere;
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
        StringBuilder parenthese = new StringBuilder();
        StringBuilder sequence = new StringBuilder();
        if (this.enfants.size() != 0) {
            for (Arbre noeud : this.enfants) {
                if (noeud.enfants.size() == 0) {
                    parenthese.append("-");
                    sequence.append(noeud.base);
                } else {
                    parenthese.append("(");
                    sequence.append(noeud.base.charAt(0));
                    parenthese.append(noeud.arbreVersARN().getAppariements());
                    sequence.append(noeud.arbreVersARN().getSequence());
                    parenthese.append(")");
                    sequence.append(noeud.base.charAt(1));
                }
            }
        }
        return new ARN(sequence.toString(), parenthese.toString());
    }


    /**
      @param a2 deuxième Arbre passé en paramètre
     * @return renvoie le plus grand Arbre commun entre a1 et a2 passés en paramètres
     */
    public Arbre plusGrandArbreCommun(Arbre a2) {
        ARN arn1 = this.arbreVersARN();
        ARN arn2 = a2.arbreVersARN();
        if (arn1.plusGrandARNCommun(arn2) != null){
            return arn1.plusGrandARNCommun(arn2).arnVersArbre();
        }
        else {
            return null;
        }
    }

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


