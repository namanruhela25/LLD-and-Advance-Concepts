package LLD.patterns.p4_observerPattern.interfaces;

public interface IChannel {
    void subscribe(ISubscriber subscriber);
    void unsubscribe(ISubscriber subscriber);
    void notifySubscribers();   
}
