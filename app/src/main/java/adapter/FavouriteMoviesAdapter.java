package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.movieretrofit.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import datapersistence.Movie;
import util.GradientTransformation;

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.CustomViewHolder> {

    private List<Movie> movieList = Collections.emptyList();
    private Context context;
    private ListItemClickListener mOnClickListener;
    public static final String BASE_URL_FOR_POSTERPATH = "https://image.tmdb.org/t/p/w342/";

    public interface ListItemClickListener {
        void onFavouritePosterClick(int clickedItemIndex, List<Movie> dataList);
    }

    public FavouriteMoviesAdapter(Context context, ListItemClickListener listener) {
        this.context = context;
        this.mOnClickListener = listener;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        TextView txtTitle;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            mOnClickListener.onFavouritePosterClick(getAdapterPosition(), movieList);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.movie_poster, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.txtTitle.setText(movieList.get(position).getTitle());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(BASE_URL_FOR_POSTERPATH + movieList.get(position).getPosterPath())
                .transform(new GradientTransformation())
                .into(holder.coverImage);
    }

    public void setMovies(List<Movie> movies) {
        movieList = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}