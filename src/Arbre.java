public class Arbre {
    private int valeur;
    private Arbre gauche;
    private Arbre droit;

    //CONSTRUCTEURS
    public Arbre(int x) {
        this.valeur = x;
    }

    public Arbre(int x, Arbre g, Arbre d){
        this.valeur = x;
        this.gauche = g;
        this.droit = d;
    }

}
