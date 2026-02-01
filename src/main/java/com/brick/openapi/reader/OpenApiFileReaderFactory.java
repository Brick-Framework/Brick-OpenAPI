package com.brick.openapi.reader;

import java.io.File;

import com.brick.logger.Logger;
import com.brick.utilities.exception.InvalidData;

public class OpenApiFileReaderFactory {
	private static final String YAML = "yaml";
	
	private static final String PATH_PREFIX = "/openapi/";
	
	private OpenApiFileReaderFactory() {
		super();
	}
	
	public static OpenAPIFileReader getReader(File file) throws InvalidData {
		String[] fileSplitParts = file.getName().toLowerCase().split("\\."); // Cannot Use a Single "." for Splitting because of regex
		String fileExtension = fileSplitParts[fileSplitParts.length - 1];
		 
		switch( fileExtension ) {
		case YAML:
			return new OpenAPIFileYamlReader(PATH_PREFIX+file.getName());
			
		default:
			InvalidData exception = new InvalidData("Unrecognizable File Extension : "+file.getName());
			Logger.logException(exception);
			throw exception;
		}
	}

}
