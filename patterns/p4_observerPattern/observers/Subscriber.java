package LLD.patterns.p4_observerPattern.observers;
import LLD.patterns.p4_observerPattern.interfaces.ISubscriber;
import LLD.patterns.p4_observerPattern.observable.Channel;

public class Subscriber implements ISubscriber{

    Channel channel;

    public Subscriber(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void update() {
        System.out.println("Subscriber received update");
        System.out.println("Latest video: " + channel.getNewVideo());
    }
    
}
