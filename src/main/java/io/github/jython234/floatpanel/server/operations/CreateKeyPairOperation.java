package io.github.jython234.floatpanel.server.operations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CreateKeyPairOperation extends Operation {
    public CreateKeyPairOperation(String uuid) {
        super(uuid, OperationType.CREATE_KEY_PAIR, new ConcurrentHashMap<>());
    }

    public CreateKeyPairOperation(String uuid, Map<String, String> data) {
        super(uuid, OperationType.CREATE_KEY_PAIR, data);
    }

    /**
     * Get the name of this key pair.
     * @return The name of the key pair.
     */
    public String getPairName() {
        return this.data.get("name");
    }

    /**
     * Get the private key from this completed operation. Will return null if the
     * operation failed or hasn't been completed yet.
     * @return The private key.
     */
    public String getPrivateKey() {
        return this.resultData.get("key");
    }

    /**
     * Get the certificate from this completed operation. Will return null if the
     * operation failed or hasn't been completed yet.
     * @return The certificate.
     */
    public String getCert() {
        return this.resultData.get("cert");
    }
}
