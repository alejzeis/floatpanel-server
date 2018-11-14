package io.github.jython234.floatpanel.server.config;

import io.github.jython234.floatpanel.server.AuthManager;
import io.github.jython234.floatpanel.server.FloatPanelServer;
import io.github.jython234.floatpanel.server.KeyNotFoundException;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Map;

public class ServerConfig {
    public final int port;

    public final String mongoUrl;
    public final String databaseName;
    public final String serverSecret;


    public ServerConfig(int port, String mongoUrl, String databaseName, String serverSecret) {
        this.port = port;
        this.mongoUrl = mongoUrl;
        this.databaseName = databaseName;
        this.serverSecret = serverSecret;
    }

    @SuppressWarnings("unchecked")
    public static ServerConfig loadConfig() throws IOException, KeyNotFoundException {
        var configLocation = new File("server.yml"); // Just search in our current directory;

        if(System.getProperty("os.name").equals("linux")) {
            // If linux, try /etc/floatpanel/server.yml
            configLocation = new File("/etc/floatpanel/server.yml");
        }

        if(!configLocation.exists() && System.getProperty("os.name").equals("linux")) {
            configLocation = new File("server.yml"); // Just search in our current directory;
        }

        if(configLocation.exists()) {
            var yaml = new Yaml();
            Map map = yaml.load(new FileInputStream(configLocation));

            var port = (int) map.get("port");
            var mongoUrl = (String) map.get("mongoUrl");
            var databaseName = (String) map.get("databaseName");
            var serverSecret = (String) map.get("serverSecret");

            if(mongoUrl == null || databaseName == null || port < 0) {
                throw new KeyNotFoundException("Failed to find all the required keys and values in the YAML configuration!");
            }

            if(serverSecret == null || serverSecret.equals("")) {
                FloatPanelServer.getLogger().warn("Failed to find \"server secret\" in configuration file, generating new secret...");
                serverSecret = AuthManager.generateNewSecret();

                map.put("serverSecret", serverSecret);
                yaml.dump(map, new FileWriter(configLocation)); // Overwrite the config file with our new secret
            }

            return new ServerConfig(port, mongoUrl, databaseName, serverSecret);
        } else throw new FileNotFoundException("Failed to find config file!");
    }
}
