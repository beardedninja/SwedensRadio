package se.harrison.swedensradio.data.channel;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alex on 22/10/2017.
 */

public class Channel {
    private String id;
    private String name;

    private String imageUrl;
    private String color;
    private String liveAudioUrl;
    private String channelType;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getColor() {
        return color;
    }

    public String getLiveAudioUrl() {
        return liveAudioUrl;
    }

    public String getChannelType() {
        return channelType;
    }

    public boolean isLive() {
        return !TextUtils.isEmpty(liveAudioUrl);
    }

    static Channel fromJSON(JSONObject json) {
        if (json == null) return null;

        Channel channel = new Channel();

        channel.id = json.optString("id");
        channel.name = json.optString("name", channel.name);
        channel.imageUrl = json.optString("image", channel.imageUrl);
        channel.color = json.optString("color", channel.color);
        if (json.has("liveaudio")) {
            try {
                JSONObject liveAudio = json.getJSONObject("liveaudio");
                channel.liveAudioUrl = liveAudio.optString("url", channel.liveAudioUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        channel.channelType = json.optString("channeltype", channel.imageUrl);

        return channel;
    }
}
