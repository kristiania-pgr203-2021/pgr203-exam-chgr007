package no.kristiania.http.controller;

import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class FileController implements HttpController {
    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        // TODO: Fjerne dev-mode
        InputStream fileStream = getClass().getResourceAsStream(request.getFileTarget());
        // TODO: Hva faen skjer med != null her? Funker jo ikke
        if (fileStream != null) {
            // TODO: Legge inn content type
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fileStream.transferTo(buffer);
            int contentLength = buffer.toString().getBytes(StandardCharsets.UTF_8).length;
            HttpResponse httpResponse = new HttpResponse(200, "OK");
            request.setHeaderField("Connection: ", "close");
            request.setHeaderField("Content-Length", String.valueOf(contentLength));
            request.setHeaderField("Content-type", getContentType(request.getFileTarget()));
            httpResponse.setMessageBody(buffer.toString());
            return httpResponse;
        } else {
            HttpResponse httpResponse = new HttpResponse(404, "File not found");
            String messageBody = String.format("<h1>File %s could not be found", request.getFileTarget());
            request.setHeaderField("Connection: ", "Close");
            request.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8)));
            request.setMessageBody(messageBody);
            return httpResponse;
        }
    }

    private String getContentType(String fileTarget) {
        String response = "text/plain";
        if(fileTarget.endsWith(".html")) response = "text/html";
        else if (fileTarget.endsWith(".txt")) response = "text/plain";
        else if (fileTarget.endsWith(".css")) response = "text/css";
        response+="; charset=UTF8";
        return response;
    }
}
