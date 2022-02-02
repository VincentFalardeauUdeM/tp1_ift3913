package main.java.metrics;
import main.java.properties.ProjectProperties;
import main.java.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* Classe responsable de calculer les métriques associées
* à un package donné en entré. Chaque méthode représentera
* une métrique exigée.
* @author Pascal St-Amour
* @author Vincent Falardeau
*/

public class PackageMetrics {


    private final String path;
    private final String  pkgName;
    private final int paquet_CLOC;
    private final int paquet_LOC;
    private final double paquet_DC;
    private final ProjectProperties p;
    private List<ClassMetrics> classMetricsList;


    /**
     * Constructeur de PackageMetrics
     * @param pkg nom package
     * @param projectProperties propriétés
     * @throws IOException
     */

    public PackageMetrics(String pkg, ProjectProperties projectProperties) throws IOException {
        this.p = projectProperties;
        this.path = pkg;
        this.classMetricsList = getClassMetricsFromPackage(pkg);
        this.pkgName = getPackageName(pkg, classMetricsList);
        this.paquet_CLOC = computePaquet_CLOC(classMetricsList);
        this.paquet_LOC = computePaquet_LOC(classMetricsList);
        this.paquet_DC = computePaquet_DC( this.paquet_CLOC, this.paquet_LOC);
    }


    /**
     * Getter métric paquet_CLOC
     * @return total de ligne
    */

    public int paquet_CLOC(){
        return this.paquet_CLOC;
    }


    /**
     * Getter métric paquet_LOC
     * @return total de ligne
    */

    public int paquet_LOC(){
        return this.paquet_LOC;
    }


    /**
     * Getter métric paquet_DC
     * @return densité de commentaires pour un paquet
    */

    public double paquet_DC() {
    return this.paquet_DC;
    }


    /**
     * Getter liste des métrics d'un package
     * @return liste des métrics
     */

    public List<ClassMetrics> getClassMetricsList(){
    return this.classMetricsList;
    }


    /**
     * Concaténane les métrics des paquets sous forme d'un string
     * @return string des métrics
     */

    @Override
    public String toString(){
        return String.format(p.get("csvOutputFormat"),
                this.path, this.pkgName, this.paquet_LOC, this.paquet_CLOC, this.paquet_DC);
    }


    /**
     * Calcul le nombre total des lignes contenant des commentaires
     * pour tous les fichiers contenu dans le paquet.
     * @param classMetricsList métrics d'un paquet
     * @return nombre total de ligne
     */

    private int computePaquet_CLOC(List<ClassMetrics> classMetricsList) {
        int cloc = 0;
        for(ClassMetrics cm: classMetricsList){
            cloc += cm.classe_CLOC();
        }
        return cloc;
    }


    /**
     * Calcul le nombre total des lignes pour tous les fichiers
     * contenu dans le paquet.
     * @param classMetricsList métrics d'un paquet
     * @return nombre total ligne avec commentaire
     */

    private int computePaquet_LOC(List<ClassMetrics> classMetricsList) {
        int loc = 0;
        for(ClassMetrics cm: classMetricsList){
            loc += cm.classe_LOC();
        }
        return loc;
    }


    /**
     * Calcul la densité de ligne comprenant des commentaires
     * sur le nombre de ligne total pour un paquet.
     * @param cloc nb lignes avec commentaires du paquet
     * @param loc nb lignes total du paquet
     * @return densité commentaire du paquet
     */

    private double computePaquet_DC(int cloc, int loc) {
        float dc = ((float)cloc) / ((float)loc);
        return Math.round(dc * 100.0) / 100.0;
    }


    /**
     * Récupère pour chaque paquet la liste des objets ClassMetrics
     * correspondant à un fichier.java ou encore à une classe du paquet.
     * @param packagePath lien relatif du paquet
     * @return liste des ClassMetrics du paquet
     * @throws IOException
     */

    private List<ClassMetrics> getClassMetricsFromPackage(String packagePath) throws IOException {
        List<ClassMetrics> classMetricsList = new ArrayList<ClassMetrics>();
        List<String> files = getJavaFilesInPackage(packagePath);
        for(String file: files){
            classMetricsList.add(new ClassMetrics(file, p)); // Création métric pour fichier.java
        }
        return classMetricsList;
    }


    /**
     * Fonction auxillaire permettant de récupérer pour un paquet
     * donné tous les fichiers qu'il contient.
     * @param pkg nom paquet courant
     * @source https://www.baeldung.com/java-list-directory-files
     * @return liste fichiers du package
     */

    private List<String> getJavaFilesInPackage(String pkg){
        File[] files = new File(pkg).listFiles();
        return Arrays.stream(files).distinct()
                .filter(file -> !file.isDirectory() && file.getName().endsWith(p.get("javaFileExt")))
                .map(File::getName)
                .map(fName-> Util.joinPaths(pkg, fName))
                .collect(Collectors.toList());
    }


    /**
     * Fonction auxilliaire permettant de get le nom exact du
     * package courant.
     * @param pkg chemin relatif du pkg
     * @param classMetricsList liste des metrics du pkg
     * @return nom du pkg
     */

    private String getPackageName(String pkg, List<ClassMetrics> classMetricsList){
        return classMetricsList.size() > 0 ?
                classMetricsList.get(0).getPackageName() :
                pkg.substring(1).replaceAll(p.get("slash"), p.get("dot"));
    }

}
