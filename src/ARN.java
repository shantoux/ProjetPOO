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



    public boolean equals(ARN arn, String methode){
        boolean res = false;
        if (methode.equals("forme")){
            res = this.appariements.equals(arn.appariements);
        }
        else if (methode.equals("sequence")){
            res = (this.sequence.equals(arn.sequence)&&this.appariements.equals(arn.appariements));
        }
        return res;
    }

    //Teste l'égalité sans les tirets de début d'appariement
    public boolean equalsSansTirets(ARN arn, String methode) {
        int i = 0;
        int j = 0;
        while (this.appariements.charAt(i) == '-') i++;
        while (arn.appariements.charAt(j) == '-') j++;
        if (methode.equals("forme")){
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

    public boolean rechercheDeMotifs(ARN arn){
        boolean res = false;
        int cptBasesEgales = 1;
        for (char b : arn.appariements.toCharArray()) {
            int i = 0;
            while (this.appariements.charAt(i) == (arn.appariements.charAt(i+arn.appariements.indexOf(b)))
                    && cptBasesEgales < this.appariements.length()){
                cptBasesEgales += 1;
                i += 1;
            }
            if (cptBasesEgales == this.appariements.length()){
                res = true;
                return res;
            }
        }
        return res;
    }






    public static void main(String[] args) throws IOException {
        ARN l = new ARN("AAAAAUGCAGUTG", "----((-))----");
        System.out.println(l);
        ARN l2 = new ARN("UUUAUGCAUUTG", "---((-))----");
        ARN l3 = new ARN("UUAUGCAGUTG", "--(((---)))");
        ARN l4 = new ARN("AAAAAUGCAGUTG", "----((-))----");

//        System.out.println(l.equalsSansTirets(l2, "forme"));
//        System.out.println(l.equalsSansTirets(l2, "sequence"));
//        System.out.println(l.equalsSansTirets(l3, "forme"));
//        System.out.println(l.equalsSansTirets(l3, "sequence"));
//        System.out.println(l.equals(l4, "forme"));
//        System.out.println(l.equals(l4,"sequence"));
        ARN arn1 = stockholmARN("RF00005.stockholm.txt");
        System.out.println(arn1.appariements);
        System.out.println(arn1.sequence);
        Arbre a1 = Arbre.parentheseVersArbre(arn1.appariements, arn1.sequence);

        a1.affichageArbre();
//        ARN motif = new ARN("AUAUA","((-))");
//        System.out.println(motif.rechercheDeMotifs(l));
//        System.out.println(l2.rechercheDeMotifs(l));

    }



}
