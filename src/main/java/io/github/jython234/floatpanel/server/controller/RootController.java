package io.github.jython234.floatpanel.server.controller;

import io.github.jython234.floatpanel.server.FloatPanelServer;
import io.github.jython234.floatpanel.server.json.ServerInfoJSON;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @RequestMapping("/floatpanel/api/info")
    public ResponseEntity<ServerInfoJSON> info() {
        return ResponseEntity.ok(new ServerInfoJSON(FloatPanelServer.PROTO_VERSION, "FloatPanel"));
    }
}
