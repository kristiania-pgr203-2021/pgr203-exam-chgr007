package no.kristiania.http;

import no.kristiania.http.dao.QuestionDao;
import no.kristiania.http.dao.RadioQuestionDao;
import no.kristiania.http.dao.RangeQuestionDao;
import no.kristiania.http.dao.TextQuestionDao;
import no.kristiania.http.model.*;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuestionDaoTest {

    DataSource dataSource = TestData.testDataSource();

    User user = TestData.retrieveTestUser();
    Questionnaire questionnaire = TestData.retrieveTestQuestionnaire();

    //Tests for testing questions

    QuestionDao questionDao = new QuestionDao(dataSource);
    RadioQuestionDao radioQuestionDao = new RadioQuestionDao(dataSource);
    RangeQuestionDao rangeQuestionDao = new RangeQuestionDao(dataSource);
    TextQuestionDao textQuestionDao = new TextQuestionDao(dataSource);

    public QuestionDaoTest() throws SQLException {
    }

    @Test
    void shouldSaveQuestionToDatabase() throws IOException, SQLException {

        Question<TextQuestion> question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.text);

        questionDao.save(question);

        Question questionFromDB = questionDao.retrieveById(question.getId());
        assertThat(question)
                .usingRecursiveComparison()
                .isEqualTo(questionFromDB);
    }

    @Test
    void shouldSaveRadioQuestion() throws SQLException {
        Question<RadioQuestion> question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.radio);
        question.setHasAnswerOptions(true);
        question.setQuestionnaireId(questionnaire.getId());


        RadioQuestion radioQuestion = new RadioQuestion();
        radioQuestion.setChoice("Jeg har det fint");

        questionDao.save(question);
        radioQuestion.setQuestionId(question.getId());

        radioQuestionDao.save(radioQuestion);

        RadioQuestion radioQuestionFromDB = radioQuestionDao.retrieveById(radioQuestion.getId());
        assertThat(radioQuestion.getQuestion())
                .isEqualTo(radioQuestionFromDB.getQuestion());

    }

    @Test
    void shouldSaveRangeQuestion() throws SQLException {
        Question<RadioQuestion> question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.radio);
        questionDao.save(question);

        RangeQuestion rangeQuestion = new RangeQuestion();
        rangeQuestion.setMin(0);
        rangeQuestion.setMax(5);
        rangeQuestion.setMaxLabel("Jeg har det fint");
        rangeQuestion.setMinLabel("Alt suger");
        rangeQuestion.setQuestionId(question.getId());

        rangeQuestionDao.save(rangeQuestion);

        RangeQuestion rangeQuestionFromDB = rangeQuestionDao.retrieveById(rangeQuestion.getId());
        assertThat(rangeQuestion)
                .usingRecursiveComparison()
                .isEqualTo(rangeQuestionFromDB);

    }

    @Test
    void shouldUpdateSavedQuestion() throws SQLException {

        Question question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.text);
        questionDao.save(question);

        question.setQuestion("Liker du fiskeboller?");
        questionDao.update(question);

        Question questionFromServer = questionDao.retrieveById(question.getId());
        assertThat(questionFromServer)
                .usingRecursiveComparison()
                .isEqualTo(question);
    }
}
