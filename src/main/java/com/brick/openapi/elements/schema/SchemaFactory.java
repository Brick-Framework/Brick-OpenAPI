package com.brick.openapi.elements.schema;


import com.brick.logger.Logger;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;

/*
    Description: Factory Pattern to Initialise Different Schema Types
 */
public class SchemaFactory {
	
	private SchemaFactory() {
		super();
	}
	
    public static Schema getSchema(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
        // Check if it is Reference if it is, Return Reference else create Object
        if( brickMap.contains(OpenAPIKeyConstants.REFERENCE) ){
            
            String referenceValue = brickMap.getString(OpenAPIKeyConstants.REFERENCE);
            if( !referenceValue.startsWith(OpenAPIKeyConstants.REFERENCE_SCHEMA) ){
                InvalidValue invalidValue = new InvalidValue(referenceValue);
                Logger.logException(invalidValue);
                throw invalidValue;
            } 
            String schemaName = referenceValue.substring(OpenAPIKeyConstants.REFERENCE_SCHEMA.length());

            return components.getSchema(schemaName);
        }else {
        	if( brickMap.contains(OpenAPIKeyConstants.ALL_OF) ) {
            	return new AllOfSchema(brickMap, components);
            }
            
            if( brickMap.contains(OpenAPIKeyConstants.ONE_OF) ) {
            	return new OneOfSchema(brickMap, components);
            }
            
            if( brickMap.contains(OpenAPIKeyConstants.ANY_OF) ) {
            	return new AnyOfSchema(brickMap, components);
            }
            
            if( brickMap.contains(OpenAPIKeyConstants.SCHEMA_TYPE) ) {
	            SchemaType type = SchemaType.fromString(brickMap.getString(OpenAPIKeyConstants.SCHEMA_TYPE));
	            switch (type) {
	                case ARRAY:
	                    return new ArraySchema(brickMap,components);
	
	                case INTEGER:
	                    return new IntegerSchema(brickMap);
	
	                case NUMBER:
	                    return new NumberSchema(brickMap);
	
	                case OBJECT:
	                    return new ObjectSchema(brickMap,components);
	
	                case STRING:
	                    return new StringSchema(brickMap);
	            }
            }
            
            InvalidValue invalidValue = new InvalidValue("Could not identify Schema Type");
            Logger.logException(invalidValue);
            throw invalidValue;
        }
    }
}
