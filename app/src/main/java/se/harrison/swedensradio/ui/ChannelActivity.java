package se.harrison.swedensradio.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import se.harrison.swedensradio.R;
import se.harrison.swedensradio.data.channel.Channel;
import se.harrison.swedensradio.data.channel.ChannelDataSource;

public class ChannelActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChannelFragment.OnListFragmentInteractionListener {

    FrameLayout fragmentContent;
    ChannelDataSource.ChannelFilter currentFilter = ChannelDataSource.ChannelFilter.all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentContent = (FrameLayout) findViewById(R.id.fragment_content);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putInt("filter", currentFilter.ordinal());
            Fragment fragment = ChannelFragment.newInstance(arguments);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_content, fragment)
                    .commit();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("filter", currentFilter.ordinal());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.channel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Bundle arguments = new Bundle();
        if (id == R.id.filter_all) {
            currentFilter = ChannelDataSource.ChannelFilter.all;
        } else if (id == R.id.filter_local) {
            currentFilter =  ChannelDataSource.ChannelFilter.local;
        } else if (id == R.id.filter_national) {
            currentFilter =  ChannelDataSource.ChannelFilter.national;
        } else if (id == R.id.filter_extra) {
            currentFilter =  ChannelDataSource.ChannelFilter.extra;
        }
        arguments.putInt("filter", currentFilter.ordinal());

        Fragment fragment = ChannelFragment.newInstance(arguments);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_content, fragment)
                .commit();

        item.setChecked(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Channel channel) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("channel_id", channel.getId());
        intent.putExtras(bundle);
        intent.setClass(this, ScheduleActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
