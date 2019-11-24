package com.abhijith.nanodegree.geonotes.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhijith.nanodegree.geonotes.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class NotesAdapter extends FirestoreRecyclerAdapter<Notes, NotesAdapter.NoteHolder> {

    private OnItemClickListener listener;

    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Notes> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Notes model) {
        holder.tvTitle.setText(model.getTitle());
        holder.tvDescription.setText(model.getDescription());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_item, parent, false);

        return new NoteHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;

        NoteHolder(View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tvListTitle);
            this.tvDescription = itemView.findViewById(R.id.tvListDescription);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                // send the click from adapter to activity/fragment
                if(position != RecyclerView.NO_POSITION && listener !=null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }

    // send the click from adapter to activity/fragment
    public  interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public  void setOnItemClickListener (OnItemClickListener listener) {
        this.listener = listener;
    }
}
