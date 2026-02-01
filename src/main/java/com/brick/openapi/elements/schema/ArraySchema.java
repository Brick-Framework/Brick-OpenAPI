package com.brick.openapi.elements.schema;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;

public class ArraySchema extends Schema{
    private final Schema items;
    private final boolean nullable;
    private final Optional<Integer> minItems;
    private final Optional<Integer> maxItems;
    private final boolean uniqueItems;

    public ArraySchema(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
        
        this.items = SchemaFactory.getSchema( brickMap.getBrickMap(OpenAPIKeyConstants.ARRAY_SCHEMA), components );
        this.minItems = brickMap.getOptionalInteger(OpenAPIKeyConstants.MINIMUM_ITEMS);
        this.maxItems = brickMap.getOptionalInteger(OpenAPIKeyConstants.MAXIMUM_ITEMS);

        if( brickMap.contains(OpenAPIKeyConstants.NULLABLE) ){
            this.nullable = brickMap.getBoolean(OpenAPIKeyConstants.NULLABLE);
        }else{
            this.nullable = false;
        }

        if( brickMap.contains(OpenAPIKeyConstants.UNIQUE_ITEMS) ){
            this.uniqueItems = brickMap.getBoolean(OpenAPIKeyConstants.UNIQUE_ITEMS);
        }else{
            this.uniqueItems = false;
        }

        
    }

	@Override
	public boolean validateData(JsonNode data) {
		//Checking for Nullable Conditions
		if( data == null || data.isNull() ) {
			return this.nullable;
		}
				
		// Checking if Node is An Array
		if( !data.isArray() ) {
			return false;
		}
		
		
		// Checking if Length of Array is as defined
		int arraySize = data.size();
		if( this.minItems.isPresent() && arraySize < this.minItems.get() ) {
			return false;
		}
		if( this.maxItems.isPresent() && arraySize > this.maxItems.get() ) {
			return false;
		}
		
		
		Set<JsonNode> visitedNodes = new HashSet<>();
		// Checking Schema Validation for Array Items
		for( JsonNode node: data ) {
			if( !this.items.validateData(node) ) {
				return false;
			}
			
			visitedNodes.add(node);
		}
		
		
		//Checking Unique Item Condition
		
		return !( this.uniqueItems && visitedNodes.size() != arraySize );
	}
    
    
}
