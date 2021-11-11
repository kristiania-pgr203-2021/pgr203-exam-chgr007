package no.kristiania.dao;

import no.kristiania.http.model.Answer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerDao extends DataAccessObject<Answer> {
    public AnswerDao(DataSource dataSource) {
        super(dataSource, "answer");
    }


    @Override
    protected void executeSave(Connection connection, Answer model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into answer(question_id, answer, person_id) values (?,?,?)",Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, model.getQuestionId());
            statement.setString(2, model.getAnswer());
            statement.setLong(3, model.getUserId());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, Answer model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update answer set answer = ? where id = ?")) {
            statement.setString(1, model.getAnswer());
            statement.setLong(2, model.getId());
            statement.executeUpdate();
        }
    }

    @Override
    protected Answer mapValuesToObject(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setId(rs.getLong("id"));
        answer.setQuestionId(rs.getLong("question_id"));
        answer.setAnswer(rs.getString("answer"));
        answer.setUserId(rs.getLong("person_id"));
        return answer;
    }


    public List<Answer> listByQuestionId(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from answer where question_id = ?")) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    List<Answer> questions = new ArrayList<>();
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
