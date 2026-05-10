package patterns.p14_builderPattern.directorBuilder;

public class DirectorBuilder {
    public static void main(String[] args) {
        HttpRequest getReq = HttpRequestDirector.createHttpGetRequest("https://diginode.in");
        getReq.execute();

        HttpRequest jsonReq = HttpRequestDirector.createHttpJsonRequest("https://diginode.in","{name:Mishti}");
        jsonReq.execute();

    }
}
