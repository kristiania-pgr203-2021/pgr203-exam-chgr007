package no.kristiania.http;

import no.kristiania.http.util.Properties;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Program {
    private static final Logger logger = LoggerFactory.getLogger(Program.class);
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080, createDataSource());

    }
    // TODO: Kanskje flytte datasource og properties ut til en egen klasse
    private static DataSource createDataSource() {
        Properties prop = new Properties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("dataSource.username"));
        dataSource.setPassword(prop.getProperty("dataSource.password"));
        dataSource.setURL(prop.getProperty("dataSource.url"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
