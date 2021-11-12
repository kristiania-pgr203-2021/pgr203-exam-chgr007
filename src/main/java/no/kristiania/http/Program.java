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
        int port = getPort(args);
        HttpServer server = new HttpServer(port, createDataSource());
    }

    private static int getPort(String[] args) {
        int port = 8080;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--port")) {
                try {
                    port = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    logger.error("Port must be a number!");
                }
            }
        }
        return port;
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
