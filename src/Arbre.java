import java.util.ArrayList;
import java.util.List;

public class Arbre {
    private ArrayList<Arbre> enfants = new ArrayList();
    private Arbre lienVersLePere;


    //CONSTRUCTEURS
    public Arbre(Arbre pere) {
        this.lienVersLePere = pere;
    }

    public Arbre(){
        this.lienVersLePere = null;
    }

}
