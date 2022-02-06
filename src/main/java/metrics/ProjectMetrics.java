package metrics;
import properties.ProjectProperties;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Classe responsable de coordoner globalement les métrics des
 * classes et projets. Récupère tous les métrics de chaque classes
 * contenus dans chaque sous paquet du paquet root.
 * @author Pascal St-Amour
 * @author Vincent Falardeau
 */
public class ProjectMetrics {


    // Objet classe ProjectProperties
    private final ProjectProperties p;

    // Liste des métrics de chaque classes du paquet et sous-paquet
    private List<PackageMetrics> packageMetricsList;


    /**
     * Constructeur de classe, initialise les objets contenus dans
     * celle-ci.
     * @param root paquet source
     * @param projectProperties propriétés du projet
     * @throws IOException
     */
    public ProjectMetrics(String root, ProjectProperties projectProperties) throws IOException {
        this.p = projectProperties;
        this.packageMetricsList = getPackageMetricsFromRoot(root);
    }


    /**
     * Récupère récursivement à partir du projet root chaque
     * sous projet et créé des objets packageMetrics pour chacun.
     * @param root paquet source
     * @return liste des objets packageMetrics
     * @throws IOException
     */
    private List<PackageMetrics> getPackageMetricsFromRoot(String root) throws IOException {

        List<PackageMetrics> packageMetricsList = new ArrayList<PackageMetrics>();
        File[] files = new File(root).listFiles();

        // Liste des sous-paquet
        List<String> dirList = getSubFoldersFromFiles(files, root);

        // Création nouvel objet packageMetrics et ajoût liste
        PackageMetrics packageMetrics = new PackageMetrics(root, p);
        packageMetricsList.add(packageMetrics);

        if(dirList.size() > 0){
            for(String dir : dirList){

                // Appel récursif pour chaque sous paquet
                packageMetricsList.addAll(getPackageMetricsFromRoot(dir));
            }
        }

        return packageMetricsList;
    }


    /**
     * Méthode auxilliaire récupérant tous les sous-paquets
     * du paquet passé en argument.
     * @param files sous fichiers du paquet courant
     * @param root paquet source
     * @source https://www.baeldung.com/java-list-directory-files
     * @return sous paquet et sous-fichiers
     */
    private List<String> getSubFoldersFromFiles(File[] files, String root){
        return Arrays.stream(files).distinct()
                .filter(file -> file.isDirectory())
                .map(File::getName)
                .map(dName-> Util.joinPaths(root, dName))
                .collect(Collectors.toList());
    }


    /**
     * Getter de tous les métrics du paquet
     * @return metrics paquet
     */
    public List<PackageMetrics> getPackageMetricsList() {
        return packageMetricsList;
    }

}
