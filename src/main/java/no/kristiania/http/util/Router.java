package no.kristiania.http.util;

import no.kristiania.http.controller.FileController;
import no.kristiania.http.controller.HttpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Socket clientSocket;
    private final Map<String, HttpController> controllers;
    private static final Logger logger = LoggerFactory.getLogger(Router.class);
    public Router(Socket clientSocket) {
        this.clientSocket = clientSocket;
        controllers = new HashMap<>();
    }

    public void route(HttpRequest message) {

        try {
            if (message.getFileTarget().equals("/")) redirect("index.html");

            FileController fileController = new FileController();
            if (controllers.containsKey(message.getFileTarget())) {
                controllers
                        .get(message.getFileTarget())
                        .handle(message)
                        .write(clientSocket);
            } else {
                fileController.handle(message).write(clientSocket);
            }
            clientSocket.close();
        } catch (SQLException e) {
            writeServerError();
            logger.error("*** SQL ERROR ***");
            logger.error(e.getMessage());

        } catch (IOException e) {
            writeServerError();
            logger.error("*** I/O ERROR ***");
            logger.error(e.getMessage());
        }
    }


    private void redirect(String location) throws IOException {
        String responseMessage = "HTTP/1.1 303 See Other\r\n" +
                "Location: " + location + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
    }
    private void writeServerError() {
        try {
            String message = "<p>OooooooOOoooOOooOOps. Something went really wrong!</p>";

            HttpResponse response = new HttpResponse(500, "Internal server error");
            response.setHeaderField("Connection", "Close");
            response.setHeaderField("Content-Type", "text/html");
            response.setHeaderField("Content-Length", String.valueOf(message.getBytes(StandardCharsets.UTF_8).length));
            response.setMessageBody(message);
            response.write(clientSocket);
        } catch (IOException e) {
            logger.error("*** Failed to write error message to user! ***");
            logger.error(e.getMessage());
        }
    }

    public void addController(String path, HttpController controller) {
        controllers.put(path,controller);
    }


}
