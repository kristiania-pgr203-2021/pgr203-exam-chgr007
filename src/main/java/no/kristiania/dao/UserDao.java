package no.kristiania.dao;

import no.kristiania.http.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao extends DataAccessObject<User> {

    public UserDao(DataSource dataSource) {
        super(dataSource, "person");
    }

    @Override
    protected void executeSave(Connection connection, User model) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into person(first_name,last_name,email,password) values (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, model.getFirstName());
            statement.setString(2, model.getLastName());
            statement.setString(3, model.getEmail());
            statement.setString(4, model.getPassword());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                model.setId(generatedKeys.getLong("id"));
            }
        }

    }

    @Override
    protected void executeUpdate(Connection connection, User model) throws SQLException {

    }

    @Override
    protected User mapValuesToObject(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    public User retrieveByEmail(String userName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from person where email = ?")) {
                statement.setString(1, userName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.isBeforeFirst()) return null;
                    resultSet.next();
                    return mapValuesToObject(resultSet);
                }
            }
        }
    }
}
