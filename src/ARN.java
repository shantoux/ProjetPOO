public class ARN {
    private String sequence;
    private String appariements;

    //CONSTRUCTEURS

    public ARN(String sequence, String appariements){
        this.sequence = sequence;
        this.appariements = appariements;
    }

    @Override
    public String toString(){
        return(" " + this.sequence + "\n " + this.appariements);
    }

    public boolean meme_forme(ARN arn) {
        int i = 0;
        int j = 0;
        boolean res = true;
        while (this.appariements.charAt(i) == '-') i++;
        while (arn.appariements.charAt(j) == '-') j++;
        while (i<this.appariements.length() && j<arn.appariements.length()){
          if (this.appariements.charAt(i) == arn.appariements.charAt(j)){
            i++;
            j++;
          }
          else {
            return false;
          }
        }
        return res;
    }

    public boolean meme_sequence(ARN arn) {
        int i = 0;
        int j = 0;
        boolean res = true;
        while (this.appariements.charAt(i) == '-') i++;
        while (arn.appariements.charAt(j) == '-') j++;
        while (i<this.appariements.length() && j<arn.appariements.length()){
            if (this.sequence.charAt(i) == arn.sequence.charAt(j)){
                i++;
                j++;
            }
            else {
                return false;
            }
        }
        return res;
    }


    public static void main(String[] args) {
        ARN l = new ARN("AAAAAUGCAGUTG", "----((-))----");
        System.out.println(l);
        ARN l2 = new ARN("UUUAUGCAUUTG", "---((-))----");
        ARN l3 = new ARN("UUAUGCAGUTG", "--(((---)))");
        System.out.println(l.meme_forme(l2));
        System.out.println(l.meme_sequence(l2));
        System.out.println(l.meme_forme(l3));
        System.out.println(l.meme_sequence(l3));
    }



}
