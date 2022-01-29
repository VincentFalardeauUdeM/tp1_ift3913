package main.java;

public class Main {

    public ProjectProperties pp;

    public Main(){
        pp = new ProjectProperties();
    }

    public static void main(String[] args) {
        String path = "../resources/jfreechart-master";

        //TODO classe ou dossier

        //TODO get tout les path des fichiers java dans ce projet
        //Prendre chaque fichier, et faire les metriques
        Main m = new Main();
        System.out.println(m.pp.getProperty("db.user"));
    }
}
