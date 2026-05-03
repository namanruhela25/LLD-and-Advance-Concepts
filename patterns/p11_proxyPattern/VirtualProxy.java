package patterns.p11_proxyPattern;

import patterns.p11_proxyPattern.interfaces.IDisplay;
import patterns.p11_proxyPattern.proxy.ImageProxy;

public class VirtualProxy {
    public static void main(String[] args) {
        String fileName = "test_image.png";
        IDisplay image = new ImageProxy(fileName);
        image.display();
    }
}