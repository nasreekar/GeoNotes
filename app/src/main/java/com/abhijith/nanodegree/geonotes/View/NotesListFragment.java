package com.abhijith.nanodegree.geonotes.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhijith.nanodegree.geonotes.Model.Notes;
import com.abhijith.nanodegree.geonotes.Model.NotesAdapter;
import com.abhijith.nanodegree.geonotes.R;
import com.abhijith.nanodegree.geonotes.Utils.GeoNotesUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotesListFragment extends Fragment {

    private static final String TAG = NotesListFragment.class.getSimpleName();

    @BindView(R.id.rv_notes)
    RecyclerView rvNotes;

    @BindView(R.id.textViewEmpty)
    TextView mTextViewEmpty;

    @BindView(R.id.imageViewEmpty)
    ImageView mImageViewEmpty;

    @BindView(R.id.progressBarLoading)
    ProgressBar mProgressBarLoading;

    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private CollectionReference notesRef = firestoreDB.collection("notes");
    private NotesAdapter adapter;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);

        ButterKnife.bind(this, rootView);

        setUpRecyclerView();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void setUpRecyclerView() {
        mProgressBarLoading.setVisibility(View.VISIBLE);
        userID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Query query = notesRef.whereEqualTo("email", userID).orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Notes> options = new FirestoreRecyclerOptions.Builder<Notes>()
                .setQuery(query, Notes.class)
                .build();

        mProgressBarLoading.setVisibility(View.INVISIBLE);
        adapter = new NotesAdapter(options);
        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotes.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(rvNotes);

        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            Notes note = documentSnapshot.toObject(Notes.class);
            String id = documentSnapshot.getId();
            String path = documentSnapshot.getReference().getPath();
            if (note != null) {
                GeoNotesUtils.showMarkerInfo((AppCompatActivity) getContext(), note.getTitle(), note.getSnippet());
            }
        });
    }
}
