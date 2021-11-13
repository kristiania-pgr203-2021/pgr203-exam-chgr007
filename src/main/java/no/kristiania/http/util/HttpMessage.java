package no.kristiania.http.util;

import org.codehaus.jackson.io.UTF8Writer;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpMessage {

    protected final Map<String, String> headerFields;
    protected Socket socket;
    protected String messageBody;
    protected String startLine;

    public HttpMessage(Socket socket) throws IOException {

        this.socket = socket;
        headerFields = new HashMap<>();
        startLine = readLine();
        getHeaders();
        readMessageBody(getContentLength());
    }
    public HttpMessage(){
        headerFields = new HashMap<>();
    };
    private void readMessageBody(int contentLength) throws IOException {
        if (contentLength <= 0) return;
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < contentLength; i++) {
            body.append((char)socket.getInputStream().read());
        }
        byte array[] = body.toString().getBytes("UTF-8");
        String encoded = new String(array, StandardCharsets.UTF_8);
        this.messageBody = body.toString();
    }


    public void setHeaderField(String key, String value) {
        Objects.requireNonNull(key, "key can not be null");
        Objects.requireNonNull(value, "value can not be null");
        headerFields.put(key,value);
    }

    public void setMessageBody(String message) {
        this.messageBody = message;
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
        return -1;
    }

    public String getMessageBody() {
        if (getContentLength() > 0) {
            System.out.println("body:" + messageBody);
            return messageBody;}
        return null;
    }

    public String getStartLine(){
        return startLine;
    }

    public void write(Socket socket) throws IOException {
        String response = generateStartAndHeaders();

        socket.getOutputStream().write(response.getBytes(StandardCharsets.UTF_8));
    }

    @NotNull
    private String generateStartAndHeaders() {
        StringBuilder headers = new StringBuilder();

        // Formats headers to key: value\r\n
        headerFields.forEach((k,v) -> headers.append(k+": "+v+"\r\n"));

        String response = startLine+"\r\n"
                + headers
                + "\r\n"
                + messageBody;
        return response;
    }

    public void writeBytes(Socket socket, InputStream fileStream) throws IOException {
        StringBuilder headers = new StringBuilder();

        // Formats headers to key: value\r\n
        headerFields.forEach((k,v) -> headers.append(k+": "+v+"\r\n"));

        String response = startLine+"\r\n"
                + headers
                + "\r\n";
        socket.getOutputStream().write(response.getBytes());
        fileStream.transferTo(socket.getOutputStream());
    }

}
