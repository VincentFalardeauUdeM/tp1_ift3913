package main.java.properties;

import java.io.*;
import java.util.Properties;

/**
 * Accès au fichier de propriétés
 * @author Pascal St-Amour
 * @author Vincent Falardeau
 */
public class ProjectProperties {

    private Properties properties;

    public ProjectProperties(String configFile) throws IOException {
        this.properties = loadProperties(configFile);
    }

    /**
     * Récupère la valeur d'une propriété depuis le fichier propriété
     * @param propertyName nom propriété
     * @return valeur propriété
     */
    public String get(String propertyName) {
        return this.properties.getProperty(propertyName);
    }

    //Src: https://mkyong.com/java/java-properties-file-examples/
    private Properties loadProperties(String configFile) throws IOException {

        InputStream input = new BufferedInputStream(new FileInputStream(configFile));
        Properties prop = new Properties();
        prop.load(input);
        return prop;
    }

}
