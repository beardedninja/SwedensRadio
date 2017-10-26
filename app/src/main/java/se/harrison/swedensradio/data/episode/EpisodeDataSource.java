package se.harrison.swedensradio.data.episode;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Alex on 25/10/2017.
 */

public interface EpisodeDataSource {

    interface LoadEpisodesCallback {
        void onEpisodesLoaded(List<Episode> episodes);
        void onDataNotAvailable();
    }

    void getEpisodes(@NonNull String channelId, @NonNull LoadEpisodesCallback callback);
}
