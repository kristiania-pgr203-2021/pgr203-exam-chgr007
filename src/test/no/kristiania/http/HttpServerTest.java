package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

    @Test
    void shouldRespond200OK() {
        HttpServer httpServer = new HttpServer(0);
        HttpClient httpClient = new HttpClient(httpServer.getPort());
        assertEquals(200, httpClient.getStatusCode());
    }
}
