package no.kristiania.dao;

import no.kristiania.http.model.Question;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public abstract class DataAccessObject<T> {

    public DataSource dataSource;
    public String dbName;
    public DataAccessObject(DataSource dataSource, String dbName) {
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

    public ArrayList<T> retrieveAll() throws SQLException{

        ArrayList<T> arrayList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("select * from " + dbName)) {
                try (ResultSet rs = statement.executeQuery()) {
                    rs.next();
                    arrayList.add(mapValuesToObject(rs));
                }
            }
        }

        return arrayList;
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
    public void update(T model) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = createPreparedStatementForUpdate(connection)) {
                setFieldsToUpdateInDB(model, statement);
                statement.executeUpdate();
            }
        }
    }

    protected abstract PreparedStatement createPreparedStatementForUpdate(Connection connection) throws SQLException;

    protected abstract PreparedStatement createPreparedStatementForSave(Connection connection) throws SQLException;
    protected abstract void setGeneratedKeys(T model, ResultSet generatedKeys) throws SQLException;
    protected abstract void setFieldsToUpdateInDB(T model, PreparedStatement statement) throws SQLException;
    protected abstract T mapValuesToObject(ResultSet rs) throws SQLException;

}
