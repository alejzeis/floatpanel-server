package io.github.jython234.floatpanel.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;

/**
 * Handles authentication for agents and users.
 *
 * @author jython234
 */
@Component
public class AuthManager {
    private final FloatPanelServer server;

    private Algorithm serverKey;
    private JWTVerifier verifier;

    @Autowired
    public AuthManager(FloatPanelServer server) {
        this.server = server;

        this.loadKey();
    }

    /**
     * Issues a JWT authentication token for a Client session. This is someone logging into the server.
     * @param uuid The user's UUID.
     * @return A JWT for the user's session.
     */
    public String issueClientToken(String uuid) {
        var calendar = Calendar.getInstance();
        calendar.setTime(Date.from(Instant.now()));
        calendar.add(Calendar.HOUR, 1); // 1 Hour session length

        var expirationDate = calendar.getTime();

        var jwt = JWT.create()
                .withIssuer("FloatPanel-Server")
                .withSubject(uuid)
                .withAudience("CLIENT")
                .withExpiresAt(expirationDate)
                .sign(this.serverKey);

        return jwt;
    }

    /**
     * Issues a JWT authentication token for an Agent. This is a VPN server which has a FloatPanel agent
     * running on it. Unlike clients, agents get one token for their entire lifespan.
     * @param uuid The UUID of the Agent.
     * @return A JWT for that agent to use.
     */
    public String issueAgentToken(String uuid) {
        var jwt = JWT.create()
                .withIssuer("FloatPanel-Server")
                .withSubject(uuid)
                .withAudience("AGENT")
                .sign(this.serverKey);

        return jwt;
    }

    public DecodedJWT verifyAndDecodeToken(String jwt) {
        try {
            return this.verifier.verify(jwt);
        } catch(JWTVerificationException e) {
            return null;
        }
    }

    private void loadKey() {
        var key = Base64.getDecoder().decode(this.server.config.serverSecret.getBytes());
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
