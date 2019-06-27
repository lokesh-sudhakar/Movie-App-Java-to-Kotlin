package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieretrofit.R;

import java.util.List;

import data.ReviewsData;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewsData.Review> reviewList;
    private Context context;

    public ReviewAdapter(Context context, List<ReviewsData.Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.reviews, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        holder.reviewName.setText(reviewList.get(position).getAuthor());
        holder.reviewContent.setText(reviewList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView reviewName;
        private TextView reviewContent;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            reviewName = mView.findViewById(R.id.reviewer_name);
            reviewContent = mView.findViewById(R.id.review_content);
        }
    }
}
