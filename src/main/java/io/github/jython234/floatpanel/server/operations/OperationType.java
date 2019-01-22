package io.github.jython234.floatpanel.server.operations;

public enum OperationType {
    CREATE_KEY_PAIR(0),
    REVOKE_KEY_PAIR(1);

    int typeAsInt;

    OperationType(int typeAsInt) {
        this.typeAsInt = typeAsInt;
    }

    public int asInt() {
        return typeAsInt;
    }
}
