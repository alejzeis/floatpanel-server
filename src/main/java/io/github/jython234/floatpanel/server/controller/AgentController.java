package io.github.jython234.floatpanel.server.controller;

import com.google.gson.Gson;
import io.github.jython234.floatpanel.server.FloatPanelServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {
    private static Gson gson = new Gson();

    @Autowired
    private FloatPanelServer server;

    @RequestMapping(value = FloatPanelServer.ROOT_PATH + "/agent/poll", method = RequestMethod.GET)
    public ResponseEntity<String> handleAgentPoll(@RequestParam("token") String token) {
        var decodedToken = this.server.getAuthManager().verifyAndDecodeToken(token);
        if(decodedToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Token verification failed.");
        }

        var operations = this.server.getOperationsManager().getNewOperationsForAgent(decodedToken.getSubject());
        if(operations.isEmpty()) {
            return ResponseEntity.ok("[]");
        } else {
            return ResponseEntity.ok(gson.toJson(operations));
        }
    }
}
