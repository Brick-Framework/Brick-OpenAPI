package com.brick.openapi.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.brick.openapi.OpenAPI;
import com.brick.openapi.elements.info.Contact;
import com.brick.openapi.elements.info.Info;
import com.brick.openapi.elements.path.Content;
import com.brick.openapi.elements.path.Path;
import com.brick.openapi.elements.path.Response;
import com.brick.openapi.elements.path.http.HttpStatusCode;
import com.brick.openapi.elements.path.http.methods.DeleteHttpMethod;
import com.brick.openapi.elements.path.http.methods.GetHttpMethod;
import com.brick.openapi.elements.path.http.methods.PostHttpMethod;
import com.brick.openapi.elements.path.http.methods.PutHttpMethod;
import com.brick.openapi.elements.path.parameter.Parameter;
import com.brick.openapi.elements.path.parameter.ParameterType;
import com.brick.openapi.elements.schema.ArraySchema;
import com.brick.openapi.elements.schema.IntegerSchema;
import com.brick.openapi.elements.schema.Schema;
import com.brick.openapi.elements.server.Server;
import com.brick.openapi.elements.server.ServerVariable;
import com.brick.openapi.exception.InvalidOpenAPISpecification;
import com.brick.utilities.exception.InvalidData;


public class OpenAPIYamlFileReaderTest {

    @Test
    public void getOpenAPI_invalidFilePath(){
        String filePath = "invalid.yaml";

        OpenAPIFileYamlReader openAPIFileYamlReader = new OpenAPIFileYamlReader(filePath);

        assertThrows(FileNotFoundException.class,()->{
            openAPIFileYamlReader.getOpenAPI();
        });
    }

    @Test
    public void getOpenAPI_invalidDataFile(){
        String filePath = "/dummy_yaml/openapi/dummy_invalid_yaml.yaml";

        OpenAPIFileYamlReader openAPIFileYamlReader = new OpenAPIFileYamlReader(filePath);

        assertThrows(InvalidData.class,()->{
            openAPIFileYamlReader.getOpenAPI();
        });
    }
    
    private void assertInvalidOpenApi(String filePath) {
    	OpenAPIFileYamlReader openAPIFileYamlReader = new OpenAPIFileYamlReader(filePath);

        assertThrows(InvalidOpenAPISpecification.class,()->{
            openAPIFileYamlReader.getOpenAPI();
        });
    }
    
    @Test
    public void getOpenApi_invalidOpenApiFile() {
    	//Info and Version Missing
    	assertInvalidOpenApi("/dummy_yaml/openapi/dummy_invalid_infoVersion_missing.yaml");
    	
    	//Path Parameter Required Missing
    	assertInvalidOpenApi("/dummy_yaml/openapi/dummy_invalid_pathParamRequired_missing.yaml");
    	
    	//Path Parameter Missing
    	assertInvalidOpenApi("/dummy_yaml/openapi/dummy_invalid_pathParam_missing.yaml");
    	
    	//Endpoint Invalid
    	assertInvalidOpenApi("/dummy_yaml/openapi/dummy_invalid_endpoint_invalid.yaml");
    	
    	//Invalid Content Type
    	assertInvalidOpenApi("/dummy_yaml/openapi/dummy_invalid_invalidContentType.yaml");
    	
    	//Empty Schema
    	assertInvalidOpenApi("/dummy_yaml/openapi/dummy_invalid_emptySchema.yaml");
    }
    
    private OpenAPI getOpenAPIFromFile(String filePath) throws FileNotFoundException, InvalidData, InvalidOpenAPISpecification {
        OpenAPIFileReader openAPIFileReader = new OpenAPIFileYamlReader(filePath);
        return openAPIFileReader.getOpenAPI();
    }
    
    @Test
    public void getOpenAPI_validFile_withoutComponents_infoContact() throws FileNotFoundException, InvalidData, InvalidOpenAPISpecification {
        OpenAPI openAPI = getOpenAPIFromFile("/dummy_yaml/openapi/dummy_valid_withoutComponents.yaml");
        
        assertEquals("3.0.4",openAPI.getOpenApiVersion());
        
        //Info Components Test
        Info info = openAPI.getInfo();
        assertEquals("Random Api Title",info.getTitle());
        assertTrue(info.getDescription().isPresent());
        assertEquals("API Definition Test",info.getDescription().get());
        assertTrue(info.getTermsOfService().isPresent());
        assertEquals("https://randomurl.com/terms-of-service", info.getTermsOfService().get());
        assertEquals("0.0.1",info.getApiVersion());
        assertTrue(info.getContact().isPresent());
        assertFalse(info.getSummary().isPresent());

        //Contact Test
        Contact contact = info.getContact().get();
        assertFalse(contact.getName().isPresent());
        assertFalse(contact.getUrl().isPresent());
        assertFalse(contact.getEmail().isPresent());
    }
    
    @Test
    public void getOpenAPI_validFile_withoutComponents_server() throws FileNotFoundException, InvalidData, InvalidOpenAPISpecification {
        OpenAPI openAPI = getOpenAPIFromFile("/dummy_yaml/openapi/dummy_valid_withoutComponents.yaml");

        //Servers Test
        Server server1 = openAPI.getServers().get(0);
        assertEquals("https://randomurl.com/development/",server1.getUrl());
        assertEquals("Development Server", server1.getDescription().get());
        assertTrue(server1.getVariables().isEmpty());

        Server server2 = openAPI.getServers().get(1);
        assertEquals("https://randomurl.com:{port}/production/{version}",server2.getUrl());
        assertEquals("Production Server", server2.getDescription().get());
        assertFalse(server2.getVariables().isEmpty());

        //Variables Test
        ServerVariable variable1 = server2.getVariables().get(0);
        assertEquals("version",variable1.getVariableName());
        assertEquals("v1",variable1.getDefaultValue());
        assertTrue(variable1.getDescription().isPresent());
        assertEquals("Value of Version",variable1.getDescription().get());

        ServerVariable variable2 = server2.getVariables().get(1);
        assertEquals("port",variable2.getVariableName());
        assertEquals("8080",variable2.getDefaultValue());
        assertTrue(variable2.getDescription().isPresent());
        assertEquals("Port of Server",variable2.getDescription().get());
        assertTrue(variable2.getPossibleValue().isPresent());
        assertEquals(Arrays.asList("8080","443"), variable2.getPossibleValue().get());
    }

    @Test
    public void getOpenAPI_validFile_withoutComponents_path() throws InvalidData, FileNotFoundException, InvalidOpenAPISpecification {
        OpenAPI openAPI = getOpenAPIFromFile("/dummy_yaml/openapi/dummy_valid_withoutComponents.yaml");
        //Path Test

        // PATH 1 Test
        List<Path> pathList = openAPI.getPaths();
        assertFalse(pathList.isEmpty() );
        Path path = pathList.get(0);
        assertEquals("/categories", path.getUri());

        assertInstanceOf(GetHttpMethod.class, path.getMethod("get") );
        assertEquals("List of All Categories",path.getMethod("get").getSummary().get());
        assertEquals("Returns a list of all categories", path.getMethod("get").getDescription().get());

        //Parameters Test
        List<Parameter> paramterList = path.getMethod("get").getParameters();
        Parameter parameter = paramterList.get(0);
        assertEquals("categoryId", parameter.getName());
        assertEquals(ParameterType.QUERY ,parameter.getType());
        assertInstanceOf(IntegerSchema.class,parameter.getSchema());

        Map<HttpStatusCode,Response> responseMap = path.getMethod("get").getResponses();
        assertFalse( responseMap.isEmpty() );
        assertTrue( responseMap.containsKey(HttpStatusCode.SUCCESS) );
        Response response = responseMap.get(HttpStatusCode.SUCCESS);
        assertEquals("A List of Categories", response.getDescription().get() );
        assertInstanceOf(Content.class,response.getContent().get());
        Schema schema = response.getContent().get().getContent().get("application/json");
        assertInstanceOf(ArraySchema.class,schema);

        //PATH 3 Test
        path = pathList.get(2);
        assertEquals("/orders",path.getUri());
        assertInstanceOf(PostHttpMethod.class, path.getMethod("post"));
        assertInstanceOf(PutHttpMethod.class, path.getMethod("put"));
        assertInstanceOf(DeleteHttpMethod.class, path.getMethod("delete"));
    }
    
    @Test
    public void getOpenApi_validFile_withComponents_infoContact() throws FileNotFoundException, InvalidData, InvalidOpenAPISpecification {
        OpenAPI openAPI = getOpenAPIFromFile("/dummy_yaml/openapi/dummy_valid_withComponents.yaml");
        
        assertEquals("3.0.4",openAPI.getOpenApiVersion());

        //Info Components Test
        Info info = openAPI.getInfo();
        assertEquals("Random Api Title",info.getTitle());
        assertTrue(info.getDescription().isPresent());
        assertEquals("API Definition Test",info.getDescription().get());
        assertTrue(info.getTermsOfService().isPresent());
        assertEquals("https://randomurl.com/terms-of-service", info.getTermsOfService().get());
        assertEquals("0.0.1",info.getApiVersion());
        assertTrue(info.getContact().isPresent());
        assertFalse(info.getSummary().isPresent());

        //Contact Test
        Contact contact = info.getContact().get();
        assertFalse(contact.getName().isPresent());
        assertFalse(contact.getUrl().isPresent());
        assertFalse(contact.getEmail().isPresent());
    }
    
    @Test
    public void getOpenApi_validFile_withComponents_server() throws FileNotFoundException, InvalidData, InvalidOpenAPISpecification {
        OpenAPI openAPI = getOpenAPIFromFile("/dummy_yaml/openapi/dummy_valid_withComponents.yaml");
        
        //Servers Test
        Server server1 = openAPI.getServers().get(0);
        assertEquals("https://randomurl.com/development/",server1.getUrl());
        assertEquals("Development Server", server1.getDescription().get());
        assertTrue(server1.getVariables().isEmpty());

        Server server2 = openAPI.getServers().get(1);
        assertEquals("https://randomurl.com:{port}/production/{version}",server2.getUrl());
        assertEquals("Production Server", server2.getDescription().get());
        assertFalse(server2.getVariables().isEmpty());

        //Variables Test
        ServerVariable variable1 = server2.getVariables().get(0);
        assertEquals("version",variable1.getVariableName());
        assertEquals("v1",variable1.getDefaultValue());
        assertTrue(variable1.getDescription().isPresent());
        assertEquals("Value of Version",variable1.getDescription().get());

        ServerVariable variable2 = server2.getVariables().get(1);
        assertEquals("port",variable2.getVariableName());
        assertEquals("8080",variable2.getDefaultValue());
        assertTrue(variable2.getDescription().isPresent());
        assertEquals("Port of Server",variable2.getDescription().get());
        assertTrue(variable2.getPossibleValue().isPresent());
        assertEquals(Arrays.asList("8080","443"), variable2.getPossibleValue().get());
    }

    @Test
    public void getOpenAPI_validFile_withComponents_path() throws  FileNotFoundException, InvalidOpenAPISpecification, InvalidData {
        OpenAPI openAPI = getOpenAPIFromFile("/dummy_yaml/openapi/dummy_valid_withComponents.yaml");
        //Path Test

        // PATH 1 Test
        List<Path> pathList = openAPI.getPaths();
        assertFalse(pathList.isEmpty() );
        Path path = pathList.get(0);
        assertEquals("/categories", path.getUri());

        assertInstanceOf(GetHttpMethod.class, path.getMethod("get") );
        assertEquals("List of All Categories",path.getMethod("get").getSummary().get());
        assertEquals("Returns a list of all categories", path.getMethod("get").getDescription().get());

        //Parameters Test
        List<Parameter> paramterList = path.getMethod("get").getParameters();
        Parameter parameter = paramterList.get(0);
        assertEquals("categoryId", parameter.getName());
        assertEquals(ParameterType.QUERY ,parameter.getType());
        assertInstanceOf(IntegerSchema.class,parameter.getSchema());

        Map<HttpStatusCode,Response> responseMap = path.getMethod("get").getResponses();
        assertFalse( responseMap.isEmpty() );
        assertTrue( responseMap.containsKey(HttpStatusCode.SUCCESS) );
        Response response = responseMap.get(HttpStatusCode.SUCCESS);
        assertEquals("A List of Categories", response.getDescription().get() );
        assertInstanceOf(Content.class,response.getContent().get());
        Schema schema = response.getContent().get().getContent().get("application/json");
        assertInstanceOf(ArraySchema.class,schema);

        //PATH 3 Test
        path = pathList.get(2);
        assertEquals("/orders",path.getUri());
        assertInstanceOf(PostHttpMethod.class, path.getMethod("post"));
        assertInstanceOf(PutHttpMethod.class, path.getMethod("put"));
        assertInstanceOf(DeleteHttpMethod.class, path.getMethod("delete"));
    }
}
