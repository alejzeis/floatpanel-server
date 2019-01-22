package io.github.jython234.floatpanel.server.operations;

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
