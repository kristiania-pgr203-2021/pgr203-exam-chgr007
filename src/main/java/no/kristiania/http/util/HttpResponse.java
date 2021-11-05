package no.kristiania.http.util;

import java.io.IOException;
import java.net.Socket;

public class HttpResponse extends HttpMessage {
    private int statusCode;
    private String statusMessage;
    public HttpResponse(Socket socket) throws IOException {
        super(socket);
        statusCode = Integer.parseInt(getStartLine().split(" ")[1]);
    }
    public HttpResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public void write(Socket socket) throws IOException {
        startLine = String.format("HTTP/1.1 %s %s",statusCode, statusMessage);
        super.write(socket);
    }
}
