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
import java.util.List;
import java.util.Map;

public class QuestionnaireController implements HttpController{

    QuestionnaireDao questionnaireDao;
    QuestionDao questionDao;

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
        if (request.getRequestType().equalsIgnoreCase("get") && request.hasQueryParam("id")) {
            Long id = Long.valueOf(request.getQueryParam("id"));
            Questionnaire questionnaire = questionnaireDao.retrieveById(id);
            String messageBody = "questionnaire="+questionnaire.getName();
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaires")) {
            String messageBody = printAllQuestionnaires();
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaire")) {
            Integer questionnaireId = Integer.parseInt(request.getQueryParam("questionnaireId"));
            System.out.println(questionnaireId);
            String messageBody = printAllQuestionnaireQuestions(questionnaireId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: " , String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
            // Add question to DB
        } else if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaireName")) {
            Integer questionnaireId = Integer.parseInt(request.getQueryParam("questionnaireId"));
            System.out.println(questionnaireId);
            String messageBody = printQuestionnaireName(questionnaireId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: " , String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
            // Add question to DB
        } else if (request.getRequestType().equalsIgnoreCase("delete")) {
            // Add question to DB
        }

        return httpResponse;
    }

    private String printAllQuestionnaires() throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        for(Questionnaire questionnaire : questionnaireDao.retrieveAll()){
            stringBuilder.append("<a href=\"questionnaire.html?questionnaireId=" + questionnaire.getId()  + "\">");
            stringBuilder.append("<div class=\"random-color flexbox-box flex-content questionnaire\" id=\"questionnaire_" + questionnaire.getId() + "\">");
            stringBuilder.append("<h2>").append(questionnaire.getName()).append("</h2>");
            stringBuilder.append("</div>");
            stringBuilder.append("</a>");
        }
        return stringBuilder.toString();
    }

    private String printQuestionnaireName(long questinnaireId) throws SQLException {
        return "<h1 id=\"questionnaire-name\" style=\"display: inline-block; text-align: center\">" + questionnaireDao.retrieveById(questinnaireId).getName() +"</h1>";
    }

    private String printAllQuestionnaireQuestions(long questionnaireId) throws SQLException {
        List<Question> questions = questionDao.listByQuestionnaireId(questionnaireId);

        StringBuilder stringBuilder = new StringBuilder();
        for(Question question : questions){
            stringBuilder.append("<div class=\"random-color flexbox-box flex-content question\">");
            stringBuilder.append(question.getQuestion());
            stringBuilder.append("</div>");
        }

        return stringBuilder.toString();
    }
}
