package no.kristiania.http;

import org.flywaydb.core.Flyway;

import java.io.IOException;


public class Program {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080);
    }
}
