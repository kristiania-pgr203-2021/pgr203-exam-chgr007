package no.kristiania.http;

import no.kristiania.http.dao.UserDao;
import no.kristiania.http.util.Authenticator;
import no.kristiania.http.util.PostValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    DataSource dataSource = TestData.testDataSource();
    UserDao userDao = new UserDao(dataSource);

    User user = userDao.retrieveByEmail("start@persson.no");

    public UserDaoTest() throws SQLException {
    }

    @BeforeAll
    void addUser() throws SQLException {
        if (user == null) {
            Authenticator authenticator = new Authenticator();
            user = new User();
            user.setEmail("start@persson.no");
            user.setFirstName("start");
            user.setLastName("Persson");
            String password = authenticator.encryptPass("420");
            user.setPassword(password);
            userDao.save(user);
        }
    }

    @Test
    void shouldAddUser() throws SQLException {
        String email = "test@persson.no";
        User user = userDao.retrieveByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFirstName("test");
            user.setLastName("Persson");
            user.setPassword("123");
            userDao.save(user);
        }
        assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(userDao.retrieveById(user.getId()));
    }

    @Test
    void shouldAuthenticateUser() throws SQLException {
        Authenticator authenticator = new Authenticator();
        User user = userDao.retrieveByEmail("start@persson.no");
        String password = "420";
        assertTrue(authenticator.validatePassword(password, user.getPassword()));
    }

    @Test
    void shouldGenerateTokenForUser() throws IOException {
        HttpServer server = new HttpServer(0, dataSource);
        HttpClient client = new HttpClient("localhost", server.getPort());
        PostValue<String, String> userName = new PostValue<>("userName", "start@persson.no");
        PostValue<String, String> password = new PostValue<>("password", "420");
        client.post(List.of(userName, password), "/api/login");

        String token = client.getHeader("Set-Cookie");
        System.out.println(token);
        assertThat(token)
                .contains("AuthToken");

    }

    @Test
    void userAlreadyExists() throws IOException {
        HttpServer server = new HttpServer(0, dataSource);
        HttpClient client = new HttpClient("localhost", server.getPort());

        PostValue<String,String> userName = new PostValue<>("userName", "start@persson.no");
        PostValue<String,String> firstName = new PostValue<>("firstName", "start");
        PostValue<String,String> lastName = new PostValue<>("lastName", "Persson");
        PostValue<String,String> password = new PostValue<>("password", "420");

        client.post(List.of(userName,firstName,lastName,password), "/api/signup");

        assertEquals(403, client.getStatusCode());
    }
}
