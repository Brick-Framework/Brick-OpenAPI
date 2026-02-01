package com.brick.openapi.exception;

public class InvalidOpenAPISpecification extends Exception {

    public enum Reason{
        KEY_NOT_FOUND("Cannot Find Key : "),
        INVALID_VALUE("Invalid Value for : "),
        PROPERTY_NOT_FOUND("Required Property Not Found : "),
        INVALID_DATA_TYPE("Invalid Data Type"),
        CYCLIC_REFERENCE("Cyclic Reference");

        private final String reasonForFailure;

        Reason(String reason) {
            this.reasonForFailure = reason;
        }

        @Override
        public String toString() {
            return reasonForFailure;
        }
    }

    public InvalidOpenAPISpecification(String filePath,Reason reason, String exceptionFor){
        super(
                reason + exceptionFor + " : File Name -> " + filePath
        );
    }
}
