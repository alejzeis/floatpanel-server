package io.github.jython234.floatpanel.server;

import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;
import io.github.jython234.floatpanel.server.operations.OperationsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
@Component
public class FloatPanelServer {
    public static final String PROTO_VERSION = "1.0";
    public static final String ROOT_PATH = "/floatpanel/api/v" + PROTO_VERSION + "";

    private static Logger logger;


    public ServerConfig config;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection usersCollection;

    @Autowired
    private OperationsManager operationsManager;
    @Autowired
    private AuthManager authManager;

    @Autowired
    public FloatPanelServer(ServerConfig config) {
        this.config = config;

        logger = LoggerFactory.getLogger("FloatPanel-Server");
        logger.info("Starting FloatPanel-Server for protocol v" + PROTO_VERSION + "...");

        this.connectToDatabase();
    }

    private void connectToDatabase() {
        logger.info("Connecting to " + config.mongoUrl + ", database: " + config.databaseName);

        this.mongoClient = MongoClients.create(config.mongoUrl);
        this.mongoDatabase = this.mongoClient.getDatabase(config.databaseName);
        this.usersCollection = this.mongoDatabase.getCollection("users");
    }

    public static void main(String[] args) {
        SpringApplication.run(FloatPanelServer.class, args);
    }

    public static Logger getLogger() {
        return logger;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public AuthManager getAuthManager() {
        return this.authManager;
    }

    public OperationsManager getOperationsManager() {
        return this.operationsManager;
    }
}
