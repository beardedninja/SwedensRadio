package se.harrison.swedensradio.data.channel;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import se.harrison.swedensradio.data.RemoteDataManager;

/**
 * Created by Alex on 22/10/2017.
 */

public class ChannelRemoteDataSource implements ChannelDataSource {

    // TODO: Pagination and sorting support
    @Override
    public void getChannels(@NonNull ChannelFilter channelFilter, @NonNull final LoadChannelsCallback callback) {
        String params = "?format=json&pagination=false";

        if (channelFilter != ChannelFilter.all) {
            params += "&filter=channel.channeltype";

            switch (channelFilter) {
                case local:
                    params += "&filtervalue=lokal";
                    break;
                case national:
                    params += "&filtervalue=rikskanal";
                    break;
                case extra:
                    params += "&filtervalue=extrakanaler";
                    break;
            }
        }

        RemoteDataManager.getInstance().GET("channels", params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Channel> channels = new ArrayList<>();

                try {
                    String body = response.body().string();
                    if (response.code() == 200 && !TextUtils.isEmpty(body)) {
                        JSONObject data = new JSONObject(body);
                        JSONArray results = data.getJSONArray("channels");
                        for (int i=0; i < results.length(); i++) {
                            Channel channel = Channel.fromJSON(results.getJSONObject(i));
                            if (channel != null) channels.add(channel);
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    callback.onDataNotAvailable();
                }

                callback.onChannelsLoaded(channels);
            }
        });
    }

    @Override
    public void getChannel(@NonNull String channelId, @NonNull final GetChannelCallback callback) {
        String params = "?format=json";
        RemoteDataManager.getInstance().GET("channels/" + channelId, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onDataNotAvailable();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //callback.onChannelLoaded();
            }
        });
    }
}
