package no.kristiania.dao;

import no.kristiania.http.model.QuestionOptions;
import no.kristiania.http.model.RangeQuestion;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RangeQuestionDao extends DataAccessObject<RangeQuestion> {
    public RangeQuestionDao(DataSource dataSource) {
        super(dataSource,"range_question");
    }

    @Override
    protected void executeSave(Connection connection, RangeQuestion model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into range_question(question_id, min, max, min_label, max_label) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, model.getQuestionId());
            statement.setInt(2, model.getMin());
            statement.setInt(3, model.getMax());
            statement.setString(4, model.getMinLabel());
            statement.setString(5, model.getMaxLabel());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, RangeQuestion model) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement("update range_question set min = ?, max = ?, min_label = ?, max_label = ? where id = ?")) {
            statement.setInt(1, model.getMin());
            statement.setInt(2, model.getMax());
            statement.setString(3, model.getMinLabel());
            statement.setString(4, model.getMaxLabel());
            statement.setLong(5, model.getId());
            statement.executeUpdate();
        }
    }

    public RangeQuestion fetchByQuestionId(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from range_question where question_id = ?")) {
                statement.setLong(1,id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    resultSet.next();
                    return mapValuesToObject(resultSet);
                }
            }
        }
    }
    @Override
    protected RangeQuestion mapValuesToObject(ResultSet rs) throws SQLException {
        RangeQuestion rangeQuestion = new RangeQuestion();
        rangeQuestion.setQuestionId(rs.getLong("question_id"));
        rangeQuestion.setId(rs.getLong("id"));
        rangeQuestion.setMin(rs.getInt("min"));
        rangeQuestion.setMax(rs.getInt("max"));
        rangeQuestion.setMinLabel(rs.getString("min_label"));
        rangeQuestion.setMaxLabel(rs.getString("max_label"));
        return rangeQuestion;
    }
}
