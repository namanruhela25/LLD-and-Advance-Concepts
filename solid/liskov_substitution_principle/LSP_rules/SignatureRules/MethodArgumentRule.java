package solid.liskov_substitution_principle.LSP_rules.SignatureRules;

class Parent {
    public void print() {
        System.out.println("Parent class : print");
    }
}

class Child extends Parent {
    public void print() {
        System.out.println("Child class : print");
    }
}

class Client {
    Parent p;
    
    Client(Parent p) {
        this.p = p;
    }

    public void printName() {
        p.print();
    }
}

public class MethodArgumentRule {
    public static void main(String[] args) {
        Parent parent = new Parent();
        Child child = new Child();

        Client client1 = new Client(parent);

        Client client2 = new Client(child);

        client1.printName();
        client2.printName();
    }    
}
