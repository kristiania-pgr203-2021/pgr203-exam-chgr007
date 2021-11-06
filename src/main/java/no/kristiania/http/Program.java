package no.kristiania.http;

import io.jsonwebtoken.security.Keys;
import no.kristiania.http.util.JWT;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;


public class Program {
    public static void main(String[] args) throws IOException {
        PasswordEncoder passwordEncoder =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        String result1 = passwordEncoder.encode("{bcrypt}thisMyPass");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String result = encoder.encode("password");
        System.out.println(result1);
        System.out.printf("Password matches: %s%n ", passwordEncoder.matches("{bcrypt}thisMyPass", result1));
        // source: https://developer.okta.com/blog/2018/10/31/jwts-with-java
        String token = JWT.generateToken(1, "christian@gregersen.eu");

        System.out.println("Token valid?: " + JWT.validateToken(token));

        HttpServer server = new HttpServer(0);
    }
}
