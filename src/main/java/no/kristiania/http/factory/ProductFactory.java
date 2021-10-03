package no.kristiania.http.factory;

public class ProductFactory<K,V> extends PostableFactory<K,V>{

    @Override
    public Postable getPostable(K key, V value) {

        return new Product(key, value);
    }

}
