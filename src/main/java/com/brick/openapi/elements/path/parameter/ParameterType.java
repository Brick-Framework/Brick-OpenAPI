package com.brick.openapi.elements.path.parameter;

import com.brick.logger.Logger;
import com.brick.openapi.exception.InvalidValue;

public enum ParameterType {
    QUERY("query"),
    HEADER("header"),
    PATH("path"),
    COOKIE("cookie");

    private final String type;

    ParameterType(String type) {
        this.type = type;
    }

    public static ParameterType fromString(String value) throws InvalidValue {
        
        for (ParameterType pt : values()) {
            if (pt.type.equalsIgnoreCase(value)) {
                return pt;
            }
        }

        InvalidValue invalidValue = new InvalidValue(value);
        Logger.logException(invalidValue);
        throw invalidValue;
    }
}
