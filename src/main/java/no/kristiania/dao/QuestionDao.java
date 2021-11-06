package no.kristiania.dao;

import no.kristiania.http.model.Question;

import javax.sql.DataSource;
import java.sql.*;

public class QuestionDao extends DataAccessObject<Question> {
    public QuestionDao(DataSource dataSource, String dbName) {
        super(dataSource, dbName);
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
        statement.setLong(2, question.getQuestionaireId());
    }


    @Override
    protected Question mapValuesToObject(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestion(rs.getString("question"));
        question.setId(rs.getLong("id"));
        question.setQuestionaireId(rs.getLong("questionnaire_id"));
        return question;
    }

}
