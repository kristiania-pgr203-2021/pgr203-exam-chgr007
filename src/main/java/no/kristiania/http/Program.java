package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.http.model.*;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.Properties;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;


public class Program {


    private static final Logger logger = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) throws IOException, SQLException {
        int port = getPort(args);
        HttpServer server = new HttpServer(port, createDataSource());

        //IF YOU WANT START DATA, MAKE TRUE
        Program.addStartData(false);
    }

    private static int getPort(String[] args) {
        int port = 8080;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--port")) {
                try {
                    port = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    logger.error("Port must be a number!");
                }
            }
        }
        return port;
    }

    private static void addStartData(Boolean bool) throws SQLException {
        if(bool){

            UserDao userDao = new UserDao(createDataSource());
            QuestionnaireDao questionnaireDao = new QuestionnaireDao(createDataSource());
            QuestionDao questionDao = new QuestionDao(createDataSource());
            AnswerDao answerDao = new AnswerDao(createDataSource());
            RangeQuestionDao rangeQuestionDao = new RangeQuestionDao(createDataSource());
            TextQuestionDao textQuestionDao = new TextQuestionDao(createDataSource());
            RadioQuestionDao radioQuestionDao = new RadioQuestionDao(createDataSource());

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
            question1.setQuestionType(QuestionType.range);
            Question question2 = new Question();
            question2.setQuestionnaireId(questionnaire.getId());
            question2.setQuestion("Hva liker du best med Avansert Java?");
            question2.setQuestionType(QuestionType.radio);

            questionDao.save(question);
            questionDao.save(question1);
            questionDao.save(question2);

            //adds questionOptions
            TextQuestion textQuestion = new TextQuestion();
            textQuestion.setQuestionId(question.getId());
            textQuestion.setMaxChars(250);
            textQuestion.setPlaceholder("Skriv et par setninger");

            textQuestionDao.save(textQuestion);

            RangeQuestion rangeQuestion = new RangeQuestion();
            rangeQuestion.setQuestionId(question1.getId());
            rangeQuestion.setMin(0);
            rangeQuestion.setMax(5);
            rangeQuestion.setMinLabel("min");
            rangeQuestion.setMaxLabel("max");

            rangeQuestionDao.save(rangeQuestion);

            RadioQuestion radioQuestion = new RadioQuestion();
            radioQuestion.setQuestionId(question2.getId());
            radioQuestion.setChoice("Læreren");

            radioQuestionDao.save(radioQuestion);

            RadioQuestion radioQuestion1 = new RadioQuestion();
            radioQuestion1.setQuestionId(question2.getId());
            radioQuestion1.setChoice("At det er avansert");

            radioQuestionDao.save(radioQuestion1);

            RadioQuestion radioQuestion2 = new RadioQuestion();
            radioQuestion2.setQuestionId(question2.getId());
            radioQuestion2.setChoice("At det er Java");

            radioQuestionDao.save(radioQuestion2);


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

    }

    // TODO: Kanskje flytte datasource og properties ut til en egen klasse
    private static DataSource createDataSource() {
        Properties prop = new Properties();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUser(prop.getProperty("dataSource.username"));
        dataSource.setPassword(prop.getProperty("dataSource.password"));
        dataSource.setURL(prop.getProperty("dataSource.url"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
