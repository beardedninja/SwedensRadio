package se.harrison.swedensradio.ui;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import se.harrison.swedensradio.R;
import se.harrison.swedensradio.data.episode.Episode;

/**
 * Created by Alex on 25/10/2017.
 */

public class EpisodeRecyclerViewAdapter extends RecyclerView.Adapter<EpisodeRecyclerViewAdapter.ViewHolder> {
    private List<Episode> episodes;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", new Locale("sv-SE"));

    public EpisodeRecyclerViewAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public void updateEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    @Override
    public EpisodeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_episode, parent, false);
        return new EpisodeRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.episode = episodes.get(position);

        String time = timeFormat.format(holder.episode.getStartTime());
        time += " - " + timeFormat.format(holder.episode.getEndTime());

        holder.time.setText(time);
        holder.title.setText(holder.episode.getTitle());
        holder.description.setText(holder.episode.getDescription());
        if (!TextUtils.isEmpty(holder.episode.getImageUrl())) {
            Picasso.with(holder.view.getContext()).load(holder.episode.getImageUrl()).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView time;
        public final TextView title;
        public final TextView description;
        public final ImageView image;
        public Episode episode;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            time = (TextView) view.findViewById(R.id.episode_time);
            title = (TextView) view.findViewById(R.id.episode_title);
            description = (TextView) view.findViewById(R.id.episode_description);
            image = (ImageView) view.findViewById(R.id.episode_image);
        }
    }
}
