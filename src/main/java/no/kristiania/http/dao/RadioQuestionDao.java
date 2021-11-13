package no.kristiania.http.dao;

import no.kristiania.http.model.RadioQuestion;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RadioQuestionDao extends DataAccessObject<RadioQuestion> {
    public RadioQuestionDao(DataSource dataSource) {
        super(dataSource, "radio_question");
    }

    @Override
    protected void executeSave(Connection connection, RadioQuestion model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into radio_question(question_id, choice) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, model.getQuestionId());
            statement.setString(2, model.getChoice());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, RadioQuestion model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update radio_question set choice = ? where id = ?")) {
            statement.setString(1, model.getChoice());
            statement.setLong(2, model.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected RadioQuestion mapValuesToObject(ResultSet rs) throws SQLException {
        RadioQuestion radioQuestion = new RadioQuestion();
        radioQuestion.setId(rs.getLong("id"));
        radioQuestion.setQuestionId(rs.getLong("question_id"));
        radioQuestion.setChoice(rs.getString("choice"));
        return radioQuestion;
    }

    public List<RadioQuestion> fetchAllByQuestionId(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from radio_question where question_id = ?")) {
                statement.setLong(1,id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<RadioQuestion> radioQuestions = new ArrayList<>();
                    while(resultSet.next()) {
                        radioQuestions.add(mapValuesToObject(resultSet));
                    }
                    return radioQuestions;
                }
            }
        }
    }

}
