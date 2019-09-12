package khlafawi.com.movietest.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import khlafawi.com.movietest.R;
import khlafawi.com.movietest.data.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private LayoutInflater Inflater;

    public ReviewAdapter(AppCompatActivity mContext) {
        this.Inflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = Inflater.inflate(R.layout.item_review, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int i) {
        final Review review = reviewList.get(i);

        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return (reviewList == null) ? 0 : reviewList.size();
    }

    public void setReviewList(List<Review> reviewList) {
        this.reviewList = new ArrayList<>();
        this.reviewList.addAll(reviewList);
        this.notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView author, content;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            this.author = itemView.findViewById(R.id.author);
            this.content = itemView.findViewById(R.id.content);
        }
    }
}
