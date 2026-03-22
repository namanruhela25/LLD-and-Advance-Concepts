package solid.dependency_inversion_principle;

// Abstraction (Interface)
interface Database {
    void save(String data);
}

// MySQL implementation (Low-level module)
class MySQLDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println(
            "Executing SQL Query: INSERT INTO users VALUES('" 
            + data + "');"
        );
    }
}

// MongoDB implementation (Low-level module)
class MongoDBDatabase implements Database {
    @Override
    public void save(String data) {
        System.out.println(
            "Executing MongoDB Function: db.users.insert({name: '" 
            + data + "'})"
        );
    }
}

class UserService{
    private final Database database;  // High-level module depends on abstraction

    public UserService(Database db) {
        this.database = db;
    }

    public void storeUser(String name) {
        database.save(name);
    }
}

public class DIP_Followed {
    public static void main(String[] args) {
        Database mongo = new MongoDBDatabase();
        Database sql = new MySQLDatabase();

        UserService us1 = new UserService(mongo);

        UserService us2 = new UserService(sql);

        us1.storeUser("Naman");

        us2.storeUser("Mishti");
    }    
}
