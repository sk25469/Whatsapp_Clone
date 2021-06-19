package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    ArrayList<String> mediaList;
    Context context;

    public MediaAdapter(Context context, ArrayList<String> mediaList) {
        this.mediaList = mediaList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, null, false);
        return new MediaViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MediaAdapter.MediaViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {

        public MediaViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }
}
