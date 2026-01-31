package com.brick.openapi.elements.path.parameter;

import java.util.Map;

import com.brick.logger.Logger;
import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class PathParameter extends Parameter{
	public PathParameter(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
		super(brickMap,components);
		
		if( ! required.isPresent() ){ //Checks if "required" property is present or not
            KeyNotFound keyNotFound = new KeyNotFound(OpenAPIKeyConstants.REQUIRED);
            Logger.logException(keyNotFound);
            throw keyNotFound;
        }else if( ! required.get() ){ // Checks if "required" property is false
            InvalidValue invlaidValue = new InvalidValue(OpenAPIKeyConstants.REQUIRED);
            Logger.logException(invlaidValue);
            throw invlaidValue;
        }
	}

	@Override
	public boolean validateParameter(BrickRequestData brickRequestData) {
		Map<String,String> pathVariables = brickRequestData.getPathVariables();
		
		JsonNode pathVariableData = new ObjectMapper().valueToTree(pathVariables.get(this.name));
		
		if( !schema.validateData(pathVariableData) ) {
			Logger.info(GENERIC_ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}
