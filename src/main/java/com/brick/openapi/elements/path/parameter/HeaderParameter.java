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

public class HeaderParameter extends Parameter {
	public HeaderParameter(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
		super(brickMap,components);
	}

	@Override
	public boolean validateParameter(BrickRequestData brickRequestData) {
		Map<String,String> headers = brickRequestData.getHeaders();
		
		if( !headers.containsKey(this.name) ) {
			if( this.required.isPresent() && this.required.get()) {
				Logger.info(PARAMTER_NOT_FOUND);
				return false;
			}
			
			return true;
		}
		    		
		JsonNode headerData = new ObjectMapper().valueToTree(headers.get(this.name));
		if( !schema.validateData(headerData) ) {
			Logger.info(GENERIC_ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
}
