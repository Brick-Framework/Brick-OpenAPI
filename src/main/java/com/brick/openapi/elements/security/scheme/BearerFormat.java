package com.brick.openapi.elements.security.scheme;

import com.brick.logger.Logger;
import com.brick.openapi.exception.InvalidValue;

public enum BearerFormat {
    JWT("JWT");

    private final String authBearerType;


    BearerFormat(String bearerFormat) {
        this.authBearerType = bearerFormat;
    }

    public static BearerFormat fromString(String value) throws InvalidValue {
        
        for (BearerFormat pt : values()) {
            if (pt.authBearerType.equalsIgnoreCase(value)) {
                return pt;
            }
        }

        InvalidValue invalidValue = new InvalidValue(value);
        Logger.logException(invalidValue);
        throw invalidValue;
    }
}
