package ru.guzeyst.mymovies.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.guzeyst.mymovies.R;
import ru.guzeyst.mymovies.data.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    ArrayList<Trailer> listTrailers = new ArrayList<>();
    OnPlayClickListener onPlayClickListener;

    public void setListTrailers(ArrayList<Trailer> listTrailers) {
        this.listTrailers = listTrailers;
        notifyDataSetChanged();
    }

    public void setOnPlayClickListener(OnPlayClickListener onPlayClickListener) {
        this.onPlayClickListener = onPlayClickListener;
    }

    public interface OnPlayClickListener{
        void onPlayClick(String url);
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = listTrailers.get(position);
        holder.tvNameOfVideo.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return listTrailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivPlay;
        private TextView tvNameOfVideo;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPlay = itemView.findViewById(R.id.ivPlay);
            tvNameOfVideo = itemView.findViewById(R.id.tvNameOfVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPlayClickListener != null){
                        onPlayClickListener.onPlayClick(listTrailers.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }
}
