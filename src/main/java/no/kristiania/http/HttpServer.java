package no.kristiania.http;

import no.kristiania.http.dao.*;
import no.kristiania.http.controller.*;
import no.kristiania.http.controller.v2.AdvancedQuestionController;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    public static boolean DEVELOPMENT = true;
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
        router.addController(new AdvancedQuestionController(new QuestionDao(dataSource), new RangeQuestionDao(dataSource), new RadioQuestionDao(dataSource), new TextQuestionDao(dataSource)));
        router.addController(new QuestionnairesController(new QuestionnaireDao(dataSource)));
        router.addController(new NewQuestionnaireController(new QuestionnaireDao(dataSource)));
        router.addController(new QuestionnaireNameController(new QuestionnaireDao(dataSource)));
        router.addController(new QuestionnaireController(new QuestionnaireDao(dataSource), new QuestionDao(dataSource), new AnswerDao(dataSource)));
        router.addController(new LoginController(new UserDao(dataSource)));
        router.addController(new SignupController(new UserDao(dataSource)));
        router.addController(new QuestionController(new QuestionDao(dataSource)));
        router.addController(new QuestionAnswersController(new QuestionDao(dataSource), new AnswerDao(dataSource)));
        router.addController(new QuestionNameController(new QuestionDao(dataSource)));
        router.addController(new AnswerController(new AnswerDao(dataSource), new QuestionDao(dataSource), new RangeQuestionDao(dataSource), new RadioQuestionDao(dataSource), new TextQuestionDao(dataSource)));
    }




    public int getPort() {
        return serverSocket.getLocalPort();
    }

}
