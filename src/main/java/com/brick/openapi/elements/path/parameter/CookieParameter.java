package com.brick.openapi.elements.path.parameter;

import java.util.List;

import com.brick.logger.Logger;
import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.KeyNotFound;

import jakarta.servlet.http.Cookie;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class CookieParameter extends Parameter {
	public CookieParameter(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
		super(brickMap,components);
	}

	@Override
	public boolean validateParameter(BrickRequestData brickRequestData) {
		List<Cookie> listOfCookies = brickRequestData.getCookies();
		
		if( null == listOfCookies ) {
			Logger.info(PARAMTER_NOT_FOUND);
			return false;
		}
		
		boolean isCookieFound = false;
		for( Cookie c : listOfCookies ) {
			if( this.name.equals(c.getName()) ) {  
				isCookieFound = true;
				JsonNode cookieData = new ObjectMapper().valueToTree(c.getValue() );
				
				if( !schema.validateData(cookieData) ) {
					Logger.info(GENERIC_ERROR_MESSAGE);
					return false;
				}
				
				break;
			}
		}
		
		if( this.required.isPresent() ) {
			if( this.required.get() && !isCookieFound ) {
				Logger.info(PARAMTER_NOT_FOUND);
				return false;
			}
		}
		
		return true;
	}
}
