package no.kristiania.http;

import no.kristiania.http.util.HttpMessage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpClientTest {

    @Test
    void shouldGetStatus200() throws IOException {
        HttpClient client = new HttpClient("httpbin.org",80);
        client.get("/html");
        HttpMessage message = client.getMessage();
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldGetStatus404() throws IOException {
        HttpClient client = new HttpClient("httpbin.org", 80);
        client.get("/notfound404");
        HttpMessage message = client.getMessage();

        assertEquals(404, client.getStatusCode());
    }

    @Test
    void shouldReadHeaderLines() throws IOException {
        HttpClient client = new HttpClient("httpbin.org",80);
        client.get("/html");
        assertEquals("text/html; charset=utf-8", client.getHeader("Content-Type"));
    }

    @Test
    void httpMessageShouldHaveContentLength() throws IOException {
        HttpClient client = new HttpClient("httpbin.org",80);
        client.get("/html");
        HttpMessage httpMessage = client.getMessage();
        assertEquals(3741, httpMessage.getContentLength());
    }

    @Test
    void shouldReturnMessageBody() throws IOException {
        HttpClient client = new HttpClient("httpbin.org",80);
        client.get("/html");
        HttpMessage message = client.getMessage();
        assertTrue(message.getMessageBody().startsWith("<!DOCTYPE html>"),
                "Expected HTML: " + message.getMessageBody());
    }
}
