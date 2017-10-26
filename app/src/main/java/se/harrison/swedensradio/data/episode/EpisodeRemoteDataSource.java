package se.harrison.swedensradio.data.episode;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import se.harrison.swedensradio.data.RemoteDataManager;

/**
 * Created by Alex on 25/10/2017.
 */

public class EpisodeRemoteDataSource implements EpisodeDataSource {
    @Override
    public void getEpisodes(@NonNull String channelId, @NonNull final LoadEpisodesCallback callback) {
        String params = "?format=json&pagination=false&channelid=" + channelId;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", new Locale("sv-SE"));
        String formattedDate = df.format(c.getTime());

        params += "&date=" + formattedDate;
        RemoteDataManager.getInstance().GET("scheduledepisodes", params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Episode> episodes = new ArrayList<>();

                try {
                    String body = response.body().string();
                    if (response.code() == 200 && !TextUtils.isEmpty(body)) {
                        JSONObject data = new JSONObject(body);
                        JSONArray results = data.getJSONArray("schedule");
                        for (int i=0; i < results.length(); i++) {
                            Episode episode = Episode.fromJSON(results.getJSONObject(i));
                            if (episode != null) episodes.add(episode);
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    callback.onDataNotAvailable();
                }

                callback.onEpisodesLoaded(episodes);
            }
        });
    }
}
