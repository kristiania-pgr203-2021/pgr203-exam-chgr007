package no.kristiania.http.controller;

import no.kristiania.http.dao.QuestionDao;
import no.kristiania.http.model.Question;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class QuestionNameController implements HttpController{
    QuestionDao questionDao;
    private String path = "/api/questionName";

    public QuestionNameController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");

        if(request.getRequestType().equalsIgnoreCase("get")) {
            Long questionId = Long.parseLong(request.getQueryParam("questionId"));
            String messageBody = printQuestionName(questionId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: " , String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("post")) {
            Long questionId = Long.parseLong(request.getPostParams().get("questionId"));

            Question question = questionDao.retrieveById(questionId);
            question.setQuestion(request.getPostParams().get("name"));

            questionDao.update(question);

            httpResponse = new HttpResponse(303, "See other");
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Location", "/question.html?questionId=" + questionId);
            return httpResponse;
        }
        return new HttpResponse(500, "Internal server error");
    }

    @Override
    public String getPath() {
        return this.path;
    }

    private String printQuestionName(long questionId) throws SQLException {
            return questionDao.retrieveById(questionId).getQuestion();
        }
}
