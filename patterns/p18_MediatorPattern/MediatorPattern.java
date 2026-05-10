package patterns.p18_MediatorPattern;

import java.util.ArrayList;
import java.util.List;

interface IMediator {
    void send(String from,String msg);
    void sendPrivate(String from,String to,String msg);
    void registerColleague(Colleague c);
}

abstract class Colleague {
    public IMediator mediator;

    public Colleague(IMediator m) {
        mediator = m;
        mediator.registerColleague(this);
    }

    abstract String getName();
    abstract void send(String msg);
    abstract void sendPrivate(String to,String msg);
    abstract void receive(String from, String msg);   
}


class ChatMediator implements IMediator {
    private List<Colleague> colleagues;
    
    public ChatMediator() {
        colleagues = new ArrayList<>();
    }

    @Override
    public void registerColleague(Colleague c) {
        colleagues.add(c);
    }

    @Override
    public void send(String from, String msg) {
        System.out.println("[" + from + " broadcasts]: " + msg);
        for (Colleague colleague : colleagues) {
            if(colleague.getName().equals(from)) continue;
            colleague.receive(from, msg);
        }
    }

    @Override
    public void sendPrivate(String from, String to, String msg) {
        System.out.println("[" + from + " Send private msg to " + to + "]: " + msg);
        
        for (Colleague colleague : colleagues) {
            if(colleague.getName().equals(to)) {
                colleague.receive(from, msg);
            } 
        }
    }
}

class User extends Colleague {
    private String name;

    public User(String name, IMediator m) {
        super(m);
        this.name = name;
    }

    @Override
    String getName() {
        return name;
    }

    @Override
    void send(String msg) {
        mediator.send(name,msg);
    }

    @Override
    void sendPrivate(String to, String msg) {
        mediator.sendPrivate(name,to, msg);
    }

    @Override
    void receive(String from, String msg) {
        System.out.println("    " + name + " got from " + from + ": " + msg);
    }
    
}
 
public class MediatorPattern {
    public static void main(String[] args) {
        IMediator chatRoom = new ChatMediator();

        User u1 = new User("Naman", chatRoom);
        User u2 = new User("Mishti", chatRoom);
        User u3 = new User("Shivam", chatRoom);

        u1.send("Good morning all...");

        u2.sendPrivate("Naman", "Hello , Naman");

        u3.send("How are you guys ?");
    
    }
}
