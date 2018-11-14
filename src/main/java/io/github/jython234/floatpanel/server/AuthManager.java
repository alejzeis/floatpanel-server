package io.github.jython234.floatpanel.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.sql.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;

/**
 * Handles authentication for agents and users.
 *
 * @author jython234
 */
public class AuthManager {
    private final FloatPanelServer server;

    private Algorithm serverKey;
    private JWTVerifier verifier;

    public AuthManager(FloatPanelServer server) {
        this.server = server;

        this.loadKey();

        System.out.println(this.issueClientToken());
    }

    public String issueClientToken() {
        var calendar = Calendar.getInstance();
        calendar.setTime(Date.from(Instant.now()));
        calendar.add(Calendar.MINUTE, 15); // 15 Minute token expiration

        var expirationDate = calendar.getTime();

        var jwt = JWT.create()
                .withIssuer("FloatPanel-Server")
                .withSubject("CLIENT")
                .withExpiresAt(expirationDate)
                .sign(this.serverKey);

        return jwt;
    }

    public boolean verifyToken(String jwt) {
        try {
            var decoded = this.verifier.verify(jwt);
            return true;
        } catch(JWTVerificationException e) {
            return false;
        }
    }

    private void loadKey() {
        var key = Base64.getDecoder().decode(FloatPanelServer.getConfig().serverSecret.getBytes());
        this.serverKey = Algorithm.HMAC512(key);
        this.verifier = JWT.require(this.serverKey)
                .withIssuer("FloatPanel-Server")
                .build();

        FloatPanelServer.getLogger().info("Loaded secret key.");
    }

    public static String generateNewSecret() {
        var key = Keys.secretKeyFor(SignatureAlgorithm.ES512);
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
