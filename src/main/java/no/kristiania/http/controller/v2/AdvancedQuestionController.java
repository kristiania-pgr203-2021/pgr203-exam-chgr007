package no.kristiania.http.controller.v2;

import no.kristiania.dao.AnswerOptionDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.http.controller.HttpController;
import no.kristiania.http.model.AnswerOption;
import no.kristiania.http.model.AnswerType;
import no.kristiania.http.model.Question;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

public class AdvancedQuestionController implements HttpController {
    private QuestionDao questionDao;
    private AnswerOptionDao answerOptionDao;

    public AdvancedQuestionController(QuestionDao questionDao, AnswerOptionDao answerOptionDao) {
        this.questionDao = questionDao;
        this.answerOptionDao = answerOptionDao;
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

                if (question.getHasAnswerOptions()) {
                    for (AnswerOption option : question.getAnswerOptionList()) {
                        option.setQuestionId(question.getId());
                        answerOptionDao.save(option);
                    }
                }
                String responseBody = objectMapper.writeValueAsString(question);
                httpResponse.setHeaderField("Content-Type", "application/json");
                httpResponse.setHeaderField("Content-Length", String.valueOf(responseBody.getBytes(StandardCharsets.UTF_8).length));
                httpResponse.setMessageBody(responseBody);
                return httpResponse;
            }
        } else if (request.getRequestType().equalsIgnoreCase("get")) {
            Question question = new Question();
            question.setQuestion("question");
            question.setId(1);
            question.setQuestionnaireId(1);
            AnswerOption answerOption = new AnswerOption();
            answerOption.setId(1);
            answerOption.setAnswerType("radio");
            answerOption.setName("name");
            answerOption.setValue("value");
            question.addAnswerOption(answerOption);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonObject = objectMapper.writeValueAsString(question);
            HttpResponse response = new HttpResponse(200, "OK");
            response.setMessageBody(jsonObject);
            return response;
        }
        return null;
    }
}
