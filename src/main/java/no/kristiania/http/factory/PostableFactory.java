package no.kristiania.http.factory;

public abstract class PostableFactory<K,V> {
    public abstract Postable<K,V> getPostable(K key, V value);
}
