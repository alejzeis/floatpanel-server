package io.github.jython234.floatpanel.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "floatpanel")
public class ServerConfig {
    @Value("${floatpanel.mongoUrl}")
    public String mongoUrl;
    @Value("${floatpanel.databaseName}")
    public String databaseName;
    @Value("${floatpanel.serverSecret}")
    public String serverSecret;
}
