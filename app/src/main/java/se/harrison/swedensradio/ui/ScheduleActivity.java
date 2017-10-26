package se.harrison.swedensradio.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.MainThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.harrison.swedensradio.R;
import se.harrison.swedensradio.data.channel.Channel;
import se.harrison.swedensradio.data.channel.ChannelDataSource;
import se.harrison.swedensradio.data.channel.ChannelRemoteDataSource;
import se.harrison.swedensradio.data.channel.ChannelRepository;
import se.harrison.swedensradio.data.episode.Episode;
import se.harrison.swedensradio.data.episode.EpisodeDataSource;
import se.harrison.swedensradio.data.episode.EpisodeRemoteDataSource;
import se.harrison.swedensradio.data.episode.EpisodeRepository;

public class ScheduleActivity extends AppCompatActivity {

    private EpisodeRepository episodeRepository = EpisodeRepository.getInstance(new EpisodeRemoteDataSource());
    private ChannelRepository channelRepository = ChannelRepository.getInstance(new ChannelRemoteDataSource());
    private List<Episode> episodes = new ArrayList<>();
    private String currentChannelId;
    private Channel currentChannel;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private ImageView toolbarImageView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarImageView = (ImageView) findViewById(R.id.toolbar_imageview);
        recyclerView = (RecyclerView) findViewById(R.id.schedule_list);
        recyclerView.setAdapter(new EpisodeRecyclerViewAdapter(episodes));
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "This would create an intent to start a service that plays the media for this station...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                fab.setImageResource(android.R.drawable.ic_media_pause);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fab.setImageResource(android.R.drawable.ic_media_play);
                    }
                }, 3000);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (!TextUtils.isEmpty(getIntent().getExtras().getString("channel_id"))) {
            currentChannelId = getIntent().getExtras().getString("channel_id");
            fetchChannel(currentChannelId);
        } else if (savedInstanceState != null){
            currentChannelId = savedInstanceState.getString("channel_id");
            fetchChannel(currentChannelId);
        }
    }

    private void fetchChannel(String channelId) {
        channelRepository.getChannel(channelId, new ChannelDataSource.GetChannelCallback() {
            @Override
            public void onChannelLoaded(Channel channel) {
                fetchEpisodes(currentChannelId);
                setChannel(channel);
            }

            @Override
            public void onDataNotAvailable() {
                // Snackbar with data error message
            }
        });
    }

    private void fetchEpisodes(String channelId) {
        episodeRepository.getEpisodes(channelId, new EpisodeDataSource.LoadEpisodesCallback() {
            @Override
            public void onEpisodesLoaded(List<Episode> episodes) {
                setEpisodes(episodes);
            }

            @Override
            public void onDataNotAvailable() {
                // Snackbar with data error message
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("channel_id", currentChannelId);
    }

    public void setChannel(final Channel channel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentChannel = channel;
                setNowPlaying(currentChannel.getName(), currentChannel.getImageUrl());
            }
        });
    }

    public void setEpisodes(final List<Episode> episodes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean found = false;
                for(Episode episode : episodes) {
                    Date now = new Date();
                    if (episode.getStartTime().before(now)  && episode.getEndTime().after(now)) {
                        found = true;
                        setNowPlaying(episode.getTitle(), episode.getImageUrl());
                        break;
                    }
                }
                if (!found && currentChannel != null) {
                    setNowPlaying(currentChannel.getName(), currentChannel.getImageUrl());
                }
                if (recyclerView != null)
                    ((EpisodeRecyclerViewAdapter) recyclerView.getAdapter()).updateEpisodes(episodes);
            }
        });

    }

    public void setNowPlaying(String title, String imageUrl) {
        Picasso.with(ScheduleActivity.this).load(imageUrl).into(toolbarImageView);
        collapsingToolbarLayout.setTitle(title);
    }
}
