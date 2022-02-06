import metrics.CsvMetrics;
import metrics.ProjectMetrics;
import properties.ProjectProperties;

import java.io.IOException;

/**
 * Classe principale de l'application qui réunira toutes
 * les composantes de celles-ci et les objets. Contient
 * également la classe statique.
 * @author Pascal St-Amour
 * @author Vincent Falardeau
 */

public class Metrics {

    // Chemin vers le fichier config.properties (unique valeur magique)
    private static final String CONFIG_FILE = "config.properties";

    public static void main(String[] args) {

        try {

            // Path du paquet cible
            String pkgLocation = args[0];

            // Objet classe ProjectProperties
            ProjectProperties projectProperties = new ProjectProperties(CONFIG_FILE);

            // Objet principal, contient les metrics des classes et sous-paquet
            ProjectMetrics projectMetrics = new ProjectMetrics(pkgLocation, projectProperties);

            CsvMetrics csvMetrics = new CsvMetrics(projectMetrics, projectProperties);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
