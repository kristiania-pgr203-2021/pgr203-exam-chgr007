package no.kristiania.http;

import no.kristiania.http.dao.*;
import no.kristiania.http.model.*;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.PostValue;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionnaireDaoTest {

    //DAOs
    UserDao userDao = new UserDao(createDataSource());
    QuestionnaireDao questionnaireDao = new QuestionnaireDao(createDataSource());
    QuestionDao questionDao = new QuestionDao(createDataSource());
    AnswerDao answerDao = new AnswerDao(createDataSource());
    RangeQuestionDao rangeQuestionDao = new RangeQuestionDao(createDataSource());
    TextQuestionDao textQuestionDao = new TextQuestionDao(createDataSource());
    RadioQuestionDao radioQuestionDao = new RadioQuestionDao(createDataSource());

    @Test
    void shouldSaveQuestionAndAnswersToDatabase() throws SQLException {
        Questionnaire questionnaire = randomFromDatabase(questionnaireDao);

        Question question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.text);
        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Jeg har det fint, men er litt sliten");
        answer1.setUserId(1);
        answerDao.save(answer1);

        Answer answer2 = new Answer();
        answer2.setQuestionId(question.getId());
        answer2.setAnswer("Livet suger as");
        answer2.setUserId(1);
        answerDao.save(answer2);

        Answer answer3 = new Answer();
        answer3.setQuestionId(question.getId());
        answer3.setAnswer(":-) nei");
        answer3.setUserId(1);

        answerDao.save(answer3);

        assertThat(answerDao.listByQuestionId(question.getId()))
                .extracting(Answer::getId)
                .contains(answer1.getId())
                .contains(answer2.getId())
                .contains(answer3.getId())
                .usingRecursiveComparison();
    }

    //used for internal databases
    private DataSource createDataSource() {

        return TestData.testDataSource();
    }


    private <T> T randomFromDatabase(DataAccessObject<T> dataAccessObject) throws SQLException {

        ArrayList<T> arrayList = dataAccessObject.retrieveAll();

        return arrayList.get(new Random().nextInt(arrayList.size()));
    }

    private <T> T getOne(T... options) {
        Random random = new Random();
        return options[random.nextInt(options.length)];
    }
}
