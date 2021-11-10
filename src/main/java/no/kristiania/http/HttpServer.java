package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.http.controller.*;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.Properties;
import no.kristiania.http.util.Router;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private int port;
    private ServerSocket serverSocket;
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private ExecutorService threadPool;
    private Thread runningThread = null;
    public HttpServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
         threadPool = Executors.newFixedThreadPool(20);
        //TODO: add thread pool
        new Thread(this::handleClient).start();
    }

    private void handleClient() {
        // Thread pool http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html
        logger.info("Server running at http://localhost:{}/", getPort());
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                this.threadPool.execute(() -> session(clientSocket));
                // TODO: håndtere feil i router, skrive ut feilmeldinger til nettleser
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void session(Socket clientSocket)  {
        synchronized(this) {
            this.runningThread = Thread.currentThread();
        }
        try {
            HttpRequest message = new HttpRequest(clientSocket);
            Router router = new Router(clientSocket);
            configureRouter(router);
            router.route(message);
        } catch (IOException e) {
            logger.error("*** I/O ERROR: Connection to client failed! ***");
            logger.error(e.getMessage());
        }
    }

    private void configureRouter(Router router) {
        router.addController("/api/question", new QuestionController(new QuestionDao(createDataSource())));
        router.addController("/api/questionnaires", new QuestionnairesController(new QuestionnaireDao(createDataSource())));
        router.addController("/api/newQuestionnaire", new NewQuestionnaireController(new QuestionnaireDao(createDataSource())));
        router.addController("/api/questionnaireName", new QuestionnaireNameController(new QuestionnaireDao(createDataSource())));
        router.addController("/api/questionnaire", new QuestionnaireController(new QuestionnaireDao(createDataSource()), new QuestionDao(createDataSource())));
        router.addController("/api/login", new LoginController(new UserDao(createDataSource())));
        router.addController("/api/signup", new SignupController(new UserDao(createDataSource())));
        router.addController("/api/newQuestion", new QuestionController(new QuestionDao(createDataSource())));
        router.addController("/api/question", new QuestionController(new QuestionDao(createDataSource()), new AnswerDao(createDataSource())));
        router.addController("/api/questionName", new QuestionNameController(new QuestionDao(createDataSource())));
        router.addController("/api/newAnswer", new AnswerController(new AnswerDao(createDataSource())));
        router.addController("/api/answerOption", new AnswerOptionController(new AnswerOptionDao(createDataSource())));
    }

    // TODO: Kanskje flytte datasource og properties ut til en egen klasse
    private DataSource createDataSource() {
        Properties prop = new Properties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("dataSource.username"));
        dataSource.setPassword(prop.getProperty("dataSource.password"));
        dataSource.setURL(prop.getProperty("dataSource.url"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }


    public int getPort() {
        return serverSocket.getLocalPort();
    }

}
