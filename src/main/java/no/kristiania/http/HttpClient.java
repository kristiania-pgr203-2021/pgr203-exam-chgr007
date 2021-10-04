package no.kristiania.http;

import no.kristiania.http.util.HttpMessage;
import no.kristiania.http.util.HttpResponse;
import no.kristiania.http.util.PostValue;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpClient {
    private int port;
    private String host;
    private Socket socket;
    private HttpResponse message;
    private int statusCode;

    public HttpClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(this.host, this.port);
    }

    private String request(String type, String target, int contentLength) {
        String request = type + " " + target + " HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "Connection: close\r\n" +
                (type.equalsIgnoreCase("POST") ?
                        "Content-Type: application/x-www-form-urlencoded\r\n" +
                                "Content-Length: " + contentLength + "\r\n": "") +
                "\r\n";
        return request;

    }
    public void get(String target) throws IOException {
        socket.getOutputStream().write(request("GET", target, 0).getBytes());
        message = new HttpResponse(socket);
        statusCode = message.getStatusCode();
    }

    public void post(List<PostValue<String,String>> postables, String target) throws IOException {
        StringBuilder keyValueString = new StringBuilder();
        postables.forEach((postable) -> keyValueString.append(postable.getKey()+"="+postable.getValue()+"&"));
        // remove the last "&" appended
        keyValueString.deleteCharAt(keyValueString.length()-1);
        int contentLength = keyValueString.toString().getBytes().length;
        String postMessage = request("POST", target, contentLength) + keyValueString;
        socket.getOutputStream().write(postMessage.getBytes(StandardCharsets.UTF_8));
        message = new HttpResponse(socket);
        statusCode = message.getStatusCode();
    }

    public String getHeader(String headerLine){
        return message.getHeader(headerLine);
    }

    public HttpMessage getMessage() {
        return message;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
