package no.kristiania.http;

public class HttpServer {
    private int port;
    public HttpServer(int port) {
        this.port = port;
    }

    public Object getPort() {
        return this.port;
    }
}
