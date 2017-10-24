package se.harrison.swedensradio.data;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Alex on 22/10/2017.
 */

public class RemoteDataManager {
    private static final RemoteDataManager ourInstance = new RemoteDataManager();

    public static RemoteDataManager getInstance() {
        return ourInstance;
    }

    private static final String BASEURL = "http://api.sr.se/api/v2/";

    private OkHttpClient client = new OkHttpClient();

    private RemoteDataManager() {
    }

    public void GET(String path, String params, Callback callback){
        Request request = new Request.Builder()
                .url(BASEURL + path + params)
                .get()
                .build();

        client.newCall(request).enqueue(callback);
    }
}
