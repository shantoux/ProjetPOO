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
        return(" " + this.sequence + "\n " + this.appariements);
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


    public static void main(String[] args) {
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
    }



}
