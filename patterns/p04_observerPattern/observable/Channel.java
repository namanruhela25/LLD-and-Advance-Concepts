package LLD.patterns.p4_observerPattern.observable;

import java.util.ArrayList;
import java.util.List;

import LLD.patterns.p4_observerPattern.interfaces.IChannel;
import LLD.patterns.p4_observerPattern.interfaces.ISubscriber;

public class Channel implements IChannel {

    String name, description;
    String latestVideo;

    public List<ISubscriber> subscribers = new ArrayList<>();

    @Override
    public void subscribe(ISubscriber subscriber) {
        subscribers.add(subscriber);
    }
    

    @Override
    public void unsubscribe(ISubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        for (ISubscriber subscriber : subscribers) {
            subscriber.update();
        }
    }

    public String getNewVideo() {
        return latestVideo;
    }

    public void uploadVideo(String title) {
        this.latestVideo = title;
        notifySubscribers();
    }
    
}
