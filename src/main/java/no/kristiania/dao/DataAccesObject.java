package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.*;

public abstract class DataAccesObject<T> {

    public DataSource dataSource;
    public String dbName;
    public DataAccesObject(DataSource dataSource, String dbName) {
        this.dataSource = dataSource;
        this.dbName = dbName;
    }


    public T retrieveById(long id)  throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from " + dbName + " where id = ?")) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    return mapValuesToObject(rs);
                }
            }
        }
    }

    public void save(T model) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = createPreparedStatementForSave(connection)) {
                setFieldsToUpdateInDB(model, statement);
                statement.executeUpdate();
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    generatedKeys.next();
                    setGeneratedKeys(model, generatedKeys);
                }
            }
        }
    }

    protected abstract PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException;
    protected abstract void setGeneratedKeys(T model, ResultSet generatedKeys) throws SQLException;
    public abstract void setFieldsToUpdateInDB(T model, PreparedStatement statement) throws SQLException;
    protected abstract T mapValuesToObject(ResultSet rs) throws SQLException;

}
