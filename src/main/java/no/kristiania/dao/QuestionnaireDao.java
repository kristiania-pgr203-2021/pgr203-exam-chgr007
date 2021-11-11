package no.kristiania.dao;

import no.kristiania.http.model.Questionnaire;

import javax.sql.DataSource;
import java.sql.*;

public class QuestionnaireDao extends DataAccessObject<Questionnaire> {
    public QuestionnaireDao(DataSource dataSource) {
        super(dataSource, "questionnaire");
    }

    @Override
    protected void executeSave(Connection connection, Questionnaire model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into questionnaire(name, person_id) values(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1,model.getName());
            statement.setLong(2, model.getPersonId());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }
    }

    @Override
    protected void executeUpdate(Connection connection, Questionnaire model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("update questionnaire set name = ? where id = ?")) {
            statement.setString(1, model.getName());
            statement.setLong(2, model.getId());
        }
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
