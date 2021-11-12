package no.kristiania.http;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Program {
    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080);
    }
}
