package no.kristiania.http.util;

import java.io.IOException;
import java.io.InputStream;

public class Properties {

    public String getProperty(String key) {
        java.util.Properties prop = getPropertiesObj();
        return prop.getProperty(key);
    }

    private java.util.Properties getPropertiesObj() {
        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("pgr203.properties")) {
            java.util.Properties properties = new java.util.Properties();
            properties.load(resourceAsStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
