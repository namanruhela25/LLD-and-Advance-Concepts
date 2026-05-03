package patterns.p7_adaptorPattern;

import patterns.p7_adaptorPattern.adaptee.XmlProvider;
import patterns.p7_adaptorPattern.adaptor.XmlDataProviderAdaptor;
import patterns.p7_adaptorPattern.interfaces.IReport;

public class AdaptorPattern {
    public static void main(String[] args) {
        XmlProvider xmlProvider = new XmlProvider();

        // pass reference of parent 
        IReport report = new XmlDataProviderAdaptor(xmlProvider);

        String rawData = "Naman:25";

        Client client = new Client();

        // pass created adaptor to client
        client.getReport(report, rawData);

    }
}
