package io.github.jython234.floatpanel.server.operations;

import io.github.jython234.floatpanel.server.FloatPanelServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Manages all the operations that are to be completed by agents,
 * and to process results from agents.
 *
 * @author jython234
 */
@Component
public class OperationsManager {
    private final FloatPanelServer server;

    public Map<String, List<Operation>> agentOperations;

    @Autowired
    public OperationsManager(FloatPanelServer server) {
        this.server = server;

        this.agentOperations = new ConcurrentHashMap<>();
    }

    /**
     * Adds an operation to an agents queue. The next time it polls the server it will receive the operation.
     * @param agentUUID The UUID of the agent.
     * @param operation The Operation to be completed.
     */
    public void addOperationToQueue(String agentUUID, Operation operation) {
        if(this.agentOperations.containsKey(agentUUID)) {
            this.agentOperations.get(agentUUID).add(operation);
        } else {
            List<Operation> list = new CopyOnWriteArrayList<>();
            list.add(operation);

            this.agentOperations.put(agentUUID, list);
        }
    }

    /**
     * Get a list of new operations to process for a specific Agent and update their status
     * to processing.
     * @param agentUUID The UUID of the Agent
     * @return A List of operations to return to the agent to process, may be empty if there are no new operations.
     */
    public List<Operation> getNewOperationsForAgent(String agentUUID) {
        var operations = this.agentOperations.get(agentUUID);
        if(operations != null) {
            operations.forEach((operation) -> {
                if(operation.status == OperationStatus.QUEUED) {
                    operation.status = OperationStatus.PROCESSING;
                }
            });

            return operations.stream().filter(op -> op.status == OperationStatus.PROCESSING).collect(Collectors.toList());
        } return Collections.emptyList();
    }

    // Clean every 30 seconds
    @Scheduled(fixedDelay = 30000)
    public void cleanOperationsList() {
        List<String> emptyPairs = new ArrayList<>();
        this.agentOperations.keySet().forEach((key) -> {
            List<Operation> expiredOperations = new ArrayList<>();

            this.agentOperations.get(key).forEach((operation) -> {
                if((operation.status == OperationStatus.SUCCESS || operation.status == OperationStatus.FAILED)
                        && operation.expiration.before(Date.from(Instant.now()))) {
                    expiredOperations.add(operation);
                }
            });

            expiredOperations.forEach(this.agentOperations.get(key)::remove);

            if(this.agentOperations.get(key).isEmpty()) emptyPairs.add(key);
        });

        emptyPairs.forEach(this.agentOperations::remove);
    }
}
