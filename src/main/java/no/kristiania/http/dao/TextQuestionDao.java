package no.kristiania.http.dao;

import no.kristiania.http.model.TextQuestion;

import javax.sql.DataSource;
import java.sql.*;

public class TextQuestionDao extends DataAccessObject<TextQuestion>{

    public TextQuestionDao(DataSource dataSource) {
        super(dataSource, "text_question");
    }


    @Override
    protected void executeSave(Connection connection, TextQuestion model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into text_question(question_id, placeholder, max_chars) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, model.getQuestionId());
            statement.setString(2, model.getPlaceholder());
            statement.setInt(3, model.getMaxChars());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, TextQuestion model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update text_question set placeholder = ?, max_chars = ? where id = ?")) {
            statement.setString(1, model.getPlaceholder());
            statement.setInt(2, model.getMaxChars());
            statement.setLong(3, model.getId());

            statement.executeUpdate();
        }
    }

    @Override
    protected TextQuestion mapValuesToObject(ResultSet rs) throws SQLException {
        TextQuestion textQuestion = new TextQuestion();
        textQuestion.setId(rs.getLong("id"));
        textQuestion.setQuestionId(rs.getLong("question_id"));
        textQuestion.setPlaceholder(rs.getString("placeholder"));
        textQuestion.setMaxChars(rs.getInt("max_chars"));
        return textQuestion;
    }

    public TextQuestion fetchByQuestionId(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from text_question where question_id = ?")) {
                statement.setLong(1,id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    TextQuestion textQuestion = new TextQuestion();
                    while(resultSet.next()) {
                        textQuestion = (mapValuesToObject(resultSet));
                    }
                    return textQuestion;
                }
            }
        }
    }
}
