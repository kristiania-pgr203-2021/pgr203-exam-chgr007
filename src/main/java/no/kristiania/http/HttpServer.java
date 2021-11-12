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
    private DataSource dataSource;
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final ExecutorService threadPool;
    private Thread runningThread = null;
    public HttpServer(int port, DataSource dataSource) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.dataSource = dataSource;
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

                logger.info("*** Accepted a client connection from: {} at port: {}! ***",clientSocket.getInetAddress().getHostName(),clientSocket.getPort());
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
        router.addController("/api/v2/question", new AdvancedQuestionController(new QuestionDao(dataSource), new RangeQuestionDao(dataSource), new RadioQuestionDao(dataSource), new TextQuestionDao(dataSource)));
        //router.addController("/api/question", new QuestionController(new QuestionDao(dataSource)));
        router.addController("/api/questionnaires", new QuestionnairesController(new QuestionnaireDao(dataSource)));
        router.addController("/api/newQuestionnaire", new NewQuestionnaireController(new QuestionnaireDao(dataSource)));
        router.addController("/api/questionnaireName", new QuestionnaireNameController(new QuestionnaireDao(dataSource)));
        router.addController("/api/questionnaire", new QuestionnaireController(new QuestionnaireDao(dataSource), new QuestionDao(dataSource)));
        router.addController("/api/login", new LoginController(new UserDao(dataSource)));
        router.addController("/api/signup", new SignupController(new UserDao(dataSource)));
        router.addController("/api/newQuestion", new QuestionController(new QuestionDao(dataSource)));
        router.addController("/api/question", new QuestionController(new QuestionDao(dataSource), new AnswerDao(dataSource)));
        router.addController("/api/questionName", new QuestionNameController(new QuestionDao(dataSource)));
        router.addController("/api/newAnswer", new AnswerController(new AnswerDao(dataSource)));
        router.addController("/api/answerOption", new AnswerOptionController(new RangeQuestionDao(dataSource)));
    }




    public int getPort() {
        return serverSocket.getLocalPort();
    }

}
