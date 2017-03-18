package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.FileViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.File;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Folder;

import java.util.ArrayList;

/**
 * Created by andre on 09.01.17.
 */
public class FileBrowserRecyclerViewAdapter extends android.support.v7.widget.RecyclerView.Adapter {

    private ArrayList<Folder> folders;
    private ArrayList<File> files;

    public FileBrowserRecyclerViewAdapter(ArrayList<File> files, ArrayList<Folder> folders) {
        this.files = files;
        this.folders = folders;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_card, parent, false);
        GeneralViewHolder holder = new FileViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Folders should be shown on top, files below.
        String text;
        int drawableResource;
        // Element is a folder
        if (position < folders.size()) {
            text = folders.get(position).getName();
            drawableResource = R.drawable.ic_folder_black_24dp;

        // Element is a file
        } else {
            text = files.get(position - folders.size()).getDisplayName();
            drawableResource = R.drawable.ic_file_download_black_24dp;
        }

        FileViewHolder viewHolder = (FileViewHolder) holder;
        viewHolder.name.setText(text);
        viewHolder.image.setImageResource(drawableResource);
    }

    @Override
    public int getItemCount() {
        return folders.size() + files.size();
    }
}
