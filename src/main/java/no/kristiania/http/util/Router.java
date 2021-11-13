package no.kristiania.http.util;

import no.kristiania.http.controller.FileController;
import no.kristiania.http.controller.HttpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Base64;
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
            if (message.getFileTarget().equals("/")) redirect("/index.html");

            // TODO: Refactor to byte-controller or whatever
            if (message.getFileTarget().endsWith(".png") || message.getFileTarget().endsWith(".jpg") || message.getFileTarget().endsWith(".ico") ) {
                logger.info(message.getFileTarget());
                InputStream fileStream = getClass().getResourceAsStream(message.getFileTarget());
                HttpResponse response = new HttpResponse(200, "OK");
                response.setHeaderField("Connection", "Close");
                response.setHeaderField("Content-Type", "image/x-icon");
                response.writeBytes(clientSocket,fileStream);
            }
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
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.useRedirectParams(location);
        httpResponse.write(clientSocket);
    }
    private void writeServerError() {
        try {
            HttpResponse response = new HttpResponse(500, "Internal server error");
            response.useServerErrorParams();
            response.write(clientSocket);
        } catch (IOException e) {
            logger.error("*** Failed to write error message to user! ***");
            logger.error(e.getMessage());
        }
    }

    public void addController(HttpController controller) {
        controllers.put(controller.getPath(),controller);
    }


}
