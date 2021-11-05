package no.kristiania.dao;

import no.kristiania.http.model.Answer;

import javax.sql.DataSource;

public class AnswerDao {
    private final DataSource dataSoruce;

    public AnswerDao(DataSource dataSource) {
        this.dataSoruce = dataSource;
    }

    public Answer retrieveByID(long id) {
        return null;
    }
}
