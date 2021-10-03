package no.kristiania.http;

import no.kristiania.http.factory.Postable;
import no.kristiania.http.factory.Product;
import no.kristiania.http.factory.ProductFactory;
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
    void shouldEchoQueryParameter() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort());
        client.get("/hello?firstName=Test&lastName=Persson");
        assertEquals("<p>Hello Persson, Test</p>", client.getMessage().getMessageBody());
    }

    @Test
    void shouldReadFileFromServerMuahaha() throws IOException {
        server.setRoot("src/main/resources");
        HttpClient client = new HttpClient("localhost", server.getPort());
        client.get("/index.html");
        assertTrue(client.getMessage().getMessageBody().startsWith("<!DOCTYPE"));
    }

    @Test
    void shouldPostNewProduct() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort());
        List<Postable> products = new ArrayList<>();
        products.add(new ProductFactory<String,String>().getPostable("productName","Findus"));
        products.add(new ProductFactory<String,String>().getPostable("category","Cat"));
        client.post(products,"/api/newProduct");
        assertEquals(303, client.getStatusCode());
        Postable product = server.getProducts().get(0);
        assertEquals("Cat", product.getValue());
        assertEquals("Findus", product.getKey());
    }

    @Test
    void shouldGetCategories() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort());
        client.get("/api/categoryOptions");
        assertTrue(client.getMessage().getMessageBody().startsWith("<option"));
    }

    @Test
    void shouldRedirectOnPost() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort());
        List<Postable> products = new ArrayList<>();
        products.add(new ProductFactory<String,String>().getPostable("Cat","Findus"));
        client.post(products,"/api/newProduct");
        assertEquals(303, client.getStatusCode());
    }

}
