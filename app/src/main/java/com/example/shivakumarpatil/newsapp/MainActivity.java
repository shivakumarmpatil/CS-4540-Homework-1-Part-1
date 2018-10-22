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
    private EditText mNewsSearchBox;
    private TextView mNewsUrlDisplay;
    private TextView mNewsSearchResultsJson;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsSearchBox = (EditText) findViewById(R.id.news_search_box);
        mNewsUrlDisplay = (TextView) findViewById(R.id.news_url_display);
        mNewsSearchResultsJson = (TextView) findViewById(R.id.news_search_results_json);
        mErrorMessageDisplay = (TextView) findViewById(R.id.news_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

    }
    private void makeNewsSearchQuery() {
        String newsQuery = mNewsSearchBox.getText().toString();
        URL newsSearchUrl = NetworkUtils.buildUrl(newsQuery);
        mNewsUrlDisplay.setText(newsSearchUrl.toString());
        new NewsQueryTask().execute(newsSearchUrl);

    }
    private void showJsonDataView()
    {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mNewsSearchResultsJson.setVisibility(View.VISIBLE);
    }
    private void showErrorMessage()
    {
        mNewsSearchResultsJson.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
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
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (newsSearchResults != null && !newsSearchResults.equals("")) {
                showJsonDataView();
                mNewsSearchResultsJson.setText(newsSearchResults);
            }
            else
            {
                showErrorMessage();
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
        if (itemThatWasClickedId == R.id.action_search) {
            makeNewsSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
