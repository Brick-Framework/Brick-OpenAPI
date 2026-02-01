package com.brick.openapi.elements.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.brick.logger.Logger;
import com.brick.openapi.elements.Components;
import com.brick.openapi.elements.security.scheme.SecurityScheme;
import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.exception.KeyNotFound;

public class Security {
    private final List<SecurityScheme> securityScheme;

    public Security(List<Map<String,Object>> listOfMap, Components components) throws KeyNotFound, InvalidValue {
        
        this.securityScheme = new ArrayList<>();

        for( Map<String,Object> map: listOfMap ){
            for( Map.Entry<String,Object> entry :  map.entrySet() ){
                if( entry.getValue() instanceof List && ((List)entry.getValue()).isEmpty() ){ // Checking Security References have "[]" following scheme name
                    this.securityScheme.add( components.getSecurityScheme(entry.getKey()) );
                }else{
                    InvalidValue invalidValue = new InvalidValue("Empty List Expected");
                    Logger.logException(invalidValue);
                    throw invalidValue;
                }
            }
        }
        
    }
}
