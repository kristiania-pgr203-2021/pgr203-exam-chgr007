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
import java.util.List;

public class QuestionController implements HttpController{
    QuestionDao questionDao;
    AnswerDao answerDao;

    public QuestionController(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }


    public QuestionController(QuestionDao questionDao, AnswerDao answerDao) {
        this.questionDao = questionDao;
        this.answerDao = answerDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");
        if (request.getRequestType().equalsIgnoreCase("get") && request.hasQueryParam("questionId")) {
            Long id = Long.valueOf(request.getQueryParam("questionId"));
            Question question = questionDao.retrieveById(id);
            String questionInfo = "questionInfo="+question.getQuestionOptionList();
            System.out.println(question.getQuestionOptionList());
            String messageBody = questionInfo;
            httpResponse.setMessageBody(messageBody);
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Content-Length", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
            return httpResponse;
        } else if(request.getRequestType().equalsIgnoreCase("post") && request.getFileTarget().equalsIgnoreCase("/api/newQuestion")) {

            Question question = new Question();
            question.setQuestion(request.getPostParams().get("question"));

            long questionnaireId = Long.parseLong(request.getPostParams().get("questionnaireId"));

            question.setQuestionnaireId(questionnaireId);
            questionDao.save(question);

            httpResponse = new HttpResponse(303, "See other");
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Location", "/questionnaire.html?questionnaireId=" + questionnaireId);
            return httpResponse;
            // Kan sende tilbake ID i body her kanskje?
        } else if (request.getRequestType().equalsIgnoreCase("post")) {
            // Add question to DB
        } else if (request.getRequestType().equalsIgnoreCase("put")) {
            // Add question to DB
        } else if (request.getRequestType().equalsIgnoreCase("delete")) {
            // Add question to DB
        }

        return null;
    }
}
