package com.brick.openapi.elements.path.parameter;

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
}
