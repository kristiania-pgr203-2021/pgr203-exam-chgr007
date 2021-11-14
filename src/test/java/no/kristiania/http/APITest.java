package no.kristiania.http;

import no.kristiania.http.dao.QuestionDao;
import no.kristiania.http.dao.QuestionnaireDao;
import no.kristiania.http.dao.RadioQuestionDao;
import no.kristiania.http.dao.RangeQuestionDao;
import no.kristiania.http.model.*;
import no.kristiania.http.util.PostValue;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class APITest {

    DataSource dataSource = TestData.testDataSource();
    Questionnaire questionnaire = TestData.retrieveTestQuestionnaire();
    QuestionDao questionDao = new QuestionDao(dataSource);

    public APITest() throws SQLException {
    }

    @Test
    void shouldInsertPlainTextAnswerOptionThrougAPI() throws IOException, SQLException {
        HttpServer server = new HttpServer(0, dataSource);
        HttpClient client = new HttpClient("localhost", server.getPort());
        Question<TextQuestion> question = new Question<>();
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.text);
        question.setQuestion("Vil dette fungere?");
        question.setHasAnswerOptions(false);
        ObjectMapper objectMapper = new ObjectMapper();
        String JSONQuestion = objectMapper.writeValueAsString(question);
        PostValue<String, String> jsonPostString = new PostValue("json",JSONQuestion);

        client.post(List.of(jsonPostString), "/api/v2/question");
        List<Question> questionOptions = questionDao.retrieveAll();

        assertThat(questionOptions)
                .extracting(Question::getQuestion)
                .contains("Vil dette fungere?");
    }

    @Test
    void shouldInsertRadioAnswerOptionThrougAPI() throws IOException, SQLException {
        HttpServer server = new HttpServer(0, dataSource);
        HttpClient client = new HttpClient("localhost", server.getPort());
        Question<RadioQuestion> question = new Question<>();
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.radio);
        question.setQuestion("Is this working?");
        question.setHasAnswerOptions(true);

        RadioQuestion questionOptions = new RadioQuestion();
        questionOptions.setChoice("Jas");
        RadioQuestion questionOptions1 = new RadioQuestion();
        questionOptions1.setChoice("Nei ass");

        question.addAnswerOption(questionOptions);
        question.addAnswerOption(questionOptions1);

        ObjectMapper objectMapper = new ObjectMapper();
        String JSONQuestion = objectMapper.writeValueAsString(question);
        PostValue<String, String> jsonPostString = new PostValue("json", JSONQuestion);

        client.post(List.of(jsonPostString), "/api/v2/question");
        List<Question> questions = questionDao.retrieveAll();

        assertThat(questions)
                .extracting(Question::getQuestion)
                .contains("Is this working?");

        RadioQuestionDao radioQuestionDao = new RadioQuestionDao(dataSource);
        List<RadioQuestion> radioQuestionOptions = radioQuestionDao.retrieveAll();

        assertThat(radioQuestionOptions)
                .extracting(RadioQuestion::getChoice)
                .contains("Jas", "Nei ass");
    }
    @Test
    void shouldInsertRangeAnswerOptionThroughAPI() throws IOException, SQLException {
        HttpServer server = new HttpServer(0, dataSource);
        HttpClient client = new HttpClient("localhost", server.getPort());
        Question<RangeQuestion> question = new Question<>();
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.range);
        question.setQuestion("Er alt i orden?");
        question.setHasAnswerOptions(true);

        RangeQuestion questionOptions = new RangeQuestion();
        questionOptions.setMin(0);
        questionOptions.setMax(10);
        questionOptions.setMaxLabel("JA!");
        questionOptions.setMinLabel("Over hodet ikke");
        question.addAnswerOption(questionOptions);

        ObjectMapper objectMapper = new ObjectMapper();
        String JSONQuestion = objectMapper.writeValueAsString(question);
        PostValue<String, String> jsonPostString = new PostValue("json", JSONQuestion);

        client.post(List.of(jsonPostString), "/api/v2/question");
        List<Question> questions = questionDao.retrieveAll();
        System.out.println(client.getStatusCode());
        assertThat(questions)
                .extracting(Question::getQuestion)
                .contains("Er alt i orden?");

        RangeQuestionDao rangeQuestionDao = new RangeQuestionDao(dataSource);
        List<RangeQuestion> rangeQuestionOptions = rangeQuestionDao.retrieveAll();

        assertThat(rangeQuestionOptions)
                .extracting(RangeQuestion::getMaxLabel)
                .contains("JA!");
    }
}
