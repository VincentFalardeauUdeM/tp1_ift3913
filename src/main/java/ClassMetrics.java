package main.java;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Classe responsable de calculer les métriques associées
 * à un fichier donné en entré. Chaque méthode représentera
 * une métrique exigée.
 * @author Pascal
 * @author Vincent
 */

public class ClassMetrics {


    public String classPath;


    /**
     * Constructeur de ClassMetrics
     * @param classPath chemin absolu de la classe
     */

    public ClassMetrics(String classPath) {
        this.classPath = classPath;
    }


    public void readJavaFile() {

        String content = Files.readString(Paths.get(classPath));
        System.out.println(classPath);

    }



}
