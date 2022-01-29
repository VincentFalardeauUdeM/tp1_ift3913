package main.java;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectProperties {

    private final String CONFIG = "config.properties";

    //https://mkyong.com/java/java-properties-file-examples/
    public String getProperty(String propertyName){
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(CONFIG)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            return prop.getProperty(propertyName);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "not found";
    }
}
