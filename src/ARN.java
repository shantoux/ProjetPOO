public class ARN {
    private String sequence;
    private String appariements;

    public ARN(String sequence, String appariements){
        this.sequence = sequence;
        this.appariements = appariements;
    }

    @Override
    public String toString(){
        return(" " + this.sequence + "\n " + this.appariements);
    }

    public static void main(String[] args) {
        ARN l = new ARN("AUGCAUUTG", "((-))----");
        System.out.println(l);

    }

}

