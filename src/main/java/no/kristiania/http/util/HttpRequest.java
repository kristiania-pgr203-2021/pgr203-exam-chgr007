package no.kristiania.http.util;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpRequest extends HttpMessage {

    private String fileTarget;
    private Map<String,String> queryParams;
    private String requestType;

    public String getRequestType() {
        return requestType;
    }

    public HttpRequest(Socket clientSocket) throws IOException {
        super(clientSocket);
        queryParams = parseRequestLine();
    }

    public String getFileTarget() {
        return fileTarget;
    }

    private Map<String,String> parseRequestLine() {
        String[] requestLineParts = getStartLine().split(" ");
        requestType = requestLineParts[0];
        HashMap<String, String> values = new HashMap<>();
        int questionMarkPosition = requestLineParts[1].indexOf("?");
        String queryLine = null;

        if (questionMarkPosition != -1) {
            queryLine = requestLineParts[1].substring(questionMarkPosition+1);
            fileTarget = requestLineParts[1].substring(0, questionMarkPosition);
        } else {
            fileTarget = requestLineParts[1];
        }
        if (queryLine != null) {
            return parseQueryLine(queryLine);
        }
        return values;
    }

    private Map<String,String> parseQueryLine(String queryLine) {
        HashMap<String, String> values = new HashMap<>();

        if (queryLine != null) {
            for (String queryPart : queryLine.split("&")) {
                int equalPos = queryPart.indexOf("=");
                values.put(URLDecoder.decode(queryPart.substring(0, equalPos),
                        StandardCharsets.UTF_8),
                        URLDecoder.decode(queryPart.substring(equalPos + 1),
                                StandardCharsets.UTF_8));
            }
        }
        return values;
    }

    public boolean hasParams() {
        return queryParams.size() > 0;
    }
    public String getQueryParam(String key) {
        Objects.requireNonNull(key);
        if (queryParams.containsKey(key)) return queryParams.get(key);
        return null;
    }

    public Map<String,String> getPostParams() {
        if (requestType.equalsIgnoreCase("POST")) {
            return parseQueryLine(getMessageBody());
        }
        return null;
    }

}
