package com.brick.openapi.elements.path;

import java.util.Optional;

import com.brick.openapi.elements.Components;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.KeyNotFound;

import tools.jackson.databind.JsonNode;

public class RequestBody {
    private final Optional<String> description;
    private final Content content;
    private final boolean required;

    public RequestBody(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
        this.description = brickMap.getOptionalString(OpenAPIKeyConstants.DESCRIPTION);
        this.content = new Content(brickMap.getBrickMap(OpenAPIKeyConstants.CONTENT), components);

        Optional<Boolean> optionalRequired = brickMap.getOptionalBoolean(OpenAPIKeyConstants.REQUIRED);
        if( optionalRequired.isPresent() ) {
            this.required = optionalRequired.get();
        }else{
            this.required = false;
        }

        
    }
    
    /*
     * Description: Validates Request Body
     */
    public boolean validateRequest(JsonNode requestBody) {
    	if( !this.required && requestBody.isEmpty()) {
			return true;
    	}
    	
    	return this.content.validateData(requestBody);
    }
}
