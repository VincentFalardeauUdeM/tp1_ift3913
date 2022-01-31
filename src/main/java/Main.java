package main.java;

import main.java.metrics.ClassMetrics;
import main.java.metrics.PackageMetrics;

import java.io.IOException;

public class Main {

    public ProjectProperties pp;

    public Main(){
        pp = new ProjectProperties();
    }

    public static void main(String[] args) {

        String file = args[0] + "/src/main/java/org/jfree/chart/annotations/AbstractAnnotation.java";

        try {
            ClassMetrics cm = new ClassMetrics(file);
            System.out.println(cm.classe_CLOC());
            System.out.println(cm.classe_LOC());
            System.out.println(cm.classe_DC());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Main m = new Main();
//

//
//        try {
//            PackageMetrics pm = new PackageMetrics("src/main/resources");
//            System.out.println(pm.paquet_CLOC());
//            System.out.println(pm.paquet_LOC());
//            System.out.println(pm.paquet_DC());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
