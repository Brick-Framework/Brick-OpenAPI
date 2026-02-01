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

import tools.jackson.databind.node.DoubleNode;
import tools.jackson.databind.node.IntNode;
import tools.jackson.databind.node.JsonNodeFactory;

public class NumberSchemaTest {
	@Test
	public void success_minMax() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/elements/schema/numberSchema_minMax.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap numberMap = fileReader.getMap();
		
		NumberSchema schema = new NumberSchema(numberMap);
		
		assertFalse( schema.validateData(null));
		assertFalse( schema.validateData(JsonNodeFactory.instance.objectNode()));
		
		DoubleNode contentNode = new DoubleNode(5);
		assertFalse( schema.validateData(contentNode) );
		
		contentNode = new DoubleNode(5.75);
		assertTrue( schema.validateData(contentNode));
		
		contentNode = new DoubleNode(11);
		assertFalse( schema.validateData(contentNode));
		
	}
	
	@Test
	public void success_enum() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/elements/schema/numberSchema_enum.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap numberMap = fileReader.getMap();
		
		NumberSchema schema = new NumberSchema(numberMap);
		
		assertTrue( schema.validateData(null) );
		
		DoubleNode contentNode = new DoubleNode(15.15);
		assertTrue( schema.validateData(contentNode) );
		
		contentNode = new DoubleNode(15);
		assertFalse( schema.validateData(contentNode));
		
	}
}
