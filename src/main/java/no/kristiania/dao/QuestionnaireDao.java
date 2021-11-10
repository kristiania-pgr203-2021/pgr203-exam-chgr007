package no.kristiania.dao;

import no.kristiania.http.model.Questionnaire;

import javax.sql.DataSource;
import java.sql.*;

public class QuestionnaireDao extends DataAccessObject<Questionnaire> {
    public QuestionnaireDao(DataSource dataSource) {
        super(dataSource, "questionnaire");
    }

    @Override
    protected PreparedStatement createPreparedStatementForUpdate(Connection connection) throws SQLException {
        return connection.prepareStatement("update questionnaire set name = ? where id = ?");

    }

    @Override
    protected PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException {
        return connection.prepareStatement("insert into questionnaire(name, person_id) values(?, ?)", Statement.RETURN_GENERATED_KEYS);

    }

    @Override
    protected void setGeneratedKeys(Questionnaire model, ResultSet generatedKeys) throws SQLException {
        model.setId(generatedKeys.getLong("id"));

    }

    @Override
    protected void setFieldsToUpdateInDB(Questionnaire questionnaire, PreparedStatement statement) throws SQLException {
        statement.setString(1, questionnaire.getName());
        statement.setLong(2, questionnaire.getId());
    }

    @Override
    protected Questionnaire mapValuesToObject(ResultSet rs) throws SQLException {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(rs.getString("name"));
        questionnaire.setId(rs.getLong("id"));
        questionnaire.setPersonId(rs.getLong("person_id"));
        return questionnaire;
    }
}
