package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.util.HashMap;
import java.util.Map;

public class ThumbnailHandler implements RequestHandler<Map<String, String>, Map<String, String>> {

    @Override
    public Map<String, String> handleRequest(Map<String, String> input, Context context) {
        // Extract "name" from input JSON
        String name = input.getOrDefault("name", "World");

        // Build response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello " + name + " from AWS Lambda");

        return response;
    }
}

