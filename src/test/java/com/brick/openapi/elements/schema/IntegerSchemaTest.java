package com.brick.openapi.elements.schema;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;

import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.node.IntNode;
import tools.jackson.databind.node.JsonNodeFactory;

public class IntegerSchemaTest {
	@Test
	public void success_minMax() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/elements/schema/integerSchema_minMax.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap integerMap = fileReader.getMap();
		
		IntegerSchema schema = new IntegerSchema(integerMap);
		
		assertFalse( schema.validateData(null));
		assertFalse( schema.validateData(JsonNodeFactory.instance.nullNode()));
		assertFalse( schema.validateData(JsonNodeFactory.instance.objectNode()));
		
		IntNode contentNode = new IntNode(7);
		assertTrue( schema.validateData(contentNode) );
		
		contentNode = new IntNode(4);
		assertFalse( schema.validateData(contentNode));
		
		contentNode = new IntNode(11);
		assertFalse( schema.validateData(contentNode));
		
	}
	
	@Test
	public void success_enum() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/elements/schema/integerSchema_enum.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap integerMap = fileReader.getMap();
		
		IntegerSchema schema = new IntegerSchema(integerMap);
		
		assertTrue( schema.validateData(null) );
		assertTrue( schema.validateData(JsonNodeFactory.instance.nullNode()));
		
		IntNode contentNode = new IntNode(7);
		assertFalse( schema.validateData(contentNode) );
		
		contentNode = new IntNode(15);
		assertTrue( schema.validateData(contentNode));
		
	}
}
