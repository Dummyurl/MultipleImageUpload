package com.soropromo.multipleimageupload.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soropromo.multipleimageupload.R;
import com.soropromo.multipleimageupload.model.Item;

import java.util.List;

public class SelectedImagesAdapter extends RecyclerView.Adapter<SelectedImagesAdapter.ViewHolder> {

    public List<Item> items;
    public Context context;

    public SelectedImagesAdapter(List<Item> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //get single item
        Item item = items.get(position);

        //set name
        holder.imageNameView.setText(item.getImageName());

        //get and set status
        String uploaded = item.getUploaded();
        if (uploaded.equals("uploading")) {
            holder.imageUploadStatusIV.setImageResource(R.mipmap.progress);
        } else {
            holder.imageUploadStatusIV.setImageResource(R.mipmap.checked);
        }

        Glide.with(context)//.setDefaultRequestOptions(placeHolderRequest)
                .load(item.getImageUri()).into(holder.actualImageView);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TextView imageNameView;
        public ImageView imageUploadStatusIV;
        public ImageView actualImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            imageNameView = (TextView) mView.findViewById(R.id.upload_image_name);
            imageUploadStatusIV = (ImageView) mView.findViewById(R.id.upload_status);
            actualImageView = (ImageView) mView.findViewById(R.id.upload_icon);

        }

    }

}
