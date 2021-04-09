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



    public boolean egalite(ARN arn, String methode){
        boolean res = false;
        if (methode.equals("forme")){
            res = this.appariements.equals(arn.appariements);
        }
        else if (methode.equals("sequence")){
            res = (this.sequence.equals(arn.sequence)&&this.appariements.equals(arn.appariements));
        }
        return res;
    }


    public boolean comparaison(ARN arn, String methode) {
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


    public static void main(String[] args) throws IOException {
        ARN l = new ARN("AAAAAUGCAGUTG", "----((-))----");
        System.out.println(l);
        ARN l2 = new ARN("UUUAUGCAUUTG", "---((-))----");
        ARN l3 = new ARN("UUAUGCAGUTG", "--(((---)))");
        ARN l4 = new ARN("AAAAAUGCAGUTG", "----((-))----");

        System.out.println(l.comparaison(l2, "forme"));
        System.out.println(l.comparaison(l2, "sequence"));
        System.out.println(l.comparaison(l3, "forme"));
        System.out.println(l.comparaison(l3, "sequence"));
        System.out.println(l.egalite(l4, "forme"));
        System.out.println(l.egalite(l4,"sequence"));
        System.out.println(stockholmARN("RF00005.stockholm.txt"));

        Arbre racine = new Arbre();
        racine.addEnfant('A');
    }



}
