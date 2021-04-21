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
    public boolean equals(ARN arn, String methode){
        boolean res = false;
        if (methode.equals("structure")){
            res = this.appariements.equals(arn.appariements);
        }
        else if (methode.equals("sequence")){
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
    public boolean equalsSansTirets(ARN arn, String methode) {
        int i = 0;
        int j = 0;
        while (this.appariements.charAt(i) == '-') i++;
        while (arn.appariements.charAt(j) == '-') j++;
        if (methode.equals("structure")){
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
        else if (methode.equals("sequence")){
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
    public boolean rechercheDeMotifs(ARN arn, String methode){
        if (arn.appariements.length() > this.appariements.length()){
            System.out.print("Recherche de motif impossible car le motif est plus grand que l'ARN testé : ");
            return false;
        }
        boolean res = false;
        int cptBasesEgales = 1;
        if (methode.equals("structure")){
            for (int j =0; j<this.appariements.length();j++){
                if (j + arn.appariements.length() <= this.appariements.length()){
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
        else if (methode.equals("sequence")){
            for (int j =0; j<this.appariements.length();j++) {
                if (j + arn.appariements.length() <= this.appariements.length()){
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
        int openParenthese = 0;
        int closedParenthese = 0;
        if (arn1.sequence.length() != 0 && arn2.sequence.length() != 0) {
            for (int i = 0; i < arn1.sequence.length(); i++) {
                for (int j = 0; j < arn2.sequence.length(); j++) {
                    ARN bufferArn = new ARN();
                    tailleBuffer = 0;
                    openParenthese = 0;
                    closedParenthese = 0;
                    int k = i;
                    int l = j;
                    while (k<arn1.sequence.length() && l<arn2.sequence.length()
                            && arn1.appariements.charAt(k) == arn2.appariements.charAt(l)
                            && arn1.sequence.charAt(k) == arn2.sequence.charAt(l)){

                        bufferArn.appariements += arn1.appariements.charAt(k);
                        bufferArn.sequence += arn1.sequence.charAt(k);
                        tailleBuffer += 1;
                        if (arn1.appariements.charAt(k) == '('){
                            openParenthese +=1;
                        }
                        else if (arn1.appariements.charAt(k) == ')'){
                            closedParenthese += 1;
                        }
                        System.out.println("i :" + i + " j :" +j + " open : " + openParenthese + " closed :" + closedParenthese + " tailles buffer : " + tailleBuffer);
                        k += 1;
                        l += 1;
                    }
                    if (tailleBuffer > tailleMaxCommun && openParenthese == closedParenthese) {
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
        ARN l = new ARN("AAAAAUGuCAGU", "((((--)--)))");
        ARN ll = new ARN("AAAAAUGCAGUTG", "((((--))))---");
        ARN l2 = new ARN("UUUAUGCAUUTG", "---((-))----");
        ARN l3 = new ARN("UUAUGCAGUTGa", "--(((---)))-");
        ARN l4 = new ARN("AAAAAUGCAGUTG", "----((-))----");
        ARN motif = new ARN("cgcuaucucCa","))))))))))-");
        ARN sousARN = new ARN("gutjdhucgGaCUuaaAAuCcga","------(((((-------)))))");
        Arbre sousarbre = Arbre.arnVersArbre(sousARN);
        ARN arn1 = stockholmARN("RF00005.stockholm.txt");
        Arbre a1 = Arbre.arnVersArbre(arn1);
        Arbre essai1 = Arbre.arnVersArbre(l);
        Arbre essai2 = Arbre.arnVersArbre(ll);
        Arbre result = Arbre.plusGrandArbreCommun(essai1, essai2);
        ARN resultat = result.arbreVersARN();
        System.out.println(resultat);









    }



}
