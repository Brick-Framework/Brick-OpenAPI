package com.brick.openapi.exception;

public class InvalidValue extends Exception {
    private final String valueForWhichExceptionOccured;

    public InvalidValue(String invalidValue) {
        super("Invalid Value : "+invalidValue);
        this.valueForWhichExceptionOccured = invalidValue;
    }

    public String getInvalidValue() {
        return valueForWhichExceptionOccured;
    }
}
