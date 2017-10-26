package org.klawx3.iot.thingspeak.listener;

import org.klawx3.iot.thingspeak.model.ChannelFeed;

@FunctionalInterface
public interface FeedChangedListener {
    void changeDetected(ChannelFeed newFeed,ChannelFeed oldFeed);
}
