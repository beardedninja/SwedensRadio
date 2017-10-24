package se.harrison.swedensradio.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.harrison.swedensradio.R;
import se.harrison.swedensradio.data.channel.Channel;
import se.harrison.swedensradio.data.channel.ChannelDataSource;
import se.harrison.swedensradio.data.channel.ChannelRemoteDataSource;
import se.harrison.swedensradio.data.channel.ChannelRepository;

import java.util.ArrayList;
import java.util.List;

public class ChannelFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private ChannelRepository channelRepository = ChannelRepository.getInstance(new ChannelRemoteDataSource());
    private List<Channel> channels = new ArrayList<>();
    private RecyclerView recyclerView;
    private ChannelDataSource.ChannelFilter currentFilter = ChannelDataSource.ChannelFilter.all;

    public ChannelFragment() {
    }

    public static ChannelFragment newInstance(Bundle bundle) {
        ChannelFragment fragment = new ChannelFragment();

        bundle.putInt(ARG_COLUMN_COUNT, 2);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            currentFilter = ChannelDataSource.ChannelFilter.values()[getArguments().getInt("filter")];
        }

        channelRepository.getChannels(currentFilter, new ChannelDataSource.LoadChannelsCallback() {
            @Override
            public void onChannelsLoaded(List<Channel> channels) {
                setChannels(channels);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ChannelRecyclerViewAdapter(channels, mListener));
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    public void setChannels(final List<Channel> channels) {
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (recyclerView != null)
                    ((ChannelRecyclerViewAdapter) recyclerView.getAdapter()).updateChannels(channels);
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Channel channel);
    }
}
