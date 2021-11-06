package no.kristiania.http;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.dao.UserDao;
import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionaireDaoTest {

    @Test
    void shouldSaveQuestionToDatabase() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(createDataSource(), "question");
        Question question = new Question();
        question.setQuestion("Hva heter svenskekongen?");
        question.setAnswer("Carl-Gustav");
        questionDao.save(question);
        System.out.println(question.getId() + ":" + question.getQuestion() + " | " + question.getAnswer());
        Question questionFromDB = questionDao.retrieveById(question.getId());
        System.out.println(questionFromDB.getId() + ":" + questionFromDB.getQuestion() + " | " + questionFromDB.getAnswer());
        assertThat(question)
                .usingRecursiveComparison()
                .isEqualTo(questionFromDB);
    }

    @Test
    void shouldSaveQuestionAndAnswersToDatabase() throws SQLException {
        QuestionDao questionDao = new QuestionDao(createDataSource(), "question");
        AnswerDao answerDao = new AnswerDao(createDataSource(), "answer");
        Question question = new Question();
        question.setQuestion("Hva heter svenskekongen?");
        question.setAnswer("Carl-Gustav");

        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Harald");
        answerDao.save(answer1);
        Answer answer2 = new Answer();
        answer2.setQuestionId(question.getId());
        answer2.setAnswer("Magnus");
        answerDao.save(answer2);
        Answer answer3 = new Answer();
        answer3.setQuestionId(question.getId());
        answer3.setAnswer("Carl-Gustav");
        answerDao.save(answer3);

        assertThat(answerDao.listByQuestionId(question.getId()))
                .extracting(Answer::getId)
                .contains(answer1.getId())
                .contains(answer2.getId())
                .contains(answer3.getId())
                .usingRecursiveComparison();
    }


    @Test
    void shouldUpdateSavedQuestion() throws SQLException {
        QuestionDao questionDao = new QuestionDao(createDataSource(), "question");
        AnswerDao answerDao = new AnswerDao(createDataSource(), "answer");
        Question question = new Question();
        question.setQuestion("Hva heter svenskekongen?");
        question.setAnswer("Carl-Gustav");

        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Harald");
        answerDao.save(answer1);

        question.setQuestion("Hva heter norskekongen?");
        questionDao.update(question);

        Question questionFromServer = questionDao.retrieveById(question.getId());
        assertThat(questionFromServer)
                .usingRecursiveComparison()
                .isEqualTo(question);
    }

    @Test
    void shouldAddUser() throws SQLException {
        User user = new User();
        user.setEmail("test@persson.no");
        user.setFirstName("test");
        user.setLastName("Persson");

        UserDao userDao = new UserDao(createDataSource());
        userDao.save(user);

        assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(userDao.retrieveById(user.getId()));
    }

    //used for internal databases
    private DataSource createDataSource() {
        //TODO: Oppdatere til dataSource.username osv. ihht. specs fra Johannes
        Properties prop = getProperties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("dataSource.username"));
        dataSource.setPassword(prop.getProperty("dataSource.password"));
        dataSource.setURL(prop.getProperty("dataSource.url"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        return dataSource;
    }

    private Properties getProperties() {
        Properties properties = new Properties();

        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("pgr203.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
