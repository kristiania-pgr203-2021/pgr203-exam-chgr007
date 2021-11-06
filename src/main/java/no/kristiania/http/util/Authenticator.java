package no.kristiania.http.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

public class Authenticator {
    public static String ENCRYPTION = "bcrypt";
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();
    public static String encryptPass(String pass) {
        return passwordEncoder.encode("{"+ENCRYPTION+"}"+pass);
    }
    public static boolean validatePassword(String pass, String encryptedPass) {
        return passwordEncoder.matches(pass,encryptedPass);
    }

    public static String generateToken(long id, String userName) {
        // https://github.com/jwtk/jjwt#install-jdk-maven
        Calendar c = Calendar.getInstance();
        Date now = new Date();
        Date expiration = new Date();
        c.setTime(expiration);
        c.add(Calendar.DATE, 1);
        expiration = c.getTime();

        String jws = Jwts.builder()
                .setIssuedAt(now)
                .setId("myId")
                .setIssuer("C&M")
                .setAudience("Johannes")
                .setExpiration(expiration)
                .signWith(key).compact();

        return jws;
    }
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            e.printStackTrace();
        }
        return false;
    }
}
