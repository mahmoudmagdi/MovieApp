package khlafawi.com.movietest.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import khlafawi.com.movietest.R;
import khlafawi.com.movietest.data.model.Movie;
import khlafawi.com.movietest.ui.adapters.MonthsAdapter;

public class FilterActivity extends AppCompatActivity {

    // extras
    public static final String FILTER_ARRAY = "filterArray";
    private ArrayList<Movie> moviesList = new ArrayList<>();
    private ArrayList<String> availableDates = new ArrayList<>();
    // extras

    // ui
    private RecyclerView date_recycler_view;
    private Button done;
    // ui

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        if (getIntent().hasExtra(FILTER_ARRAY)) {
            moviesList.addAll(getIntent().<Movie>getParcelableArrayListExtra(FILTER_ARRAY));
        }

        initView();                  // set up elements
        initActions();               // set up actions
        getAvailableDates();         // fetch movies' release dates
    }

    private void initView() {
        this.date_recycler_view = findViewById(R.id.date_recycler_view);
        this.done = findViewById(R.id.done);
    }

    private void initActions() {
        this.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void getAvailableDates() {
        MonthsAdapter monthsAdapter = new MonthsAdapter(FilterActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false);

        for (Movie movie : moviesList) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(movie.getRelease_date());
                SimpleDateFormat newFormat = new SimpleDateFormat("MMM, yyyy");
                if (!availableDates.contains(newFormat.format(date)))
                    availableDates.add(newFormat.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        monthsAdapter.setDates(availableDates);
        date_recycler_view.setAdapter(monthsAdapter);
        date_recycler_view.setLayoutManager(linearLayoutManager);
    }
}
