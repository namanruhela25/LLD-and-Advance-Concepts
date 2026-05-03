package patterns.p11_proxyPattern;

interface IDataService {
    String fetchData();
}

class RealDataService implements IDataService {
    public RealDataService() {
        // Imagine this connects to a remote server or loads heavy resources.
        System.out.println("[RealDataService] Initialized (simulating remote setup)");
    }

    @Override
    public String fetchData() {
        return "[RealDataService] Data from server";
    }
}

// Remote proxy
class DataServiceProxy implements IDataService {
    private RealDataService realService;

    public DataServiceProxy() {
        this.realService = null; // Lazy initialization
    }

    @Override
    public String fetchData() {
        System.out.println("[DataServiceProxy] Connecting to remote service...");
        if (realService == null) {
            realService = new RealDataService();
        }
        return realService.fetchData();
    }
}

public class RemoteProxy {
    public static void main(String[] args) {
        IDataService dataService = new DataServiceProxy();
        String result = dataService.fetchData();
        System.out.println(result);
    }
}