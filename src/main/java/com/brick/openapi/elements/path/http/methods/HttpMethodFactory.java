package com.brick.openapi.elements.path.http.methods;

import com.brick.logger.Logger;
import com.brick.utilities.exception.KeyNotFound;import com.brick.openapi.elements.Components;
import com.brick.openapi.elements.path.http.HttpMethodConstants;
import com.brick.openapi.elements.security.Security;
import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;

import java.util.Map;
import java.util.Optional;

/*
    Description: Factory Pattern to Initialise Different Http Methods
 */
public class HttpMethodFactory {
	
	private HttpMethodFactory() {
		super();
	}
	
    public static HttpMethod getHttpMethod(String method, Map<String,Object> methodData, Components components, Optional<Security> rootSecurity) throws InvalidValue, KeyNotFound {
        
        switch( method ){
            case HttpMethodConstants.GET:
                return new GetHttpMethod( new BrickMap(methodData),components,  rootSecurity);

            case HttpMethodConstants.POST:
                return new PostHttpMethod( new BrickMap(methodData), components,  rootSecurity );

            case HttpMethodConstants.PUT:
                return new PutHttpMethod(new BrickMap(methodData), components,  rootSecurity );

            case HttpMethodConstants.DELETE:
                return new DeleteHttpMethod( new BrickMap(methodData), components,  rootSecurity );

            default:
                InvalidValue invalidValue = new InvalidValue(method);
                Logger.logException(invalidValue);
                throw invalidValue;
        }
    }
}
