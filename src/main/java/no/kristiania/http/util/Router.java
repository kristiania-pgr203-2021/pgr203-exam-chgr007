package no.kristiania.http.util;

import no.kristiania.http.model.Product;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Router {
    private final Socket clientSocket;
    private final List<Product> products;
    private Map<String,String> values;

    public Router(Socket clientSocket, List<Product> products) {
        this.clientSocket = clientSocket;
        this.products = products;
    }

    public void route(HttpRequest message, Path rootDirectory) throws IOException {

        switch (message.getFileTarget()) {
            case "/api/newProduct":
                values = message.getPostParams();
                redirect("/listProducts.html");
                break;
            case "/":
                redirect("/index.html");
                break;
            case "/api/products":
                writeOkResponse(printProducts(), "/listProducts.html");
                break;
            case "/api/categoryOptions":
                writeOkResponse(printCategories(), "/newProduct.html");
                break;
            default:
                if (rootDirectory != null && Files.exists(rootDirectory.resolve(message.getFileTarget().substring(1)))) {
                    String responseTxt = Files.readString(rootDirectory.resolve(message.getFileTarget().substring(1)));
                    writeOkResponse(responseTxt, message.getFileTarget());
                } else {
                    writeNotFoundResponse(message);
                }
                break;
        }
        clientSocket.close();
    }

    private void writeNotFoundResponse(HttpRequest message) throws IOException {
        HttpResponse httpResponse = new HttpResponse(404, "File not found");
        httpResponse.setHeaderField("Connection","close");
        httpResponse.write(clientSocket);
//        String responseText = "File not found: " + message.getFileTarget();
//        String response = "HTTP/1.1 404 Not found\r\n" +
//                "Content-Length: " + responseText.getBytes(StandardCharsets.UTF_8).length+ "\r\n" +
//                "Connection: close\r\n" +
//                "\r\n" +
//                responseText;
//        clientSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
    }

    private void redirect(String location) throws IOException {
        String responseMessage = "HTTP/1.1 303 See Other\r\n" +
                "Location: " + location + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
    }

    private String getContentType(String fileTarget) {
        String response = "text/plain";
        if(fileTarget.endsWith(".html")) response = "text/html";
        else if (fileTarget.endsWith(".txt")) response = "text/plain";
        else if (fileTarget.endsWith(".css")) response = "text/css";
        response+="; charset=UTF8";
        return response;
    }

    private void writeOkResponse(String responseTxt, String fileTarget) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + responseTxt.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                "Content-Type: " + getContentType(fileTarget) + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseTxt;
        clientSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
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
