package se.harrison.swedensradio.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import se.harrison.swedensradio.R;
import se.harrison.swedensradio.data.channel.Channel;
import se.harrison.swedensradio.ui.ChannelFragment.OnListFragmentInteractionListener;

import java.util.List;

public class ChannelRecyclerViewAdapter extends RecyclerView.Adapter<ChannelRecyclerViewAdapter.ViewHolder> {

    private List<Channel> channels;
    private final OnListFragmentInteractionListener listener;

    public ChannelRecyclerViewAdapter(List<Channel> channels, OnListFragmentInteractionListener listener) {
        this.channels = channels;
        this.listener = listener;
    }

    public void updateChannels(List<Channel> channels) {
        this.channels = channels;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_channel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.channel = channels.get(position);
        holder.title.setText(holder.channel.getName());
        Picasso.with(holder.view.getContext()).load(holder.channel.getImageUrl()).into(holder.image);
        holder.live.setVisibility(holder.channel.isLive() ? View.VISIBLE : View.GONE);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onListFragmentInteraction(holder.channel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final ImageView image;
        public final TextView live;
        public Channel channel;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            title = (TextView) view.findViewById(R.id.channel_title);
            image = (ImageView) view.findViewById(R.id.channel_image);
            live = (TextView) view.findViewById(R.id.channel_live);
        }
    }
}
