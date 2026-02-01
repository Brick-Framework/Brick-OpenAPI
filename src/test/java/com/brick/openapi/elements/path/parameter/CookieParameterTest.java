package com.brick.openapi.elements.path.parameter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.BrickRequestData;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

import jakarta.servlet.http.Cookie;

public class CookieParameterTest {
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
