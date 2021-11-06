package no.kristiania.http;

import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.QuestionnaireDao;
import no.kristiania.http.controller.QuestionController;
import no.kristiania.http.controller.QuestionnaireController;
import no.kristiania.http.model.Product;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.Router;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HttpServer {

    private int port;
    private ServerSocket serverSocket;
    private List<Product> products;

    public HttpServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        new Thread(this::handleClient).start();
    }

    private void handleClient() {
        while(true){
            try{
                System.out.printf("Server running at port: %s%n", getPort());
                Socket clientSocket = serverSocket.accept();
                HttpRequest message = new HttpRequest(clientSocket);
                Router router = new Router(clientSocket);
                router.addController("/api/question", new QuestionController(new QuestionDao(createDataSource(), "question")));
                router.addController("/api/questionnaires", new QuestionnaireController(new QuestionnaireDao(createDataSource(), "questionnaire")));
                router.route(message);
                // TODO: håndtere feil i router, skrive ut feilmeldinger til nettleser
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO: Kanskje flytte datasource og properties ut til en egen klasse
    private DataSource createDataSource() {
        Properties prop = getProperties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("dataSource.username"));
        dataSource.setPassword(prop.getProperty("dataSource.password"));
        dataSource.setURL(prop.getProperty("dataSource.url"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
    private Properties getProperties() {
        Properties properties = new Properties();

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("pgr203.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

}
