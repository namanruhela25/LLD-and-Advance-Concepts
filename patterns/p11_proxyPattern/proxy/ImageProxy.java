package patterns.p11_proxyPattern.proxy;

import patterns.p11_proxyPattern.interfaces.IDisplay;
import patterns.p11_proxyPattern.resource.RealImage;

public class ImageProxy implements IDisplay {
    private String fileName;
    private RealImage realImage;

    public ImageProxy(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }   
}
