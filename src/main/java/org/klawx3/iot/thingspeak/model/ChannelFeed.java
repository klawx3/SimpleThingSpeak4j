package org.klawx3.iot.thingspeak.model;

import java.util.List;

public class ChannelFeed {
    private ChannelModel channel;
    private List<FeedModel> feeds;

    public ChannelModel getChannel() {
        return channel;
    }

    public void setChannel(ChannelModel channel) {
        this.channel = channel;
    }

    public List<FeedModel> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedModel> feeds) {
        this.feeds = feeds;
    }

    @Override
    public String toString() {
        return "ChannelFeed{" +
                "channel=" + channel +
                ", feeds=" + feeds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelFeed that = (ChannelFeed) o;
        if (!channel.equals(that.channel)) return false;
        return feeds.equals(that.feeds);
    }

    @Override
    public int hashCode() {
        int result = channel.hashCode();
        result = 31 * result + feeds.hashCode();
        return result;
    }
}
