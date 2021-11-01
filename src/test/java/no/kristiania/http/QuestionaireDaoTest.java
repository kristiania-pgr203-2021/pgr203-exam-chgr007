package no.kristiania.http;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionaireDaoTest {


    @Test
    void getPropertiesWorksWithResourcesFile() {
        Properties props = getProperties();
        assertEquals("test", props.getProperty("test"));
    }
    //used for internal databases
    private DataSource createDataSource() {

        Properties prop = getProperties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("username"));
        dataSource.setPassword(prop.getProperty("password"));
        dataSource.setURL(prop.getProperty("URL"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        return dataSource;
    }

    private Properties getProperties() {
        Properties properties = new Properties();

        try (InputStream fileReader = getClass().getClassLoader().getResourceAsStream("pgr203.properties")) {
            properties.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
