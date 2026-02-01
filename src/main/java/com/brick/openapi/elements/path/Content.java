package com.brick.openapi.elements.path;

import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;

import com.brick.logger.Logger;
import com.brick.openapi.elements.Components;
import com.brick.openapi.elements.schema.Schema;
import com.brick.openapi.elements.schema.SchemaFactory;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;

import java.util.*;

public class Content {
    private final Map<String,Schema> contentSchemaMap;
    
    private static final String JSON_DATA = "application/json";

    /*
        Description: List of Valid Content Type Supported by Current Version of OpenApi Parse
     */
    private final List<String> validContentType = new ArrayList<>(
            Arrays.asList(
                    JSON_DATA
            )
    );

    public Content(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
        
        this.contentSchemaMap = new HashMap<>();
        for(Map.Entry<String,Object> entry : brickMap ){
            if( !validContentType.contains(entry.getKey()) ){
                InvalidValue invalidValue = new InvalidValue(entry.getKey());
                Logger.logException(invalidValue);
                throw invalidValue;
            }
            this.contentSchemaMap.put(entry.getKey(), SchemaFactory.getSchema(new BrickMap( entry.getValue() ) .getBrickMap(OpenAPIKeyConstants.SCHEMA), components ) );
        }

        
    }

    public Map<String, Schema> getContent() {
        return contentSchemaMap;
    }
    
    /*
     * Description: Validates Request Body
     */
    public boolean validateData(JsonNode data) {
    	for( Map.Entry<String, Schema> entry: contentSchemaMap.entrySet() ) {
    		if( entry.getValue().validateData(data) ) {
    			return true;
    		}
    	}
    	
    	return false;
    }
}
