package main.java.metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMetrics {


//    public ProjectMetrics(){
//
//    }
//
//
//
//    //TODO detect packages
//    //TODO build list of packagesMetrics
//    //TODO create method to get all classeMetrics in the projet, across all the packages
//
//    /**
//     * Récupère récursivement les chemins des fichiers .java dans le package donné
//     * @param packagePath chemin du package
//     * @return ensemble contenant les chemins des fichiers .java dans le package
//     */
//    private List<String> getJavaFilesInPackage(String packagePath){
//
//
//
//        File[] files = new File(packagePath).listFiles();
//        List<String> fileList =  getJavaFilesFromFiles(files, packagePath);
//        List<String> dirList = getSubFoldersFromFiles(files, packagePath);
//
//        //Obtenir les fichiers .java des sous-dossiers
//        if(dirList.size() > 0){
//            for (String dir : dirList) {
//                fileList.addAll(getJavaFilesInPackage(dir));
//            }
//        }
//
//        return fileList;
//    }
//
//    //Liste des fichiers .java
//    //Source: https://www.baeldung.com/java-list-directory-files
//    private List<String> getJavaFilesFromFiles(File[] files, String root){
//        return Arrays.stream(files).distinct()
//                .filter(file -> !file.isDirectory() && file.getName().endsWith(".java"))
//                .map(File::getName)
//                .map(fName->root + "/" + fName)
//                .collect(Collectors.toList());
//    }
//
//    //Liste des sous-dossiers
//    //Source: https://www.baeldung.com/java-list-directory-files
//    private List<String> getSubFoldersFromFiles(File[] files, String root){
//        return Arrays.stream(files).distinct()
//                .filter(file -> file.isDirectory())
//                .map(File::getName)
//                .map(fName->root + "/" + fName)
//                .collect(Collectors.toList());
//    }
//
//    private List<ClassMetrics> getClassMetricsFromPackage(String packagePath) throws IOException {
//        List<ClassMetrics> classMetricsList = new ArrayList<ClassMetrics>();
//        List<String> files = getJavaFilesInPackage(packagePath);
//        for(String file: files){
//            classMetricsList.add(new ClassMetrics(file, p));
//        }
//        return classMetricsList;
//    }
}
