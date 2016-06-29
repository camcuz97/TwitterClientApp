package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    Tweet tweet;
    EditText etTweet;
    TextView tvChars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();
        client.getUserInfo(null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                //My current user account's information
                getSupportActionBar().setTitle("@" + user.getScreenName());
                populateComposeHeader(user);
            }
        });
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvChars = (TextView) findViewById(R.id.tvChars);
        etTweet.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                tvChars.setText(String.valueOf(140-s.length()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_compose, menu);
//        return true;
//    }

    public void onPostTweet(View view){
        String post = etTweet.getText().toString();
        client.postStatus(post, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweet = Tweet.fromJSON(response);
                Intent data = new Intent();
                data.putExtra("tweet", Parcels.wrap(tweet));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void populateComposeHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvUsername);
        TextView tvHandle = (TextView) findViewById(R.id.tvHandle);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.getName());
        tvHandle.setText("@" + user.getScreenName());
        Picasso.with(getApplicationContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
    }
}
