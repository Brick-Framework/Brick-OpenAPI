package com.brick.openapi.elements.path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.brick.openapi.elements.schema.Schema;
import com.brick.openapi.exception.InvalidValue;
import com.brick.utilities.BrickMap;
import com.brick.utilities.exception.InvalidData;
import com.brick.utilities.exception.KeyNotFound;
import com.brick.utilities.file.FileReader;
import com.brick.utilities.file.YamlFileReader;

import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

public class ContentTest {
	
	@Test
	public void content_success() throws FileNotFoundException, InvalidData, KeyNotFound, InvalidValue {
		String filePath = "/dummy_yaml/path/content_success.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap contentMap = fileReader.getMap();
		
		Content content = new Content(contentMap, null);
		
		Map<String,Schema> schemaMap = content.getContent();
		assertTrue( schemaMap.containsKey("application/json") );
		
		ObjectNode contentNode = JsonNodeFactory.instance.objectNode();
		contentNode.put("name", "name");
		contentNode.put("age",15);
		assertTrue( content.validateData(contentNode) );
		
		ObjectNode contentNodeInvalid = JsonNodeFactory.instance.objectNode();
		contentNodeInvalid.put("height", "height");
		contentNodeInvalid.put("weight","weight");
		assertFalse( content.validateData(contentNodeInvalid) );
	}
	
	@Test
	public void content_invalidContentType() throws FileNotFoundException, InvalidData {
		String filePath = "/dummy_yaml/path/content_invalidContentType.yaml";
		FileReader fileReader = new YamlFileReader(filePath);
		BrickMap contentMap = fileReader.getMap();
		
		assertThrows(InvalidValue.class, ()->{
			new Content(contentMap, null);
		});
	}

}
