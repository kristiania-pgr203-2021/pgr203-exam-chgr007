package no.kristiania.http;

import no.kristiania.dao.AnswerDao;
import no.kristiania.dao.QuestionDao;
import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;
import org.checkerframework.checker.units.qual.A;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuestionaireDaoTest {

    @Test
    void shouldSaveQuestionToDatabase() throws IOException, SQLException {
        QuestionDao questionDao = new QuestionDao(createDataSource());
        Question question = new Question();
        question.setQuestion("Hva heter svenskekongen?");
        question.setAnswer("Carl-Gustav");
        questionDao.save(question);
        System.out.println(question.getId() + ":" + question.getQuestion() + " | " + question.getAnswer());

        Question questionFromDB = questionDao.retrieveById("question",question.getId());

        System.out.println(questionFromDB.getId() + ":" + questionFromDB.getQuestion() + " | " + questionFromDB.getAnswer());

        assertThat(question)
                .usingRecursiveComparison()
                .isEqualTo(questionFromDB);
    }

    @Test
    void shouldSaveQuestionAndAnswersToDatabase() throws SQLException {
        QuestionDao questionDao = new QuestionDao(createDataSource());
        AnswerDao answerDao = new AnswerDao(createDataSource());
        Question question = new Question();
        question.setQuestion("Hva heter svenskekongen?");
        question.setAnswer("Carl-Gustav");

        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Harald");

        Answer answer2 = new Answer();
        answer2.setQuestionId(question.getId());
        answer2.setAnswer("Magnus");

        Answer answer3 = new Answer();
        answer3.setQuestionId(question.getId());
        answer3.setAnswer("Carl-Gustav");

        assertThat(answerDao.retrieveByID(question.getId())
                .usingRecursiveComparison()
                .contains(answer1)
                .contains(answer2)
                .contains(answer3);
    }
    }

    @Test
    void getPropertiesWorksWithResourcesFile() {
        //TODO: Fjerne denne før man merger. Github actions vil nødvendigvis ikke ha filen :P
        Properties props = getProperties();
        assertEquals("test", props.getProperty("test"));
    }
    //used for internal databases
    private DataSource createDataSource() {
        //TODO: Oppdatere til dataSource.username osv. ihht. specs fra Johannes
        Properties prop = getProperties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("username"));
        dataSource.setPassword(prop.getProperty("password"));
        dataSource.setURL(prop.getProperty("URL"));

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
