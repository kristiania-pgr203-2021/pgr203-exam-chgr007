package no.kristiania.http.controller;

import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;

public interface HttpController {
    HttpResponse handle(HttpRequest request) throws SQLException, IOException;
}
