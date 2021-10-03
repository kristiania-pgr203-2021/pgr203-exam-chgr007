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

        HttpClient client = new HttpClient("localhost", server.getPort());
        List<Postable> products = new ArrayList<>();
        products.add(new ProductFactory<String,String>().getPostable("productName","Findus"));
        products.add(new ProductFactory<String,String>().getPostable("category","Cat"));
        client.post(products,"/api/newProduct");
        Postable product = server.getProducts().get(0);
        product.getValue();
        product.getKey();
    }
}
