package no.kristiania.http.controller;

import no.kristiania.dao.AnswerOptionDao;
import no.kristiania.http.model.AnswerOption;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class AnswerOptionController implements HttpController {
    private AnswerOptionDao answerOptionDao;

    public AnswerOptionController(AnswerOptionDao answerOptionDao) {
        this.answerOptionDao = answerOptionDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        if (request.getRequestType().equalsIgnoreCase("get")) {
            //TODO: okey denne må vi huske å implementere
            return new HttpResponse(418, "I'm a teapot");
        } else if (request.getRequestType().equalsIgnoreCase("post")) {

            Map<String,String> params = request.getPostParams();
            if (params.containsKey("answerOption")) {
                AnswerOption answerOption = new AnswerOption();
                int answerId = Integer.parseInt(params.get("answerId"));
                int range = Integer.parseInt(params.get("range"));
                answerOption.setAnswerOption(params.get("answerOption"));
                answerOption.setAnswerId(answerId);
                answerOption.setRange(range);
                answerOption.setMinRangeName(params.get("minRangeName"));
                answerOption.setMaxRangeName(params.get("maxRangeName"));
                answerOptionDao.save(answerOption);
                return new HttpResponse(200, "OK");
            }
            return new HttpResponse(500, "Internal Server Error");
        }
        return new HttpResponse(500, "Internal Server Error");
    }
}
