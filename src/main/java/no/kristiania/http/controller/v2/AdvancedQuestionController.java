package no.kristiania.http.controller.v2;

import no.kristiania.dao.RadioQuestionDao;
import no.kristiania.dao.RangeQuestionDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.TextQuestionDao;
import no.kristiania.http.controller.HttpController;
import no.kristiania.http.model.*;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdvancedQuestionController implements HttpController {
    private QuestionDao questionDao;
    private RangeQuestionDao rangeQuestionDao;
    private RadioQuestionDao radioQuestionDao;
    private TextQuestionDao textQuestionDao;

    public AdvancedQuestionController(QuestionDao questionDao, RangeQuestionDao rangeQuestionDao, RadioQuestionDao radioQuestionDao, TextQuestionDao textQuestionDao) {
        this.questionDao = questionDao;
        this.rangeQuestionDao = rangeQuestionDao;
        this.radioQuestionDao = radioQuestionDao;
        this.textQuestionDao = textQuestionDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {


        if (request.getRequestType().equalsIgnoreCase("post")) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> postValues = request.getPostParams();
            HttpResponse httpResponse = new HttpResponse(200, "OK");

            if (postValues != null && postValues.containsKey("json")) {
                String questionJson = postValues.get("json");

                Question question = objectMapper.readValue(questionJson, Question.class);
                questionDao.save(question);

                if (question.getHasAnswerOptions() && question.getQuestionType() == QuestionType.range) {
                    for (QuestionOptions option : question.getQuestionOptionList()) {
                        option.setQuestionId(question.getId());
                        rangeQuestionDao.save((RangeQuestion) option);
                    }
                } else if (question.getHasAnswerOptions() && question.getQuestionType() == QuestionType.radio) {
                    for (QuestionOptions option : question.getQuestionOptionList()) {
                        option.setQuestionId(question.getId());
                        radioQuestionDao.save((RadioQuestion) option);
                    }
                } else if (question.getHasAnswerOptions() && question.getQuestionType() == QuestionType.text) {
                    for (QuestionOptions option : question.getQuestionOptionList()) {
                        option.setQuestionId(question.getId());
                        textQuestionDao.save((TextQuestion) option);
                    }
                }
                String responseBody = objectMapper.writeValueAsString(question);
                httpResponse.setHeaderField("Content-Type", "application/json; charset=UTF-8");
                httpResponse.setHeaderField("Connection", "close");
                httpResponse.setHeaderField("Content-Length", String.valueOf(responseBody.getBytes(StandardCharsets.UTF_8).length));
                httpResponse.setMessageBody(responseBody);
                return httpResponse;
            }
        } else if (request.getRequestType().equalsIgnoreCase("get")) {
            List<Question> questionList = questionDao.retrieveAll();
            ObjectMapper objectMapper = new ObjectMapper();

            String jsonObject = objectMapper.writeValueAsString(questionList);
            HttpResponse response = new HttpResponse(200, "OK");
            response.setHeaderField("Connection", "Close");
            response.setHeaderField("Content-Type", "application/json; charset=UTF-8");
            response.setHeaderField("Content-Length", String.valueOf(jsonObject.getBytes(StandardCharsets.UTF_8).length));
            response.setMessageBody(jsonObject);
            return response;
        }
        return new HttpResponse(500, "Internal Server Error");
    }
}
