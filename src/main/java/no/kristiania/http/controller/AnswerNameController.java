package no.kristiania.http.controller;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class AnswerNameController implements HttpController{
    AnswerDao answerDao;

    public AnswerNameController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");

        if(request.getRequestType().equalsIgnoreCase("get")) {
            Long answerId = Long.parseLong(request.getQueryParam("answerId"));
            String messageBody = printAnswerName(answerId);
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection: ", "close");
            httpResponse.setHeaderField("Content-Length: " , String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if (request.getRequestType().equalsIgnoreCase("post")) {
            Long answerId = Long.parseLong(request.getPostParams().get("questionId"));

            Answer answer = answerDao.retrieveById(answerId);
            answer.setAnswer(request.getPostParams().get("name"));

            answerDao.update(answer);

            httpResponse = new HttpResponse(303, "See other");
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Location", "/question.html?question=" + answerId);
            return httpResponse;
        }
        return null;    }

    private String printAnswerName(Long answerId) throws SQLException {
        return answerDao.retrieveById(answerId).getAnswer();
    }
}
