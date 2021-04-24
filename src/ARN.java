import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ARN {
    protected String sequence;
    protected String appariements;

    //CONSTRUCTEURS

    public ARN(String sequence, String appariements) {
        this.sequence = sequence;
        this.appariements = appariements;
    }

    public ARN() {
        this.sequence = "";
        this.appariements = "";
    }

    public ARN(String appariements) {
        this.appariements = appariements;
    }

    @Override
    public String toString() {
        return (this.sequence + "\n" + this.appariements);
    }

    /**
     * Parser de fichiers txt stockholm afin de récupérer la séquence consensus ainsi que l'appariements au format
     * parenthèses-tirets et non WUSS
     *
     * @param file fichier txt de l'ARN dont on veut la séquence et la structure
     * @return un ARN(sequence, appariements)
     */
    public static ARN stockholmToARN(String file) throws IOException {
        FileReader stockholm = new FileReader(file);
        BufferedReader br = new BufferedReader(stockholm);
        String appariements = null;
        String sequence = null;
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("#=GC SS_cons")) {
                appariements = line.substring(line.indexOf("SS_cons") + 7);
                appariements = appariements.trim();
                appariements = appariements.replaceAll("\\.", "");
                appariements = appariements.replaceAll("<", "(");
                appariements = appariements.replaceAll(">", ")");
                appariements = appariements.replaceAll("_", "-");
                appariements = appariements.replaceAll(",", "-");
                appariements = appariements.replaceAll(":", "-");
            }
            if (line.contains("#=GC RF")) {
                sequence = line.substring(line.indexOf("RF") + 2);
                sequence = sequence.trim();
                sequence = sequence.replaceAll("\\.", "");
            }
        }
        return new ARN(sequence, appariements);
    }


    /**
     * Méthode qui permet de vérifier l'égalité stricte de deux ARN, soit en comparer leurs structures seules ou leurs
     * structures et séquences
     *
     * @param arn     : l'arn avec lequel on veut comparer l'arn qui appelle la méthode
     * @param methode : methode.structure si on veut comparer les structures et methode.sequence lorsqu'on veut comparer
     *                structure et séquences
     * @return un booléen, si vrai les deux ARNs sont égaux, si faux, ils ne le sont pas
     */
    public boolean equals(ARN arn, String methode) {
        boolean res = false;
        if (methode.equals("structure")) {
            res = this.appariements.equals(arn.appariements);
        } else if (methode.equals("sequence")) {
            res = (this.sequence.equals(arn.sequence) && this.appariements.equals(arn.appariements));
        }
        return res;
    }

    /**
     * @return ARN délesté des bases non appariées en début et fin de chaîne
     */
    public ARN removeTiretsDebutEtFinARN() {
        String appariements = this.appariements;
        String sequence = this.sequence;
        if (appariements.indexOf('(') != 0) {
            int index = appariements.indexOf('(');
            appariements = appariements.substring(index);
            sequence = sequence.substring(index);
        }
        if (appariements.lastIndexOf(')') != -1) {
            int endIndex = appariements.lastIndexOf(')');
            appariements = appariements.substring(0, endIndex + 1);
            sequence = sequence.substring(0, endIndex + 1);
        }
        return new ARN(sequence, appariements);
    }

    /**
     * teste l'égalité entre deux ARNs, sans tenir compte des bases non appariées de début de chaînes
     *
     * @param arn     que l'on veut comparer
     * @param methode : "structure" si on veut comparer les structures et "sequence" lorsqu'on veut comparer
     *                *                structure et séquences
     * @return un booléen : vrai si les ARNs sont égaux, sans compter les bases non appariées en début et fin de chaîne
     */
    public boolean equalsSansTirets(ARN arn, String methode) {
        ARN trimedArn1 = this.removeTiretsDebutEtFinARN();
        ARN trimedArn2 = arn.removeTiretsDebutEtFinARN();
        if (methode.equals("structure")) {
            return (trimedArn1.equals(trimedArn2,"structure"));
        } else if (methode.equals("sequence")) {
            return (trimedArn1.equals(trimedArn2,"sequence"));
        }
        return false;
    }

    /**
     * Méthode pour rechercher si l'arn qui appelle la méthode contient le motif (ARN) passé en paramètre.
     *
     * @param motif   : motif recherché
     * @param methode : "structure" si on recherche un motif uniquement structurel "sequence" si on recherche structure
     *                ET séquence
     * @return true si le motif est trouvé
     */
    public String rechercheDeMotifs(ARN motif, String methode) {

        if (motif.appariements.length()<=this.appariements.length()) {
            if (methode.equals("structure")){
                for (int i = 0; i < this.appariements.length()-(motif.appariements.length()-1); i++){
                    String subAppariements = this.appariements.substring(i,i+motif.appariements.length());
                    if (subAppariements.equals(motif.appariements)){
                        return "motif trouvé";
                    }
                }
            }
            if (methode.equals("sequence")){
                for (int i=0; i < this.appariements.length()-(motif.appariements.length()-1); i++){
                    ARN subARN = new ARN(this.sequence.substring(i,i+motif.sequence.length()),
                            this.appariements.substring(i,i+motif.appariements.length()));
                    if (subARN.equals(motif,"sequence")){
                        return "motif trouvé";
                    }
                }
            }
            return "motif non trouvé";
        } else {
            return ("Recherche de motif impossible --> le motif entré est plus grand que l'ARN dans lequel s'effectue " +
                    "la recherche. Essayez d'inverser les entrées.");
        }
    }

    /**
     * @param arn1 : premier ARN entré
     * @param arn2 : deuxième ARN entré
     * @return retourne le plus grand motif commun à arn1 et arn2 sous forme d'un nouvel ARN
     */
    public static ARN plusGrandARNCommun(ARN arn1, ARN arn2) {
        ARN plusGrandARNCommun = new ARN();
        int tailleMaxCommun = 0;
        int tailleBuffer;
        int openParenthese;
        int closedParenthese;
        if (arn1.sequence.length() != 0 && arn2.sequence.length() != 0) {
            for (int i = 0; i < arn1.sequence.length(); i++) {
                for (int j = 0; j < arn2.sequence.length(); j++) {
                    ARN bufferArn = new ARN();
                    tailleBuffer = 0;
                    openParenthese = 0;
                    closedParenthese = 0;
                    int k = i;
                    int l = j;
                    while (k < arn1.sequence.length() && l < arn2.sequence.length()
                            && arn1.appariements.charAt(k) == arn2.appariements.charAt(l)
                            && arn1.sequence.charAt(k) == arn2.sequence.charAt(l)) {

                        bufferArn.appariements += arn1.appariements.charAt(k);
                        bufferArn.sequence += arn1.sequence.charAt(k);
                        tailleBuffer += 1;
                        if (arn1.appariements.charAt(k) == '(') {
                            openParenthese += 1;
                        } else if (arn1.appariements.charAt(k) == ')') {
                            closedParenthese += 1;
                        }
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
        if (plusGrandARNCommun.sequence.length() != 0) {
            return plusGrandARNCommun;
        } else {
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
        ARN motif = new ARN("cgcuaucucCa", "))))))))))-");
        ARN sousARN = new ARN("gutjdhucgGaCUuaaAAuCcga", "------(((((-------)))))");
        Arbre sousarbre = Arbre.arnVersArbre(sousARN);
        ARN arn1 = stockholmToARN("RF00005.stockholm.txt");
        System.out.println(arn1.removeTiretsDebutEtFinARN() + "\n" + arn1);
        System.out.println(arn1.rechercheDeMotifs(motif,"sequence"));
        System.out.println(motif.rechercheDeMotifs(arn1,"structure"));
        System.out.println(stockholmToARN("essai.txt"));
        System.out.println(stockholmToARN("essai1.txt"));
    }


}
