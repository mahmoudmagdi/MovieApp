package khlafawi.com.movietest.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import khlafawi.com.movietest.R;
import khlafawi.com.movietest.data.model.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    private List<Genre> genreList;
    private LayoutInflater Inflater;

    public GenreAdapter(Context Context) {
        this.Inflater = LayoutInflater.from(Context);
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = Inflater.inflate(R.layout.item_genre, viewGroup, false);
        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int i) {
        Genre genre = genreList.get(i);
        holder.genre_title.setText(genre.getName());
    }

    @Override
    public int getItemCount() {
        return (genreList == null) ? 0 : genreList.size();
    }

    public void setGenresList(List<Genre> genreList) {
        this.genreList = new ArrayList<>();
        this.genreList.addAll(genreList);
        this.notifyDataSetChanged();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView genre_title;

        GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            this.genre_title = itemView.findViewById(R.id.genre_title);
        }
    }
}
