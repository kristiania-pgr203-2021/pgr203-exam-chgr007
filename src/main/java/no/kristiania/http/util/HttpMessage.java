package no.kristiania.http.util;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpMessage {

    private int statusCode;
    private Map<String, String> headerFields;
    private Socket socket;
    private String body;
    private String startLine;

    public HttpMessage(Socket socket) throws IOException {

        this.socket = socket;
        headerFields = new HashMap<>();
        startLine = readLine();
        getHeaders();
        readMessageBody(getContentLength());
    }

    private void readMessageBody(int contentLength) throws IOException {

        StringBuilder body = new StringBuilder();

        for (int i = 0; i < contentLength; i++) {
            body.append((char)socket.getInputStream().read());
        }
        this.body = body.toString();
    }


    private String readLine() throws IOException {
        StringBuilder buffer = new StringBuilder();

        int c;

        while((c=this.socket.getInputStream().read()) != '\r'){
            buffer.append((char) c);
        }

        int expectedNewLine = this.socket.getInputStream().read();
        assert expectedNewLine == '\n';
        return buffer.toString();
    }

    private void getHeaders() throws IOException {
        String line;
        while (!(line = readLine()).isBlank()) {
            int colonPosition = line.indexOf(":");
            headerFields.put(line.substring(0, colonPosition), line.substring(colonPosition+1).trim());
        }
    }
    public String getHeader(String header)  {
        Objects.requireNonNull(header);
        if (headerFields.containsKey(header)) return headerFields.get(header);
        return null;
    }

    public int getContentLength() {
        if (getHeader("Content-Length") != null) {
            return Integer.parseInt(getHeader("Content-Length"));
        }
        return 0;
    }

    public String getMessageBody() {
        if (getContentLength() > 0) {
            System.out.println("body:" + body);
            return body;}
        return null;
    }

    public String getStartLine(){
        return startLine;
    }

}
