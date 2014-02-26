package com.luisdelarosa.redditviewerglass;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.luisdelarosa.reddit.RedditPost;
import com.luisdelarosa.reddit.RedditResponse;
import com.luisdelarosa.reddit.RedditService;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    protected static final String LOG_TAG = "MainActivity";
	private RedditPostCardScrollAdapter mAdapter;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAdapter = new RedditPostCardScrollAdapter(this);
        
        CardScrollView cardScrollView = new CardScrollView(this);
        cardScrollView.setAdapter(mAdapter);
        cardScrollView.activate();
        setContentView(cardScrollView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	protected void onResume() {
		super.onResume();

		loadHotPostsFromSubreddit("androiddev");
	}

	private void loadHotPostsFromSubreddit(String subreddit) {
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				"http://reddit.com").build();

		RedditService service = restAdapter.create(RedditService.class);
		
		Callback<RedditResponse> callback = new Callback<RedditResponse>() {
			@Override
			public void success(RedditResponse redditResponse, Response response) {
				processRedditResponse(redditResponse);
			}

			private void processRedditResponse(RedditResponse redditResponse) {
				Log.d(LOG_TAG, "reddit response kind:" + redditResponse.kind);	
				Log.d(LOG_TAG, "reddit response modhash:" + redditResponse.data.modhash);	
				
				Log.d(LOG_TAG, "reddit response children:" + redditResponse.data.children);
				Log.d(LOG_TAG, "# of posts:" + redditResponse.data.children.size());
				
				for (RedditPost post : redditResponse.data.children) {
					String title = post.data.title;
					Log.d(LOG_TAG, "post title:" + title);
				}
				
				mAdapter.setRedditResponse(redditResponse);
				mAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void failure(RetrofitError error) {
				Log.d(LOG_TAG, "reddit error:" + error);	
			}

		};
		service.listHotPostsInSubreddit(subreddit, callback);
	}    
}
