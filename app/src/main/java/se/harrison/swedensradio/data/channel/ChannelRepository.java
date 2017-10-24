package se.harrison.swedensradio.data.channel;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 22/10/2017.
 */

public class ChannelRepository implements ChannelDataSource {
    private static ChannelRepository ourInstance = null;

    public static ChannelRepository getInstance(ChannelDataSource channelRemoteDataSource) {
        if (ourInstance == null) {
            ourInstance = new ChannelRepository(channelRemoteDataSource);
        }
        return ourInstance;
    }

    private final ChannelDataSource channelRemoteDataSource;

    private Map<String, Channel> cachedChannels;
    private ChannelFilter currentFilter = ChannelFilter.none;

    private ChannelRepository(@NonNull ChannelDataSource channelRemoteDataSource) {
        this.channelRemoteDataSource = channelRemoteDataSource;
    }

    @Override
    public void getChannels(@NonNull ChannelFilter channelFilter, @NonNull final LoadChannelsCallback callback) {
        if (channelFilter != currentFilter && cachedChannels != null) {
            cachedChannels.clear();
        }
        if (cachedChannels == null || cachedChannels.isEmpty()) {
            channelRemoteDataSource.getChannels(channelFilter, new LoadChannelsCallback() {
                @Override
                public void onChannelsLoaded(List<Channel> channels) {
                    refreshCache(channels);
                    callback.onChannelsLoaded(channels);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        } else {
            callback.onChannelsLoaded(new ArrayList<>(cachedChannels.values()));
        }
    }

    // TODO: Attempt fetch from cache first
    @Override
    public void getChannel(String channelId, @NonNull GetChannelCallback callback) {
        channelRemoteDataSource.getChannel(channelId, callback);
    }

    private void refreshCache(List<Channel> channels) {
        if (cachedChannels == null) {
            cachedChannels = new LinkedHashMap<>();
        }

        for (Channel channel : channels) {
            cachedChannels.put(channel.getId(), channel);
        }
    }
}
