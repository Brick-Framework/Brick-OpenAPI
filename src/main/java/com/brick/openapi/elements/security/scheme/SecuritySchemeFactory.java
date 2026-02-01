package com.brick.openapi.elements.security.scheme;

import com.brick.utilities.exception.KeyNotFound;import com.brick.logger.Logger;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;


public class SecuritySchemeFactory {
    private static final String SCHEME_TYPE_HTTP = "http";

    private static final String SCHEME_HTTP_BASIC = "basic";
    private static final String SCHEME_HTTP_BEARER = "bearer";
    
    private SecuritySchemeFactory() {
    	super();
    }
    
    public static SecurityScheme getSecurityScheme(BrickMap brickMap) throws KeyNotFound, InvalidValue {
        
        String type = brickMap.getString(OpenAPIKeyConstants.SCHEME_TYPE);

        switch ( type ){
            case SCHEME_TYPE_HTTP:
                return getHttpTypeSecurityScheme(brickMap);

            default:
                InvalidValue invalidValue = new InvalidValue(type);
                Logger.logException(invalidValue);
                throw invalidValue;
        }
    }

    private static SecurityScheme getHttpTypeSecurityScheme(BrickMap brickMap) throws KeyNotFound, InvalidValue {
        
        String scheme = brickMap.getString(OpenAPIKeyConstants.SCHEME);

        switch ( scheme ){
            case SCHEME_HTTP_BASIC:
                return new BasicAuthScheme();

            case SCHEME_HTTP_BEARER:
                return new BearerAuthScheme(brickMap);

            default:
                InvalidValue invalidValue = new InvalidValue(scheme);
                Logger.logException(invalidValue);
                throw invalidValue;
        }
    }
}
