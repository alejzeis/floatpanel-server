package io.github.jython234.floatpanel.server.tests;

import io.github.jython234.floatpanel.server.FloatPanelServer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FloatPanelServer.class)
@AutoConfigureMockMvc
@ContextConfiguration
class AgentTest {

    private static File configurationFile;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() throws IOException {
        configurationFile = new File("application.properties");

        var in = new DefaultResourceLoader().getResource("testingConfiguration.properties").getInputStream();
        FileUtils.copyInputStreamToFile(in, configurationFile);
    }

    @Test
    void testAgentPolling() throws Exception {
        // Authentication
        this.mockMvc.perform(get(
                FloatPanelServer.ROOT_PATH + "/agent/poll"
        )).andExpect(status().isBadRequest());

        this.mockMvc.perform(get(
                FloatPanelServer.ROOT_PATH + "/agent/poll?token="
                + "badtoken"
        )).andExpect(status().isUnauthorized());

        this.mockMvc.perform(get(
                FloatPanelServer.ROOT_PATH + "/agent/poll?token="
                        + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmYWtlaWQiLCJhdWQiOiJBR0VOVCIsImlzcyI6IkZsb2F0UGFuZWwtU2VydmVyIn0.AP236OrD4F8v1oWMF_MXiznEbzSGMHZEHVIlgzLrQ2xa9OZUNOAcp2_OFutfsZ6FP0kgob1CyHVujgioGp8o4Q"
        )).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @AfterAll
    static void cleanup() {
        //configurationFile.delete();
    }
}