package patterns.p7_adaptorPattern;

import patterns.p7_adaptorPattern.interfaces.IReport;

public class Client {
    public void getReport(IReport report, String rawData) {
        System.out.println("Processed JSON: "
            + report.getJsonData(rawData));
    }
}
