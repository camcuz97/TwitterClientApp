package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {
    TextView tvUsername;
    TextView tvHandle;
    ImageView ivProfileImage;
    TextView tvTimeStamp;
    TextView tvBody;
    TextView tvLikes;
    TextView tvRetweets;
    ImageView ivMedia;

    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupViews();
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvUsername.setText(tweet.getUser().getName());
        tvHandle.setText(tweet.getUser().getScreenName());
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        tvTimeStamp.setText(tweet.getCreatedAt());
        tvBody.setText(tweet.getBody());
        tvLikes.setText("" + tweet.getFavouriteCount() + " favorites");
        tvRetweets.setText("" + tweet.getRetweetCount() + " retweets");
        Picasso.with(this).load(tweet.getMedia()).into(ivMedia);
    }

    private void setupViews() {
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvHandle = (TextView) findViewById(R.id.tvHandle);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvTimeStamp = (TextView) findViewById(R.id.tvTimeStamp);
        tvBody = (TextView) findViewById(R.id.tvBody);
        tvLikes = (TextView) findViewById(R.id.tvLikes);
        tvRetweets = (TextView) findViewById(R.id.tvRetweets);
        ivMedia = (ImageView) findViewById(R.id.ivMedia);

    }
}
