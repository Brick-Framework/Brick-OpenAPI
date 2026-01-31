package com.brick.openapi.elements;

import com.brick.utilities.exception.KeyNotFound;
import com.brick.logger.Logger;
import com.brick.openapi.elements.path.Response;
import com.brick.openapi.elements.path.parameter.Parameter;
import com.brick.openapi.elements.schema.Schema;
import com.brick.openapi.elements.schema.SchemaFactory;
import com.brick.openapi.elements.security.scheme.SecurityScheme;
import com.brick.openapi.elements.security.scheme.SecuritySchemeFactory;
import com.brick.openapi.exception.CyclicReferenceFound;
import com.brick.openapi.exception.InvalidValue;
import com.brick.openapi.reader.OpenAPIKeyConstants;
import com.brick.utilities.BrickMap;

import java.util.*;

public class Components {
    private final Map<String, Parameter> parameters;
    private final Map<String, Schema> schemas;
    private final Map<String, Response> responses;
    private final Map<String, SecurityScheme> securityScheme;

    public Components(BrickMap brickMap) throws KeyNotFound, InvalidValue, CyclicReferenceFound {
        
        this.schemas = new HashMap<>();
        populateSchemas(brickMap);

        this.parameters = new HashMap<>();
        populateParameters(brickMap);

        this.responses = new HashMap<>();
        populateResponses(brickMap);

        this.securityScheme = new HashMap<>();
        populateSecurityScheme(brickMap);
        
    }


    /*
        Description: Return Schema From Map of Schemas
     */
    public Schema getSchema(String schemaName) throws KeyNotFound {
        if( this.schemas.containsKey(schemaName) ){
            return this.schemas.get(schemaName);
        }

        KeyNotFound keyNotFound = new KeyNotFound(schemaName);
        Logger.logException(keyNotFound);
        throw keyNotFound;
    }

    /*
        Description : Return Parameter from Map of Parameters
     */
    public Parameter getParameter(String parameterName) throws KeyNotFound {
        if( this.parameters.containsKey(parameterName) ){
            return this.parameters.get(parameterName);
        }

        KeyNotFound keyNotFound = new KeyNotFound(parameterName);
        Logger.logException(keyNotFound);
        throw keyNotFound;
    }

    /*
        Description: Return Response From Map of Responses
     */
    public Response getResponse(String responseName) throws KeyNotFound {
        if( this.responses.containsKey(responseName) ){
            return this.responses.get(responseName);
        }

        KeyNotFound keyNotFound = new KeyNotFound(responseName);
        Logger.logException(keyNotFound);
        throw keyNotFound;
    }

    /*
        Description: Return SecurityScheme From Map of Responses
     */
    public SecurityScheme getSecurityScheme(String securitySchemeName) throws KeyNotFound {
        if( this.securityScheme.containsKey(securitySchemeName) ){
            return this.securityScheme.get(securitySchemeName);
        }

        KeyNotFound keyNotFound = new KeyNotFound(securitySchemeName);
        Logger.logException(keyNotFound);
        throw keyNotFound;
    }

    /*
        Description : Populating Parameter of Components
     */
    private void populateParameters(BrickMap brickMap) throws KeyNotFound, InvalidValue {
        
        if( brickMap.contains(OpenAPIKeyConstants.COMPONENT_PARAMETERS) ) {
            BrickMap parameterMap = brickMap.getBrickMap(OpenAPIKeyConstants.COMPONENT_PARAMETERS);
            for (Map.Entry<String, Object> entry : parameterMap) {
                Parameter parameter = Parameter.getParameter(parameterMap.getBrickMap(entry.getKey()), this);
                this.parameters.put(entry.getKey(), parameter);
            }
        }
        
    }

    /*
        Description : Populating Response of Components
     */
    private void populateResponses(BrickMap brickMap) throws KeyNotFound, InvalidValue {
        
        if( brickMap.contains(OpenAPIKeyConstants.COMPONENT_RESPONSES) ) {
            BrickMap responsesMap = brickMap.getBrickMap(OpenAPIKeyConstants.COMPONENT_RESPONSES);
            for (Map.Entry<String, Object> entry : responsesMap) {
                Response response = Response.getResponse(responsesMap.getBrickMap(entry.getKey()), this);
                this.responses.put(entry.getKey(), response);
            }
        }
        
    }

    /*
        Description: Populating Security Scheme of Components
     */
    private void populateSecurityScheme(BrickMap brickMap) throws KeyNotFound, InvalidValue {
        
        if( brickMap.contains(OpenAPIKeyConstants.COMPONENT_SECURITY_SCHEMES) ){
            BrickMap securitySchemeMap = brickMap.getBrickMap(OpenAPIKeyConstants.COMPONENT_SECURITY_SCHEMES);
            for( Map.Entry<String,Object> entry: securitySchemeMap ){
                SecurityScheme securityScheme = SecuritySchemeFactory.getSecurityScheme(securitySchemeMap.getBrickMap(entry.getKey()));
                this.securityScheme.put(entry.getKey(), securityScheme);
            }
        }
        
    }
    
    private Map<String,List<String>> generateGraph(BrickMap schemaMap) throws KeyNotFound, InvalidValue{
    	Map<String, List<String>> schemaReferenceGraph = new HashMap<>();
        for (Map.Entry<String, Object> entry : schemaMap) {
            List<String> reference = Schema.getReferences(schemaMap.getBrickMap(entry.getKey()));  
            schemaReferenceGraph.put(entry.getKey(), reference);
        }
        
        return schemaReferenceGraph;
    }
    
    /*
     * Description: Returns a Queue with all nodes that have indegree zero
     */
    private Queue<String> getNodesWithZeroIndegree(Map<String,List<String>> schemaReferenceGraph) {
    	Queue<String> queue = new ArrayDeque<>();
        for (Map.Entry<String, List<String>> entry : schemaReferenceGraph.entrySet()) {
            if (entry.getValue().isEmpty()) {
                queue.add(entry.getKey());
            }
        }
        
        return queue;
    }

    /*
        Description: Populating Schemas of Components
     */
    private void populateSchemas(BrickMap brickMap) throws KeyNotFound, InvalidValue, CyclicReferenceFound {
        
        if( brickMap.contains(OpenAPIKeyConstants.COMPONENT_SCHEMAS) ) {
            BrickMap schemaMap = brickMap.getBrickMap(OpenAPIKeyConstants.COMPONENT_SCHEMAS);
            // Creating Graph of Where Schema is a Node and Reference is a Vertex
            Map<String, List<String>> schemaReferenceGraph = generateGraph(schemaMap);

            cyclicSchemaReferenceCheck(schemaReferenceGraph); // Performing Cyclic check in creating graph to ensure there is no cyclic dependency

            // Creating Schema Based on OutDegree to ensure schema with references to other schema are created at last
            Queue<String> queue = getNodesWithZeroIndegree(schemaReferenceGraph);

            Set<String> completed = new HashSet<>();
            while (!queue.isEmpty()) {
                String current = queue.poll();

                if (completed.contains(current)) {
                    continue;
                }

                Schema newSchema = SchemaFactory.getSchema(schemaMap.getBrickMap(current), this);

                this.schemas.put(current, newSchema);

                for (Map.Entry<String, List<String>> entry : schemaReferenceGraph.entrySet()) {

                    entry.getValue().remove(current);

                    if (entry.getValue().isEmpty()) {
                        queue.add(entry.getKey());
                    }

                }

                completed.add(current);
            }
        }
        
    }

    /*
        Description: Checks Whether the schemas mentioned have a cyclic reference
     */
    private void cyclicSchemaReferenceCheck(Map<String, List<String>> schemaReferenceGraph) throws CyclicReferenceFound {
        //DFS for Cyclic Check
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for( String node : schemaReferenceGraph.keySet() ){
            if( dfs(node,schemaReferenceGraph,visited,recursionStack) ){
                CyclicReferenceFound cyclicReferenceFound = new CyclicReferenceFound("Cyclic Reference Found in Components");
                Logger.logException(cyclicReferenceFound);
                throw cyclicReferenceFound;
            }
        }

    }

    /*
        Description: Uses DFS Algorithm to detect cycle in graph
     */
    private boolean dfs(String node, Map<String,List<String>> graph,Set<String> visited,Set<String> recursionStack){
        if( recursionStack.contains(node) ){
            return true;
        }

        if( visited.contains(node) ){
            return false;
        }

        visited.add(node);
        recursionStack.add(node);

        for( String neighbor : graph.get(node) ){
            if( dfs(neighbor,graph,visited,recursionStack) ){
                return true;
            }
        }

        recursionStack.remove(node);
        return false;
    }
}
