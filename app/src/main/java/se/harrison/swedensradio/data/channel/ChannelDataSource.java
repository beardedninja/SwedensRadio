package se.harrison.swedensradio.data.channel;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Alex on 22/10/2017.
 */

public interface ChannelDataSource {

    enum ChannelFilter {
        all,
        national,
        local,
        extra,
        none
    }

    interface LoadChannelsCallback {
        void onChannelsLoaded(List<Channel> channels);
        void onDataNotAvailable();
    }

    interface GetChannelCallback {
        void onChannelLoaded(Channel channel);
        void onDataNotAvailable();
    }

    void getChannels(@NonNull ChannelFilter channelFilter, @NonNull LoadChannelsCallback callback);

    void getChannel(String channelId, @NonNull GetChannelCallback callback);
}
