package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import data.MovieResult;
import util.GradientTransformation;

import com.example.movieretrofit.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MoviePageListadapter extends PagedListAdapter<MovieResult.Result, MoviePageListadapter.MovieViewHolder> {

    private Context context;
    private ListItemClickListener recyclerViewOnClickListner;
    private static final String BASE_URL_FOR_POSTERPATH = "https://image.tmdb.org/t/p/w342/";

    public MoviePageListadapter(Context context, MoviePageListadapter.ListItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.recyclerViewOnClickListner = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from(context).inflate(R.layout.movie_poster, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        holder.txtTitle.setText(getItem(position).getTitle());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(BASE_URL_FOR_POSTERPATH + getItem(position).getPosterPath())
                .transform(new GradientTransformation())
                .into(holder.coverImage);
    }

    private static DiffUtil.ItemCallback<MovieResult.Result> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<MovieResult.Result>() {
                @Override
                public boolean areItemsTheSame(@NonNull MovieResult.Result oldItem, @NonNull MovieResult.Result newItem) {
                    return oldItem.getTitle() == newItem.getTitle();
                }

                @Override
                public boolean areContentsTheSame(@NonNull MovieResult.Result oldItem, @NonNull MovieResult.Result newItem) {
                    return true;
                }
            };

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        TextView txtTitle;
        private ImageView coverImage;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            recyclerViewOnClickListner.onListItemClick(getAdapterPosition());
        }
    }
}
