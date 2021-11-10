package no.kristiania.dao;

import no.kristiania.http.model.AnswerOption;

import javax.sql.DataSource;
import java.sql.*;

public class AnswerOptionDao extends DataAccessObject<AnswerOption> {
    public AnswerOptionDao(DataSource dataSource) {
        super(dataSource,"answer_option");
    }

    @Override
    protected PreparedStatement createPreparedStatementForUpdate(Connection connection) throws SQLException {
        return connection.prepareStatement("update answer_option set answer_type = ?, question_id = ?, value = ?, name = ?");
    }

    @Override
    protected PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException {
        return connection.prepareStatement("insert into answer_option(answer_type," +
                "value,name,question_id) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void setGeneratedKeys(AnswerOption model, ResultSet generatedKeys) throws SQLException {
        model.setId(generatedKeys.getLong("id"));
    }

    @Override
    protected void setFieldsToUpdateInDB(AnswerOption model, PreparedStatement statement) throws SQLException {
        statement.setObject(1, model.getAnswerType().toString(), Types.OTHER);
        statement.setLong(2, model.getQuestionId());
        statement.setString(3, model.getValue());
        statement.setString(4, model.getName());
    }

    @Override
    protected AnswerOption mapValuesToObject(ResultSet rs) throws SQLException {
        AnswerOption answerOption = new AnswerOption();
        answerOption.setAnswerType(rs.getString("answer_type"));
        answerOption.setQuestionId(rs.getLong("question_id"));
        answerOption.setValue(rs.getString("value"));
        answerOption.setId(rs.getLong("id"));
        answerOption.setName(rs.getString("name"));
        return answerOption;
    }
}
