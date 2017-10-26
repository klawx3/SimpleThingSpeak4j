package org.klawx3.iot.thingspeak;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.klawx3.iot.thingspeak.model.ChannelFeed;
import org.klawx3.iot.thingspeak.model.FeedModel;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ThingSpeakRest {

    public static final int ERROR = -1;
    public static final int NO_CHANGE = 0;

    public static String PROXY_USER_NAME;
    public static String PROXY_PASSWORD;

    private static String API_URL = "https://api.thingspeak.com";

    private String channel;
    private String readApiKey;

    private String channelFeedString;
    private String channelFieldString;
    private String updateChannelFeed;

    private ObjectMapper mapper;
    private boolean updateAvailable;

    public ThingSpeakRest(String channelId,String readApiKey,String updateApiKey){
        this.channel = channelId;
        this.channel = this.readApiKey;
        updateAvailable = false;

        StringBuilder channelFeed = new StringBuilder();
        StringBuilder channelField = new StringBuilder();
        StringBuilder updateChannelFeed = new StringBuilder();
        channelFeed.append(API_URL).append("/channels/").append(channelId)
                .append("/feeds.json?");
        channelField.append(API_URL).append("/channels/").append(channelId).append("/fields/")
                .append("%d.json?");
        if(updateApiKey != null){
            updateChannelFeed.append(API_URL).append("/update?api_key=").append(updateApiKey)
                    .append("&");
            this.updateChannelFeed = updateChannelFeed.toString();
            updateAvailable = true;
        }

        if(readApiKey != null){
            channelFeed.append("api_key=").append(readApiKey).append("&");
            channelField.append("api_key=").append(readApiKey).append("&");
        }
        channelFeed.append("results=%d");
        channelField.append("results=%d");

        channelFeedString = channelFeed.toString();
        channelFieldString = channelField.toString();

        mapper = new ObjectMapper();

    }

    public ChannelFeed getChannelFeed(int results){
        String jsonString = getGetRequest(String.format(channelFeedString,results),null,null);
        try {
            ChannelFeed channelFeed = mapper.readValue(jsonString, ChannelFeed.class);
            return channelFeed;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChannelFeed getChannelFeed(int field,int results){
        String jsonString = getGetRequest(String.format(channelFieldString,field,results),null,null);
        try {
            ChannelFeed channelFeed = mapper.readValue(jsonString, ChannelFeed.class);
            return channelFeed;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int setChannelFeed(final FeedModel feed){
        if(updateAvailable){
            String url = getUpdateFeedUrl(feed);
            System.out.println(url);
            String lastInsertedId = getGetRequest(url, null, null);
            if(lastInsertedId != null){
                return Integer.parseInt(lastInsertedId);
            }
            return ERROR;
        }
        System.err.println("Update not available. API key not setted");
        return ERROR;
    }

    private String getUpdateFeedUrl(final FeedModel feed){
        StringBuilder url = new StringBuilder();
        url.append(updateChannelFeed);
        boolean firstFieldFounded = false;
        for(int i = 1 ; i <= FeedModel.FIELD_LENGHT ; i++){
            if(feed.getField(i) != null){
                if(firstFieldFounded){
                    url.append("&");
                }else{
                    firstFieldFounded = true;
                }
                url.append("field").append(i)
                        .append("=").append(feed.getField(i));
            }
        }
        return url.toString();
    }

    private String getGetRequest(final String httpsUrl, final String proxyName, final Integer proxyPort){
        try {
            HttpsURLConnection con;
            URL url = new URL(httpsUrl);
            if (proxyName == null) {
                con = (HttpsURLConnection) url.openConnection();
            } else {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyName, proxyPort));
                con = (HttpsURLConnection) url.openConnection(proxy);
                Authenticator authenticator = new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return (new PasswordAuthentication(PROXY_USER_NAME, PROXY_PASSWORD.toCharArray()));
                    }
                };
                Authenticator.setDefault(authenticator);
            }
            return getContent(con);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getContent(HttpsURLConnection con) {
        try {
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = (encoding == null) ? "UTF-8" : encoding;
            return IOUtils.toString(in, encoding);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }


    @Override
    public String toString() {
        return "ThingSpeakRest{" +
                "channel='" + channel + '\'' +
                ", readApiKey='" + readApiKey + '\'' +
                ", channelFeedString='" + channelFeedString + '\'' +
                ", channelFieldString='" + channelFieldString + '\'' +
                '}';
    }
}
