package udacity.example.com.abfitness.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.abfitness.R;
import udacity.example.com.abfitness.interfaces.OnNewsClickHandler;
import udacity.example.com.abfitness.model.News;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    private final OnNewsClickHandler mClickHandler;
    private ArrayList<News> mNewsList;

    public NewsAdapter(OnNewsClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.news_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        NewsAdapter.NewsViewHolder viewHolder = new NewsAdapter.NewsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, int i) {
        String header = mNewsList.get(i).getNewsHeader();
        String newsLogoUrl = mNewsList.get(i).getNewsPicUrl();
        holder.newsHeader.setText(header);

        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round)
                .placeholder(R.color.colorPrimaryLight)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(holder.itemView).load(newsLogoUrl).apply(options).into(holder.newsLogo);

    }

    @Override
    public int getItemCount() {
        if (mNewsList == null) {
            return 0;
        }
        return mNewsList.size();
    }

    public void setNewsList(ArrayList<News> newsList) {
        mNewsList = newsList;
        notifyDataSetChanged();
    }

    public ArrayList<News> getNewsList() {
        return mNewsList;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.news_item_header_tv)
        TextView newsHeader;

        @BindView(R.id.news_item_iv)
        ImageView newsLogo;

        public NewsViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String header = mNewsList.get(adapterPosition).getNewsHeader();
            String logoUrl = mNewsList.get(adapterPosition).getNewsPicUrl();
            String article = mNewsList.get(adapterPosition).getNewsBody();

            mClickHandler.onNewsClick(header, logoUrl, article);
        }
    }
}

