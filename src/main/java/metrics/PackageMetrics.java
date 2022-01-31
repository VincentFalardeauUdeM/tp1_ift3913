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
     * @param pkg chemin absolu de la classse
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
     * Récupère le total de ligne des le package qui contient
     * des commentaires en excluant les lignes vides.
     * @return total de ligne
     */
    public int paquet_CLOC(){
        return this.paquet_CLOC;
    }

    /**
     * Récupère le total de ligne des fichiers dans le package
     * comprenant les commentaires et excluant les lignes vides.
     * @return total de ligne
     */
    public int paquet_LOC(){
        return this.paquet_LOC;
    }

    /**
     * Calcul de la densité de commentaires pour un paquet
     * @return densité de commentaires pour un paquet
     */
    public double paquet_DC() {
      return this.paquet_DC;
    }

    /**
     * Donne la liste des métriques des
     * */
    public List<ClassMetrics> getClassMetricsList(){
        return this.classMetricsList;
    }

    @Override
    public String toString(){
        return String.format(p.get("csvOutputFormat"),
                this.path, this.pkgName, this.paquet_LOC, this.paquet_CLOC, this.paquet_DC);
    }

    private int computePaquet_CLOC(List<ClassMetrics> classMetricsList) {
        int cloc = 0;
        for(ClassMetrics cm: classMetricsList){
            cloc += cm.classe_CLOC();
        }
        return cloc;
    }

    private int computePaquet_LOC(List<ClassMetrics> classMetricsList) {
        int loc = 0;
        for(ClassMetrics cm: classMetricsList){
            loc += cm.classe_LOC();
        }
        return loc;
    }

    private double computePaquet_DC(int cloc, int loc) {
        float dc = ((float)cloc) / ((float)loc);
        return Math.round(dc * 100.0) / 100.0;
    }

    private List<ClassMetrics> getClassMetricsFromPackage(String packagePath) throws IOException {
        List<ClassMetrics> classMetricsList = new ArrayList<ClassMetrics>();
        List<String> files = getJavaFilesInPackage(packagePath);
        for(String file: files){
            classMetricsList.add(new ClassMetrics(file, p));
        }
        return classMetricsList;
    }

    //Src: https://www.baeldung.com/java-list-directory-files
    private List<String> getJavaFilesInPackage(String pkg){
        File[] files = new File(pkg).listFiles();
        return Arrays.stream(files).distinct()
                .filter(file -> !file.isDirectory() && file.getName().endsWith(p.get("javaFileExt")))
                .map(File::getName)
                .map(fName-> Util.joinPaths(pkg, fName))
                .collect(Collectors.toList());
    }

    private String getPackageName(String pkg, List<ClassMetrics> classMetricsList){
        return classMetricsList.size() > 0 ?
                classMetricsList.get(0).getPackageName() :
                pkg.substring(1).replaceAll(p.get("slash"), p.get("dot"));
    }
}
