package khlafawi.com.movietest.ui.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import khlafawi.com.movietest.R;
import khlafawi.com.movietest.data.model.Trailer;
import khlafawi.com.movietest.utils.Constants;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailerList;
    private LayoutInflater Inflater;
    private AppCompatActivity mContext;

    public TrailerAdapter(AppCompatActivity mContext) {
        this.mContext = mContext;
        this.Inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = Inflater.inflate(R.layout.item_trailer, viewGroup, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int i) {
        final Trailer trailer = trailerList.get(i);

        Glide.with(mContext)
                .load(Constants.YOUTUBE_IMAGE + trailer.getKey() + Constants.YOUTUBE_ZERO)
                .into(holder.trailer_image);

        holder.trailer_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_PATH + trailer.getKey())));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (trailerList == null) ? 0 : trailerList.size();
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = new ArrayList<>();
        this.trailerList.addAll(trailerList);
        this.notifyDataSetChanged();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder {

        ImageView trailer_image;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            this.trailer_image = itemView.findViewById(R.id.trailer_image);
        }
    }
}
