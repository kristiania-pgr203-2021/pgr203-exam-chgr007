package no.kristiania.http.controller;

import no.kristiania.dao.UserDao;
import no.kristiania.http.User;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class LoginController implements HttpController {
    private UserDao userDao;

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

            User user = userDao.retrieveByEmail(userName);

            if(user != null && authenticator.validatePassword(password,user.getPassword())) {
                String token = authenticator.generateToken(user.getId(),userName);
                HttpResponse response = new HttpResponse(200, "OK");
                response.setHeaderField("Connection","close");
                response.setHeaderField("Set-Cookie", "AuthToken="+token);
                response.setMessageBody("<h1>Hello "+userName + ", welcome back!</h1>");
                return response;
            }
        } else {
            // show login form
        }
        return null;
    }
}
