package com.sumerge.auth.common.enums;

public enum OperationType {
    CREATE("CREATE"), UPDATE("UPDATE"), DELETE("DELETE");
    private final String value;
    OperationType(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
