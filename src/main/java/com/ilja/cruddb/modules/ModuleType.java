package com.ilja.cruddb.modules;

public enum ModuleType {
    WRITER("1", "Manage Writers"),
    POST("2", "Manage Posts"),
    LABEL("3", "Manage Labels"),
    EXIT("0", "Exit");

    private final String code;
    private final String description;

    ModuleType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() { return code; }
    public String getDescription() { return description; }

    public static ModuleType fromCode(String code) {
        for (ModuleType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}