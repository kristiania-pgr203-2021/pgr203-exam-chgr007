package no.kristiania.dao;

import no.kristiania.http.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao extends DataAccesObject<User> {

    public UserDao(DataSource dataSource) {
        super(dataSource, "person");
    }

    @Override
    protected PreparedStatement createPreparedStatementForUpdate(Connection connection) throws SQLException {
        return connection.prepareStatement("update person set first_name = ?, last_name = ?, email = ?");
    }

    @Override
    protected PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException {
        return connection.prepareStatement("insert into person(first_name,last_name,email) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void setGeneratedKeys(User model, ResultSet generatedKeys) throws SQLException {
        model.setId(generatedKeys.getLong("id"));
    }

    @Override
    protected void setFieldsToUpdateInDB(User model, PreparedStatement statement) throws SQLException {
        statement.setString(1, model.getFirstName());
        statement.setString(2, model.getLastName());
        statement.setString(3, model.getEmail());
    }

    @Override
    protected User mapValuesToObject(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
