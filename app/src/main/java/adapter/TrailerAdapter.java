package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieretrofit.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import data.VideoDatabase;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    public static final String TRAILER_THUMBNAIL_BASE_LINK = "https://img.youtube.com/vi/";
    public static final String FILE_NAME = "/sddefault.jpg";
    private List<VideoDatabase.Result> trailerList;
    private Context context;
    private TrailerItemClickListener onTrailerClickListner;

    public interface TrailerItemClickListener {
        void onListItemClick(int clickedItemIndex,List<VideoDatabase.Result> trailerList);
    }

    public TrailerAdapter(Context context, List<VideoDatabase.Result> trailerList,TrailerItemClickListener listner) {
        this.onTrailerClickListner = listner;
        this.context=context;
        this.trailerList=trailerList;
    }

    @NonNull
    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.trailer, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerViewHolder holder, int position) {
        holder.trailerTittle.setText(trailerList.get(position).getName());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(TRAILER_THUMBNAIL_BASE_LINK+trailerList.get(position).getKey()+ FILE_NAME)
                .into(holder.trailerView);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View mView;
        private ImageView trailerView;;
        private TextView trailerTittle;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            trailerTittle = mView.findViewById(R.id.trailer_title);
            trailerView = mView.findViewById(R.id.trailer_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTrailerClickListner.onListItemClick(getAdapterPosition(),trailerList);
        }
    }
}
