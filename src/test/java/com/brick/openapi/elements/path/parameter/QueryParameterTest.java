package com.brick.openapi.elements.path.parameter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

public class QueryParameterTest {
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
}
