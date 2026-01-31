package com.brick.openapi.elements.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.brick.logger.Logger;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;

public abstract class Schema {
	
	private static List<String> getReferencesFromAllOfList(BrickMap brickMap) throws KeyNotFound, InvalidValue{
		List<String> references = new ArrayList<>();
		Optional<List<Map<String,Object>>> allOfList = brickMap.getOptionalListOfMap(OpenAPIKeyConstants.ALL_OF);
        if( allOfList.isPresent() ){ // Checking for References in All Of List
            for( Map<String,Object> m: allOfList.get() ) {
                references.addAll( getReferences(new BrickMap(m)) );
            }
        }
        return references;
	}
	
	private static List<String> getReferencesFromAnyOfList(BrickMap brickMap) throws KeyNotFound, InvalidValue{
		List<String> references = new ArrayList<>();
		Optional<List<Map<String,Object>>> anyOfList = brickMap.getOptionalListOfMap(OpenAPIKeyConstants.ANY_OF);
        if( anyOfList.isPresent() ){ // Checking for References in Any of List
            for( Map<String,Object> m :  anyOfList.get() ){
                references.addAll( getReferences( new BrickMap(m) ) );
            }
        }
        return references;
	}
	
	private static List<String> getReferencesFromOneOfList(BrickMap brickMap) throws KeyNotFound, InvalidValue{
		List<String> references = new ArrayList<>();
		Optional<List<Map<String,Object>>> oneOfList = brickMap.getOptionalListOfMap(OpenAPIKeyConstants.ONE_OF);
        if( oneOfList.isPresent() ){ // Checking References in One Of List
            for( Map<String,Object> m : oneOfList.get() ){
                references.addAll( getReferences( new BrickMap(m) ) );
            }
        }
        return references;
	}
	
	private static List<String> getReferencesFromSchemaDefinition(BrickMap brickMap) throws KeyNotFound, InvalidValue{
		List<String> references = new ArrayList<>();
		if( brickMap.contains(OpenAPIKeyConstants.SCHEMA_TYPE) ) {
            SchemaType schemaType = SchemaType.fromString( brickMap.getString(OpenAPIKeyConstants.SCHEMA_TYPE) );
            switch (schemaType) { // Checking for References based on Schema Type
                case ARRAY:
                    references.addAll(Schema.getReferences( brickMap.getBrickMap(OpenAPIKeyConstants.ARRAY_SCHEMA) ));
                    break;

                case NUMBER:
                    break;

                case INTEGER:
                    break;

                case OBJECT:
                    BrickMap propertyMap = brickMap.getBrickMap(OpenAPIKeyConstants.PROPERTIES);
                    for( Map.Entry<String, Object> entry: propertyMap ){
                        references.addAll( Schema.getReferences( propertyMap.getBrickMap(entry.getKey()) )  );
                    }
                    break;

                case STRING:
                    break;
            }
        }
        return references;
	}

    /*
        Description: Returns a List of All the references that current schema uses
     */
    public static List<String> getReferences(BrickMap brickMap) throws KeyNotFound, InvalidValue {
        if( brickMap.isEmpty() ){
            InvalidValue invalidValue = new InvalidValue("Empty Object");
            Logger.logException(invalidValue);
            throw invalidValue;
        }

        List<String> references = new ArrayList<>();
        if( brickMap.contains(OpenAPIKeyConstants.REFERENCE) ){
            String refValue = brickMap.getString(OpenAPIKeyConstants.REFERENCE);
            if( !refValue.startsWith(OpenAPIKeyConstants.REFERENCE_SCHEMA) ){
                InvalidValue invalidValue = new InvalidValue(refValue);
                Logger.logException(invalidValue);
                throw  invalidValue;
            }

            String referenceSchema = refValue.substring(OpenAPIKeyConstants.REFERENCE_SCHEMA.length());
            references.add(referenceSchema);
        }else {
        	//Getting References From all of list
        	references.addAll( getReferencesFromAllOfList(brickMap) );

            //Getting References From any of list
            references.addAll( getReferencesFromAnyOfList(brickMap) );

            //Getting References From one of list
            references.addAll( getReferencesFromOneOfList(brickMap) );

            // Getting References From Schema Definition
            references.addAll( getReferencesFromSchemaDefinition(brickMap) );
        }

        return references;
    }
    
    /*
     * Description Validates Data
     */
    public abstract boolean validateData(JsonNode data);
}
