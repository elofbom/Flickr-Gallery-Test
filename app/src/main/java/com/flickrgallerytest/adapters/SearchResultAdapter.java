package com.flickrgallerytest.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.flickrgallerytest.R;
import com.flickrgallerytest.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<String> mUrls;
    private Context mContext;

    public SearchResultAdapter(List<String> urls, Context context) {
        mUrls = urls;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_main_grid_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(mContext).load(mUrls.get(position)).config(Bitmap.Config.RGB_565).fit().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public SquareImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (SquareImageView) itemView.findViewById(R.id.activity_main_grid_item_image);
        }
    }
}
