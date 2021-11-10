package no.kristiania.dao;

import no.kristiania.http.model.Answer;
import no.kristiania.http.model.Question;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao extends DataAccessObject<Question> {
    public QuestionDao(DataSource dataSource) {
        super(dataSource, "question");
    }

    @Override
    protected PreparedStatement createPreparedStatementForUpdate(Connection connection) throws SQLException {
        return connection.prepareStatement("update question set question = ?, questionnaire_id = ?");
    }

    @Override
    protected PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException {
        return connection.prepareStatement("insert into question(question, questionnaire_id) values(?,?)", Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void setGeneratedKeys(Question model, ResultSet generatedKeys) throws SQLException {
        model.setId(generatedKeys.getLong("id"));
    }

    @Override
    public void setFieldsToUpdateInDB(Question question, PreparedStatement statement) throws SQLException {
        statement.setString(1, question.getQuestion());
        statement.setLong(2, question.getQuestionnaireId());
    }


    @Override
    protected Question mapValuesToObject(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestion(rs.getString("question"));
        question.setId(rs.getLong("id"));
        question.setQuestionnaireId(rs.getLong("questionnaire_id"));
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
