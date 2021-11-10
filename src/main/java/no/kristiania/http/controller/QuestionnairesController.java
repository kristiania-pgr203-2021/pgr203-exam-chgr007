package no.kristiania.http.controller;

import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.QuestionnaireDao;
import no.kristiania.http.model.Question;
import no.kristiania.http.model.Questionnaire;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.HttpMessage;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionnairesController implements HttpController {

    private QuestionnaireDao questionnaireDao;
    private QuestionDao questionDao;
    private boolean validToken = false;

    public QuestionnairesController(QuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    public QuestionnairesController(QuestionnaireDao questionnaireDao, QuestionDao questionDao) {
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
    }


    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");
        String cookieString;
        long userId = -1;
        if (request.getPostParams() != null && request.getPostParams().containsKey("cookie")) {
            Authenticator authenticator = new Authenticator();
            cookieString = request.getPostParams().get("cookie");
            int equalPosition = cookieString.indexOf("=");
            if (equalPosition != -1) {
                validToken = authenticator.validateToken(cookieString.substring(equalPosition + 1));
                if (validToken) {
                    userId = authenticator.getIdFromToken(cookieString.substring(equalPosition + 1));
                }
            }
        }

        if (request.getRequestType().equalsIgnoreCase("get") && request.hasQueryParam("id")) {
            Long id = Long.valueOf(request.getQueryParam("id"));
            Questionnaire questionnaire = questionnaireDao.retrieveById(id);

// ********************             Alternativ måte å gjøre ting på:
//            questionDao.listByQuestionnaireId(id).forEach(q -> questionnaire.addQuestion(q));
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            String questionnaireJSON = objectMapper.writeValueAsString(questionnaire);
//            httpResponse.setHeaderField("Connection: ", "close");
//            httpResponse.setHeaderField("Content-Type", "application/json");
//            httpResponse.setHeaderField("Content-Length", String.valueOf(questionnaireJSON.getBytes(StandardCharsets.UTF_8).length));
//            httpResponse.setMessageBody(questionnaireJSON);
// *******************************************************************************************************************************************

            httpResponse.setHeaderField("Connection: ", "close");
            String messageBody = "questionnaire=" + questionnaire.getName();
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("get")) {
            String messageBody = printAllQuestionnaires(userId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("delete")) {
            // Add question to DB
        }

        return httpResponse;
    }

    private String printAllQuestionnaires(long userId) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();

        // Only get the logged in users questionnaires
        List<Questionnaire> questionnaires = questionnaireDao.retrieveAll()
                .stream()
                .filter(q -> q.getPersonId() == userId)
                .collect(Collectors.toList());

        for (Questionnaire questionnaire : questionnaires) {
            stringBuilder.append("<a href=\"questionnaire.html?questionnaireId=" + questionnaire.getId() + "\">");
            stringBuilder.append("<div class=\"random-color flexbox-box flex-content questionnaire\" id=\"questionnaire_" + questionnaire.getId() + "\">");
            stringBuilder.append("<h2>").append(questionnaire.getName()).append("</h2>");
            stringBuilder.append("</div>");

        }
        return stringBuilder.toString();
    }
}
