import java.util.ArrayList;
import java.util.List;

public class Arbre {
    private ArrayList<Arbre> enfants = new ArrayList();
    private Arbre lienVersLePere;
    private char base;
    private Arbre lienVersLaPaire;


    //CONSTRUCTEURS
    public Arbre(Arbre pere){
        this.lienVersLePere = pere;
    }
    public Arbre(){
        this.lienVersLePere = null;
    }

    public void addEnfant(char base) {
        Arbre enfant = new Arbre(this);
        this.enfants.add(enfant);
        enfant.base = base;
        enfant.lienVersLaPaire = null;
    }

    public void addNode(){
        Arbre node = new Arbre(this);
        this.enfants.add(node);
    }


}
