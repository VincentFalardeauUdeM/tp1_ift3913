package util;

import java.io.File;


/**
 * Classe auxillaire/util de l'application
 * @author Pascal St-Amour
 * @author Vincent Falardeau
 */
public class Util {


    /**
     * Métohode afin de joindre les chemins de 2 fichiers
     * @param path1
     * @param path2
     * @source https://stackoverflow.com/questions/412380/how-to-combine-paths-in-java
     * @return
     */
    public static String joinPaths(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }
}
