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

public class HeaderParameterTest {
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
}
