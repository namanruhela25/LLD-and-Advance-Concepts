package patterns.p14_builderPattern.simpleBuilder;

public class SimpleBuilder {
    public static void main(String[] args) {
        HttpRequest req = new HttpRequest.HttpRequestBuilder()
        .setUrl("localhost:127:0:0:1")
        .setMethod("POST")
        .addHeader("Token", "32r23k")
        .addQueryParam("SQL", "234")
        .setBody("{name:Naman}")
        .setTimeout(1)
        .build();

        req.execute();
    }
}
