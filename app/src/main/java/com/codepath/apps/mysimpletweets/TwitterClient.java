package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "rfgdXA4VMzyRvmJNUgibmb6Kt";       // Change this
	public static final String REST_CONSUMER_SECRET = "egtOHduFqXmOoe6Uw46Rx2hGLHGiCcZUqQtnGqayNe8GZ99v4Z"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	//GET statuses/home_timeline.json
	//   count = 25
	//   since_id = 1

	public void getHomeTimeline(AsyncHttpResponseHandler handler){
		String apiUrl =  getApiUrl("statuses/home_timeline.json");
		//Specify the params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		//Execute request
		Log.d("DEBUG", apiUrl + params);
		getClient().get(apiUrl, params, handler);
	}

	public void getMentionTimeline(JsonHttpResponseHandler handler) {
		String apiUrl =  getApiUrl("statuses/mentions_timeline.json");
		//Specify the params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		//Execute request
		getClient().get(apiUrl, params, handler);
	}

	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("screen_name", screenName);
		getClient().get(apiUrl, params, handler);
	}

	public void getSearchTimeline(String query, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("search/tweets.json");
		RequestParams params = new RequestParams();
		params.put("q", query);
		Log.d("DEBUG", apiUrl + params);
		getClient().get(apiUrl, params, handler);
	}

	public void getUserInfo(String screenName, AsyncHttpResponseHandler handler){
		if(screenName == null){
			String apiUrl = getApiUrl("account/verify_credentials.json");
			getClient().get(apiUrl, null, handler);
		}
		else{
			String apiUrl = getApiUrl("users/show.json");
			RequestParams params = new RequestParams();
			params.put("screen_name", screenName);
			getClient().get(apiUrl,params,handler);
		}
	}

	public void getRateStatus(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("application/rate_limit_status.json");
		getClient().get(apiUrl, null, handler);
	}

	public void postStatus(long id, String status, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		if(id != (long)0){
			params.put("in_reply_to_status_id", id);
		}
		params.put("status", status);
		getClient().post(apiUrl, params, handler);
		Log.d("DEBUG", apiUrl + params);
	}

	public void favorite(long id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("favorites/create.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(apiUrl, params, handler);
	}

	public void unfavorite(long id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("favorites/destroy.json");
		RequestParams params = new RequestParams();
		params.put("id", id);
		getClient().post(apiUrl, params, handler);
	}

	public void retweet(long id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/retweet/" + id + ".json");
		getClient().post(apiUrl, null, handler);
	}

	public void unretweet(long id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/unretweet/" + id + ".json");
		getClient().post(apiUrl, null, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}