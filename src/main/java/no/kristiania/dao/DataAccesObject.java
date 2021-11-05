package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DataAccesObject<T> {

    public DataSource dataSource;
    public DataAccesObject(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public T retrieveById(String db, long id)  throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from question where id = ?")) {
                //statement.setString(2, db);
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return mapValuesToObject(rs);
                }
            }
        }
    }

    public abstract void save(T model) throws SQLException;

    protected abstract T mapValuesToObject(ResultSet rs) throws SQLException;

}
