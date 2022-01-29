package main.java;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe responsable de calculer les métriques associées
 * à un fichier donné en entré. Chaque méthode représentera
 * une métrique exigée.
 * @author Pascal St-Amour
 * @author Vincent Falardeau
 */

public class ClassMetrics {


    private List<String> lines;

    /**
     * Constructeur de ClassMetrics
     * @param classPath chemin absolu de la classe
     */

    public ClassMetrics(String classPath) throws IOException {

        this.lines = Files.readAllLines(Paths.get(classPath),  StandardCharsets.UTF_8)
                .stream()
                .filter(line -> line.trim().length() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Récupère le total de ligne du fichier java source en
     * comprenant les commentaires et excluant les lignes vides.
     * @return total de ligne
     */

    public int classe_CLOC() {
        return lines.size();
    }

    /**
     * Récupère le total de ligne comportant des commentaires du
     * fichier java source et excluant les lignes vides.
     * @return total de ligne comportant des commentaires
     */

    public int classe_LOC() {

        int nbDeLignesDeCommentaire = 0;
        boolean inBlocComment = false; // Switch pour savoir quand on est dans un bloc /*  ... */ ou pas

        for (String line : lines) {

            // Commentaires de type //
            if(line.contains("//") && !inBlocComment && !line.contains("/*")) {
                nbDeLignesDeCommentaire++;
            }
            // Cas ou un bloc /*  ... */ s'ouvre et se referme sur la même ligne
            else if(line.contains("/*") && line.contains("*/") && !inBlocComment){
                nbDeLignesDeCommentaire++;
            }
            else{
                // Bloc /*  ... */ sur plusieurs lignes
                if(line.contains("/*") && inBlocComment == false) inBlocComment = true;
                if(inBlocComment == true) nbDeLignesDeCommentaire++;
                if(inBlocComment == true && line.contains("*/")) inBlocComment = false;
            }
        }

        return nbDeLignesDeCommentaire;
    }

    /**
     * Calcul de la densité des commentaires i.e. nombre de
     * commentaires par ligne de code.
     * @return densité des commentaires
     */

    public float classe_DC() {
        return this.classe_CLOC() / this.classe_LOC();
    }
}

