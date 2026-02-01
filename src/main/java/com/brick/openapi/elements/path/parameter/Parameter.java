package com.brick.openapi.elements.path.parameter;

import java.util.Optional;

import com.brick.logger.Logger;
import com.brick.openapi.elements.Components;
import com.brick.openapi.elements.schema.Schema;
import com.brick.openapi.elements.schema.SchemaFactory;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.KeyNotFound;

public abstract class Parameter {
    protected final String name;
    private final ParameterType type;
    protected final Optional<String> description;
    protected final Optional<Boolean> required;// this property is required if type = PATH
    protected final Schema schema;
    
    protected String GENERIC_ERROR_MESSAGE;
    protected String PARAMTER_NOT_FOUND;

    protected Parameter(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
        this.name = brickMap.getString(OpenAPIKeyConstants.NAME);
        this.description = brickMap.getOptionalString(OpenAPIKeyConstants.DESCRIPTION);
        this.type = ParameterType.fromString( brickMap.getString(OpenAPIKeyConstants.PARAMETER_TYPE) );
        this.required = brickMap.getOptionalBoolean(OpenAPIKeyConstants.REQUIRED);
        
        this.GENERIC_ERROR_MESSAGE = "Parameter : "+this.name+" coud not be validated";
        this.PARAMTER_NOT_FOUND = "Parameter : "+this.name+" Required but could not be found";

        this.schema = SchemaFactory.getSchema( brickMap.getBrickMap(OpenAPIKeyConstants.SCHEMA), components );
        
    }

    /*
        Description : Check if Reference present in components then return reference else create Object
     */
    public static Parameter getParameter(BrickMap brickMap, Components components) throws KeyNotFound, InvalidValue {
        if(brickMap.contains(OpenAPIKeyConstants.REFERENCE)){
            
            String refValue = brickMap.getString(OpenAPIKeyConstants.REFERENCE);
            if( !refValue.startsWith(OpenAPIKeyConstants.REFERENCE_PARAMETER) ){
                InvalidValue invalidValue = new InvalidValue(refValue);
                Logger.logException(invalidValue);
                throw invalidValue;
            }

            String parameterName = refValue.substring(OpenAPIKeyConstants.REFERENCE_PARAMETER.length());
            return components.getParameter(parameterName);
        }else{
            
            return ParameterFactory.getParameter(brickMap, components);
        }
    }
    
    /*
     * Description: Check if Parameter is Present and Correct
     */
    public abstract boolean validateParameter(BrickRequestData brickRequestData);

    public String getName() {
        return name;
    }

    public ParameterType getType() {
        return type;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Optional<Boolean> getRequired() {
        return required;
    }

    public Schema getSchema() {
        return schema;
    }
}
