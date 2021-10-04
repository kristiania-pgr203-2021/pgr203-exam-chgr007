package no.kristiania.http;

import no.kristiania.http.model.Product;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HttpServer {

    private int port;
    private ServerSocket serverSocket;
    private Path rootDirectory;
    private List<Product> products;

    public HttpServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        products = new ArrayList<>();
        new Thread(this::handleClient).start();
    }

    private void handleClient() {
        while(true){
            try{
                System.out.printf("Server running at port: %s%n", getPort());
                Socket clientSocket = serverSocket.accept();
                HttpRequest message = new HttpRequest(clientSocket);
                Router router = new Router(clientSocket, products);
                router.route(message, rootDirectory);
                if (message.getRequestType().equalsIgnoreCase("POST")) {
                    products.add(
                            new Product(router.getProducts().get("productName"),
                            router.getProducts().get("category"))
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRoot(String root){
        this.rootDirectory = Path.of(root);
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public List<Product> getProducts() {
        return products;
    }

}
