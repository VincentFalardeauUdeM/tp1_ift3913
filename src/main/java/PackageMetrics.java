package main.java;

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

    private List<ClassMetrics> classMetricsList;

    /**
     * Constructeur de PackageMetrics
     * @param packagePath chemin absolu de la classse
     */

    public PackageMetrics(String packagePath) throws IOException {
        classMetricsList = new ArrayList<ClassMetrics>();
        List<String> files = getJavaFilesInPackage(packagePath);
        for(String file: files){
            classMetricsList.add(new ClassMetrics(file));
        }
    }


    /**
     * Récupère le total de ligne des fichiers dans le package
     * comprenant les commentaires et excluant les lignes vides.
     * @return total de ligne
     */

    public int paquet_LOC(){
        int loc = 0;
        for(ClassMetrics cm: classMetricsList){
            loc += cm.classe_LOC();
        }
        return loc;
    }


    /**
     * Récupère le total de ligne des le package qui contient
     * des commentaires en excluant les lignes vides.
     * @return total de ligne
     */

    public int paquet_CLOC(){
        int cloc = 0;
        for(ClassMetrics cm: classMetricsList){
            cloc += cm.classe_CLOC();
        }
        return cloc;
    }


    /**
     * Calcul de la densité de commentaires pour un paquet
     * @return densité de commentaires pour un paquet
     */

    public float paquet_DC() {
      return this.paquet_CLOC() / this.paquet_LOC();
    }


    /**
     * Récupère récursivement les chemins des fichiers .java dans le package donné
     * @param packagePath chemin du package
     * @return ensemble contenant les chemins des fichiers .java dans le package
     */

    private List<String> getJavaFilesInPackage(String packagePath){
        //Source: https://www.baeldung.com/java-list-directory-files

        File[] files = new File(packagePath).listFiles();

        //Liste des fichiers .java
        List<String> fileList =  Arrays.stream(files).distinct()
                .filter(file -> !file.isDirectory() && file.getName().endsWith(".java"))
                .map(File::getName)
                .map(fName->packagePath + "/" + fName)
                .collect(Collectors.toList());

        //Liste des sous-dossiers
        List<String> dirList =  Arrays.stream(files).distinct()
                .filter(file -> file.isDirectory())
                .map(File::getName)
                .map(fName->packagePath + "/" + fName)
                .collect(Collectors.toList());

        //Obtenir les fichiers .java des sous-dossiers
        if(dirList.size() > 0){
            for (String dir : dirList) {
                fileList.addAll(getJavaFilesInPackage(dir));
            }
        }

        return fileList;
    }
}
