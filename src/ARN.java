import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ARN {
    protected String sequence;
    protected String appariements;

    //CONSTRUCTEURS

    public ARN(String sequence, String appariements){
        this.sequence = sequence;
        this.appariements = appariements;
    }
    public ARN(){
        this.sequence = "";
        this.appariements = "";
    }
    public ARN(String appariements){
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
    public static boolean equals(ARN arn1, ARN arn, String methode){
        boolean res = false;
        if (methode.equals("structure")){
            res = arn1.appariements.equals(arn.appariements);
        }
        else if (methode.equals("sequence")){
            res = (arn1.sequence.equals(arn.sequence)&&arn1.appariements.equals(arn.appariements));
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

    public static ARN plusGrandARNCommun(ARN arn1, ARN arn2) {
        ARN plusGrandARNCommun = new ARN();
        int tailleMaxCommun = 0;
        int tailleBuffer = 0;
        if (arn1.sequence.length() != 0 && arn2.sequence.length() != 0) {
            for (int i = 0; i < arn1.sequence.length(); i++) {
                ARN bufferArn = new ARN();
                tailleBuffer = 0;
                for (int j = 0; j < arn2.sequence.length(); j++) {
                    while (i<arn1.sequence.length() && j<arn2.sequence.length()
                            && arn1.appariements.charAt(i) == arn2.appariements.charAt(j)
                            && arn1.sequence.charAt(i) == arn2.sequence.charAt(j)){

                        bufferArn.appariements += arn1.appariements.charAt(i);
                        bufferArn.sequence += arn1.sequence.charAt(i);
                        tailleBuffer += 1;
                        i += 1;
                        j += 1;
                    }
                    if (tailleBuffer > tailleMaxCommun) {
                        tailleMaxCommun = tailleBuffer;
                        plusGrandARNCommun = bufferArn;
                    }
                }
            }

        }
        if (plusGrandARNCommun.sequence.length() != 0){
            return plusGrandARNCommun;
        }
        else {
            System.out.println("Pas de sous ARN commun trouvé => Pas de sous arbre commun");
            return null;
        }
    }








    public static void main(String[] args) throws IOException {
        ARN l = new ARN("AAAAAUGCAGUTG", "----((-))----");
        ARN l2 = new ARN("UUUAUGCAUUTG", "---((-))----");
        ARN l3 = new ARN("UUAUGCAGUTGa", "--(((---)))-");
        ARN l4 = new ARN("AAAAAUGCAGUTG", "----((-))----");
        ARN motif = new ARN("cgcuaucucCa","))))))))))-");
        ARN sousARN = new ARN("gutjdhucgGaCUuaaAAuCcga","------(((((-------)))))");
        Arbre sousarbre = Arbre.arnVersArbre(sousARN);
        ARN arn1 = stockholmARN("RF00005.stockholm.txt");
        Arbre a1 = Arbre.arnVersArbre(arn1);
        Arbre a5 = Arbre.plusGrandArbreCommun(a1,sousarbre);
        System.out.println(plusGrandARNCommun(arn1, sousARN));
        System.out.println(a5.arbreVersARN());








    }



}
