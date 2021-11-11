package no.kristiania.dao;

import no.kristiania.http.model.QuestionOptions;

import javax.sql.DataSource;
import java.sql.*;

public class RangeQuestionDao extends DataAccessObject<QuestionOptions> {
    public RangeQuestionDao(DataSource dataSource) {
        super(dataSource,"range_question");
    }

    @Override
    protected void executeSave(Connection connection, QuestionOptions model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into range_question(question_id, range, name_min_val, name_max_val) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, model.getQuestionId());
            statement.setInt(2, model.getRange());
            statement.setString(3, model.getNameMinVal());
            statement.setString(4, model.getNameMaxVal());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, QuestionOptions model) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement("update range_question set range = ?, name_min_val = ?, name_max_val = ? where id = ?")) {
            statement.setInt(1, model.getRange());
            statement.setString(2, model.getNameMinVal());
            statement.setString(3, model.getNameMaxVal());
            statement.setLong(4, model.getId());
            statement.executeUpdate();
        }
    }


    @Override
    protected QuestionOptions mapValuesToObject(ResultSet rs) throws SQLException {
        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setQuestionId(rs.getLong("question_id"));
        questionOptions.setId(rs.getLong("id"));
        questionOptions.setRange(rs.getInt("range"));
        questionOptions.setNameMinVal(rs.getString("name_min_val"));
        questionOptions.setNameMaxVal(rs.getString("name_max_val"));
        return questionOptions;
    }
}
