package no.kristiania.http.util;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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

    public HttpResponse() {

    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public void useServerErrorParams(){
        headerFields.clear();
        statusCode = 500;
        statusMessage = "Internal server error";
        messageBody = "<p>OooooooOOoooOOooOOps. Something went really wrong!</p>";
        setHeaderField("Connection", "Close");
        setHeaderField("Content-Type", "text/html");
        setHeaderField("Content-Length", String.valueOf(statusMessage.getBytes(StandardCharsets.UTF_8).length));
    }
    public void useRedirectParams(String location) {
        statusCode = 303;
        statusMessage = "See other";
        headerFields.clear();
        setHeaderField("Location", location);
        setHeaderField("Connection", "Close");
    }
    public void useNotFoundParams() {
        headerFields.clear();
        statusCode = 404;
        statusMessage = "Not found";
        messageBody = "<p>The resource could not be found</p>";
        setHeaderField("Connection", "Close");
        setHeaderField("Content-Type", "text/html");
        setHeaderField("Content-Length", String.valueOf(statusMessage.getBytes(StandardCharsets.UTF_8).length));
    }
    @Override
    public void write(Socket socket) throws IOException {
        startLine = String.format("HTTP/1.1 %s %s",statusCode, statusMessage);
        super.write(socket);
    }
}
