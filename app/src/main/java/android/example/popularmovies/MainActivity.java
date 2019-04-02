package android.example.popularmovies;

import android.example.popularmovies.Data.Movie;
import android.example.popularmovies.Utils.MySingleton;
import android.example.popularmovies.Utils.NetworkUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String TAG = "MainActivity";
    private static final String POPULAR_BASE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String RATING_BASE_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=";
    private static final String LANGUAGE = "&language=en-US";
    private static final String PAGE = "&page=";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780";
    private int pageCount = 1;
    private int totalPages;
    private String current_url;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mMovieList = new ArrayList<>();

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.removeAllViewsInLayout();
                getView();
            }
        });

        if (!NetworkUtils.isConnectedToNetwork(this))
            Toast.makeText(MainActivity.this, "Error: No internet connection.", Toast.LENGTH_LONG).show();
        else {
            getMovieData(POPULAR_BASE_URL);
            getView();
        }
    }

    private void getView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(8);
        mAdapter = new MovieAdapter(mMovieList, this);
        mRecyclerView.setAdapter(mAdapter);
        if(mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    private void getMovieData(String urlQuery) {
        if (!NetworkUtils.isConnectedToNetwork(this))
            Toast.makeText(MainActivity.this, "Error: No internet connection.", Toast.LENGTH_LONG).show();
        else {
            mSwipeRefreshLayout.setRefreshing(true);

            String url = urlQuery
                    + android.example.popularmovies.Data.BuildConfig.API_KEY
                    + LANGUAGE
                    + PAGE
                    + pageCount;
            current_url = url;
            Log.d(TAG, url);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                totalPages = Integer.parseInt(response.getString("total_pages"));
                                JSONArray results = response.getJSONArray("results");
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject jsonObject = results.getJSONObject(i);
                                    String title = jsonObject.getString("title");
                                    String rating = jsonObject.getString("vote_average");
                                    if (rating.equals("0")) {
                                        rating = "No Ratings Yet.";
                                    }
                                    String synopsis = jsonObject.getString("overview");
                                    String release = jsonObject.getString("release_date");
                                    String posterPath = jsonObject.getString("poster_path");
                                    String posterUrl = IMAGE_BASE_URL + posterPath;
                                    mMovieList.add(new Movie(title, release, rating, synopsis, posterUrl));
                                }
                                mAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            error.printStackTrace();
                        }
                    });
            Log.d(TAG, "JSON data retrieval successful");
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    private void updateView(String url){
        mMovieList.clear();
        mAdapter.notifyDataSetChanged();
        getMovieData(url);
        getView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.sort_by_popularity:
                pageCount = 1;
                updateView(POPULAR_BASE_URL);
                break;
            case R.id.sort_by_rating:
                pageCount = 1;
                updateView(RATING_BASE_URL);
                break;
            case R.id.next_page:
                if(pageCount < totalPages){
                    pageCount++;
                    updateView(current_url);
                }
                break;
            case R.id.previous_page:
                if(pageCount > 1){
                    pageCount--;
                    updateView(current_url);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int clickedItemIndex) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
    }
}
