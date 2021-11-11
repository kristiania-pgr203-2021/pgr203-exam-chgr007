package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.http.model.*;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.PostValue;
import no.kristiania.http.util.Properties;
import org.codehaus.jackson.map.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.postgresql.ds.PGSimpleDataSource;

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


    @BeforeAll
    void addStartData() throws SQLException {

        //adds user
        User user = userDao.retrieveByEmail("start@persson.no");
        if (user == null) {
            Authenticator authenticator = new Authenticator();
            user = new User();
            user.setEmail("start@persson.no");
            user.setFirstName("start");
            user.setLastName("Persson");
            String password = authenticator.encryptPass("420");
            user.setPassword(password);
            userDao.save(user);
        }
        //adds questionnaire
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName("Eksamensundersøkelse");
        questionnaire.setPersonId(user.getId());

        questionnaireDao.save(questionnaire);

        //adds questions
        Question question = new Question();
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestion("Hva tenker du om eksamen i Avansert Java?");
        question.setQuestionType(QuestionType.text);
        Question question1 = new Question();
        question1.setQuestionnaireId(questionnaire.getId());
        question1.setQuestion("På en skala fra 1-5, hvor stresset er du?");
        question1.setQuestionType(QuestionType.text);

        questionDao.save(question);
        questionDao.save(question1);

        //adds answers
        Answer answer = new Answer();
        answer.setQuestionId(question.getId());
        answer.setAnswer("Det går egentlig veldig greit!");
        answer.setUserId(user.getId());
        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Jeg skal overleve");
        answer1.setUserId(user.getId());

        Answer answer2 = new Answer();
        answer2.setQuestionId(question.getId());
        answer2.setAnswer("(-: nei");
        answer2.setUserId(user.getId());

        answerDao.save(answer);
        answerDao.save(answer1);
        answerDao.save(answer2);

        Answer answer3 = new Answer();
        answer3.setQuestionId(question1.getId());
        answer3.setAnswer("5");
        answer3.setUserId(user.getId());

        Answer answer4 = new Answer();
        answer4.setQuestionId(question1.getId());
        answer4.setAnswer("3");
        answer4.setUserId(user.getId());

        Answer answer5 = new Answer();
        answer5.setQuestionId(question1.getId());
        answer5.setAnswer("1");
        answer5.setUserId(user.getId());

        answerDao.save(answer3);
        answerDao.save(answer4);
        answerDao.save(answer5);

    }


    @Test
    void shouldSaveQuestionToDatabase() throws IOException, SQLException {
        Questionnaire questionnaire = randomFromDatabase(questionnaireDao);

        Question question = new Question();
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


    @Test
    void shouldUpdateSavedQuestion() throws SQLException {
        Questionnaire questionnaire = randomFromDatabase(questionnaireDao);

        Question question = new Question();
        question.setQuestion("Hvordan har du det i dag?");
        question.setQuestionnaireId(questionnaire.getId());
        question.setQuestionType(QuestionType.text);
        questionDao.save(question);

        Answer answer1 = new Answer();
        answer1.setQuestionId(question.getId());
        answer1.setAnswer("Jeg har det fint");
        answer1.setUserId(1);
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
        String email = "test@persson.no";
        User user = userDao.retrieveByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstName("test");
            user.setLastName("Persson");
            user.setPassword("123");
            userDao.save(user);
        }
        assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(userDao.retrieveById(user.getId()));
    }

    @Test
    void shouldAuthenticateUser() throws SQLException {
        Authenticator authenticator = new Authenticator();
        User user = userDao.retrieveByEmail("start@persson.no");
        String password = "420";
        String encryptedPass = authenticator.encryptPass(password);
        User userFromServer = userDao.retrieveById(user.getId());
        assertTrue(authenticator.validatePassword(password, user.getPassword()));
    }

    @Test
    void shouldGenerateTokenForUser() throws IOException {
        HttpServer server = new HttpServer(0);
        HttpClient client = new HttpClient("localhost", server.getPort());
        PostValue<String, String> userName = new PostValue<>("userName", "start@persson.no");
        PostValue<String, String> password = new PostValue<>("password", "420");
        client.post(List.of(userName, password), "/api/login");

        String token = client.getHeader("Set-Cookie");
        System.out.println(token);
        assertThat(token)
                .contains("AuthToken");

    }

    @Test
    void userAlreadyExists() throws IOException {
        HttpServer server = new HttpServer(0);
        HttpClient client = new HttpClient("localhost", server.getPort());

        PostValue<String,String> userName = new PostValue<>("userName", "start@persson.no");
        PostValue<String,String> firstName = new PostValue<>("firstName", "start");
        PostValue<String,String> lastName = new PostValue<>("lastName", "Persson");
        PostValue<String,String> password = new PostValue<>("password", "420");

        client.post(List.of(userName,firstName,lastName,password), "/api/signup");

        assertEquals(403, client.getStatusCode());
    }

    @Test
    void shouldInsertPlainTextAnswerOptionThrougAPI() throws IOException, SQLException {
        HttpServer server = new HttpServer(0);
        HttpClient client = new HttpClient("localhost", server.getPort());
        Question question = randomFromDatabase(questionDao);
        question.setQuestionType(QuestionType.text);
        question.setQuestion("Vil dette fungere?");
        question.setHasAnswerOptions(false);
        ObjectMapper objectMapper = new ObjectMapper();
        String JSONQuestion = objectMapper.writeValueAsString(question);
        PostValue<String, String> jsonPostString = new PostValue("json", JSONQuestion);

        client.post(List.of(jsonPostString), "/api/v2/question");
        List<Question> questionOptions = questionDao.retrieveAll();

        assertThat(questionOptions)
                .extracting(Question::getQuestion)
                .contains("Vil dette fungere?");
    }

    @Test
    void shouldInsertRadioAnswerOptionThrougAPI() throws IOException, SQLException {
        HttpServer server = new HttpServer(0);
        HttpClient client = new HttpClient("localhost", server.getPort());
        User user = randomFromDatabase(userDao);
        Question question = randomFromDatabase(questionDao);
        question.setQuestionType(QuestionType.radio);
        question.setQuestion("Is this working?");
        question.setHasAnswerOptions(true);

        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setQuestion("Jas");
        QuestionOptions questionOptions1 = new QuestionOptions();
        questionOptions1.setQuestion("Nei ass");

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

        RadioQuestionDao radioQuestionDao = new RadioQuestionDao(createDataSource());
        List<QuestionOptions> radioQuestionOptions = radioQuestionDao.retrieveAll();

        assertThat(radioQuestionOptions)
                .extracting(QuestionOptions::getQuestion)
                .contains("Jas", "Nei ass");
    }
    @Test
    void shouldInsertRangeAnswerOptionThrougAPI() throws IOException, SQLException {
        HttpServer server = new HttpServer(0);
        HttpClient client = new HttpClient("localhost", server.getPort());
        Question question = randomFromDatabase(questionDao);
        question.setQuestionType(QuestionType.range);
        question.setQuestion("Is this working?");
        question.setHasAnswerOptions(true);

        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setRange(10);
        questionOptions.setNameMaxVal("Helt fantastisk bra");
        questionOptions.setNameMinVal("Helt sjukt dårlig :(");
        question.addAnswerOption(questionOptions);

        ObjectMapper objectMapper = new ObjectMapper();
        String JSONQuestion = objectMapper.writeValueAsString(question);
        PostValue<String, String> jsonPostString = new PostValue("json", JSONQuestion);

        client.post(List.of(jsonPostString), "/api/v2/question");
        List<Question> questions = questionDao.retrieveAll();

        assertThat(questions)
                .extracting(Question::getQuestion)
                .contains("Is this working?");

        RangeQuestionDao rangeQuestionDao = new RangeQuestionDao(createDataSource());
        List<QuestionOptions> rangeQuestionOptions = rangeQuestionDao.retrieveAll();

        assertThat(rangeQuestionOptions)
                .extracting(QuestionOptions::getNameMaxVal)
                .contains("Helt fantastisk bra");
    }
    @Test
    void shouldRegisterAnswerOption() throws SQLException {
        Question question = randomFromDatabase(questionDao);

        QuestionOptions questionOptions = new QuestionOptions();
        question.setQuestionType(QuestionType.range);
        questionOptions.setQuestionId(question.getId());
        questionOptions.setRange(0);
        questionOptions.setNameMinVal("min");
        questionOptions.setNameMaxVal("max");

        rangeQuestionDao.save(questionOptions);

        QuestionOptions questionOptionsFromServer = rangeQuestionDao.retrieveById(questionOptions.getId());
        assertThat(questionOptionsFromServer)
                .usingRecursiveComparison()
                .isEqualTo(questionOptions);

    }

    //used for internal databases
    private DataSource createDataSource() {
        Properties prop = new Properties();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("dataSource.username"));
        dataSource.setPassword(prop.getProperty("dataSource.password"));
        dataSource.setURL(prop.getProperty("dataSource.url"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        return dataSource;
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
