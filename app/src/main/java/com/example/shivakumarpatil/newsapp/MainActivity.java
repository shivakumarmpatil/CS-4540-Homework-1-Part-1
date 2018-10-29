package com.example.shivakumarpatil.newsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivakumarpatil.newsapp.Utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mNewsUrlDisplay;
    private TextView mNewsSearchResultsJson;
    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsUrlDisplay = (TextView) findViewById(R.id.news_url_display);
        mNewsSearchResultsJson = (TextView) findViewById(R.id.news_search_results_json);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }
    private void makeNewsSearchQuery() {
        URL newsSearchUrl = NetworkUtils.buildUrl();
        mNewsUrlDisplay.setText(newsSearchUrl.toString());
        new NewsQueryTask().execute(newsSearchUrl);

    }

    public class NewsQueryTask extends AsyncTask<URL, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String newsSearchResults = null;
            try {
                newsSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newsSearchResults;
        }
        @Override
        protected void onPostExecute(String newsSearchResults) {
            mLoadingIndicator.setVisibility(View.GONE);
            if (newsSearchResults != null && !newsSearchResults.equals("")) {
                mNewsSearchResultsJson.setText(newsSearchResults);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.get_news) {
            makeNewsSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
