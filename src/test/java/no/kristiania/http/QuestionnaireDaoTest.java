package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;
import no.kristiania.http.model.Questionnaire;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.PostValue;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QuestionnaireDaoTest {

    //DAOs
    UserDao userDao = new UserDao(createDataSource());
    QuestionnaireDao questionnaireDao = new QuestionnaireDao(createDataSource(), "questionnaire");
    QuestionDao questionDao = new QuestionDao(createDataSource(), "question");
    AnswerDao answerDao = new AnswerDao(createDataSource(), "answer");


    @BeforeAll
    void addStartData() throws SQLException {

        //adds user
        User user = new User();
        user.setEmail("start@persson.no");
        user.setFirstName("start");
        user.setLastName("Persson");
        user.setPassword("420");
        userDao.save(user);

        //adds questionnaire
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName("Eksamensundersøkelse");
        questionnaire.setPersonId(user.getId());

        questionnaireDao.save(questionnaire);

        //adds questions
        Question question = new Question();
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestion("Hva tenker du om eksamen i Avansert Java?");

        Question question1 = new Question();
        question1.setQuestionnaireId(questionnaire.getId());
        question1.setQuestion("På en skala fra 1-5, hvor stresset er du?");

        questionDao.save(question);
        questionDao.save(question1);

        //adds answers
        Answer answer = new Answer();
        answer.setQuestionId(question.getId());
        answer.setAnswer("Det går egentlig veldig greit!");

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Jeg skal overleve");

        Answer answer2 = new Answer();
        answer2.setQuestionId(question.getId());
        answer2.setAnswer("(-: nei");

        answerDao.save(answer);
        answerDao.save(answer1);
        answerDao.save(answer2);

        Answer answer3 = new Answer();
        answer3.setQuestionId(question1.getId());
        answer3.setAnswer("5");

        Answer answer4 = new Answer();
        answer4.setQuestionId(question1.getId());
        answer4.setAnswer("3");

        Answer answer5 = new Answer();
        answer5.setQuestionId(question1.getId());
        answer5.setAnswer("1");

        answerDao.save(answer3);
        answerDao.save(answer4);
        answerDao.save(answer5);

    }

    /*
    @Test
    void shouldSaveQuestionToDatabase() throws IOException, SQLException {
        Questionnaire questionnaire = randomFromDatabase(questionnaireDao);

        Question question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());

        questionDao.save(question);

        Question questionFromDB = questionDao.retrieveById(question.getId());
        assertThat(question)
                .usingRecursiveComparison()
                .isEqualTo(questionFromDB);
    }

    @Test
    void shouldSaveQuestionAndAnswersToDatabase() throws SQLException {
        Questionnaire questionnaire = randomFromDatabase(questionnaireDao);

        Question question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Jeg har det fint, men er litt sliten");
        answerDao.save(answer1);

        Answer answer2 = new Answer();
        answer2.setQuestionId(question.getId());
        answer2.setAnswer("Livet suger as");
        answerDao.save(answer2);

        Answer answer3 = new Answer();
        answer3.setQuestionId(question.getId());
        answer3.setAnswer(":-) nei");
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
        Questionnaire questionnaire = randomFromDatabase(questionnaireDao);

        Question question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Jeg har det fint");
        answerDao.save(answer1);

        question.setQuestion("Liker du fiskeboller?");
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
        user.setPassword("123");
        userDao.save(user);

        assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(userDao.retrieveById(user.getId()));
    }*/

    @Test
    void shouldAuthenticateUser() throws SQLException {
        Authenticator authenticator = new Authenticator();
        UserDao userDao = new UserDao(createDataSource());
        User user = new User();
        user.setFirstName("Person");
        user.setLastName("Per");
        user.setEmail("perpersson@person.no");
        String password = "superSe<r3tP4ssw0rd";
        String encryptedPass = authenticator.encryptPass(password);
        user.setPassword(encryptedPass);

        userDao.save(user);

        User userFromServer = userDao.retrieveById(user.getId());
        assertTrue(authenticator.validatePassword(password, userFromServer.getPassword()));
    }

    @Test
    void shouldGenerateTokenForUser() throws IOException {
        HttpServer server = new HttpServer(0);
        HttpClient client = new HttpClient("localhost", server.getPort());
        PostValue<String,String> userName = new PostValue<>("userName", "perpersson@person.no");
        PostValue<String,String> password = new PostValue<>("password", "superSe<r3tP4ssw0rd");
        client.post(List.of(userName,password), "/api/login");

        String token = client.getHeader("Set-Cookie");
        System.out.println(token);
        assertThat(token)
                .contains("AuthToken");

    }

    @Test
    void shouldRegisterUser() throws IOException, SQLException {
        HttpServer server = new HttpServer(0);
        HttpClient client = new HttpClient("localhost", server.getPort());

        PostValue<String,String> firstName = new PostValue<>("firstName", "Per");
        PostValue<String,String> lastName = new PostValue<>("lastName", "Persson");
        PostValue<String,String> userName = new PostValue<>("userName", "perpersson@person.no");
        PostValue<String,String> password = new PostValue<>("password", "superSe<r3tP4ssw0rd");

        client.post(List.of(userName,password,firstName,lastName), "/api/signup");

        UserDao userDao = new UserDao(createDataSource());
        User user = userDao.retrieveByEmail("perpersson@person.no");

        assertThat(user)
                .extracting(User::getEmail)
                .isEqualTo("perpersson@person.no");
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

    private <T> T randomFromDatabase(DataAccessObject<T> dataAccessObject) throws SQLException {

        ArrayList<T> arrayList = dataAccessObject.retrieveAll();

        return arrayList.get(new Random().nextInt(arrayList.size()));
    }

    private <T> T getOne(T... options) {
        Random random = new Random();
        return options[random.nextInt(options.length)];
    }
}
