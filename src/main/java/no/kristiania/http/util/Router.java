package no.kristiania.http.util;

import no.kristiania.http.factory.Postable;
import no.kristiania.http.factory.Product;


import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Router {
    private Socket clientSocket;
    private Map<String,String> values;
    private List<Postable> products;

    public Router(Socket clientSocket, List<Postable> products) {
        this.clientSocket = clientSocket;
        this.products = products;
    }

    public void route(HttpRequest message, Path rootDirectory) throws IOException {

        if (message.getFileTarget().equalsIgnoreCase("/hello") && message.hasParams()) {

            String messageBody = String.format("<p>Hello %s, %s</p>",message.getQueryParam("lastName"), message.getQueryParam("firstName"));
            String responseMessage = "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: " + messageBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                    "Connection: close" + messageBody + "\r\n" +
                    "\r\n" +
                    messageBody;
            clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
        }  else if (message.getFileTarget().equals("/api/newProduct")){
            values = message.getPostParams();
            String responseMessage = "HTTP/1.1 303 See Other\r\n" +
                    "Location: /listProducts.html\r\n" +
                    "\r\n";
            clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
            clientSocket.close();

        } else if (message.getFileTarget().equals("/")){
            values = message.getPostParams();
            String responseMessage = "HTTP/1.1 303 See Other\r\n" +
                    "Location: /newProduct.html\r\n" +
                    "\r\n";
            clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
            clientSocket.close();

        }
        else if (message.getFileTarget().equals("/api/products")){
            values = message.getPostParams();
            String messageBody = printProducts();
            String responseMessage = "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: " + messageBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    messageBody;
            clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
        } else if (message.getFileTarget().equals("/api/categoryOptions")) {
            values = message.getPostParams();
          
            String messageBody = printCategories();

            String responseMessage = "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: " + messageBody.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    messageBody;
            clientSocket.getOutputStream().write(responseMessage.getBytes(StandardCharsets.UTF_8));
        } else {

            if(rootDirectory != null && Files.exists(rootDirectory.resolve(message.getFileTarget().substring(1)))){
                String responseTxt = Files.readString(rootDirectory.resolve(message.getFileTarget().substring(1)));
                writeOkResponse(clientSocket, responseTxt, message.getFileTarget());
                return;

            } else {
                String responseText = "File not found: " + message.getFileTarget();

                String response = "HTTP/1.1 404 Not found\r\n" +
                        "Content-Length: " + responseText.length() + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n" +
                        responseText;
                clientSocket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
            }
        }
    }
    private String getContentType(String fileTarget) {
        String response = "text/plain";
        if(fileTarget.endsWith(".html")) response = "text/html";
        else if (fileTarget.endsWith(".txt")) response = "text/plain";
        else if (fileTarget.endsWith(".css")) response = "text/css";
        return response+="; charset=UTF8";
    }

    private void writeOkResponse(Socket clientSocket, String responseTxt, String fileTarget) throws IOException {
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
            string.append("<th>" + category + "</th>");
        }

        string.append("</tr>");
        string.append("<tr>");
        for (String category : categories) {
            string.append("<td>" + printProductsByCategory(category) + "</td>");
        }
        string.append("</tr>");



        /*
        for(int i = 0; i<products.size(); i++){
            string.append(products.get(i).printProduct());
        }*/

        return string.toString();
    }

    private String printProductsByCategory(String category) {

        StringBuilder string = new StringBuilder();

        for(Postable p : products){
            if(p.getValue().equals(category)){
                string.append("<li>");
                string.append(p.getKey());
                string.append("</li>");
            }
        }
        return string.toString();
    }

    private String printCategories() {

        StringBuilder string = new StringBuilder();

        String[] categories = getCategories();

        for(String category : categories){
            string.append("<option>" + category + "</option>");
        }

        return string.toString();
    }

    private String[] getCategories() {

        String[] categories = {"Katt", "Hund", "Hest", "Slange"};

        return categories;
    }

}
