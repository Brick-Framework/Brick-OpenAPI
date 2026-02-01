package com.brick.openapi.elements.path;

import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;import com.brick.logger.Logger;
import com.brick.openapi.elements.Components;
import com.brick.openapi.elements.path.http.methods.HttpMethod;
import com.brick.openapi.elements.path.http.methods.HttpMethodFactory;
import com.brick.openapi.elements.security.Security;
import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Path {
    private final String uri;
    private final Map<String,HttpMethod> httpMethod;
    private final List<String> pathParameters;

    public Path(String endPoint, BrickMap brickMap, Components components, Optional<Security> rootSecurity) throws InvalidValue, KeyNotFound {
        
        this.uri = endPoint;

        //Getting Parameters from endPoint
        this.pathParameters = new ArrayList<>();
        String[] pathParts = endPoint.split("/");
        for( String part : pathParts ) {
        	if( part.startsWith("{") && part.endsWith("}") ) {
        		this.pathParameters.add(part.substring(1,part.length()-1));
        	}else if( part.startsWith("{") || part.endsWith("}") ) {
        		InvalidValue invalidValue = new InvalidValue(endPoint);
              Logger.logException(invalidValue);
              throw invalidValue;
        	}
        }

        this.httpMethod = new HashMap<>();
        for(Map.Entry<String,Object> entry: brickMap ){
            HttpMethod newMethod = HttpMethodFactory.getHttpMethod(entry.getKey(), (Map<String,Object>)entry.getValue(), components, rootSecurity);


            // Checks if all parameters are present in every http method
            for( String parameterName : pathParameters ){
                if( !newMethod.hasParameter(parameterName) ){
                    InvalidValue invalidValue = new InvalidValue(parameterName);
                    Logger.logException(invalidValue);
                    throw invalidValue;
                }
            }

            this.httpMethod.put(entry.getKey(), newMethod );
        }

        
    }  

    public String getUri() {
		return uri;
	}

    /*
     * Description: Returns HttpMethod If Present based on verb or method type else returns null
     */
	public HttpMethod getMethod(String verb) {
        return this.httpMethod.getOrDefault(verb.toLowerCase(), null);
    }
    
    
    /*
     * Check If Given uri matches the pattern of uri
     */
    public boolean matches(String uri) {
    	if( this.pathParameters.isEmpty() ) {
    		return this.uri.equals(uri);
    	}
    	
    	String[] patternParts = this.uri.split("/");
		String[] actualParts = uri.split("/");
		
		if( patternParts.length != actualParts.length ) {
			return false;
		}
		
		for( int i = 1; i < patternParts.length; i++ ) { // Iteration Starts from 1 cause first string is empty string
			if( patternParts[i].charAt(0) != '{' && !patternParts[i].equals(actualParts[i]) ) {
				return false;
			}
		}
		
    	return true;
    }
    
    /*
     * Returns Values of Path Variables in Map Format with variableName as key
     */
    public Map<String,String> getPathVariables(String uri) throws InvalidData{
    	Map<String,String> pathVariables = new HashMap<String, String>();
		
		if( !matches(uri) ) {
			InvalidData invalidData = new InvalidData(uri + "does not match path pattern " + this.uri);
			Logger.logException(invalidData);
			throw invalidData;
		}
		
		String[] patternParts = this.uri.split("/");
		String[] actualParts = uri.split("/");
		
		for( int i = 1; i < patternParts.length; i++ ) { // Iteration Starts from 1 cause first string is empty string
			if( patternParts[i].charAt(0) == '{' ) {
				pathVariables.put(patternParts[i].substring(1,patternParts[i].length()-1), actualParts[i]);
			}
		}
		
		return pathVariables;
    }
}
