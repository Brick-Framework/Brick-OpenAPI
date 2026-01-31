package com.brick.openapi.elements.path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.brick.openapi.elements.path.parameter.ParameterType;
import com.brick.openapi.exception.InvalidValue;

public class ParameterTypeTest {
	@Test
	public void testAll() throws InvalidValue {
		assertEquals(ParameterType.QUERY, ParameterType.fromString("query"));
		assertEquals(ParameterType.HEADER, ParameterType.fromString("header"));
		assertEquals(ParameterType.PATH, ParameterType.fromString("path"));
		assertEquals(ParameterType.COOKIE, ParameterType.fromString("cookie"));
		assertThrows(InvalidValue.class, ()->{
			ParameterType.fromString("invalid");
		});
	}

}
