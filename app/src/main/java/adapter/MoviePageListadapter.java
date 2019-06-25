package adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.movieretrofit.MainActivity;
import com.example.movieretrofit.MasterListFragment;
import com.example.movieretrofit.MovieResult;
import com.example.movieretrofit.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.MatchResult;

public class MoviePageListadapter extends PagedListAdapter<MovieResult.Result,MoviePageListadapter.MovieViewHolder> {

    private Context context;
    private ListItemClickListener recyclerViewOnClickListner;

    public MoviePageListadapter(Context context, MoviePageListadapter.ListItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.context=context;
        this.recyclerViewOnClickListner = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int position );
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.from (context).inflate(R.layout.movie_poster, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        holder.txtTitle.setText(getItem(position).getTitle());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load("https://image.tmdb.org/t/p/w342/"+getItem(position).getPosterPath())
                .into(holder.coverImage);



    }

    private static DiffUtil.ItemCallback<MovieResult.Result> DIFF_CALLBACK=
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
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        TextView txtTitle;
        private ImageView coverImage;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
            Log.d("clicked position"," "+getAdapterPosition());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context,"clicked position is"+getAdapterPosition(),Toast.LENGTH_SHORT);
            recyclerViewOnClickListner.onListItemClick(getAdapterPosition());

        }
    }
}
