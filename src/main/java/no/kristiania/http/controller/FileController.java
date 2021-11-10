package no.kristiania.http.controller;

import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class FileController implements HttpController {
    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        // TODO: Fjerne dev-mode
        if (Files.exists(Path.of("/src/java/resources" + request.getFileTarget()))) {
            String messageBody = Files.readString(Path.of("/src/java/resources" + request.getFileTarget()));
            HttpResponse httpResponse = new HttpResponse(200, "OK");
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Content-Length", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            httpResponse.setHeaderField("Content-type", getContentType(request.getFileTarget()));
            httpResponse.setMessageBody(messageBody);
            return httpResponse;
        }
        InputStream fileStream = getClass().getResourceAsStream(request.getFileTarget());
        if (fileStream != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fileStream.transferTo(buffer);
            int contentLength = buffer.toString().getBytes(StandardCharsets.UTF_8).length;
            HttpResponse httpResponse = new HttpResponse(200, "OK");
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Content-Length", String.valueOf(contentLength));
            httpResponse.setHeaderField("Content-type", getContentType(request.getFileTarget()));
            httpResponse.setMessageBody(buffer.toString());
            return httpResponse;
        } else {
            HttpResponse httpResponse = new HttpResponse(404, "File not found");
            String messageBody = String.format("<h2>File %s could not be found</h2>", request.getFileTarget());
            httpResponse.setHeaderField("Connection", "Close");
            httpResponse.setHeaderField("Content-Type", "text/html");
            httpResponse.setHeaderField("Content-Length", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8)));
            httpResponse.setMessageBody(messageBody);
            return httpResponse;
        }
    }

    private String getContentType(String fileTarget) {
        String response = "text/plain";
        if(fileTarget.endsWith(".html")) response = "text/html";
        else if (fileTarget.endsWith(".txt")) response = "text/plain";
        else if (fileTarget.endsWith(".css")) response = "text/css";
        else if (fileTarget.endsWith(".js")) response = "text/javascript";
        response+="; charset=UTF8";
        return response;
    }
}
