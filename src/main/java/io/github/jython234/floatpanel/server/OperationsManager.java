package io.github.jython234.floatpanel.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages all the operations that are to be completed by agents,
 * and to process results from agents.
 *
 * @author jython234
 */
public class OperationsManager {
    private final FloatPanelServer server;

    public Map<String, List<Operation>> agentOperations;

    public OperationsManager(FloatPanelServer server) {
        this.server = server;

        this.agentOperations = new ConcurrentHashMap<>();
    }

    /**
     * Get a list of operations to process for a specific Agent and update their status
     * to processing.
     * @param agentUUID The UUID of the Agent
     * @return A List of operations to return to the agent to process, or <code>null</code> if there are none.
     */
    public List<Operation> handleNewOperationsForAgent(String agentUUID) {
        var operations = this.agentOperations.get(agentUUID);
        if(operations != null) {
            operations.forEach((operation) -> operation.status = OperationStatus.PROCESSING);
            return operations;
        } return null;
    }

    public void clearOperations(String agentUUID) {
        this.agentOperations.remove(agentUUID);
    }

    /**
     * Represents an Operation that an Agent can complete.
     */
    public static class Operation {
        public final String uuid;
        public final OperationType type;

        protected OperationStatus status;
        public Map<String, String> data;

        public Operation(String uuid, OperationType type, Map<String, String> data) {
            this.status = OperationStatus.QUEUED;

            this.uuid = uuid;
            this.type = type;
            this.data = data;
        }

        public Operation(String uuid, OperationType type) {
            this(uuid, type, new HashMap<>());
        }

        public OperationStatus getStatus() {
            return status;
        }
    }

    public enum OperationType {
        CREATE_KEY_PAIR(1),
        REVOKE_KEY_PAIR(2);

        int typeAsInt;

        OperationType(int typeAsInt) {
            this.typeAsInt = typeAsInt;
        }

        public int asInt() {
            return typeAsInt;
        }
    }

    public enum OperationStatus {
        QUEUED(0),
        PROCESSING(1),
        SUCCESS(2),
        FAILED(3);

        int typeAsInt;

        OperationStatus(int typeAsInt) {
            this.typeAsInt = typeAsInt;
        }

        public int asInt() {
            return typeAsInt;
        }
    }
}
