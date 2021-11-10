package no.kristiania.http.controller;

import no.kristiania.dao.QuestionnaireDao;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class QuestionnaireNameController implements HttpController {
    private QuestionnaireDao questionnaireDao;

    public QuestionnaireNameController(QuestionnaireDao questionnaireDao    ) {
        this.questionnaireDao = questionnaireDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");
        if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaireName")) {
            Long questionnaireId = Long.parseLong(request.getQueryParam("questionnaireId"));
            String messageBody = printQuestionnaireName(questionnaireId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        }
        return new HttpResponse(500, "Internal server error");
    }

    private String printQuestionnaireName(long questinnaireId) throws SQLException {
        return questionnaireDao.retrieveById(questinnaireId).getName();
    }
}
