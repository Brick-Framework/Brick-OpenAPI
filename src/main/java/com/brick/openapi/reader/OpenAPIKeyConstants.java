package com.brick.openapi.reader;

public class OpenAPIKeyConstants {
	
	private OpenAPIKeyConstants() {
		super();
	}

    //Common ELEMENTS
    public static final String DESCRIPTION = "description";
    public static final String SUMMARY = "summary";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String ENUM = "enum";
    public static final String REQUIRED = "required";
    public static final String CONTENT = "content";
    public static final String NULLABLE = "nullable";

    //ROOT ELEMENTS
    public static final String OPEN_API_VERSION = "openapi";
    public static final String INFO = "info";
    public static final String PATHS = "paths";
    public static final String SERVERS = "servers";
    public static final String COMPONENTS = "components";
    public static final String REFERENCE = "$ref";
    public static final String REFERENCE_SCHEMA = "#/components/schemas/";
    public static final String REFERENCE_PARAMETER = "#/components/parameters/";
    public static final String REFERENCE_RESPONSE = "#/components/responses/";
    public static final String SECURITY = "security";

    //INFO ELEMENTS
    public static final String INFO_TITLE = "title";
    public static final String INFO_API_VERSION = "version";
    public static final String INFO_TERMS_OF_SERVICE = "termsOfService";
    public static final String INFO_CONTACT = "contact";
    public static final String INFO_LICENSE = "license";

    //CONTACT ELEMENTS
    public static final String CONTACT_EMAIL = "email";

    //SERVER ELEMENTS
    public static final String SERVER_VARIABLES = "variables";

    //VARIABLE ELEMENTS
    public static final String VARIABLE_DEFAULT = "default";

    //PATH ELEMENTS
    public static final String PARAMETERS = "parameters";
    public static final String OPERATION_ID = "operationId";
    public static final String DEPRECATED = "deprecated";

    //PARAMETER ELEMENTS
    public static final String PARAMETER_TYPE = "in";

    //RESPONSES ELEMENTS
    public static final String RESPONSES = "responses";

    //SCHEMA ELEMENTS
    public static final String SCHEMA = "schema";
    public static final String SCHEMA_TYPE = "type";
    public static final String ARRAY_SCHEMA = "items";
    public static final String MINIMUM = "minimum";
    public static final String MAXIMUM = "maximum";
    public static final String FORMAT = "format";
    public static final String MINIMUM_LENGTH = "minLength";
    public static final String MAXIMUM_LENGTH = "maxLength";
    public static final String PATTERN = "pattern";
    public static final String PROPERTIES = "properties";
    public static final String MINIMUM_ITEMS = "minItems";
    public static final String MAXIMUM_ITEMS = "maxItems";
    public static final String UNIQUE_ITEMS = "uniqueItems";
    public static final String ALL_OF = "allOf";
    public static final String ANY_OF = "anyOf";
    public static final String ONE_OF = "oneOf";

    //REQUEST BODY ELEMENTS
    public static final String REQUEST_BODY = "requestBody";

    //COMPONENT ELEMENTS
    public static final String COMPONENT_SCHEMAS = "schemas";
    public static final String COMPONENT_PARAMETERS = "parameters";
    public static final String COMPONENT_RESPONSES = "responses";
    public static final String COMPONENT_SECURITY_SCHEMES = "securitySchemes";

    //SCHEME ELEMENTS
    public static final String SCHEME = "scheme";
    public static final String SCHEME_TYPE = "type";
    public static final String BEARER_FORMAT = "bearerFormat";
}
