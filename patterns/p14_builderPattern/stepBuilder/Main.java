package patterns.p14_builderPattern.stepBuilder;

public class Main {
    public static void main(String[] args) {
        
        HttpRequest req = new HttpRequest.HttpRequestBuilder()
        .setUrl("https://diginode.in")
        .setMethod("GET")
        .addHeader("Content-type", "Application/json")
        .build();

        /*
            This methods throws an error because after method set Object is expecting a HeaderStep type interface 
            
            HttpRequest req = new HttpRequest.HttpRequestBuilder()
            .setUrl("https://diginode.in")
            .setMethod("GET").build(); 
            
            // Error : The method build() is undefined for the type HttpRequest.HeaderStepJava(67108964)
        */
        
        req.execute();
    }   
}
