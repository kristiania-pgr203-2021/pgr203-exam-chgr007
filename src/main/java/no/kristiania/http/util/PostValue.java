package no.kristiania.http.util;

public class PostValue<K,V> {
    private K key;
    private V value;

    public PostValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

}
