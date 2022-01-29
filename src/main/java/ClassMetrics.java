package main.java;

/**
 * Classe responsable de calculer les métriques associées
 * à un fichier donné en entré. Chaque méthode représentera
 * une métrique exigée.
 * @author Pascal
 * @author Vincent
 */

public class ClassMetrics {

    // Chemin absolu de la classe cible
    public String classPath;


    /**
     * Constructeur de ClassMetrics
     * @param classPath chemin absolu de la classe
     */

    public ClassMetrics(String classPath) {
        this.classPath = classPath;
    }
}
