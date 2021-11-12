package no.kristiania.http.controller;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.http.model.Answer;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class QuestionAnswersController implements HttpController {

    QuestionDao questionDao;
    AnswerDao answerDao;
    private final String path = "/api/questionAnswers";

    public QuestionAnswersController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }


    public QuestionAnswersController(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");

        if (request.getRequestType().equalsIgnoreCase("get")) {
            Long questionId = Long.parseLong(request.getQueryParam("questionId"));
            String messageBody = printAllQuestionAnswers(questionId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Content-Length", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
            // Add question to DB
        }

        return new HttpResponse(500, "Internal Server Error");

    }

    @Override
    public String getPath() {
        return this.path;
    }

    private String printAllQuestionAnswers(Long questionId) throws SQLException {
        List<Answer> answers = answerDao.listByQuestionId(questionId);

        StringBuilder stringBuilder = new StringBuilder();
        for(Answer answer : answers){
            stringBuilder.append("<div class=\"random-color flexbox-box flex-content question\">");
            stringBuilder.append(answer.getAnswer());
            stringBuilder.append("</div>");
        }
        return stringBuilder.toString();
    }
}
