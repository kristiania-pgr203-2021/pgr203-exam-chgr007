package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.http.controller.*;
import no.kristiania.http.model.Product;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.Router;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public HttpServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        new Thread(this::handleClient).start();
    }

    private void handleClient() {
        logger.info("Server running at http://localhost:{}/", getPort());
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                HttpRequest message = new HttpRequest(clientSocket);
                Router router = new Router(clientSocket);
                router.addController("/api/question", new QuestionController(new QuestionDao(createDataSource())));
                router.addController("/api/questionnaires", new QuestionnaireController(new QuestionnaireDao(createDataSource())));
                router.addController("/api/newQuestionnaire", new QuestionnaireController(new QuestionnaireDao(createDataSource())));
                router.addController("/api/questionnaireName", new QuestionnaireController(new QuestionnaireDao(createDataSource())));
                router.addController("/api/questionnaire", new QuestionnaireController(new QuestionnaireDao(createDataSource()), new QuestionDao(createDataSource())));
                router.addController("/api/login", new LoginController(new UserDao(createDataSource())));
                router.addController("/api/signup", new SignupController(new UserDao(createDataSource())));
                router.addController("/api/newQuestion", new QuestionController(new QuestionDao(createDataSource())));
                router.addController("/api/question", new QuestionController(new QuestionDao(createDataSource()), new AnswerDao(createDataSource())));
                router.addController("/api/questionName", new QuestionController(new QuestionDao(createDataSource())));
                router.addController("/api/newAnswer", new AnswerController(new AnswerDao(createDataSource())));
                router.addController("/api/answerOption", new AnswerOptionController(new AnswerOptionDao(createDataSource())));
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
