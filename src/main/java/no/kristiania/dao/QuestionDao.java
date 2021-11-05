package no.kristiania.dao;

import no.kristiania.http.model.Question;

import javax.sql.DataSource;
import java.sql.*;

public class QuestionDao extends DataAccesObject<Question> {
    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Question question) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("insert into question(question, correct_answer) values(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, question.getQuestion());
                statement.setString(2, question.getAnswer());
                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    question.setId(rs.getLong("id"));
                }
            }
        }

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
