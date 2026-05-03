package LLD.patterns.p4_observerPattern;

import LLD.patterns.p4_observerPattern.observable.Channel;
import LLD.patterns.p4_observerPattern.observers.Subscriber;

public class ObserverPattern {
    public static void main(String[] args) {
        Channel channel = new Channel();
        Subscriber subscriber1 = new Subscriber(channel);
        Subscriber subscriber2 = new Subscriber(channel);

        channel.subscribe(subscriber1);
        channel.subscribe(subscriber2);

        channel.uploadVideo("Observer Pattern in Java");

        Subscriber subscriber3 = new Subscriber(channel);
        channel.subscribe(subscriber3);
        
        // All three will receive update
        channel.uploadVideo("Observer Pattern in Python");
    }
}
