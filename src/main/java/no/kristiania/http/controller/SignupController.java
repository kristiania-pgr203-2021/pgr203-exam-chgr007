package no.kristiania.http.controller;

import no.kristiania.dao.UserDao;
import no.kristiania.http.User;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.HttpRequest;
import no.kristiania.http.util.HttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class SignupController implements HttpController {
    private UserDao userDao;

    public SignupController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public HttpResponse handle(HttpRequest request) throws SQLException, IOException {

        if (request.getRequestType().equalsIgnoreCase("post")) {
            Map<String, String> postValues = request.getPostParams();
            String userName = postValues.get("userName");
            String password = postValues.get("password");
            String firstName = postValues.get("firstName");
            String lastName = postValues.get("lastName");
            if(validateUserData(firstName,lastName,userName,password)) {
                if (userExists(userName)) return new HttpResponse(403, "Already exists");

                Authenticator auth = new Authenticator();

                User user = new User();
                user.setPassword(auth.encryptPass(password));
                user.setEmail(userName);
                user.setFirstName(firstName);
                user.setLastName(lastName);

                userDao.save(user);

                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                Date expiration = c.getTime();

                HttpResponse response = new HttpResponse(303, "See other");
                response.setHeaderField("Connection", "close");
                response.setHeaderField("Set-Cookie", "AuthToken="+auth.generateToken(user.getId(),userName)+" ;Expires="+expiration.toString()+ "; Path=/");
                response.setHeaderField("Location", "/questionnaire.html");
                return response;
            };

        }
        return null;
    }

    private boolean userExists(String email) throws SQLException {
        User user = userDao.retrieveByEmail(email);
        if (user != null) return true;
        return false;
    }

    private boolean validateUserData(String firstName, String lastName, String userName, String password) {
        if (userName != null && password != null && firstName != null && lastName != null && !firstName.isBlank() && !lastName.isBlank() && !userName.isBlank() && !password.isBlank()) {
            return true;
        }
        return false;
    }
}
