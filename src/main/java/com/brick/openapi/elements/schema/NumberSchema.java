package com.brick.openapi.elements.schema;

import java.util.List;
import java.util.Optional;

import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;

public class NumberSchema extends Schema {
    private Optional<Double> minimum;
    private Optional<Double> maximum;
    private final Optional<List<Double>> possibleValues;
    private final boolean nullable;

    public NumberSchema(BrickMap brickMap) throws KeyNotFound, InvalidValue {
        
        this.minimum = brickMap.getOptionalDouble(OpenAPIKeyConstants.MINIMUM);
        this.maximum = brickMap.getOptionalDouble(OpenAPIKeyConstants.MAXIMUM);
        this.possibleValues = brickMap.getOptionalListOfDouble(OpenAPIKeyConstants.ENUM);
        if( brickMap.contains(OpenAPIKeyConstants.NULLABLE) ){
            this.nullable = brickMap.getBoolean(OpenAPIKeyConstants.NULLABLE);
        }else{
            this.nullable = false;
        }
        
    }
    
    @Override
	public boolean validateData(JsonNode data) {
		
		//Checking Nullable Condition
		if( data == null || data.isNull() ) {
			return this.nullable;
		}
		
		//Checking if Data is Integer
		if( !data.isDouble() ) {
			return false;
		}
		
		//Checking Range of Values
		double value = data.asDouble();
		if( this.minimum.isPresent() && value < this.minimum.get() ) {
			return false;
		}
		if( this.maximum.isPresent() && value > this.maximum.get() ) {
			return false;
		}
		
		if( this.possibleValues.isPresent() && ! this.possibleValues.get().contains(value) ) {
			return false;
		}
		
		return true;
	}
}
