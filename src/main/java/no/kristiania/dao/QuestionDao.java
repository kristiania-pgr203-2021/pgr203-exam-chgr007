package no.kristiania.dao;

import no.kristiania.http.model.Question;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionDao extends DataAccesObject<Question> {
    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Question model) {

    }

    @Override
    protected Question mapValuesToObject(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setQuestion(rs.getString("question"));
        question.setId(rs.getLong("id"));

        return question;
    }

}
