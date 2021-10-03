package no.kristiania.http;

import no.kristiania.http.factory.Postable;
import no.kristiania.http.factory.ProductFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(0);
        server.setRoot("src/main/resources");
    }
}
