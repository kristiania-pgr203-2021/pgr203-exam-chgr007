package no.kristiania.http.controller;

import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.QuestionnaireDao;
import no.kristiania.http.model.Question;
import no.kristiania.http.model.Questionnaire;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class QuestionnaireController implements HttpController{

    QuestionnaireDao questionnaireDao;

    public QuestionnaireController(QuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }



    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");
        if (request.getRequestType().equalsIgnoreCase("get") && request.hasQueryParam("id")) {
            Long id = Long.valueOf(request.getQueryParam("id"));
            Questionnaire questionnaire = questionnaireDao.retrieveById(id);
            String messageBody = "questionnaire="+questionnaire.getName();
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("get")) {
            String messageBody = printAllQuestionnaires();
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("put")) {
            // Add question to DB
        } else if (request.getRequestType().equalsIgnoreCase("delete")) {
            // Add question to DB
        }

        return null;
    }

    private String printAllQuestionnaires() throws SQLException {

        StringBuilder stringBuilder = new StringBuilder();
        for(Questionnaire questionnaire : questionnaireDao.retrieveAll()){
            stringBuilder.append("<div class=\"questionnaire flex-content\">");
            stringBuilder.append("<h1>").append(questionnaire.getName()).append("</h1>");
            stringBuilder.append("</div>");
        }

        return stringBuilder.toString();
    }
}
