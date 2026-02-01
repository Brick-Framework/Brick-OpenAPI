package com.brick.openapi.elements.path.parameter;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

public class ParameterFactoryTest {
	@Test
	public void cookie_test() throws FileNotFoundException, InvalidData, InvalidValue, KeyNotFound {
		String filePath = "/dummy_yaml/path/parameter_cookie_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter p = ParameterFactory.getParameter(parameterMap, null);
		assertInstanceOf(CookieParameter.class, p);
	}
	
	@Test
	public void header_test() throws FileNotFoundException, InvalidData, InvalidValue, KeyNotFound {
		String filePath = "/dummy_yaml/path/parameter_header_success_required.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter p = ParameterFactory.getParameter(parameterMap, null);
		assertInstanceOf(HeaderParameter.class, p);
	}
	
	@Test
	public void path_test() throws FileNotFoundException, InvalidData, InvalidValue, KeyNotFound {
		String filePath = "/dummy_yaml/path/parameter_path_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter p = ParameterFactory.getParameter(parameterMap, null);
		assertInstanceOf(PathParameter.class, p);
	}
	
	@Test
	public void query_test() throws FileNotFoundException, InvalidData, InvalidValue, KeyNotFound {
		String filePath = "/dummy_yaml/path/parameter_query_notRequired.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap parameterMap = fileReader.getMap();
		
		Parameter p = ParameterFactory.getParameter(parameterMap, null);
		assertInstanceOf(QueryParameter.class, p);
	}
}
