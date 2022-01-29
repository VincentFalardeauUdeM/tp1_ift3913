package main.java;

public class Main {

    public ProjectProperties pp;

    public Main(){
        pp = new ProjectProperties();
    }

    public static void main(String[] args) {

        // TODO classe ou dossier
        // TODO get tout les path des fichiers java dans ce projet

        Main m = new Main();
        ClassMetrics cm = new ClassMetrics("src/main/resources/test");
        cm.readJavaFile();

    }
}
