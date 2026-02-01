package com.brick.openapi.elements.schema;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.brick.logger.Logger;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;

enum StringSchemaFormat {
    DATE("date"), // Currently No Validation Supported
    DATE_TIME("date-time"), // Currently No Validation Supported
    PASSWORD("password"), // Currently No Validation Supported
    UUID("uuid"), // Currently No Validation Supported
    EMAIL("email"); // Currently No Validation Supported

    private final String type;

    StringSchemaFormat(String type) {
        this.type = type;
    }

    public static StringSchemaFormat fromString(String value) throws InvalidValue {
        
        for (StringSchemaFormat pt : values()) {
            if (pt.type.equalsIgnoreCase(value)) {
                return pt;
            }
        }

        InvalidValue invalidValue = new InvalidValue(value);
        Logger.logException(invalidValue);
        throw invalidValue;
    }
}

public class StringSchema extends Schema{
    private final Optional<StringSchemaFormat> format;
    private final Optional<String> pattern;
    private final Optional<Integer> minLength;
    private final Optional<Integer> maxLength;
    private final Optional<List<String>> possibleValues;// enum in openapi
    private final boolean nullable;

    public StringSchema(BrickMap brickMap) throws InvalidValue, KeyNotFound {
        
        Optional<String> stringSchemaType = brickMap.getOptionalString(OpenAPIKeyConstants.FORMAT);
        if( stringSchemaType.isPresent() ){
            this.format = Optional.of(StringSchemaFormat.fromString(stringSchemaType.get()));
        }else{
            this.format = Optional.empty();
        }
        this.pattern = brickMap.getOptionalString(OpenAPIKeyConstants.PATTERN);
        this.minLength = brickMap.getOptionalInteger(OpenAPIKeyConstants.MINIMUM_LENGTH);
        this.maxLength = brickMap.getOptionalInteger(OpenAPIKeyConstants.MAXIMUM_LENGTH);
        this.possibleValues = brickMap.getOptionalListOfString(OpenAPIKeyConstants.ENUM);

        if( brickMap.contains(OpenAPIKeyConstants.NULLABLE) ){
            this.nullable = brickMap.getBoolean(OpenAPIKeyConstants.NULLABLE);
        }else{
            this.nullable = false;
        }

        
    }

	@Override
	public boolean validateData(JsonNode data) {
		// Nullability Check
		if( data == null || data.isNull() ) {
			return this.nullable;
		}
		
		if( !data.isString() ) {
			return false;
		}
		
		String value = data.asString();
		// Length Checks
		if( this.minLength.isPresent() && value.length() < this.minLength.get() ) {
			return false;
		}
		if( this.maxLength.isPresent() && value.length() > this.maxLength.get() ) {
			return false;
		}
		
		
		// Performing Regex Check
		if( this.pattern.isPresent() && !Pattern.matches(this.pattern.get(), value) ) {
			return false;
			
		}
		
		if( this.possibleValues.isPresent() && !this.possibleValues.get().contains(value) ) {
			return false;
		}
		
		return true;
	}
}
