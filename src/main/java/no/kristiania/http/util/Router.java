package no.kristiania.http.util;

import no.kristiania.http.controller.FileController;
import no.kristiania.http.controller.HttpController;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Socket clientSocket;
    private Map<String, HttpController> controllers;
    public Router(Socket clientSocket) {
        this.clientSocket = clientSocket;
        controllers = new HashMap<>();
    }

    public void route(HttpRequest message) throws IOException, SQLException {
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
    }

    private void redirect(String location) throws IOException {
        String responseMessage = "HTTP/1.1 303 See Other\r\n" +
                "Location: " + location + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
    }

    public void addController(String path, HttpController controller) {
        controllers.put(path,controller);
    }


}
