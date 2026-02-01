package com.brick.openapi.elements.path.parameter;

import java.util.Map;

import com.brick.logger.Logger;
import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

public class QueryParameter extends Parameter {
	
	public QueryParameter(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
		super(brickMap,components);
	}

	@Override
	public boolean validateParameter(BrickRequestData brickRequestData) {
		Map<String,String[]> parameterMap = brickRequestData.getQueryParams();
		
		if( !parameterMap.containsKey(this.name) ) {
			if( this.required.isPresent() && Boolean.TRUE.equals(this.required.get()) ) {
				Logger.info(PARAMTER_NOT_FOUND);
				return false;
			}
			
			return true;
		}
		
		if( parameterMap.get(this.name).length == 1 ) {
    		JsonNode parameterData = new ObjectMapper().valueToTree(parameterMap.get(this.name)[0] );
    		if( !schema.validateData(parameterData) ) {
    			Logger.info(GENERIC_ERROR_MESSAGE);
    			return false;
    		}
		}else {
    		ArrayNode jsonArray = new ObjectMapper().createArrayNode();
    		for( String val : parameterMap.get(this.name) ) {
    			jsonArray.add(val);
    		}
    		
    		
    		if( !schema.validateData(jsonArray) ) {
    			Logger.info(GENERIC_ERROR_MESSAGE);
    			return false;
    		}
		}
		
		return true;
	}
	
}
