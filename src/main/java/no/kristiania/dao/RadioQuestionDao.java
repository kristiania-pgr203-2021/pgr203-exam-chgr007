package no.kristiania.dao;

import no.kristiania.http.model.QuestionOptions;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RadioQuestionDao extends DataAccessObject<QuestionOptions> {
    public RadioQuestionDao(DataSource dataSource) {
        super(dataSource, "radio_question");
    }

    @Override
    protected void executeSave(Connection connection, QuestionOptions model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into radio_question(question_id, question) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, model.getQuestionId());
            statement.setString(2, model.getQuestion());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, QuestionOptions model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update radio_question set question = ? where id = ?")) {
            statement.executeUpdate();
        }
    }

    @Override
    protected QuestionOptions mapValuesToObject(ResultSet rs) throws SQLException {
        QuestionOptions questionOptions = new QuestionOptions();
        questionOptions.setId(rs.getLong("id"));
        questionOptions.setQuestionId(rs.getLong("question_id"));
        questionOptions.setQuestion(rs.getString("question"));
        return questionOptions;
    }
    public List<QuestionOptions> fetchAllByQuestionId(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from radio_question where question_id = ?")) {
                statement.setLong(1,id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<QuestionOptions> questionOptions = new ArrayList<>();
                    while(resultSet.next()) {
                        questionOptions.add(mapValuesToObject(resultSet));
                    }
                    return questionOptions;
                }
            }
        }
    }

}
