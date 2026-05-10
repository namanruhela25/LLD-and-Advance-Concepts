package patterns.p14_builderPattern.simpleBuilder;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private String url;
    private String method;
    private Map<String, String> headers;
    private Map<String,String> queryParams;
    private String body;
    private int timeout; // in seconds

    // Private constructor - can only be accessed by the Builder
    HttpRequest() {
        headers = new HashMap<>();
        queryParams = new HashMap<>();
        body = "";
    }

    // Method to execute the HTTP request
    public void execute() {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParams.isEmpty()) {
            System.out.println("Query Parameters:");
            for (Map.Entry<String, String> param : queryParams.entrySet()) {
                System.out.println("  " + param.getKey() + "=" + param.getValue());
            }
        }

        System.out.println("Headers:");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println("  " + header.getKey() + ": " + header.getValue());
        }

        if (body != null && !body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }
    
    public static class HttpRequestBuilder {
        public HttpRequest req;

        public HttpRequestBuilder() {
            req = new HttpRequest();
        }
        public HttpRequestBuilder setUrl(String url) {
            req.url = url;
            return this;
        }

        public HttpRequestBuilder setMethod(String method) {
            req.method = method;
            return this;
        }

        public HttpRequestBuilder addHeader(String key, String value) {
            req.headers.put(key, value);
            return this;
        }

        public HttpRequestBuilder addQueryParam(String key, String value) {
            req.queryParams.put(key, value);
            return this;
        }

        public HttpRequestBuilder setBody(String body) {
            req.body = body;
            return this;
        }

        public HttpRequestBuilder setTimeout(int timeout) {
            req.timeout = timeout;
            return this;
        }

        public HttpRequest build() {
            if (req.url == null || req.method == null) {
                throw new IllegalStateException("URL and Method are required fields.");
            }
            return req;
        }
    }
}
