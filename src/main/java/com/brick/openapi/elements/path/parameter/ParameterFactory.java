package com.brick.openapi.elements.path.parameter;

import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.KeyNotFound;

public class ParameterFactory {
	public static Parameter getParameter(BrickMap brickMap, Components components) throws InvalidValue, KeyNotFound {
		ParameterType type = ParameterType.fromString( brickMap.getString(OpenAPIKeyConstants.PARAMETER_TYPE) );
		switch( type ) {
			case COOKIE:
				return new CookieParameter(brickMap, components);
				
			case HEADER:
				return new HeaderParameter(brickMap, components);
				
			case PATH:
				return new PathParameter(brickMap, components);
				
			case QUERY:
				return new QueryParameter(brickMap, components);
		}
		
		return null; // Dead Code
	}

}
