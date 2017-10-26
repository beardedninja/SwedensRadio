package se.harrison.swedensradio.data.episode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex on 22/10/2017.
 */

public class Episode {
    private String id;
    private String title;
    private String description;

    private Date startTimeCache;
    private Date endTimeCache;
    private String startTimeRaw;
    private String endTimeRaw;

    private String programName;
    private String programId;

    private String channelName;
    private String channelId;

    private String imageUrl;
    private String imageUrlTemplate;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartTime() {
        if (startTimeCache == null) {
            startTimeCache = parseDate(startTimeRaw);
        }
        return startTimeCache;
    }

    public Date getEndTime() {
        if (endTimeCache == null) {
            endTimeCache = parseDate(endTimeRaw);
        }
        return endTimeCache;
    }

    // Yuck, wasnt expecting the "/Date(234234)/" format...this would probably be handled properly by
    // the Gson library.
    private Date parseDate(String utc) {
        if (!utc.startsWith("/Date")) return null;

        Calendar calendar = Calendar.getInstance();
        String rawMillis = utc.replace("/Date(", "").replace(")/", "");
        Long timeInMillis = Long.valueOf(rawMillis);
        calendar.setTimeInMillis(timeInMillis);

        return calendar.getTime();
    }

    public String getProgramName() {
        return programName;
    }

    public String getProgramId() {
        return programId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageUrlTemplate() {
        return imageUrlTemplate;
    }

    public static Episode fromJSON(JSONObject json) {
        if (json == null) return null;

        Episode episode = new Episode();

        episode.id = json.optString("episodeid");
        episode.title = json.optString("title", episode.title);
        episode.description = json.optString("description", episode.description);
        episode.startTimeRaw = json.optString("starttimeutc", episode.startTimeRaw);
        episode.endTimeRaw = json.optString("endtimeutc", episode.endTimeRaw);

        if (json.has("program")) {
            try {
                JSONObject program = json.getJSONObject("program");
                episode.programId = program.optString("id", episode.programId);
                episode.programName = program.optString("name", episode.programName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (json.has("channel")) {
            try {
                JSONObject channel = json.getJSONObject("channel");
                episode.channelId = channel.optString("id", episode.channelId);
                episode.channelName = channel.optString("name", episode.channelName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        episode.imageUrl = json.optString("imageurl", episode.imageUrl);
        episode.imageUrlTemplate = json.optString("imageurltemplate", episode.imageUrlTemplate);

        return episode;
    }
}