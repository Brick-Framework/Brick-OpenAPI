package com.brick.openapi.elements;

import com.brick.openapi.elements.path.Response;
import com.brick.openapi.elements.path.parameter.Parameter;
import com.brick.openapi.elements.schema.Schema;
import com.brick.openapi.elements.security.scheme.BasicAuthScheme;
import com.brick.openapi.elements.security.scheme.BearerAuthScheme;
import com.brick.openapi.elements.security.scheme.SecurityScheme;
import com.brick.openapi.exception.CyclicReferenceFound;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.IntNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;
import tools.jackson.databind.node.StringNode;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ComponentsTest {

    @Test
    public void componentGeneration_cyclicReferenceInSchema() throws FileNotFoundException, InvalidData{
    	String filePath = "/dummy_yaml/components/components_cyclicSchema.yaml";
    	FileReader fileReader = new YamlFileReader(filePath);
    	BrickMap componentsMap = fileReader.getMap();
        
        assertThrows(CyclicReferenceFound.class,()->{
            new Components( componentsMap );
        });
    }
    
    @Test
    public void sucesss_schemas() throws KeyNotFound, InvalidValue, CyclicReferenceFound, FileNotFoundException, InvalidData {
    	String filePath = "/dummy_yaml/components/components_schema_success.yaml";
    	FileReader fileReader = new YamlFileReader(filePath);
    	BrickMap componentsMap = fileReader.getMap();
    	
    	Components components = new Components(componentsMap);
    	Schema schemaD = components.getSchema("D");


    	assertTrue(schemaD.validateData( new IntNode(15) ) );
    	assertFalse(schemaD.validateData( new IntNode(5) ) );
    	assertFalse(schemaD.validateData( new IntNode(110) ) );
    	
    	assertThrows(KeyNotFound.class, ()->{
    		components.getSchema("E");
    	});
    }
    
    @Test
    public void success_parameters() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue, CyclicReferenceFound {
    	String filePath = "/dummy_yaml/components/components_parameters_success.yaml";
    	FileReader fileReader = new YamlFileReader(filePath);
    	BrickMap componentsMap = fileReader.getMap();
    	
    	Components components = new Components(componentsMap);
    	
    	Map<String,String[]> queryParams = new HashMap<String, String[]>();
    	queryParams.put("categoryId1", new String[]{"abc"});
    	queryParams.put("categoryId2", new String[]{"abcdefg"});
    	BrickRequestData brickRequestData = new BrickRequestData(null, null, null, null, queryParams);
    	
    	Parameter categoryQueryParam1 = components.getParameter("categoryIdQueryParam1");
    	assertTrue( categoryQueryParam1.validateParameter(brickRequestData) );
    	
    	Parameter categoryQueryParam2 = components.getParameter("categoryIdQueryParam2");
    	assertFalse( categoryQueryParam2.validateParameter(brickRequestData) );
    	
    	assertThrows(KeyNotFound.class,()->{
    		components.getParameter("invalidValue");
    	});
    }
    
    @Test
    public void success_responses() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue, CyclicReferenceFound {
    	String filePath = "/dummy_yaml/components/components_responses_success.yaml";
    	FileReader fileReader = new YamlFileReader(filePath);
    	BrickMap componentsMap = fileReader.getMap();
    	
    	Components components = new Components(componentsMap);
    	Response badRequestResponse = components.getResponse("BadRequest");
    	
    	ObjectNode responseBody = JsonNodeFactory.instance.objectNode();
    	responseBody.put("error","error");
    	responseBody.put("message", "message");
    	
    	
    	assertTrue(badRequestResponse.validateResponse(responseBody));
    	
    	assertThrows(KeyNotFound.class, ()->{
    		components.getResponse("invalidValue");
    	});
    }
    
    @Test
    public void success_securitySheme() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue, CyclicReferenceFound {
    	String filePath = "/dummy_yaml/components/components_securityScheme_success.yaml";
    	FileReader fileReader = new YamlFileReader(filePath);
    	BrickMap componentsMap = fileReader.getMap();
    	
    	Components components = new Components(componentsMap);
    	
    	SecurityScheme basicAuthScheme = components.getSecurityScheme("BasicAuth");
    	assertTrue( basicAuthScheme instanceof BasicAuthScheme );
    	
    	SecurityScheme bearerAuthScheme = components.getSecurityScheme("BearerAuth");
    	assertTrue( bearerAuthScheme instanceof BearerAuthScheme );
    	
    	assertThrows(KeyNotFound.class, ()->{
    		components.getSecurityScheme("invalidScheme");
    	});
    }
    
}
