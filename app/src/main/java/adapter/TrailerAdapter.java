package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieretrofit.MovieResult;
import com.example.movieretrofit.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

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
        builder.build().load("https://img.youtube.com/vi/"+trailerList.get(position).getKey()+"/sddefault.jpg")
                .into(holder.trailerView);
        Log.d("getId","trailer key "+trailerList.get(position).getKey()+"movie-"+trailerList.get(position).getName());
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
