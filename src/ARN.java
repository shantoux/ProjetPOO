import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ARN {
    private final String sequence;
    private final String appariements;

    //CONSTRUCTEURS

    public ARN(String sequence, String appariements){
        this.sequence = sequence;
        this.appariements = appariements;
    }

    @Override
    public String toString(){
        return(this.sequence + "\n" + this.appariements);
    }

    /**
     * Parser de fichiers txt stockholm afin de récupérer la séquence consensus ainsi que l'appariements au format
     * parenthèses-tirets et non WUSS
     * @param file fichier txt de l'ARN dont on veut la séquence et la structure
     * @return un ARN(sequence, appariements)
     */
    public static ARN stockholmARN(String file) throws IOException {
        FileReader stockholm = new FileReader(file);
        BufferedReader br = new BufferedReader(stockholm);
        String appariements=null;
        String sequence = null;
        String line;
        while((line =  br.readLine()) != null){
            if (line.contains("#=GC SS_cons"))
            {
                appariements = line.substring(line.indexOf("SS_cons") + 7);
                appariements = appariements.trim();
                appariements = appariements.replaceAll("\\.","");
                appariements = appariements.replaceAll("<", "(");
                appariements = appariements.replaceAll(">",")");
                appariements = appariements.replaceAll("_","-");
                appariements = appariements.replaceAll(",","-");
                appariements = appariements.replaceAll(":","-");
            }
            if (line.contains("#=GC RF"))
            {
                sequence = line.substring(line.indexOf("RF") + 2);
                sequence = sequence.trim();
                sequence = sequence.replaceAll("\\.", "");
            }
        }
        return new ARN(sequence, appariements);
    }

    //définition d'une énumération pour le type de méthode à effectuer pour certaines méthodes
    public enum Methode {structure, sequence};

    /**
     * Méthode qui permet de vérifier l'égalité stricte de deux ARN, soit en comparer leurs structures seules ou leurs
     * structures et séquences
     * @param arn : l'arn avec lequel on veut comparer l'arn qui appelle la méthode
     * @param methode : methode.structure si on veut comparer les structures et methode.sequence lorsqu'on veut comparer
     *                structure et séquences
     * @return
     */
    public boolean equals(ARN arn, Methode methode){
        boolean res = false;
        if (methode == methode.structure){
            res = this.appariements.equals(arn.appariements);
        }
        else if (methode == methode.sequence){
            res = (this.sequence.equals(arn.sequence)&&this.appariements.equals(arn.appariements));
        }
        return res;
    }


    /**
     * teste l'égalité entre deux ARNs, sans tenir compte des bases non appariées de début de chaînes
     * @param arn que l'on veut comparer
     * @param methode : methode.structure si on veut comparer les structures et methode.sequence lorsqu'on veut comparer
     *      *                structure et séquences
     * @return
     */
    public boolean equalsSansTirets(ARN arn, Methode methode) {
        int i = 0;
        int j = 0;
        while (this.appariements.charAt(i) == '-') i++;
        while (arn.appariements.charAt(j) == '-') j++;
        if (methode == methode.structure){
            while (i<this.appariements.length() && j<arn.appariements.length()){
                if (this.appariements.charAt(i) == arn.appariements.charAt(j)){
                    i++;
                    j++;
                }
                else {
                    return false;
                }
            }
        }
        else if (methode == methode.sequence){
            while (i<this.appariements.length() && j<arn.appariements.length()){
                if (this.sequence.charAt(i) == arn.sequence.charAt(j)){
                    i++;
                    j++;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Méthode pour rechercher si l'arn qui appelle la méthode contient le motif (ARN) passé en paramètre.
     * @param arn : motif recherché
     * @param methode : "structure" si on recherche un motif uniquement structurel "sequence" si on recherche structure
     *                ET séquence
     * @return true si le motif est trouvé
     */
    //
    public boolean rechercheDeMotifs(ARN arn, Methode methode){
        if (arn.sequence.length() > this.sequence.length()){
            System.out.print("Recherche de motif impossible car le motif est plus grand que l'ARN testé : ");
            return false;
        }
        boolean res = false;
        int cptBasesEgales = 1;
        if (methode == methode.structure){
            for (int j =0; j<this.appariements.length();j++){
                if (j + arn.sequence.length() <= this.appariements.length()){
                    int i = 0;
                    while (arn.appariements.charAt(i) == (this.appariements.charAt(i+j))
                            && cptBasesEgales < arn.appariements.length()){
                        cptBasesEgales += 1;
                        i += 1;
                    }
                    if (cptBasesEgales == arn.appariements.length()){
                        res = true;
                        return res;
                    }
                }
            }
        }
        else if (methode == methode.sequence){
            for (int j =0; j<this.appariements.length();j++) {
                if (j + arn.sequence.length() <= this.appariements.length()){
                    int i = 0;
                    while (arn.appariements.charAt(i) == (this.appariements.charAt(i+j))
                            && cptBasesEgales < arn.appariements.length()
                            && arn.sequence.charAt(i) == (this.sequence.charAt(i+j))){
                        cptBasesEgales += 1;
                        i += 1;
                    }
                    if (cptBasesEgales == arn.appariements.length()){
                        res = true;
                        return res;
                    }
                }
            }
        }
        return res;
    }






    public static void main(String[] args) throws IOException {
        ARN l = new ARN("AAAAAUGCAGUTG", "----((-))----");
        ARN l2 = new ARN("UUUAUGCAUUTG", "---((-))----");
        ARN l3 = new ARN("UUAUGCAGUTGa", "--(((---)))-");
        ARN l4 = new ARN("AAAAAUGCAGUTG", "----((-))----");

//        System.out.println(l.equalsSansTirets(l2, "forme"));
//        System.out.println(l.equalsSansTirets(l2, "sequence"));
//        System.out.println(l.equalsSansTirets(l3, "forme"));
//        System.out.println(l.equalsSansTirets(l3, "sequence"));
//        System.out.println(l.equals(l4, "forme"));
//        System.out.println(l.equals(l4,"sequence"));
        ARN arn1 = stockholmARN("RF00005.stockholm.txt");
        System.out.println(arn1.appariements);

        Arbre a1 = Arbre.parentheseVersArbre(arn1.appariements, arn1.sequence);

        a1.affichageArbre();

        ARN motif = new ARN("cgcuaucucCa","))))))))))-");
        System.out.println(motif.rechercheDeMotifs(arn1, Methode.structure));
        System.out.println(arn1.rechercheDeMotifs(motif, Methode.sequence));
        Arbre a2 = Arbre.parentheseVersArbre(l3.appariements, l3.sequence);
        a2.affichageArbre();
        System.out.println(l3.appariements);
        System.out.println(a2.arbreVersParenthese());
        System.out.println(arn1.appariements);
        System.out.println(a1.arbreVersParenthese());
        System.out.println(a2.equals(a2));

    }

//essai


}
