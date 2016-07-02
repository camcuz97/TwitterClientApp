package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by camcuz97 on 6/27/16.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{
    TwitterClient client = TwitterApplication.getRestClient();

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Get the tweet
        final Tweet tweet = getItem(position);
        // 2. Inflate the template
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        // 3. Find the subviews
        final ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvHandle = (TextView) convertView.findViewById(R.id.tvHandle);
        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
        final TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        final TextView tvRetweets = (TextView) convertView.findViewById(R.id.tvRetweets);
        ImageView ivReply = (ImageView) convertView.findViewById(R.id.ivReply);
        ImageView ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);
        if(tweet.getMedia() != null){
            ivMedia.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(tweet.getMedia()).into(ivMedia);
        }
        else{
            ivMedia.setVisibility(View.GONE);
        }
        final ImageView ivLikes = (ImageView) convertView.findViewById(R.id.ivLikes);
        if(tweet.isFavorited() == true){
            ivLikes.setImageResource(R.drawable.ic_fav_true);
        }
        else{
            ivLikes.setImageResource(R.drawable.ic_fav);
        }
        final ImageView ivRetweet = (ImageView) convertView.findViewById(R.id.ivRetweets);
        if(tweet.isRetweeted() == true){
            ivRetweet.setImageResource(R.drawable.ic_retweet_true);
        }
        else{
            ivRetweet.setImageResource(R.drawable.ic_retweet);
        }
        // 4. Populate data into subviews
        tvTimeStamp.setText(tweet.getTimeAgo());
        tvUsername.setText(tweet.getUser().getName());
        tvHandle.setText("@" +tweet.getUser().getScreenName());
        tvRetweets.setText("" + tweet.getRetweetCount());
        tvLikes.setText("" + tweet.getFavouriteCount());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setTag(tweet.getUser().getScreenName());

        ivProfileImage.setImageResource(android.R.color.transparent); //clear out old image for recycled view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        final String tag = (String) ivProfileImage.getTag();
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("screen_name", tag);
                getContext().startActivity(i);
            }
        });
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ComposeActivity.class);
                i.putExtra("id", tweet.getUid());
                i.putExtra("handle", tweet.getUser().getScreenName());
                getContext().startActivity(i);
            }
        });
        ivLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tweet.isFavorited() == true){
                    client.unfavorite(tweet.getUid(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setFavorited(false);
                            ivLikes.setImageResource(R.drawable.ic_fav);
                            tvLikes.setText((tweet.getFavouriteCount()) + "");
                        }
                    });
                }
                else{
                    client.favorite(tweet.getUid(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setFavorited(true);
                            ivLikes.setImageResource(R.drawable.ic_fav_true);
                            tvLikes.setText((tweet.getFavouriteCount() + 1) + "");
                        }
                    });
                }
            }
        });
        ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tweet.isRetweeted() == true){
                    client.unretweet(tweet.getUid(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setRetweeted(false);
                            ivRetweet.setImageResource(R.drawable.ic_retweet);
                            tvRetweets.setText((tweet.getRetweetCount())+"");
                        }
                    });
                }
                else{
                    client.retweet(tweet.getUid(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweet.setRetweeted(true);
                            ivRetweet.setImageResource(R.drawable.ic_retweet_true);
                            tvRetweets.setText((tweet.getRetweetCount() + 1)+ "");
                        }
                    });
                }
            }
        });
        // 5. Return the view to be inserted into the list
        return convertView;
    }
}
