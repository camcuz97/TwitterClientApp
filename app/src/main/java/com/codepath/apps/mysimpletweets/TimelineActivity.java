package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 20;
    HomeTimelineFragment htlf;
    MentionsTimelineFragment mtlf;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //Get the viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        //Set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //Find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        client = TwitterApplication.getRestClient();
        client.getRateStatus(new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("Rate", responseBody.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        //Attach the tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);
        htlf = new HomeTimelineFragment();
        mtlf = new MentionsTimelineFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void onProfileView(MenuItem mi){
        //Launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    public void onComposeTweet(MenuItem item) {
        //Launch the compose tweet view
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("id", (long)0);
        i.putExtra("handle", "");
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            htlf.addTweet(tweet);
        }

    }

    public void onSearch(MenuItem item) {
        Intent i = new Intent(this, SearchActivity.class);
        startActivity(i);
    }

    //Return the order of the fragments in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter{
        private String tabTitles[] = {"Home", "Mentions"};

        //Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        //The order and creation of fragments within the pager
        public Fragment getItem(int position){
            if(position == 0){
                return htlf;
            }
            else if (position == 1){
                return mtlf;
            }
            else{
                return null;
            }
        }

        //Return the tab title
        public CharSequence getPageTitle(int position){
            return tabTitles[position];
        }

        //How many fragments there are to swipe between
        public int getCount(){
            return tabTitles.length;
        }
    }

}
