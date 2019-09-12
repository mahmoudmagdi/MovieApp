package khlafawi.com.movietest.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import khlafawi.com.movietest.BuildConfig;
import khlafawi.com.movietest.R;
import khlafawi.com.movietest.data.Client;
import khlafawi.com.movietest.data.Service;
import khlafawi.com.movietest.data.model.Movie;
import khlafawi.com.movietest.data.remote.MoviesResponse;
import khlafawi.com.movietest.ui.adapters.MoviesAdapter;
import khlafawi.com.movietest.utils.InfiniteScrollListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static MainActivity mInstance;

    public static synchronized MainActivity getInstance() {
        return mInstance;
    }

    private Service movieService;

    //ui
    private LinearLayout main_view;
    private RecyclerView movies_recycler_view;
    private RelativeLayout progress, no_internet;
    private ImageView filter;
    private Button refresh;
    //ui

    //Main Movies List Adapter
    private MoviesAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    //Main Movies List Adapter

    //for infinite scrolling
    private final int PAGE_START = 1;
    private final int TOTAL_PAGES = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int totalPages = TOTAL_PAGES;
    private int currentPage = PAGE_START;
    //for infinite scrolling

    // for filter purpose
    private ArrayList<Movie> moviesList = new ArrayList<>();
    private int FILTER_RESULT = 1000;
    public ArrayList<String> selectedDates = new ArrayList<>();
    // for filter purpose

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstance = this;

        initView();                                                  // set up elements
        initActions();                                               // set up actions
        movieService = Client.getClient().create(Service.class);     // init service and load data
        getMovies(true);                                  // fetch latest movies
    }

    private void initView() {
        this.main_view = findViewById(R.id.main_view);
        this.movies_recycler_view = findViewById(R.id.movies_recycler_view);
        this.progress = findViewById(R.id.progress);
        this.no_internet = findViewById(R.id.no_internet);
        this.refresh = findViewById(R.id.refresh);
        this.filter = findViewById(R.id.filter);

        this.adapter = new MoviesAdapter(this);
        this.movies_recycler_view.setAdapter(adapter);
        this.linearLayoutManager = new GridLayoutManager(this, 3);
        this.movies_recycler_view.setLayoutManager(linearLayoutManager);
        this.movies_recycler_view.setItemAnimator(new DefaultItemAnimator());
    }

    private void initActions() {
        this.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (movieService == null)
                    movieService = Client.getClient().create(Service.class);

                getMovies(true);
            }
        });

        this.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, FilterActivity.class)
                        .putExtra(FilterActivity.FILTER_ARRAY, moviesList), FILTER_RESULT);
            }
        });

        this.movies_recycler_view.addOnScrollListener(new InfiniteScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMovies(false);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILTER_RESULT) {
            if (resultCode == RESULT_OK) {
                adapter.clear();
                currentPage = 1;
                getMovies(true);
            }
        }
    }

    private void getMovies(final boolean isFirstPage) {
        callTopRatedMoviesApi().enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {

                // Got data. Send it to adapter
                if (response != null) {

                    List<Movie> results = fetchResults(response);
                    moviesList.addAll(results);

                    if (selectedDates.size() == 0) {
                        adapter.addAll(results);
                    } else {
                        adapter.addAll(getFilteredResult(results));
                    }

                    if (isFirstPage) {
                        totalPages = fetchTotalPages(response);

                        if (currentPage <= totalPages)
                            adapter.addLoadingFooter();
                        else isLastPage = true;
                    } else {
                        adapter.removeLoadingFooter();
                        isLoading = false;

                        if (currentPage != totalPages)
                            adapter.addLoadingFooter();
                        else isLastPage = true;
                    }

                    // set up view after result
                    main_view.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    no_internet.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
                main_view.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                no_internet.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * @param results extracts List<{@link Movie >} from response
     * @return List<Movie>
     */
    @SuppressLint("SimpleDateFormat")
    private List<Movie> getFilteredResult(List<Movie> results) {
        List<Movie> newFilteredResult = new ArrayList<>();

        for (Movie movie : results) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(movie.getRelease_date());
                SimpleDateFormat newFormat = new SimpleDateFormat("MMM, yyyy");

                // If the list of the selected months contains the release date of the movie, put it in the results.
                if (selectedDates.contains(newFormat.format(date)))
                    newFilteredResult.add(movie);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return newFilteredResult;
    }

    /**
     * @param response extracts List<{@link Movie >} from response
     * @return List<Movie>
     */
    private List<Movie> fetchResults(Response<MoviesResponse> response) {
        MoviesResponse latestMovies = response.body();
        if (latestMovies != null) return latestMovies.getResults();
        else return null;
    }

    /**
     * @param response extracts Integer from response
     * @return Integer
     */
    private int fetchTotalPages(Response<MoviesResponse> response) {
        MoviesResponse totalPages = response.body();
        if (totalPages != null) return totalPages.getTotalPages();
        else return 10;
    }

    /**
     * Performs a Retrofit call to the latest movies API.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link InfiniteScrollListener} to load next page.
     */
    private Call<MoviesResponse> callTopRatedMoviesApi() {
        return movieService.getNowPlayingNowMovies(
                BuildConfig.THE_MOVIE_DB_API_KEY,
                currentPage
        );
    }
}
