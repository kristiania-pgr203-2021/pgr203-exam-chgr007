package no.kristiania.http.controller;

import no.kristiania.http.dao.RangeQuestionDao;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class AnswerOptionController implements HttpController {
    private RangeQuestionDao rangeQuestionDao;
    private String path = "/api/answerOption";

    public AnswerOptionController(RangeQuestionDao rangeQuestionDao) {
        this.rangeQuestionDao = rangeQuestionDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        if (request.getRequestType().equalsIgnoreCase("get")) {
            //TODO: okey denne må vi huske å implementere
            return new HttpResponse(418, "I'm a teapot");
        } else if (request.getRequestType().equalsIgnoreCase("post")) {

            Map<String,String> params = request.getPostParams();
            if (params.containsKey("answer_type")) {
//                AnswerOption answerOption = new AnswerOption();
//                int questionId = Integer.parseInt(params.get("question_id"));
//                answerOption.setAnswerType(params.get("answer_type"));
//                answerOption.setQuestionId(questionId);
//                answerOption.setValue(params.get("value"));
//                answerOption.setName(params.get("name"));
//                rangeQuestionDao.save(answerOption);
//                return new HttpResponse(200, "OK");
            }
            return new HttpResponse(500, "Internal Server Error");
        }
        return new HttpResponse(500, "Internal Server Error");
    }

    @Override
    public String getPath() {
        return this.path;
    }
}