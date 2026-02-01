package com.brick.openapi.elements.path.parameter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.brick.openapi.elements.schema.StringSchema;
import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

public class PathParameterTest {
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
}
