package io.github.jython234.floatpanel.server.config;

import io.github.jython234.floatpanel.server.KeyNotFoundException;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class ServerConfig {
    public final int port;

    public final String mongoUrl;
    public final String databaseName;


    public ServerConfig(int port, String mongoUrl, String databaseName) {
        this.port = port;
        this.mongoUrl = mongoUrl;
        this.databaseName = databaseName;
    }

    public static ServerConfig loadConfig() throws FileNotFoundException, KeyNotFoundException {
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

            if(mongoUrl == null || databaseName == null || port < 0) {
                throw new KeyNotFoundException("Failed to find all the required keys and values in the YAML configuration!");
            }

            return new ServerConfig(port, mongoUrl, databaseName);
        } else throw new FileNotFoundException("Failed to find config file!");
    }
}
