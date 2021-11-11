package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.http.controller.*;
import no.kristiania.http.controller.v2.AdvancedQuestionController;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final int port;
    private ServerSocket serverSocket;
    private final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final ExecutorService threadPool;
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
                logger.info("*** Accepted a client connection! ***");
                this.threadPool.execute(() -> session(clientSocket));
                // TODO: håndtere feil i router, skrive ut feilmeldinger til nettleser
            } catch (IOException e) {
                logger.error("*** I/O ERROR: Connection to client failed! ***");
                logger.error(e.getMessage());
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
            logger.error("*** ERROR: Failed to read from client socket! ***");
            logger.error(e.getMessage());
        }
    }

    private void configureRouter(Router router) {
        router.addController("/api/v2/question", new AdvancedQuestionController(new QuestionDao(createDataSource()), new RangeQuestionDao(createDataSource()), new RadioQuestionDao(createDataSource())));
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
        router.addController("/api/answerOption", new AnswerOptionController(new RangeQuestionDao(createDataSource())));
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
