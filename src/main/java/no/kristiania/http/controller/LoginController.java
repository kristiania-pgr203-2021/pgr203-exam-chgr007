package no.kristiania.http.controller;

import no.kristiania.http.dao.UserDao;
import no.kristiania.http.User;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class LoginController implements HttpController {
    private UserDao userDao;
    private final String path = "/api/login";

    public LoginController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {
        Authenticator authenticator = new Authenticator();
        String userName;
        String password;

        if(request.getRequestType().equalsIgnoreCase("post")) {
            Map<String,String> postValues = request.getPostParams();
            userName = postValues.containsKey("userName") ? postValues.get("userName") : null;
            password = postValues.containsKey("password") ? postValues.get("password") : null;

            // TODO: legge inn sjekk på om brukeren eksisterer
            User user = userDao.retrieveByEmail(userName);

            if(user != null && authenticator.validatePassword(password,user.getPassword())) {
                String token = authenticator.generateToken(user.getId(),userName);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                Date expiration = c.getTime();

                HttpResponse response = new HttpResponse(303, "See other");
                response.setHeaderField("Connection","close");
                response.setHeaderField("Set-Cookie", "AuthToken="+token+"; Path=/; Expires="+expiration);
                response.setHeaderField("Location", "/index.html");
                return response;
            }
        } else {
            // show login form
        }
        return new HttpResponse(500, "Internal server error");
    }

    @Override
    public String getPath() {
        return this.path;
    }
}
