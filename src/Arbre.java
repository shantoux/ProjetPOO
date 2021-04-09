import java.util.ArrayList;
import java.util.List;

public class Arbre {
    private ArrayList<Arbre> enfants = new ArrayList();
    private Arbre lienVersLePere = null;
    private char base;
    private Arbre lienVersLaPaire;


    //CONSTRUCTEURS
    public Arbre(char base, Arbre pere){
        this.base = base;
        this.lienVersLePere = pere;
    }
    public Arbre(){
    }

    public Arbre(Arbre pere){
        this.lienVersLePere = pere;
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
