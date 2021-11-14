package no.kristiania.http;

import no.kristiania.http.dao.AnswerDao;
import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class AnswerDaoTest {

    DataSource dataSource = TestData.testDataSource();

    AnswerDao answerDao = new AnswerDao(dataSource);
    Question question = TestData.retrieveTestQuestion();
    User user = TestData.retrieveTestUser();

    public AnswerDaoTest() throws SQLException {
    }

    @Test
    void addNewAnswerToQuestion() throws SQLException {
        Answer answer = new Answer();
        answer.setAnswer("I'm good!");
        answer.setQuestionId(question.getId());
        answer.setUserId(user.getId());

        answerDao.save(answer);

        Answer answerFromDb = answerDao.retrieveById(answer.getId());

        assertThat(answer)
                .usingRecursiveComparison()
                .isEqualTo(answerFromDb);
    }
}
