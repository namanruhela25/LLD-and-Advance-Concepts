package patterns.p7_adaptorPattern.adaptor;

import patterns.p7_adaptorPattern.adaptee.XmlProvider;
import patterns.p7_adaptorPattern.interfaces.IReport;

public class XmlDataProviderAdaptor implements IReport {
    private XmlProvider xmlProvider;
    
    public XmlDataProviderAdaptor(XmlProvider xmlProvider) {
        this.xmlProvider = xmlProvider;
    }

    public String getJsonData(String data) {
        String xmlData = xmlProvider.getXmlData(data);
        // Convert XML to JSON (simplified for demonstration)
        String name = xmlData.split("<name>")[1].split("</name>")[0];
        String id   = xmlData.split("<id>")[1].split("</id>")[0];
        return "{ \"name\": \"" + name + "\", \"id\": \"" + id + "\" }";
    }
}
