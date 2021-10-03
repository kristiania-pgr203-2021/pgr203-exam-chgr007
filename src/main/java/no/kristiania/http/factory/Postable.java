package no.kristiania.http.factory;

public interface Postable<K,V>{
    K getKey();
    V getValue();
    String printProduct();
}
