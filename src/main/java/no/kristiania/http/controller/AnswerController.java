package no.kristiania.http.controller;

import no.kristiania.dao.*;
import no.kristiania.http.model.*;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class AnswerController implements HttpController{

    private AnswerDao answerDao;
    private QuestionDao questionDao;
    private RangeQuestionDao rangeQuestionDao;
    private RadioQuestionDao radioQuestionDao;
    private TextQuestionDao textQuestionDao;
    private final String path = "/api/newAnswer";

    public AnswerController(AnswerDao answerDao) {
        this.answerDao = answerDao;
    }

    public AnswerController(AnswerDao answerDao, QuestionDao questionDao, RangeQuestionDao rangeQuestionDao, RadioQuestionDao radioQuestionDao, TextQuestionDao textQuestionDao) {
        this.answerDao = answerDao;
        this.questionDao = questionDao;
        this.rangeQuestionDao = rangeQuestionDao;
        this.radioQuestionDao = radioQuestionDao;
        this.textQuestionDao = textQuestionDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        HttpResponse httpResponse = new HttpResponse(200, "OK");

        if(request.getRequestType().equalsIgnoreCase("post") && request.getFileTarget().equalsIgnoreCase("/api/newAnswer")) {
            Answer answer = new Answer();
            answer.setAnswer(request.getPostParams().get("answer"));
            long questionId = Long.parseLong(request.getPostParams().get("questionId"));
            long personId = Long.parseLong(request.getPostParams().get("userId"));
            String questionType = request.getPostParams().get("questionType");
            answer.setQuestionId(questionId);
            answer.setUserId(personId);
            answerDao.save(answer);

            httpResponse = new HttpResponse(303, "See other");
            httpResponse.setHeaderField("Connection", "close");
            httpResponse.setHeaderField("Location", "/question.html?questionId=" + questionId + "&questionType=" + questionType + "&questionnaireId=" + questionDao.retrieveById(questionId).getQuestionnaireId());
            return httpResponse;
            // Kan sende tilbake ID i body her kanskje?
        } else if (request.getRequestType().equalsIgnoreCase("get") && request.hasQueryParam("questionId")){
            HttpResponse response = new HttpResponse(200, "OK");

            Long questionId = Long.parseLong(request.getQueryParam("questionId"));
            String questionType = request.getQueryParam("questionType");

            if(questionType.equals("text")){
                TextQuestion textQuestions = textQuestionDao.fetchByQuestionId(questionId);

                String messageBody = textQuestions.generateHtml();
                response.setMessageBody(messageBody);
                response.setHeaderField("Connection", "close");
                response.setHeaderField("Content-Length", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
                return response;
            } else if(questionType.equals("range")){
                RangeQuestion range = rangeQuestionDao.fetchByQuestionId(questionId);

                String messageBody = range.generateHtml();
                response.setMessageBody(messageBody);
                response.setHeaderField("Connection", "close");
                response.setHeaderField("Content-Length", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
                return response;
            } else if(questionType.equals("radio")){
                List<RadioQuestion> radioQuestions = radioQuestionDao.fetchAllByQuestionId(questionId);

                StringBuilder stringBuilder = new StringBuilder();

                for(RadioQuestion r : radioQuestions){
                    stringBuilder.append(r.generateHtml());
                }

                String messageBody = stringBuilder.toString();
                response.setMessageBody(messageBody);
                response.setHeaderField("Connection", "close");
                response.setHeaderField("Content-Length", String.valueOf(messageBody.getBytes(StandardCharsets.UTF_8).length));
                return response;
            }
        }
        return null;

    }

    @Override
    public String getPath() {
        return path;
    }
}
