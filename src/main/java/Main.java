package main.java;

import java.io.IOException;

public class Main {

    public ProjectProperties pp;

    public Main(){
        pp = new ProjectProperties();
    }

    public static void main(String[] args) {

        Main m = new Main();

        try {
            ClassMetrics cm = new ClassMetrics("src/main/resources/Test.java");
            System.out.println(cm.classe_CLOC());
            System.out.println(cm.classe_LOC());
            System.out.println(cm.classe_DC());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            PackageMetrics pm = new PackageMetrics("src/main/resources");
            System.out.println(pm.paquet_CLOC());
            System.out.println(pm.paquet_LOC());
            System.out.println(pm.paquet_DC());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
