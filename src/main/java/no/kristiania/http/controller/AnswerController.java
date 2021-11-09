package no.kristiania.http.controller;

import no.kristiania.dao.AnswerDao;
import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;

public class AnswerController implements HttpController{

    AnswerDao answerDao;

    public AnswerController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");

        if(request.getRequestType().equalsIgnoreCase("post") && request.getFileTarget().equalsIgnoreCase("/api/newAnswer")) {
            Answer answer = new Answer();
            answer.setAnswer(request.getPostParams().get("answer"));
            long questionId = Long.parseLong(request.getPostParams().get("questionId"));
            answer.setQuestionId(questionId);
            httpResponse.setHeaderField("Connection", "close");
            answerDao.save(answer);

            httpResponse = new HttpResponse(303, "See other");
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Location", "/question.html?questionId=" + questionId);
            return httpResponse;
            // Kan sende tilbake ID i body her kanskje?
        }
        return null;

    }
}
