package properties;

import java.io.*;
import java.util.Properties;

/**
 * Accès au fichier de propriétés
 * @author Pascal St-Amour
 * @author Vincent Falardeau
 */
public class ProjectProperties {


    private Properties properties;


    /**
     * Constructeur de la classe
     * @param configFile path vers fichier de config
     * @throws IOException
     */
    public ProjectProperties(String configFile) throws IOException {
        this.properties = loadProperties(configFile);
    }


    /**
     * Récupère la valeur d'une propriété depuis le fichier propriété
     * @param propertyName nom propriété
     * @source: https://mkyong.com/java/java-properties-file-examples/
     * @source: https://stackoverflow.com/questions/6068197/utils-to-read-resource-text-file-to-string-java
     * @return valeur propriété
     */
    public String get(String propertyName) {
        return this.properties.getProperty(propertyName);
    }


    /**
     * Méthode afin de loader les propriétés du projet
     * @param configFile path vers fichier de config
     * @return les propriétés du fichier
     * @throws IOException
     */
    private Properties loadProperties(String configFile) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream input = classLoader.getResourceAsStream(configFile);
        Properties prop = new Properties();
        prop.load(input);
        return prop;
    }

}
