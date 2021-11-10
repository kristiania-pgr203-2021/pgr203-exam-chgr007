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
import java.util.List;

public class QuestionnaireController implements HttpController {

    private QuestionnaireDao questionnaireDao;
    private QuestionDao questionDao;
    private boolean validToken = false;

    public QuestionnaireController(QuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    public QuestionnaireController(QuestionnaireDao questionnaireDao, QuestionDao questionDao) {
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

            if (!validToken || questionnaire.getPersonId() != userId) {
                return new HttpResponse(401, "Unauthorized");
            }
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
        } else if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaires")) {
            String messageBody = printAllQuestionnaires();
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaire")) {
            Long questionnaireId = Long.parseLong(request.getQueryParam("questionnaireId"));
            String messageBody = printAllQuestionnaireQuestions(questionnaireId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
            // Add question to DB
        } else if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaireName")) {
            Long questionnaireId = Long.parseLong(request.getQueryParam("questionnaireId"));
            String messageBody = printQuestionnaireName(questionnaireId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
            // Add question to DB
        } else if (request.getRequestType().equalsIgnoreCase("post") && request.getFileTarget().equalsIgnoreCase("/api/newQuestionnaire")) {
            Questionnaire questionnaire = new Questionnaire();
            questionnaire.setName(request.getPostParams().get("questionnaireName"));
            questionnaire.setPersonId(Long.parseLong(request.getPostParams().get("userId")));
            questionnaireDao.save(questionnaire);
            // Kan sende tilbake ID i body her kanskje?

            HttpResponse response = new HttpResponse(303, "See other");
            response.setHeaderField("Connection", "close");
            response.setHeaderField("Location", "/index.html");
            return response;

        } else if (request.getRequestType().equalsIgnoreCase("delete")) {
            // Add question to DB
        }

        return httpResponse;
    }

    private String printAllQuestionnaires() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Questionnaire questionnaire : questionnaireDao.retrieveAll()) {
            stringBuilder.append("<a href=\"questionnaire.html?questionnaireId=" + questionnaire.getId() + "\">");
            stringBuilder.append("<div class=\"random-color flexbox-box flex-content questionnaire\" id=\"questionnaire_" + questionnaire.getId() + "\">");
            stringBuilder.append("<h2>").append(questionnaire.getName()).append("</h2>");
            stringBuilder.append("</div>");

        }
        return stringBuilder.toString();
    }

    private String printQuestionnaireName(long questinnaireId) throws SQLException {
        return questionnaireDao.retrieveById(questinnaireId).getName();
    }

    private String printAllQuestionnaireQuestions(long questionnaireId) throws SQLException {
        List<Question> questions = questionDao.listByQuestionnaireId(questionnaireId);

        StringBuilder stringBuilder = new StringBuilder();
        for (Question question : questions) {
            stringBuilder.append("<a href=\"question.html?questionId=" + question.getId() + "\">");
            stringBuilder.append("<div class=\"random-color flexbox-box flex-content question\">");
            stringBuilder.append(question.getQuestion());
            stringBuilder.append("</div>");
            stringBuilder.append("</a>");
        }

        return stringBuilder.toString();
    }
}
