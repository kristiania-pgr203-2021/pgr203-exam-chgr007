package no.kristiania.http.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Authenticator {
    private final static String ENCRYPTION = "bcrypt";

    private byte[] apiKeySecretBytes;
    private Key key;
    public PasswordEncoder passwordEncoder;

    public Authenticator() {
        Properties properties = new Properties("token.properties");
        apiKeySecretBytes = properties.getProperty("token.key").getBytes();
        key = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    public String encryptPass(String pass) {
        return passwordEncoder.encode("{"+ENCRYPTION+"}"+pass);
    }
    public boolean validatePassword(String pass, String encryptedPass) {
        return passwordEncoder.matches("{"+ENCRYPTION+"}"+pass,encryptedPass);
    }

    public String generateToken(long id, String userName) {
        Calendar c = Calendar.getInstance();
        Date expiration = new Date();
        c.setTime(expiration);
        c.add(Calendar.DATE, 1);
        expiration = c.getTime();

        // https://github.com/jwtk/jjwt#install-jdk-maven
        String jws = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .setId(String.valueOf(id))
                .setIssuer("Stigen & Gregersen")
                .setSubject(userName)
                .signWith(key)
                .compact();

        return jws;
    }
    public Long getIdFromToken(String token) {
        if (validateToken(token)) {
            Long id = Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getId());
            return id;
        }
        return -1l;
    }
    public boolean validateToken(String token) {
        Jws<Claims> jws;
        try {
            jws = Jwts.parserBuilder()
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
