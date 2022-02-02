package main.java.metrics;
import main.java.properties.ProjectProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        this.classe_DC = computeClasse_DC(this.classe_CLOC,  this.classe_LOC);
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
     * @return densité des commentaires
    */

    public double classe_DC() {
        return this.classe_DC;
    }


    /**
     * Concaténane les métrics des classes sous forme d'un string
     * @return string des métrics
    */

    @Override
    public String toString(){
        return String.format(p.get("csvOutputFormat"),
            this.path, this.className, this.classe_LOC, this.classe_CLOC, this.classe_DC);
    }


    /**
     * Calcul le nombre de lignes de code présente dans un fichier
     * d'une classe. Inclut les commentaires et exclus les lignes vides.
     * @return nombre ligne total
     */

    private int computeClasse_CLOC(List<String> lines){
        return lines.size();
    }


    /**
     * Calcul le nombre de lignes de code contenant des commentaires
     * présente dans un fichier. Exclus les lignes vides. Prend en
     * compte tous les types de commentaires.
     * @param lines liste des lignes de codes
     * @return nombre de lignes contenant des commentaires
     */

    private int computeClasse_LOC(List<String> lines) {

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
        float dc = ((float)cloc) / ((float)loc);
        return Math.round(dc * 100.0) / 100.0;
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

