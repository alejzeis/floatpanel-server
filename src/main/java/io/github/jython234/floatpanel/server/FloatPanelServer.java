package io.github.jython234.floatpanel.server;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import io.github.jython234.floatpanel.server.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootApplication
public class FloatPanelServer {
    public static final String PROTO_VERSION = "1.0";
    public static final String ROOT_PATH = "/floatpanel/api/v" + PROTO_VERSION + "";

    private static Logger logger;
    private static ServerConfig config;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection usersCollection;

    private OperationsManager operationsManager;
    private AuthManager authManager;

    public FloatPanelServer() {
        this.connectToDatabase();

        this.authManager = new AuthManager(this);
        this.operationsManager = new OperationsManager();
    }

    private void connectToDatabase() {
        logger.info("Connecting to " + config.mongoUrl + ", database: " + config.databaseName);

        this.mongoClient = MongoClients.create(config.mongoUrl);
        this.mongoDatabase = this.mongoClient.getDatabase(config.databaseName);
        this.usersCollection = this.mongoDatabase.getCollection("users");
    }

    public static void main(String[] args) {
        logger = LoggerFactory.getLogger("FloatPanel-Server");
        logger.info("Starting FloatPanel-Server for protocol v" + PROTO_VERSION + "...");

        try {
            config = ServerConfig.loadConfig();
        } catch (FileNotFoundException e) {
            logger.error("Failed to find configuration file server.yml!");
            logger.error("Please place a server.yml file in the current directory, or /etc/floatpanel/server.yml if on Linux.");
            System.exit(1);
        } catch(IOException e) {
            logger.error("IOException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (KeyNotFoundException e) {
            logger.error("Failed to find all the required keys and values in configuration file server.yml!");
            System.exit(1);
        }

        SpringApplication.run(FloatPanelServer.class, args);
    }

    public static Logger getLogger() {
        return logger;
    }

    public static ServerConfig getConfig() {
        return config;
    }
}
