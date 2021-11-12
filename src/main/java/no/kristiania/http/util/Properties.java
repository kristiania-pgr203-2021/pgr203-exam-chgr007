package no.kristiania.http.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Properties {
    private static final Logger logger = LoggerFactory.getLogger(Properties.class);
    public String getProperty(String key) {
        java.util.Properties prop = getPropertiesObj();
        return prop.getProperty(key);
    }

    private java.util.Properties getPropertiesObj() {
        //TODO: Legge til working directory (FileReader reader = new FileReader("pgr203.properties"))
        Path workingDir = Paths.get("").toAbsolutePath();
        logger.info("Loading properties file from: {}",workingDir);
        try (FileReader reader = new FileReader(workingDir+"/pgr203.properties")) {
            java.util.Properties properties = new java.util.Properties();
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            logger.error("Failed to load properties file! Create one in your current working directory, or feel the wrath of JVM");
            logger.error(e.getMessage());
        }
        return null;
    }
}
