package ru.guzeyst.mymovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.guzeyst.mymovies.R;
import ru.guzeyst.mymovies.data.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private ArrayList<Review> listReviews = new ArrayList<>();

    public void setListReviews(ArrayList<Review> listReviews) {
        this.listReviews = listReviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = listReviews.get(position);
        holder.tvContent.setText(review.getContent());
        holder.tvAuthor.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        return listReviews.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAuthor, tvContent;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}
