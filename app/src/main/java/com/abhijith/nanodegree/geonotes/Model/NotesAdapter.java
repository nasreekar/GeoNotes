package com.abhijith.nanodegree.geonotes.Model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhijith.nanodegree.geonotes.R;
import com.abhijith.nanodegree.geonotes.Utils.Constants;
import com.abhijith.nanodegree.geonotes.Utils.GeoNotesUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
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
        holder.mapView.setTag(model);
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

    class NoteHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        MapView mapView;
        GoogleMap map;
        TextView tvTitle;
        TextView tvDescription;

        NoteHolder(View itemView) {
            super(itemView);
            this.mapView = itemView.findViewById(R.id.mapView);
            this.tvTitle = itemView.findViewById(R.id.tvListTitle);
            this.tvDescription = itemView.findViewById(R.id.tvListDescription);

            if (mapView != null) {
                // Initialise the MapView
                mapView.onCreate(null);
                // Set the map ready callback to receive the GoogleMap object
                mapView.getMapAsync(this);
            }

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                // send the click from adapter to activity/fragment
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            MapsInitializer.initialize(itemView.getContext());
            map = googleMap;
            setMapLocation();
        }

        private void setMapLocation() {
            if (map == null) return;

            Notes note = (Notes) mapView.getTag();
            if (note == null) return;

            if (note.getPosition() != null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(note.getPosition(), Constants.DEFAULT_ZOOM));
                map.addMarker(new MarkerOptions()
                        .position(note.getPosition())
                        .title(note.getTitle())
                        .icon(GeoNotesUtils.bitmapDescriptorFromVector(itemView.getContext(), R.drawable.ic_marker_cluster)));
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        }
    }

    // send the click from adapter to activity/fragment
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
