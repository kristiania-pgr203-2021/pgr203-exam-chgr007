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
    protected PreparedStatement createPreparedStatementForUpdate(Connection connection) throws SQLException {
        return connection.prepareStatement("update answer set answer = ?, question_id = ?");
    }

    @Override
    protected PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException {
        return connection.prepareStatement("insert into answer(answer, question_id) values(?,?)", Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void setGeneratedKeys(Answer answer, ResultSet generatedKeys) throws SQLException {
        answer.setId(generatedKeys.getLong("id"));
    }

    @Override
    public void setFieldsToUpdateInDB(Answer answer, PreparedStatement statement) throws SQLException {
        statement.setLong(2, answer.getQuestionId());
        statement.setString(1, answer.getAnswer());
    }

    @Override
    protected Answer mapValuesToObject(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setId(rs.getLong("id"));
        answer.setQuestionId(rs.getLong("question_id"));
        answer.setAnswer(rs.getString("answer"));
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
