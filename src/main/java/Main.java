package main.java;

import main.java.metrics.ClassMetrics;
import main.java.metrics.PackageMetrics;
import main.java.metrics.ProjectMetrics;
import main.java.properties.ProjectProperties;

import java.io.IOException;
import java.util.List;

public class Main {

    //La seule valeur magique du projet ;)
    //TODO changer pour le jar
    private static final String CONFIG_FILE = "src/main/resources/config.properties";

    public static void main(String[] args) {

        try {
            String pkgLocation = args[0];
            ProjectProperties projectProperties = new ProjectProperties(CONFIG_FILE);

            ProjectMetrics projectMetrics = new ProjectMetrics(pkgLocation, projectProperties);
            //TODO output to csv
            int i = 0;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
