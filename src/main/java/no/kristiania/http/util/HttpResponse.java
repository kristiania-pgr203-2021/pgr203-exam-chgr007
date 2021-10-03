package no.kristiania.http.util;

import java.io.IOException;
import java.net.Socket;

public class HttpResponse extends HttpMessage {
    private int statusCode;
    public HttpResponse(Socket socket) throws IOException {
        super(socket);
        statusCode = Integer.parseInt(getStartLine().split(" ")[1]);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
