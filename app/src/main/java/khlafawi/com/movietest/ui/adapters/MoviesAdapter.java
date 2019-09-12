package khlafawi.com.movietest.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import khlafawi.com.movietest.R;
import khlafawi.com.movietest.data.model.Movie;
import khlafawi.com.movietest.ui.main.MovieDetailsActivity;
import khlafawi.com.movietest.utils.Constants;

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Movie> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public MoviesAdapter(Context context) {
        this.context = context;
        movieResults = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;

            case LOADING:
                View v2 = inflater.inflate(R.layout.item_movie_progress, parent, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_movie, parent, false);
        viewHolder = new MovieViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final Movie movie = movieResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieViewHolder movieVH = (MovieViewHolder) holder;

                movieVH.movie_title.setText(movie.getTitle());
                movieVH.movie_rate.setText(String.valueOf(movie.getVote_average()));
                movieVH.poster.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, MovieDetailsActivity.class)
                                .putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie));
                    }
                });

                if (movie.getPoster_path() != null)
                    Glide.with(context)
                            .load(Constants.IMAGE_PATH + movie.getPoster_path())
                            .into(movieVH.poster);

                break;

            case LOADING:
                //Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
    */

    private void add(Movie r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Movie> moveResults) {
        if (moveResults != null)
            for (Movie result : moveResults) {
                add(result);
            }
    }

    private void remove(Movie r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Movie result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    private Movie getItem(int position) {
        return movieResults.get(position);
    }

   /*
   View Holders
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView poster;
        private TextView movie_title, movie_rate;

        MovieViewHolder(View itemView) {
            super(itemView);

            this.poster = itemView.findViewById(R.id.poster);
            this.movie_title = itemView.findViewById(R.id.movie_title);
            this.movie_rate = itemView.findViewById(R.id.movie_rate);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}