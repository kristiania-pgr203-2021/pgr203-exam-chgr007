package no.kristiania.http;

import no.kristiania.http.dao.QuestionnaireDao;
import no.kristiania.http.dao.UserDao;
import no.kristiania.http.model.Questionnaire;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class TestData {

    public static User retrieveTestUser() throws SQLException {
        User user = new User();
        UserDao userDao = new UserDao(testDataSource());

        user.setPassword("123");
        user.setFirstName("test");
        user.setLastName("user");
        user.setEmail("test@user.com");

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

    public static DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:questionnaire;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
