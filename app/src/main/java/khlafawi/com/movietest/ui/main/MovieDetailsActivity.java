package khlafawi.com.movietest.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import khlafawi.com.movietest.BuildConfig;
import khlafawi.com.movietest.R;
import khlafawi.com.movietest.data.model.Genre;
import khlafawi.com.movietest.data.model.Movie;
import khlafawi.com.movietest.data.model.Review;
import khlafawi.com.movietest.data.model.Trailer;
import khlafawi.com.movietest.ui.adapters.GenreAdapter;
import khlafawi.com.movietest.ui.adapters.ReviewAdapter;
import khlafawi.com.movietest.ui.adapters.TrailerAdapter;
import khlafawi.com.movietest.utils.Constants;
import pref.MovieApp;

public class MovieDetailsActivity extends AppCompatActivity {

    //tag
    private final String TAG = MainActivity.class.getSimpleName();
    //tag

    //extras
    public static final String EXTRA_MOVIE = "extra_movie";
    private Movie movie;
    //extras

    //ui
    private ImageView backdrop, adult_image;
    private TextView movie_title, movie_overview, movie_rate, adult_text, release_date, trailer_title, review_title;
    private RecyclerView genres_recycler_view, trailer_recycler_view, review_recycler_view;
    //ui

    //adapters
    private GenreAdapter genreAdapter;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    //adapters

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            this.movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        }

        initView();              // set up elements
        initActions();           // set up actions
        getMovieDetails();       // fetch movie details
    }

    private void initView() {
        this.movie_title = findViewById(R.id.movie_title);
        this.movie_overview = findViewById(R.id.movie_overview);
        this.backdrop = findViewById(R.id.backdrop);
        this.movie_rate = findViewById(R.id.movie_rate);
        this.adult_image = findViewById(R.id.adult_image);
        this.adult_text = findViewById(R.id.adult_text);
        this.release_date = findViewById(R.id.release_date);
        this.trailer_title = findViewById(R.id.trailer_title);
        this.review_title = findViewById(R.id.review_title);

        this.genres_recycler_view = findViewById(R.id.genres_recycler_view);
        this.genres_recycler_view.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));

        this.trailer_recycler_view = findViewById(R.id.trailer_recycler_view);
        this.trailer_recycler_view.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));

        this.review_recycler_view = findViewById(R.id.review_recycler_view);
        this.review_recycler_view.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.VERTICAL, false));

        genreAdapter = new GenreAdapter(MovieDetailsActivity.this);
        trailerAdapter = new TrailerAdapter(MovieDetailsActivity.this);
        reviewAdapter = new ReviewAdapter(MovieDetailsActivity.this);
    }

    private void initActions() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(movie.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" "); //careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getMovieDetails() {

        // basic movie details
        this.movie_title.setText(movie.getTitle());
        this.movie_overview.setText(movie.getOverview());
        this.movie_rate.setText(String.valueOf(movie.getVote_average()));
        this.release_date.setText(getResources().getString(R.string.release_date) + " " + movie.getRelease_date());

        // movie backdrop
        Glide.with(MovieDetailsActivity.this)
                .load(Constants.IMAGE_PATH + movie.getBackdrop_path())
                .into(backdrop);

        getAdult();                      // fetch if movie 18+
        getGenres(movie.getGenres());    // get all movie's Genres
        getTrailers();                   // get all movie's Trailers
        getReviews();                    // get all movie's People Reviews from MovieDB
    }

    private void getAdult() {
        if (movie.isAdult()) {
            this.adult_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_adult));
            this.adult_text.setText(getResources().getString(R.string.this_movie_suitable_for_adults_only));
        } else {
            this.adult_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_child));
            this.adult_text.setText(getResources().getString(R.string.suitable_for_children));
        }
    }

    private void getGenres(final List<Integer> genreIdsList) {

        final ArrayList<Genre> genresList = new ArrayList<>();
        String url = Constants.GENRE_DB_PATH + Constants.API_PARAM + BuildConfig.THE_MOVIE_DB_API_KEY;

        final StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray genres = jObj.getJSONArray(Constants._genres);

                    if (genres.length() > 0) {
                        for (int i = 0; i < genres.length(); i++) {

                            JSONObject item = genres.getJSONObject(i);

                            if (item.has(Genre._id) && item.has(Genre._name))
                                if (genreIdsList.contains(item.getInt(Genre._id)))
                                    genresList.add(new Genre(item.getInt(Genre._id), item.getString(Genre._name)));
                        }

                        genreAdapter.setGenresList(genresList);
                        genres_recycler_view.setAdapter(genreAdapter);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        MovieApp.getInstance().addToRequestQueue(strReq, "GENRES_REQUEST");
    }

    private void getTrailers() {
        final ArrayList<Trailer> trailerList = new ArrayList<>();
        String url = Constants.MOVIE_DB_PATH + movie.getId() + Constants.VIDEOS + Constants.API_PARAM + BuildConfig.THE_MOVIE_DB_API_KEY;

        final StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray results = jObj.getJSONArray(Constants._results);

                    if (results.length() > 0) {
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject item = results.getJSONObject(i);

                            if (item.has(Trailer._id) && item.has(Trailer._key))
                                trailerList.add(new Trailer(item.getString(Trailer._id), item.getString(Trailer._key)));
                        }

                        trailerAdapter.setTrailerList(trailerList);
                        trailer_recycler_view.setAdapter(trailerAdapter);
                    } else {
                        trailer_title.setVisibility(View.GONE);
                        trailer_recycler_view.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        MovieApp.getInstance().addToRequestQueue(strReq, "TRAILERS_REQUEST");
    }

    private void getReviews() {
        final ArrayList<Review> reviewsList = new ArrayList<>();
        String url = Constants.MOVIE_DB_PATH + movie.getId() + Constants.REVIEWS + Constants.API_PARAM + BuildConfig.THE_MOVIE_DB_API_KEY;

        final StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jObj = new JSONObject(response);
                    JSONArray results = jObj.getJSONArray(Constants._results);

                    if (results.length() > 0) {
                        for (int i = 0; i < results.length(); i++) {

                            JSONObject item = results.getJSONObject(i);

                            if (item.has(Review._content) && item.has(Review._author))
                                reviewsList.add(new Review(item.getString(Review._id), item.getString(Review._author), item.getString(Review._content), item.getString(Review._url)));
                        }

                        reviewAdapter.setReviewList(reviewsList);
                        review_recycler_view.setAdapter(reviewAdapter);
                    } else {
                        review_title.setVisibility(View.GONE);
                        review_recycler_view.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

            }
        });

        // Adding request to request queue
        MovieApp.getInstance().addToRequestQueue(strReq, "REVIEWS_REQUEST");
    }
}
