package main.java;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class ProjectProperties {

    // Chemin relatif du fichier propreties
    private final String CONFIG = "src/main/resources/config.properties";


    /**
     * Récupère la valeur d'une propriété depuis le fichier
     * config.properties
     * @param propertyName nom propriété
     * @return valeur propriété
     */

    public String getProperty(String propertyName) {

        // Parse le fichier de config
        try (InputStream input = new BufferedInputStream(new FileInputStream(CONFIG))) {

            // Création objet Properties
            Properties prop = new Properties();

            // Load le properties file dans notre objet
            prop.load(input);

            // Retourn la valeur associée à propertyName
            return prop.getProperty(propertyName);

        } catch (IOException ex) {
            return ex.getMessage();
        }
    }

}
