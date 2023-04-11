package com.etrungpro.appshoppet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etrungpro.appshoppet.R;
import com.etrungpro.appshoppet.models.Slide;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.PhotoViewHolder> {

    private Context mContext;
    private List<Slide> mListPhoto;

    public SlideAdapter(Context mContext, List<Slide> mListPhoto) {
        this.mContext = mContext;
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item, parent, false);

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Slide slide = mListPhoto.get(position);
        if (slide == null) {
            return;
        }

        Glide.with(mContext).load(slide.getResourceId()).into(holder.slidePhoto);
    }

    @Override
    public int getItemCount() {
        if(mListPhoto != null) {
            return mListPhoto.size();
        }
        return 0;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        private ImageView slidePhoto;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            slidePhoto = itemView.findViewById(R.id.slide_photo);
        }
    }

}
