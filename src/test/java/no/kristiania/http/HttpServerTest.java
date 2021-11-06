package no.kristiania.http;

import no.kristiania.http.model.Product;
import no.kristiania.http.util.PostValue;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpServerTest {
    HttpServer server = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }



    @Test
    void shouldReadFileFromServerMuahaha() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort());
        client.get("/index.html");
        assertTrue(client.getMessage().getMessageBody().startsWith("<!DOCTYPE"));
    }

}
