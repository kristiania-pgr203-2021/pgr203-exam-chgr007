package no.kristiania.dao;

import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;
import no.kristiania.http.model.QuestionType;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao extends DataAccessObject<Question> {
    public QuestionDao(DataSource dataSource) {
        super(dataSource, "question");
    }


    @Override
    protected void executeSave(Connection connection, Question model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into question(question, questionnaire_id,answer_type) values(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getQuestion());
            statement.setLong(2, model.getQuestionnaireId());
            statement.setObject(3, model.getQuestionType().toString(),Types.OTHER);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, Question model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update question set question = ? where id = ?")) {
            statement.setString(1, model.getQuestion());
            statement.setLong(2, model.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected Question mapValuesToObject(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestion(rs.getString("question"));
        question.setId(rs.getLong("id"));
        question.setQuestionnaireId(rs.getLong("questionnaire_id"));
        question.setQuestionType(QuestionType.valueOf(rs.getString("answer_type")));
        return question;
    }

    public List<Question> listByQuestionnaireId(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from question where questionnaire_id = ?")) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Question> questions = new ArrayList<>();
                    while(resultSet.next()) {
                        questions.add(
                                mapValuesToObject(resultSet)
                        );
                    }
                    return questions;
                }
            }
        }
    }

}
