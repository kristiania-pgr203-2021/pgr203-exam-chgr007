package no.kristiania.http;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(0);
        server.setRoot("src/main/resources");
        System.out.println(server.getPort());
    }
}
