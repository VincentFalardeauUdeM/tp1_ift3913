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
     * Récupère le total de ligne du fichier java source en
     * comprenant les commentaires et excluant les lignes vides.
     * @return total de ligne
     */
    public int classe_CLOC() {
        return this.classe_CLOC;
    }

    /**
     * Récupère le total de ligne comportant des commentaires du
     * fichier java source et excluant les lignes vides.
     * @return total de ligne comportant des commentaires
     */
    public int classe_LOC() {
        return this.classe_LOC;
    }

    /**
     * Calcul de la densité des commentaires i.e. nombre de
     * commentaires par ligne de code.
     * @return densité des commentaires
     */
    public double classe_DC() {
        return this.classe_DC;
    }

    @Override
    public String toString(){
        return String.format(p.get("csvOutputFormat"),
                this.path, this.className, this.classe_LOC, this.classe_CLOC, this.classe_DC);
    }

    private int computeClasse_CLOC(List<String> lines){
        return lines.size();
    }

    private int computeClasse_LOC(List<String> lines){
        int nbDeLignesDeCommentaire = 0;
        boolean inBlocComment = false; // Switch pour savoir quand on est dans un bloc /*  ... */ ou pas

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

    private double computeClasse_DC(int cloc, int loc){
        float dc = ((float)cloc) / ((float)loc);
        return Math.round(dc * 100.0) / 100.0;
    }

    //S'assurer que // n'est pas dans un string. Exemples:
    //String a = "//";                      n'est pas un commentaire
    //String b = "//" + "//";//allo world   est un commentaire
    //String d = "//" + "//";               n'est pas un commentaire
    private boolean commentIsNotInString(String line){
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

    private List<String> readAndRemoveEmptyLinesAndSetPackageName(String file) throws IOException {

        List<String> lines = new ArrayList<>();
        
        //Src: https://www.javatpoint.com/how-to-read-file-line-by-line-in-java
        Scanner scan = new Scanner(new FileInputStream(file));
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            String trimmedLine = line.trim();

            int pkgIdx = trimmedLine.indexOf(p.get("packageDeclarationPrefix"));
            if(pkgIdx >= 0){
                this.packageName = trimmedLine.substring(p.get("packageDeclarationPrefix").length(), trimmedLine.length()-1);
            }

            if(!trimmedLine.isEmpty()) {
                lines.add(line);
            }
        }
        scan.close();
        return lines;
    }

    private String getClassName(String file){
        File f = new File(file);

        return f.getName().substring(0, f.getName().length() - p.get("javaFileExt").length());
    }

    public String getPackageName() {
        return this.packageName;
    }
}

