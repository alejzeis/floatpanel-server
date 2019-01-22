package io.github.jython234.floatpanel.server.operations;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Operation that an Agent can complete.
 *
 * @author jython234
 */
public abstract class Operation {
    transient protected Date expiration; // The time at which this operation will be removed from memory (the agentOperations Map)

    @SerializedName("id")
    public final String uuid;
    public final OperationType type;

    transient protected OperationStatus status;

    public Map<String, String> data;
    transient public Map<String, String> resultData;

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
