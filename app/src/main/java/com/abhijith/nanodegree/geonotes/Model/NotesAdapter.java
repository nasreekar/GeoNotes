package com.abhijith.nanodegree.geonotes.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.abhijith.nanodegree.geonotes.R;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Notes> dataList;

    public NotesAdapter(Context context, ArrayList<Notes> data) {
        this.context = context;
        this.dataList = data;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvListTitle);
            this.tvDescription = itemView.findViewById(R.id.tvListDescription);
        }
    }

    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NotesAdapter.ViewHolder holder, final int position) {
        holder.tvTitle.setText(dataList.get(position).getTitle());
        holder.tvDescription.setText(dataList.get(position).getDescription());

        holder.itemView.setOnClickListener(v -> Toast.makeText(context, "Item " + position + " is clicked.", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
