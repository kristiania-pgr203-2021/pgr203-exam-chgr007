package no.kristiania.http.util;

import no.kristiania.http.controller.FileController;
import no.kristiania.http.controller.HttpController;
import no.kristiania.http.model.Product;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Router {
    private final Socket clientSocket;
    private final List<Product> products;
    private Map<String,String> values;
    private Map<String, HttpController> controllers;
    public Router(Socket clientSocket, List<Product> products) {
        this.clientSocket = clientSocket;
        this.products = products;
        controllers = new HashMap<>();
    }

    public void route(HttpRequest message, Path rootDirectory) throws IOException, SQLException {

        FileController fileController = new FileController();
        if (controllers.containsKey(message.getFileTarget())) {
            controllers
                    .get(message.getFileTarget())
                    .handle(message)
                    .write(clientSocket);
        } else {
            fileController.handle(message).write(clientSocket);
        }
//        switch (message.getFileTarget()) {
//            case "/api/newProduct":
//                values = message.getPostParams();
//                redirect("/listProducts.html");
//                break;
//            case "/":
//                redirect("/index.html");
//                break;
//            case "/api/products":
//                fileController.handle(message);
//                break;
//            case "/api/categoryOptions":
//                fileController.handle(message);
//                break;
//            default:
//
//                break;
//        }
        clientSocket.close();
    }

    private void redirect(String location) throws IOException {
        String responseMessage = "HTTP/1.1 303 See Other\r\n" +
                "Location: " + location + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String,String> getProducts() {
        return values;
    }

    public String printProducts() {
      
        StringBuilder string = new StringBuilder();
        String[] categories = getCategories();
      
        string.append("<table>");
        string.append("<tr>");

        for (String category : categories) {
            string.append(String.format("<th>%s</th>",category));
        }

        string.append("</tr>");
        string.append("<tr>");
        for (String category : categories) {
            string.append(String.format("<td>%s</td>",printProductsByCategory(category)));
        }
        string.append("</tr>");

        return string.toString();
    }

    public void addController(String path, HttpController controller) {
        controllers.put(path,controller);
    }

    private String printProductsByCategory(String category) {
      
        StringBuilder string = new StringBuilder();

        for(Product p : products){
            if(p.getCategory().equals(category)){
                string.append("<li>");
                string.append(p.getName());
                string.append("</li>");
            }
        }
        return string.toString();
    }

    private String printCategories() {


        StringBuilder string = new StringBuilder();

        String[] categories = getCategories();

        for(String category : categories){
            string.append("<option>");
            string.append(category);
            string.append("</option>");
        }

        return string.toString();
    }

    private String[] getCategories() {
        return new String[]{"Katt", "Hund", "Hest", "Slange"};
    }

}
