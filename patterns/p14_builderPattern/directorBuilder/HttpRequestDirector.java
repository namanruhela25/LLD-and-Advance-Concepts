package patterns.p14_builderPattern.directorBuilder;

public class HttpRequestDirector {
    public static HttpRequest createHttpGetRequest(String url) {
        return new HttpRequest.HttpRequestBuilder()
        .setUrl(url)
        .setMethod("GET")
        .build();
    }

    public static HttpRequest createHttpJsonRequest(String url, String jsonBody) {
        return new HttpRequest.HttpRequestBuilder()
        .setUrl(url)
        .setMethod("POST")
        .addHeader("Token", "32r23k")
        .addHeader("Content-Type", "application/json")
        .addQueryParam("SQL", "234")
        .setBody(jsonBody)
        .setTimeout(1)
        .build();
    }

}
