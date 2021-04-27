import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ARN {
    private String sequence;
    private String appariements;

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

    //GETTERS

    public String getAppariements() {
        return appariements;
    }

    public String getSequence() {
        return sequence;
    }

    //Vérification de la validité d'une structure secondaire entrée :
    public boolean validiteSSentree() {
        int cpt_opened_parentheses = 0;
        int cpt_closed_parentheses = 0;
        for (char item : this.appariements.toCharArray()) {
            if (item == '(') cpt_opened_parentheses += 1;
            else if (item == ')') cpt_closed_parentheses += 1;
        }
        return cpt_closed_parentheses == cpt_opened_parentheses;
    }


    //TOSTRING
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
    public static ARN stockholmToARN(String file) {
        try {//Try/Catch : Gestion de l'exception si File not Found
            FileReader stockholm = new FileReader(file); //Lecture du fichier et stockage en FileReader
            BufferedReader br = new BufferedReader(stockholm); //BufferedReader construit à partir du FileReader
            String appariements = null; //Initialisation du String appariements et du String sequence
            String sequence = null; // qui serviront à la construction de la nouvelle instance ARN
            String line; //Initialisation du buffer "line"
            while ((line = br.readLine()) != null) {//Tant que la ligne n'est pas vide, on poursuit la lecture des lignes
                if (line.contains("#=GC SS_cons")) { //Tag dans le fichier stockholm situé avant le parenthésage
                    // en format WUSS
                    appariements = line.substring(line.indexOf("SS_cons") + 7); //Récupère le substring de la
                    // ligne sans le tag
                    appariements = appariements.trim();//Retire les espaces
                    appariements = appariements.replaceAll("\\.", "");//Conversion de WUSS
                    appariements = appariements.replaceAll("<", "("); //en simple format
                    appariements = appariements.replaceAll(">", ")");//parenthèses-tirets
                    appariements = appariements.replaceAll("_", "-");
                    appariements = appariements.replaceAll(",", "-");
                    appariements = appariements.replaceAll(":", "-");
                }
                if (line.contains("#=GC RF")) { //Tag de la séquence dans le fichier stockholm
                    sequence = line.substring(line.indexOf("RF") + 2);//Récupère le substring de line sans le tag
                    sequence = sequence.trim();//Retire les espaces
                    sequence = sequence.replaceAll("\\.", "");//Retire les gaps (représentez en points)
                    sequence = sequence.toUpperCase();//Tout en majuscule, afin de ne pas tenir compte de la casse
                    // lors des comparaisons
                }
            }
            return new ARN(sequence, appariements);//retourne l'ARN construit à partir des String sequence et appariements
        } catch (IOException e) { //handle de l'exception
            e.printStackTrace();
            System.out.println("Erreur : Fichier Stockholm non trouvé dans le répertoire local");//Affichage
            // d'un message d'erreur
            return null;//La fonction retourne null
        }
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
     * Méthode qui permet de retirer à un ARN ses bases non appariées au niveau de sa séquence et de son parenthésage
     * Car ce sont des éléments peu informatifs au niveau de la structure de l'ARN et soumis à variabilité
     *
     * @return ARN délesté des bases non appariées en début et fin de chaîne
     */
    public ARN removeTiretsDebutEtFinARN() {
        String appariements = this.appariements;//Récupère le String de l'attribut
        String sequence = this.sequence;//idem
        appariements = appariements.substring(appariements.indexOf('('));
        if (sequence != null) { //Si l'ARN appelant la méthode contient une séquence
            sequence = sequence.substring(appariements.indexOf('('));
        }

        if (appariements.lastIndexOf(')') != -1) { //Si la parenthèse fermante n'est pas déjà à la fin de la ligne
            int endIndex = appariements.lastIndexOf(')');
            appariements = appariements.substring(0, endIndex + 1);
            if (sequence != null) { //Si l'ARN appelant la méthode contient une séquence
                sequence = sequence.substring(0, endIndex + 1);
            }

        }
        if (sequence == null) { //Si l'ARN appelant la méthode ne contient pas de séquence
            return new ARN(appariements); //Renvoie le nouvel ARN construit uniquement avec le String appariements
        } else {
            return new ARN(sequence, appariements);//Sinon, construction à partir des deux String
        }

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
            return (trimedArn1.equals(trimedArn2, "structure"));
        } else if (methode.equals("sequence")) {
            return (trimedArn1.equals(trimedArn2, "sequence"));
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

        if (motif.appariements.length() <= this.appariements.length()) {//Si le motif recherché est
            // bien plus petit que l'ARN qui appelle la méthode
            if (methode.equals("structure")) { //Si l'on a choisi de ne comparer que les structures
                for (int i = 0; i < this.appariements.length() - (motif.appariements.length() - 1); i++) {//parcours
                    //de l'appariement jusqu'à s'arrêter à l'index qui ne permet plus la comparaison avec le motif
                    //sur toute la longueur du motif
                    String subAppariements = this.appariements.substring(i, i + motif.appariements.length());//Récupère
                    //découpe un substring de longueur égale au motif et commençant à l'index en cours
                    if (subAppariements.equals(motif.appariements)) { //Si le substring et le motif sont égaux
                        return "motif trouvé";//On retourne motif trouvé
                    }
                }
            }
            if (methode.equals("sequence")) {//Si l'on a choisi de comparer les structures et les séquences
                for (int i = 0; i < this.appariements.length() - (motif.appariements.length() - 1); i++) { //idem
                    ARN subARN = new ARN(this.sequence.substring(i, i + motif.sequence.length()), //Sous-ARN construit
                            this.appariements.substring(i, i + motif.appariements.length())); //à partir des substrings
                    //d'appariement et de sequence
                    if (subARN.equals(motif, "sequence")) {//Si ils sont égaux
                        return "motif trouvé";//On retourne "motif trouvé'
                    }
                }
            }
            return "motif non trouvé";//Sinon, pour les deux méthodes, on retourne "motif non trouvé"
        } else {
            return ("Recherche de motif impossible --> le motif entré est plus grand que l'ARN dans lequel s'effectue " +
                    "la recherche. Essayez d'inverser les entrées.");//else du if de début de fonction
        }
    }

    /**
     * Méthode qui permet d'obtenir un Arbre représentant la structure secondaire d'un ARN
     *
     * @return un Arbre raciné dont les noeuds internes sont les bases appariées et les feuilles des bases non appariées
     */

    public Arbre arnVersArbre() {
        String appariements = this.getAppariements();
        String sequence = this.getSequence();
        Arbre racine = new Arbre();
        Arbre pere = racine; //buffer
        for (int i = 0; i < appariements.length(); i++) {
            if (appariements.charAt(i) == '-') { //tiret = base non appariée
                pere.addEnfant(Character.toString(sequence.charAt(i))); //ajout d'un enfant au buffer
            } else if (appariements.charAt(i) == '(') { //( = première base d'un couple de bases appariées
                pere.addEnfant(Character.toString(sequence.charAt(i))); //ajout d'un enfant au buffer
                pere = pere.getDernierEnfantAjoute();//l'enfant ajouté devient le nouveau buffer
            } else if (appariements.charAt(i) == ')') { //) = deuxième base d'un couple de bases appariées
                pere.addPaire(Character.toString(sequence.charAt(i)));//ajout d'une base au buffer
                // qui contient désormais une paire de bases
                pere = pere.getLienVersLePere();//l'ancêtre du buffer devient le nouveau buffer
            }
        }
        return racine;//retour de l'arbre créé
    }

    /**
     * Méthode implémentée afin de retourner le Plus Grand Sous-Arbre commun comme nous n'arrivions pas à implémenter
     * cette méthode dans la classe Arbre, avec des objets Arbre.
     * Pour qu'un motif commun soit trouvé, il faut donc nécessairement qu'il s'agisse également d'un sous-Arbre commun
     * si les deux ARNs étaient représentés en Arbre. C'est pourquoi un motif commun doit comprendre :
     * - au moins une parenthèse ouvrante et fermante --> Au moins un noeud interne
     * - et qu'il y ait le même nombre de parenthèses ouvrantes et fermantes --> noeud interne complet avec bases appariées
     *
     * @param arn2 : deuxième ARN entré
     * @return retourne le plus grand motif commun à arn1 et arn2 sous forme d'un nouvel ARN
     */
    public ARN plusGrandMotifCommun(ARN arn2, String methode) {
        ARN plusGrandARNCommun = new ARN(); //ARN retourné par la méthode
        int tailleMaxCommun = 0; //Remplacé par la taille du buffer si elle est plus grande
        int tailleBuffer;//taille de l'ARN buffer
        int openParenthese;//compteur de parenthèses ouvrantes
        int closedParenthese;//compteur de parenthèses fermantes
        if (this.appariements.length() != 0 || arn2.appariements.length() != 0) { //si l'un est vide, pas de motif possible
            for (int i = 0; i < this.appariements.length(); i++) { //boucle sur la sequence arn1
                for (int j = 0; j < arn2.appariements.length(); j++) { //boucle sur la sequence arn2
                    ARN bufferArn = new ARN(); //instanciation d'un ARN buffer
                    tailleBuffer = 0; //mise à zero de la taille
                    openParenthese = 0; //mise à zéro du cpt de parentheses ouvrantes
                    closedParenthese = 0; // mise à zéro du cpt de parenthèses fermantes
                    int k = i; //index de la sous-boucle de parcours de arn1
                    int l = j; //index de la sous-boucle de parcours de arn2
                    if (methode.equals("sequence")) {
                        while (k < this.sequence.length() && l < arn2.sequence.length()
                                && this.appariements.charAt(k) == arn2.appariements.charAt(l)
                                && this.sequence.charAt(k) == arn2.sequence.charAt(l)) {

                            bufferArn.appariements += this.appariements.charAt(k);//extension du String appariements
                            bufferArn.sequence += this.sequence.charAt(k);//extension du String sequence
                            tailleBuffer += 1;//ajout d'un au compteur de la taille de l'ARN Buffer
                            if (this.appariements.charAt(k) == '(') {
                                openParenthese += 1;
                            } else if (this.appariements.charAt(k) == ')') {
                                closedParenthese += 1;
                            }
                            k += 1;
                            l += 1;
                        }
                    } else if (methode.equals("structure")) {
                        while (k < this.appariements.length() && l < arn2.appariements.length()
                                && this.appariements.charAt(k) == arn2.appariements.charAt(l)) {

                            bufferArn.appariements += this.appariements.charAt(k);//extension du String appariements
                            tailleBuffer += 1;//ajout d'un au compteur de la taille de l'ARN Buffer
                            if (this.appariements.charAt(k) == '(') {
                                openParenthese += 1;
                            } else if (this.appariements.charAt(k) == ')') {
                                closedParenthese += 1;
                            }
                            k += 1;
                            l += 1;
                        }
                    }
                    if (tailleBuffer > tailleMaxCommun && openParenthese == closedParenthese && openParenthese != 0) {
                        //Si la taille du Buffer est plus grande que la tailleMaxCommun trouvée jusqu'à présent
                        //ET que le nombre de parenthèses fermantes et ouvrantes sont identiques --> Sous-Arbre complet
                        //ET qu'il y a au moins une parenthèse ouvrante et fermante --> Au moins un noeud interne
                        tailleMaxCommun = tailleBuffer;//La tailleMaxCommun devient la taille du Buffer
                        plusGrandARNCommun = bufferArn;//et le plus GrandARNCommun prend l'adresse du bufferARN trouvé
                    }
                }
            }
        }
        if (plusGrandARNCommun.appariements.length() != 0) {//Si au moins une séquence a été trouvée en commun
            return plusGrandARNCommun;
        } else {
            System.out.println("Pas de motif commun trouvé => Pas de sous arbre commun");
            return null;
        }
    }


    public static void main(String[] args) {
        ARN l = new ARN("AAAAAUGuCAGU", "((((--)--)))");
        ARN ll = new ARN("AAAAAUGCAGUTG", "((((--))))---");
        ARN l2 = new ARN("UUUAUGCAUUTG", "---((-))----");
        ARN l3 = new ARN("UUAUGCAGUTGa", "--(((---)))-");
        ARN l4 = new ARN("AAAAAUGCAGUTG", "----((-))----");
        ARN motif = new ARN("cgcuaucucCa", "))))))))))-");
        ARN sousARN = new ARN("gutjdhucgGaCUuaaAAuCcga", "------(((((-------)))))");
        Arbre sousarbre = sousARN.arnVersArbre();
        ARN arn1 = stockholmToARN("RF00005.stockholm.txt");
        System.out.println(arn1);
        Arbre a1 = arn1.arnVersArbre();
        System.out.println(a1.arbreVersARN());
        a1.affichageArbre();



    }


}
