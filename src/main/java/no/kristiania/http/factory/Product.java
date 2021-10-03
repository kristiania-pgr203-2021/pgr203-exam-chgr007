package no.kristiania.http.factory;

public class Product<K,V> implements Postable<K,V>{
    K key;
    V value;

    public Product(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public String printProduct(){
        return String.format("<p>%s</p>",getValue(), getKey());
    }
}
