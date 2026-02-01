package com.brick.openapi.elements.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;

public class ObjectSchema extends Schema {
    private final Map<String,Schema> properties;
    private final List<String> requiredProperties;
    private final boolean nullable;

    public ObjectSchema(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
        
        this.properties = new HashMap<>();
        BrickMap propertiesMap = brickMap.getBrickMap(OpenAPIKeyConstants.PROPERTIES);
        for( Map.Entry<String,Object> entry : propertiesMap ){
            this.properties.put(entry.getKey(), SchemaFactory.getSchema( new BrickMap( entry.getValue() ), components ) );
        }

        Optional<List<String>> optionalListOfString = brickMap.getOptionalListOfString(OpenAPIKeyConstants.REQUIRED);
        this.requiredProperties = optionalListOfString.orElseGet(ArrayList::new);

        if( brickMap.contains(OpenAPIKeyConstants.NULLABLE) ){
            this.nullable = brickMap.getBoolean(OpenAPIKeyConstants.NULLABLE);
        }else{
            this.nullable = false;
        }
        
    }

	@Override
	public boolean validateData(JsonNode data) {
		
		//Checking Nullable Condition
		if( data == null || data.isNull()  || ( data.isObject() && data.isEmpty()) ) {
			return this.nullable;
		}
				
		for( Map.Entry<String, Schema> entry: this.properties.entrySet() ) {
			if( isSchemaNotAvailableAndRequired(data, entry) || isSchemaAvailableAndNotValid(data, entry) ) { // If that Schema is available then Validate for That Schema
				return false;
			}
		}
		
		return true;
	}

	private boolean isSchemaNotAvailableAndRequired(JsonNode data, Map.Entry<String, Schema> entry) {
		boolean isDataAvailable = data.has(entry.getKey());
		boolean isDataRequired = this.requiredProperties.contains(entry.getKey());
		return !isDataAvailable && isDataRequired;
	}

	private boolean isSchemaAvailableAndNotValid(JsonNode data, Map.Entry<String, Schema> entry) {
		boolean isDataAvailable = data.has(entry.getKey());
		boolean isDataValid = entry.getValue().validateData(data.get(entry.getKey()));
		return isDataAvailable && !isDataValid;
	}

}
