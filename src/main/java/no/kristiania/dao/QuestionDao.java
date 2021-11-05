package no.kristiania.dao;

import no.kristiania.http.model.Question;

import javax.sql.DataSource;
import java.sql.*;

public class QuestionDao extends DataAccesObject<Question> {
    public QuestionDao(DataSource dataSource, String dbName) {
        super(dataSource, dbName);
    }

    @Override
    protected PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException {
        return connection.prepareStatement("insert into question(question,correct_answer) values(?,?)", Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void setGeneratedKeys(Question model, ResultSet generatedKeys) throws SQLException {
        model.setId(generatedKeys.getLong("id"));
    }

    @Override
    public void setFieldsToUpdateInDB(Question question, PreparedStatement statement) throws SQLException {
        statement.setString(1, question.getQuestion());
        statement.setString(2, question.getAnswer());
    }


    @Override
    protected Question mapValuesToObject(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestion(rs.getString("question"));
        question.setId(rs.getLong("id"));
        question.setAnswer(rs.getString("correct_answer"));
        return question;
    }

}
