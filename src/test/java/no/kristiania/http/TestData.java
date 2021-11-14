package no.kristiania.http;

import no.kristiania.http.dao.QuestionDao;
import no.kristiania.http.dao.QuestionnaireDao;
import no.kristiania.http.dao.UserDao;
import no.kristiania.http.model.Question;
import no.kristiania.http.model.QuestionType;
import no.kristiania.http.model.Questionnaire;
import no.kristiania.http.model.TextQuestion;
import no.kristiania.http.util.Authenticator;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TestData {

    public static User retrieveTestUser() throws SQLException {
        User user = new User();
        UserDao userDao = new UserDao(testDataSource());
        Authenticator authenticator = new Authenticator();
        user.setPassword("123");
        user.setFirstName("test");
        user.setLastName("user");
        user.setEmail(authenticator.encryptPass("test@user.com"));

        userDao.save(user);

        return user;
    }

    public static Questionnaire retrieveTestQuestionnaire() throws SQLException {
        Questionnaire questionnaire = new Questionnaire();
        QuestionnaireDao questionnaireDao = new QuestionnaireDao(testDataSource());

        questionnaire.setName("Test Questionnaire");
        questionnaire.setPersonId(retrieveTestUser().getId());

        questionnaireDao.save(questionnaire);

        return questionnaire;
    }

    public static Question retrieveTestQuestion() throws SQLException {
        Question question = new Question<>();
        QuestionDao questionDao = new QuestionDao(testDataSource());

        question.setQuestion("How are you?");
        question.setQuestionnaireId(retrieveTestQuestionnaire().getId());
        question.setQuestionType(QuestionType.text);

        questionDao.save(question);

        return question;
    }

    public static DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:questionnaire;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
