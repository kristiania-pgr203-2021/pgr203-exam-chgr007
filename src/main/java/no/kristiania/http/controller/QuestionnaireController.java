package no.kristiania.http.controller;

import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.QuestionnaireDao;
import no.kristiania.http.model.Question;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class QuestionnaireController implements HttpController {
    private QuestionnaireDao questionnaireDao;
    private QuestionDao questionDao;

    public QuestionnaireController(QuestionnaireDao questionnaireDao, QuestionDao questionDao) {
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");
        if (request.getRequestType().equalsIgnoreCase("get") && request.getFileTarget().equals("/api/questionnaire")) {
            Long questionnaireId = Long.parseLong(request.getQueryParam("questionnaireId"));
            String messageBody = printAllQuestionnaireQuestions(questionnaireId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: ", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
            // Add question to DB
        }
        return new HttpResponse(500, "Internal server error");
    }
    private String printAllQuestionnaireQuestions(long questionnaireId) throws SQLException {
        List<Question> questions = questionDao.listByQuestionnaireId(questionnaireId);

        StringBuilder stringBuilder = new StringBuilder();
        for (Question question : questions) {
            stringBuilder.append("<a href=\"question.html?questionId=" + question.getId() + "&questionType="+ question.getQuestionType() + "\" class=\"question random-color flexbox-box flex-content \">");
            stringBuilder.append("<h2>" + question.getQuestion() + "</h2>");
            stringBuilder.append("<p class=\"bottom\">" + question.getQuestionType() + "</p>");
            stringBuilder.append("</a>");
        }

        return stringBuilder.toString();
    }
}
