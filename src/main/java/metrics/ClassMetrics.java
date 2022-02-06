package metrics;
import properties.ProjectProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
  * Classe responsable de calculer les métriques associées
  * à un fichier .java donné en entré. Chaque méthode représentera
  * une métrique exigée.
  * @author Pascal St-Amour
  * @author Vincent Falardeau
 */
public class ClassMetrics {


    private final String path;
    private final String  className;
    private final int classe_CLOC;
    private final int classe_LOC;
    private final double classe_DC;
    private final int wmc;
    private final double classe_BC;
    private final ProjectProperties p;
    private String packageName;


    /**
     * Constructeur de ClassMetrics
     * @param file chemin du fichier .java de la classe
    */
    public ClassMetrics(String file, ProjectProperties projectProperties) throws IOException {
        this.p = projectProperties;
        this.path = file;
        this.className = getClassName(file);
        packageName = "";
        List<String> lines = readAndRemoveEmptyLinesAndSetPackageName(file);
        this.classe_CLOC = computeClasse_CLOC(lines);
        this.classe_LOC = computeClasse_LOC(lines);
        this.wmc = computeWMC(lines);
        this.classe_DC = computeClasse_DC(this.classe_CLOC,  this.classe_LOC);
        this.classe_BC = compute_classe_BC(classe_DC, wmc);
    }


    /**
     * Getter métric classe_CLOC
     * @return total de ligne
    */
    public int classe_CLOC() {
    return this.classe_CLOC;
    }


    /**
     * Getter pour le métric classe_LOC
     * @return total de ligne comportant des commentaires
    */
    public int classe_LOC() {
    return this.classe_LOC;
    }


    /**
     * Getter pour le métric classe_DC
     * @return metric classe_DC
    */
    public double classe_DC() {
        return this.classe_DC;
    }


    /**
     * Getter du Weighted Methods per Class
     * @return metric wmc
     */
    public int WMC() { return this.wmc; }


    /**
     * Getter dy degré selon lequel une classe est bien commentée
     * @return metric classe_BC
     */
    public double classe_BC() { return this.classe_BC; }


    /**
     * Concaténane les métrics des classes sous forme d'un string
     * @return string des métrics
    */
    @Override
    public String toString(){
        return String.format(p.get("csvOutputFormat"),
            this.path, this.className, this.classe_LOC, this.classe_CLOC, this.classe_DC, this.wmc, this.classe_BC);
    }


    /**
     * Calcul le nombre de lignes de code présente dans un fichier
     * d'une classe. Inclut les commentaires et exclus les lignes vides.
     * @return nombre ligne total
     */
    private int computeClasse_LOC(List<String> lines){
        return lines.stream().filter(line -> !line.trim().isEmpty()).collect(Collectors.toList()).size();
    }


    /**
     * Calcul le nombre de lignes de code contenant des commentaires
     * présente dans un fichier. Exclus les lignes vides. Prend en
     * compte tous les types de commentaires.
     * @param lines liste des lignes de codes
     * @return nombre de lignes contenant des commentaires
     */
    private int computeClasse_CLOC(List<String> lines) {

        // Compteur des commentaires
        int nbDeLignesDeCommentaire = 0;

        // Switch pour savoir quand on est dans un bloc /*  ... */ ou pas
        boolean inBlocComment = false;

        for (String line : lines) {

            // Commentaires de type //
            if(line.contains(p.get("basicComment")) && !inBlocComment && !line.contains(p.get("beginningOfBlocComment"))
                    && commentIsNotInString(line)) {
                nbDeLignesDeCommentaire++;
            }
            // Cas ou un bloc /*  ... */ s'ouvre et se referme sur la même ligne
            else if(line.contains(p.get("beginningOfBlocComment")) && line.contains(p.get("endOfBlocComment")) && !inBlocComment){
                nbDeLignesDeCommentaire++;
            }
            else{
                // Bloc /*  ... */ sur plusieurs lignes
                if(line.contains(p.get("beginningOfBlocComment")) && inBlocComment == false) {
                    inBlocComment = true;
                }
                if(inBlocComment == true) {
                    nbDeLignesDeCommentaire++;
                }
                if(inBlocComment == true && line.contains(p.get("endOfBlocComment"))) {
                    inBlocComment = false;
                }
            }
        }

        return nbDeLignesDeCommentaire;
    }


    /**
     * Calcul métric classe_dc soit la densité de commentaire
     * par rapport au nombre de lignes total.
     * @param cloc nb ligne de code avec commentaire de la classe
     * @param loc nb ligne de code total de la classe
     * @return densité commentaire de la classe
    */
    private double computeClasse_DC(int cloc, int loc) {
        return ((double)cloc) / ((double)loc);
    }


    /**
     * Méthode auxillaire permettant de déterminer les cas ou "//"
     * se trouve dans un string.
     * String a = "//" n'est pas un commentaire
     * String b = "//" + "//";
     * //allo world est un commentaire
     * String d = "//" + "//" n'est pas un commentaire
     * @param line une ligne de code
     * @return boolean
    */
    private boolean commentIsNotInString(String line) {
        int first, next = -1;
        int idx = line.indexOf(p.get("basicComment"));
        while(idx > 0){
            first  = line.indexOf(p.get("quote"), next+1);
            next = line.indexOf(p.get("quote"), first+1);
            if(!(first < idx && idx < next)) {
                return true;
            }
            idx = line.indexOf(p.get("basicComment"), idx+1);
        }
        return false;
    }


    /**
      * Méthode auxillaire permettant d'éliminer les lignes vides
      * des fichiers. Nomme également le nom du paquet qui contient
      * le fichier .java courant.
      * @param file fichier .java courant.
      * @return lignes de code trimmé(s)
      * @source https://www.javatpoint.com/how-to-read-file-line-by-line-in-java
      * @throws IOException
     */
    private List<String> readAndRemoveEmptyLinesAndSetPackageName(String file) throws IOException {

        List<String> lines = new ArrayList<>();

        Scanner scan = new Scanner(new FileInputStream(file));
        while(scan.hasNextLine()) {

            String line = scan.nextLine();
            String trimmedLine = line.trim();

            int pkgIdx = trimmedLine.indexOf(p.get("packageDeclarationPrefix"));
            if(pkgIdx >= 0){
                this.packageName = trimmedLine.substring(p.get("packageDeclarationPrefix").length(), trimmedLine.length()-1);
            }

            if(!trimmedLine.isEmpty()) { lines.add(line); }
        }
        scan.close();
        return lines;
    }


    /**
     * Trouver le degré de complexité d'une classe en observant
     * pour chaque ligne si une
     * sources https://www.baeldung.com/java-count-regex-matches
     * @param lines lignes de la classe
     * @return complexité de McCabe pour la classe
     */
    private int computeWMC(List<String> lines) {

        Pattern complexityPattern =  Pattern.compile(p.get("regexp_class_complexity"));
        Matcher complexityMatcher;
        int count = 0;

        for (String line: lines) {
            complexityMatcher = complexityPattern.matcher(line);
            if (complexityMatcher.find()) {
                count++;
            }
        }

        return count + 1;
    }


    /**
     * Méthode calculant le ratio de la densité de commentaire
     * et la complexité d'une classe = métric classe_BC.
     * @param classe_DC densité commentaire classe
     * @param wmc complexité McCabe classe
     * @return metric classe_BC
     */
    private double compute_classe_BC(double classe_DC, int wmc) {
        return (classe_DC) / ((double)wmc);
    }


    /**
      * Récupère le nom de la classe du fichier
      * @param file nom fichier
      * @return nom de la classe
     */
    private String getClassName(String file){
        File f = new File(file);
        return f.getName().substring(0, f.getName().length() - p.get("javaFileExt").length());
    }


    /**
      * Getter pour le nom du package
      * @return nom package
     */
    public String getPackageName() {
        return this.packageName;
    }

}

