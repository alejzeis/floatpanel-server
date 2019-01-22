package io.github.jython234.floatpanel.server.operations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RevokeKeyPairOperation extends Operation {
    public RevokeKeyPairOperation(String uuid) {
        super(uuid, OperationType.REVOKE_KEY_PAIR, new ConcurrentHashMap<>());
    }

    public RevokeKeyPairOperation(String uuid, Map<String, String> data) {
        super(uuid, OperationType.REVOKE_KEY_PAIR, data);
    }

    /**
     * Get the name of this key pair.
     * @return The name of the key pair.
     */
    public String getPairName() {
        return this.data.get("name");
    }
}
