package com.brick.openapi.elements.schema;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.StringNode;

public class StringSchemaTest {
	@Test
	public void success_minMax() throws FileNotFoundException, InvalidData, InvalidValue, KeyNotFound {
		String filePath = "/dummy_yaml/elements/schema/string_minMax.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap stringMap = fileReader.getMap();
		
		StringSchema schema = new StringSchema(stringMap);
		assertFalse( schema.validateData(null));
		assertFalse(schema.validateData(JsonNodeFactory.instance.nullNode()));
		assertFalse( schema.validateData(JsonNodeFactory.instance.objectNode()));
		
		StringNode contentNode = new StringNode("abcd");
		assertFalse( schema.validateData(contentNode));
		
		contentNode = new StringNode("abcde");
		assertTrue( schema.validateData(contentNode));
		
		contentNode = new StringNode("abcdefghijkl");
		assertFalse( schema.validateData(contentNode));
	}
	
	@Test
	public void success_enum() throws FileNotFoundException, InvalidData, InvalidValue, KeyNotFound {
		String filePath = "/dummy_yaml/elements/schema/string_enum.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap stringMap = fileReader.getMap();
		
		StringSchema schema = new StringSchema(stringMap);
		assertTrue( schema.validateData(null));
		assertTrue(schema.validateData(JsonNodeFactory.instance.nullNode()));
		
		StringNode contentNode = new StringNode("abcd");
		assertFalse( schema.validateData(contentNode));
		
		contentNode = new StringNode("abcdef");
		assertTrue( schema.validateData(contentNode));
	}
	
	@Test
	public void success_pattern() throws FileNotFoundException, InvalidData, InvalidValue, KeyNotFound {
		String filePath = "/dummy_yaml/elements/schema/string_pattern.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap stringMap = fileReader.getMap();
		
		StringSchema schema = new StringSchema(stringMap);
		
		StringNode contentNode = new StringNode("abcd");
		assertFalse( schema.validateData(contentNode));
		
		contentNode = new StringNode("SN-1234");
		assertTrue( schema.validateData(contentNode));
		
		contentNode = new StringNode("SN-12345");
		assertFalse( schema.validateData(contentNode));
		
		contentNode = new StringNode("SN-123");
		assertFalse( schema.validateData(contentNode));
	}
	
	@Test
	public void stringFormatException() {
		BrickMap brickMap = mock(BrickMap.class);
		
		when( brickMap.getOptionalString(OpenAPIKeyConstants.FORMAT)).thenReturn(Optional.of("date"));
		assertDoesNotThrow(()->{
			new StringSchema(brickMap);
		});
		
		when( brickMap.getOptionalString(OpenAPIKeyConstants.FORMAT)).thenReturn(Optional.of("date-time"));
		assertDoesNotThrow(()->{
			new StringSchema(brickMap);
		});
		
		when( brickMap.getOptionalString(OpenAPIKeyConstants.FORMAT)).thenReturn(Optional.of("password"));
		assertDoesNotThrow(()->{
			new StringSchema(brickMap);
		});
		
		when( brickMap.getOptionalString(OpenAPIKeyConstants.FORMAT)).thenReturn(Optional.of("uuid"));
		assertDoesNotThrow(()->{
			new StringSchema(brickMap);
		});
		
		when( brickMap.getOptionalString(OpenAPIKeyConstants.FORMAT)).thenReturn(Optional.of("email"));
		assertDoesNotThrow(()->{
			new StringSchema(brickMap);
		});
		
		when( brickMap.getOptionalString(OpenAPIKeyConstants.FORMAT)).thenReturn(Optional.of("invalid"));
		assertThrows(InvalidValue.class,()->{
			new StringSchema(brickMap);
		});
	}
}
