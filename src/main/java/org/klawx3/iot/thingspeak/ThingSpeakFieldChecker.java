package org.klawx3.iot.thingspeak;

import org.klawx3.iot.thingspeak.listener.FeedChangedListener;
import org.klawx3.iot.thingspeak.model.ChannelFeed;
import org.klawx3.iot.thingspeak.model.ChannelModel;

import java.util.ArrayList;
import java.util.List;

public class ThingSpeakFieldChecker implements Runnable {

    private List<FeedChangedListener> listener;
    private final ThingSpeakRest ts;
    private final long millisecondsToCheck;
    private boolean running;
    private ChannelFeed lastChannelFeed;
    private int fieldToCheck;
    private Thread thread;

    public ThingSpeakFieldChecker(final ThingSpeakRest ts, long millisecondsToCheck, int fieldToCheck) {
        listener = new ArrayList<>();
        this.fieldToCheck = fieldToCheck;
        this.millisecondsToCheck = millisecondsToCheck;
        this.ts = ts;
        running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(millisecondsToCheck);
                final ChannelFeed feed = ts.getChannelFeed(fieldToCheck);
                if (feed != null) {
                    if (!feed.equals(lastChannelFeed)) {
                        fireEventChanged(feed, lastChannelFeed);
                        lastChannelFeed = feed;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void start(){
        thread = new Thread(this);
        thread.start();
    }

    private void fireEventChanged(final ChannelFeed newFeed, final ChannelFeed oldFeed) {
        for (FeedChangedListener l : listener) {
            l.changeDetected(newFeed, oldFeed);
        }
    }

    public void addListener(final FeedChangedListener l) {
        listener.add(l);
    }

    public void removeListener(final FeedChangedListener l) {
        listener.remove(l);
    }

    public void stop() {
        if (running) {
            running = false;
        }
    }

}
