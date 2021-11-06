package no.kristiania.http.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

public class JWT {
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

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
