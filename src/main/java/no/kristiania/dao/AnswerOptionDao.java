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
        return connection.prepareStatement("update answer_option set answer_option = ?, answer_range = ?, answer_range_max_name = ?, answer_range_min_name = ?, answer_id = ?");
    }

    @Override
    protected PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException {
        return connection.prepareStatement("insert into answer_option(answer_option,answer_range," +
                "answer_range_max_name,answer_range_min_name,answer_id) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void setGeneratedKeys(AnswerOption model, ResultSet generatedKeys) throws SQLException {
        model.setId(generatedKeys.getLong("id"));
    }

    @Override
    protected void setFieldsToUpdateInDB(AnswerOption model, PreparedStatement statement) throws SQLException {
        statement.setString(1, model.getAnswerOption());
        statement.setInt(2, model.getRange());
        statement.setString(3, model.getMaxRangeName());
        statement.setString(4, model.getMinRangeName());
        statement.setLong(5, model.getAnswerId());
    }

    @Override
    protected AnswerOption mapValuesToObject(ResultSet rs) throws SQLException {
        AnswerOption answerOption = new AnswerOption();
        answerOption.setAnswerOption(rs.getString("answer_option"));
        answerOption.setRange(rs.getInt("answer_range"));
        answerOption.setAnswerId(rs.getLong("answer_id"));
        answerOption.setId(rs.getLong("id"));
        answerOption.setMaxRangeName(rs.getString("answer_range_max_name"));
        answerOption.setMinRangeName(rs.getString("answer_range_min_name"));
        return answerOption;
    }
}
