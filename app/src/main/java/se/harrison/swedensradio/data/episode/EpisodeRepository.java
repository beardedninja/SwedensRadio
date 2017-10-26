package se.harrison.swedensradio.data.episode;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 25/10/2017.
 */

public class EpisodeRepository implements EpisodeDataSource {
    private static EpisodeRepository ourInstance = null;

    public static EpisodeRepository getInstance(EpisodeDataSource channelRemoteDataSource) {
        if (ourInstance == null) {
            ourInstance = new EpisodeRepository(channelRemoteDataSource);
        }
        return ourInstance;
    }

    private final EpisodeDataSource channelRemoteDataSource;

    private Map<String, Episode> cachedEpisodes;
    private String currentChannelId = "";

    private EpisodeRepository(@NonNull EpisodeDataSource channelRemoteDataSource) {
        this.channelRemoteDataSource = channelRemoteDataSource;
    }

    @Override
    public void getEpisodes(@NonNull final String channelId, @NonNull final EpisodeDataSource.LoadEpisodesCallback callback) {
        if (!channelId.equals(currentChannelId) && cachedEpisodes != null) {
            cachedEpisodes.clear();
        }
        if (cachedEpisodes == null || cachedEpisodes.isEmpty()) {
            channelRemoteDataSource.getEpisodes(channelId, new EpisodeDataSource.LoadEpisodesCallback() {
                @Override
                public void onEpisodesLoaded(List<Episode> episodes) {
                    refreshCache(episodes);
                    currentChannelId = channelId;
                    callback.onEpisodesLoaded(episodes);
                }

                @Override
                public void onDataNotAvailable() {
                    callback.onDataNotAvailable();
                }
            });
        } else {
            callback.onEpisodesLoaded(new ArrayList<>(cachedEpisodes.values()));
        }
    }

    private void refreshCache(List<Episode> episodes) {
        if (cachedEpisodes == null) {
            cachedEpisodes = new LinkedHashMap<>();
        }

        for (Episode episode : episodes) {
            cachedEpisodes.put(episode.getId(), episode);
        }
    }
}
