package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpServerTest {
    HttpServer server = new HttpServer(0, TestData.testDataSource());

    public HttpServerTest() throws IOException {
    }



    @Test
    void shouldReadFileFromServerMuahaha() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort());
        client.get("/index.html");
        assertTrue(client.getMessage().getMessageBody().startsWith("<!DOCTYPE"));
    }

}
