package patterns.p11_proxyPattern.resource;
import patterns.p11_proxyPattern.interfaces.IDisplay;

public class RealImage implements IDisplay {
    private String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading " + fileName);
    }

    @Override
    public void display() {
        System.out.println("Displaying " + fileName);
    }

}