package com.luisdelarosa.redditviewerglass;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;
import com.luisdelarosa.reddit.RedditPost;
import com.luisdelarosa.reddit.RedditResponse;

public class RedditPostCardScrollAdapter extends CardScrollAdapter {

	private RedditResponse mRedditResponse;
	private Context mContext;

	public RedditPostCardScrollAdapter(Context context) {
		mContext = context;
	}

	@Override
	public int findIdPosition(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int findItemPosition(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCount() {
		if (mRedditResponse == null) {
			return 0;
		}

		return mRedditResponse.getPostCount();
	}

	@Override
	public Object getItem(int position) {
		if (mRedditResponse == null) {
			return null;
		}
		if (position < 0) {
			return null;
		}
		if (position >= getCount()) {
			return null;
		}

		return mRedditResponse.getPostAt(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the post and its title
		RedditPost post = (RedditPost) getItem(position);
		String title = post.getTitle();
		
		// Create card so we can make a Glass-appropriate View
		Card card = new Card(mContext);
		card.setText(title);
		View view = card.toView();
		
		// ensure we call setItemForView
		setItemOnCard(post, view);
		
		return view;
	}

	public void setRedditResponse(RedditResponse redditResponse) {
		mRedditResponse = redditResponse;
	}

}
