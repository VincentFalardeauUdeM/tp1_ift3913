package main.java;

import main.java.metrics.ClassMetrics;
import main.java.metrics.PackageMetrics;
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

            PackageMetrics pkgMetrics = new PackageMetrics(pkgLocation, projectProperties);
            System.out.println(pkgMetrics);//TODO output to csv

            List<ClassMetrics> classMetricsList = pkgMetrics.getClassMetricsList();
            for(ClassMetrics classMetrics: classMetricsList){
                System.out.println(classMetrics);//TODO output to csv
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
