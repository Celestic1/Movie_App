package android.example.popularmovies;


import android.example.popularmovies.Data.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private static final String TAG = "MovieAdapter";
    private ArrayList<Movie> mMovieData;
    final private MovieAdapterOnClickHandler mOnClickListener;

    public interface MovieAdapterOnClickHandler {
        void onClick(int clickedItemIndex);
    }

    public MovieAdapter(ArrayList<Movie> data, MovieAdapterOnClickHandler listener) {
        mMovieData = data;
        mOnClickListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mMoviePoster;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_image_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onClick(adapterPosition);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_poster, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String posterPath = mMovieData.get(i).getThumbnailPath();
        Log.d(TAG, posterPath);
        Picasso.get().setLoggingEnabled(true);
        Picasso.get()
                .load(posterPath)
                .into(myViewHolder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

}
