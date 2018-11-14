package io.github.jython234.floatpanel.server.json;

public class ServerInfoJSON {
    public String protocol;
    public String software;

    public ServerInfoJSON(String protocol, String software) {
        this.protocol = protocol;
        this.software = software;
    }
}
