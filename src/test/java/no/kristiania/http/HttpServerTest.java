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
        server.setRoot("src/main/resources");
        HttpClient client = new HttpClient("localhost", server.getPort());
        client.get("/index.html");
        assertTrue(client.getMessage().getMessageBody().startsWith("<!DOCTYPE"));
    }

    @Test
    void shouldPostNewProduct() throws IOException {
        HttpClient client = new HttpClient("localhost", server.getPort());
        List<PostValue<String,String>> products = new ArrayList<>();
        products.add(new PostValue("productName","Findus"));
        products.add(new PostValue("category","Cat"));
        client.post(products,"/api/newProduct");
        assertEquals(303, client.getStatusCode());
        Product product = server.getProducts().get(0);
        assertEquals("Cat", product.getCategory());
        assertEquals("Findus", product.getName());

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
        List<PostValue<String,String>> products = new ArrayList<>();
        products.add(new PostValue("productName","Findus"));
        products.add(new PostValue("category","Cat"));
        client.post(products,"/api/newProduct");
        assertEquals(303, client.getStatusCode());
    }

}
