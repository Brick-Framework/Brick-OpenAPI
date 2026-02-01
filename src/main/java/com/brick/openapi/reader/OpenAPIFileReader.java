package com.brick.openapi.reader;

import java.io.FileNotFoundException;

import com.brick.openapi.OpenAPI;
import com.brick.openapi.exception.InvalidOpenAPISpecification;
import com.brick.utilities.exception.InvalidData;

public abstract class OpenAPIFileReader {

    protected String fileName;

    protected OpenAPIFileReader(String fileName) {
        this.fileName = fileName;
    }

    public abstract OpenAPI getOpenAPI() throws FileNotFoundException, InvalidData, InvalidOpenAPISpecification;
}
