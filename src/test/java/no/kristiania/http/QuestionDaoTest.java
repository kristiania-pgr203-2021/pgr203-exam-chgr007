package no.kristiania.http;

import no.kristiania.http.dao.QuestionDao;
import no.kristiania.http.dao.RadioQuestionDao;
import no.kristiania.http.dao.RangeQuestionDao;
import no.kristiania.http.dao.TextQuestionDao;
import no.kristiania.http.model.Question;
import no.kristiania.http.model.QuestionType;
import no.kristiania.http.model.RadioQuestion;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

public class QuestionDaoTest {

    DataSource dataSource = TestData.testDataSource();

    //Tests for testing questions

    QuestionDao questionDao = new QuestionDao(dataSource);
    RadioQuestionDao radioQuestionDao = new RadioQuestionDao(dataSource);
    RangeQuestionDao rangeQuestionDao = new RangeQuestionDao(dataSource);
    TextQuestionDao textQuestionDao = new TextQuestionDao(dataSource);

    @Test
    void shouldSaveRadioQuestion() throws SQLException {

        Question question = new Question();

        question.setQuestionType(QuestionType.radio);
        question.setQuestion("Liker du ost?");

        questionDao.save(question);

        RadioQuestion radioQuestion = new RadioQuestion();

        radioQuestion.setChoice("Ja");
        radioQuestion.setQuestionId(question.getId());

        radioQuestionDao.save(radioQuestion);

        assertThat

    }
}
