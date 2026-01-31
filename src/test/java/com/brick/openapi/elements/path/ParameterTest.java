package com.brick.openapi.elements.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.brick.openapi.elements.Components;
import com.brick.openapi.elements.path.parameter.Parameter;
import com.brick.openapi.elements.path.parameter.ParameterType;
import com.brick.openapi.elements.schema.IntegerSchema;
import com.brick.openapi.elements.schema.StringSchema;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

import jakarta.servlet.http.Cookie;

public class ParameterTest {
	@Test
	public void path_success() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_path_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterMap, null);
		assertEquals("categoryId", parameter.getName());
		assertEquals(ParameterType.PATH, parameter.getType());
		assertEquals("path variable description", parameter.getDescription().get());
		assertTrue(parameter.getRequired().get());
		assertTrue( parameter.getSchema() instanceof StringSchema );
	}
	
	@Test
	public void path_keyNotFound() throws FileNotFoundException, InvalidData {
		String filePath = "/dummy_yaml/path/parameter_path_keyNotFound.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		assertThrows(KeyNotFound.class,()->{
			Parameter.getParameter(parameterMap, null);
		});
	}
	
	@Test
	public void path_InvalidValue() throws FileNotFoundException, InvalidData {
		String filePath = "/dummy_yaml/path/parameter_path_invalidValue.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		assertThrows(InvalidValue.class,()->{
			Parameter.getParameter(parameterMap, null);
		});
	}
	
	@Test
	public void getParameter_success() throws KeyNotFound, FileNotFoundException, InvalidData, InvalidValue {
		BrickMap parameterMap = mock(BrickMap.class);
		String refValue = OpenAPIKeyConstants.REFERENCE_PARAMETER+"parameterName";
		when( parameterMap.contains(OpenAPIKeyConstants.REFERENCE ) ).thenReturn(true);
		when( parameterMap.getString(OpenAPIKeyConstants.REFERENCE ) ).thenReturn(refValue).thenReturn("invalid");
		
		String filePath = "/dummy_yaml/path/parameter_path_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterFileMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterFileMap, null);
		Components components = mock(Components.class);
		when( components.getParameter("parameterName") ).thenReturn(parameter);
		
		assertEquals(parameter,Parameter.getParameter(parameterMap, components) );
		
		assertThrows(InvalidValue.class, ()->{
			Parameter.getParameter(parameterMap, components);
		});
	}
	
	@Test
	public void validateParameter_path() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_path_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		
		// Validation Succesful
		Map<String,String> pathVariables = new HashMap<String,String>();
		pathVariables.put("categoryId", "123" );
		
		BrickRequestData brickRequestData = new BrickRequestData(null, pathVariables, null, null, null);
		
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		pathVariables.put("categoryId", "123456" );
		brickRequestData = new BrickRequestData(null, pathVariables, null, null, null);
		assertFalse( parameter.validateParameter(brickRequestData) );
	}
	
	@Test
	public void validateParameter_header_required() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_header_success_required.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		Map<String,String> header = new HashMap<String,String>();
		
		//Validation Succesful
		header.put("categoryId", "123" );
		BrickRequestData brickRequestData = new BrickRequestData(null, null, header, null, null);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		header.put("categoryId", "123456" );
		brickRequestData = new BrickRequestData(null, null, header, null, null);
		assertFalse( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		header.clear();
		brickRequestData = new BrickRequestData(null, null, header, null, null);
		assertFalse( parameter.validateParameter(brickRequestData) );
	}
	
	@Test
	public void validateParameter_header_notRequired() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_header_success_notRequired.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		Map<String,String> header = new HashMap<String,String>();
		
		//Validation Succesful
		header.put("categoryId", "123" );
		BrickRequestData brickRequestData = new BrickRequestData(null, null, header, null, null);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Succesful
		header.clear();
		brickRequestData = new BrickRequestData(null, null, header, null, null);
		assertTrue( parameter.validateParameter(brickRequestData) );
	}
	
	@Test
	public void validateParameter_query_notRequired() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_query_notRequired.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();

		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		Map<String,String[]> query = new HashMap<String,String[]>();
		
		//Validation Succesful
		query.put("categoryId", new String[]{"123"} );		
		BrickRequestData brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Succesful
		query.clear();		
		brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		query.put("categoryId", new String[]{"123456"} );
		brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertFalse( parameter.validateParameter(brickRequestData) );
	}
	
	@Test
	public void validateParameter_query_single() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_query_success_single.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();

		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		Map<String,String[]> query = new HashMap<String,String[]>();
		
		//Validation Succesful
		query.put("categoryId", new String[]{"123"} );		
		BrickRequestData brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		query.put("categoryId", new String[]{"123456"} );
		brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertFalse( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		query = new HashMap<String, String[]>();
		brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertFalse( parameter.validateParameter(brickRequestData) );
	}
	
	@Test
	public void validateParameter_query_array() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_query_success_array.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();

		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		Map<String,String[]> query = new HashMap<String,String[]>();
		
		//Validation Succesful
		query.put("categoryId", new String[]{"123","456"} );		
		BrickRequestData brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		query.put("categoryId", new String[]{"123","4567867"} );
		brickRequestData = new BrickRequestData(null, null, null, null, query);
		assertFalse( parameter.validateParameter(brickRequestData) );
	}
	
	@Test
	public void validateParameter_cookieListNull() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_cookie_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		
		//Validation Failed
		List<Cookie> cookiesList = null;
		BrickRequestData brickRequestData = new BrickRequestData(null, null, null, cookiesList, null);
		assertFalse( parameter.validateParameter(brickRequestData) );
		
	}
	
	@Test
	public void validateParameter_cookie() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_cookie_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		
		//Validation Succesful
		List<Cookie> cookiesList = new ArrayList<Cookie>();
		Cookie cookie = new Cookie("categoryId", "123");
		cookiesList.add(cookie);
		BrickRequestData brickRequestData = new BrickRequestData(null, null, null, cookiesList, null);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		cookiesList = new ArrayList<Cookie>();
		cookie = new Cookie("categoryId", "123456");
		cookiesList.add(cookie);
		brickRequestData = new BrickRequestData(null, null, null, cookiesList, null);
		assertFalse( parameter.validateParameter(brickRequestData) );
		
		cookiesList = new ArrayList<Cookie>();
		brickRequestData = new BrickRequestData(null, null, null, cookiesList, null);
		assertFalse( parameter.validateParameter(brickRequestData) );
	}
	
	@Test
	public void validateParameter_cookieNotRequired() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/parameter_cookie_notRequired.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter parameter = Parameter.getParameter(parameterMap, null);
		
		
		//Validation Succesful
		List<Cookie> cookiesList = new ArrayList<Cookie>();
		Cookie cookie = new Cookie("categoryId", "123");
		cookiesList.add(cookie);
		BrickRequestData brickRequestData = new BrickRequestData(null, null, null, cookiesList, null);
		assertTrue( parameter.validateParameter(brickRequestData) );
		
		//Validation Failed
		cookiesList = new ArrayList<Cookie>();
		cookie = new Cookie("categoryId", "123456");
		cookiesList.add(cookie);
		brickRequestData = new BrickRequestData(null, null, null, cookiesList, null);
		assertFalse( parameter.validateParameter(brickRequestData) );
	}
	
}
