package metrics;
import properties.ProjectProperties;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


/**
 * Classe auxillaire prenant en entré les métrics des classes et
 * paquet et qui créera des fichiers csv avec ceux-ci.
 * @author Pascal St-Amour
 * @author Vincent Falardeau
 */

public class CsvMetrics {

    private final ProjectProperties p;
    private final ProjectMetrics pm;

    /**
     * Constructeur de la classe, intialise les objets contenus
     * dans celle-ci ainsi que la création des fichiers csv.
     * @param projectProperties
     */

    public CsvMetrics(ProjectMetrics projetMetrics, ProjectProperties projectProperties) throws FileNotFoundException {
        this.p = projectProperties;
        this.pm = projetMetrics;
        writePackageCvs();
        writeClassCvs();
    }


    /**
     * Méthode s'occupant du formattage et de la sortie des
     * metrics des paquets en ficher csv.
     * @source https://www.baeldung.com/java-csv
     * @return void
     */

    public void writePackageCvs() {

        String fileName = p.get("packageCsvFilename");
        String header = p.get("packageCsvFirstLine");

        try (PrintWriter writer = new PrintWriter(fileName)) {

            StringBuilder sb = new StringBuilder();
            sb.append(header);

            for (PackageMetrics pm : pm.getPackageMetricsList()) {
                sb.append(pm);
            }
            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Méthode s'occupant du formattage et de la sortie des
     * metrics des classes en ficher csv.
     * @source https://www.baeldung.com/java-csv
     * @return void
     */

    public void writeClassCvs() {

        String fileName = p.get("classCsvFilename");
        String header = p.get("classCsvFirstLine");

        try (PrintWriter writer = new PrintWriter(fileName)) {

            StringBuilder sb = new StringBuilder();
            sb.append(header);

            for (PackageMetrics pkg : pm.getPackageMetricsList()) {
                for (ClassMetrics cm:  pkg.getClassMetricsList()) {
                    sb.append(cm);
                }
            }
            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}
