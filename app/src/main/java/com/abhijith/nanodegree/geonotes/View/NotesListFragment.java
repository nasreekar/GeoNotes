package com.abhijith.nanodegree.geonotes.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhijith.nanodegree.geonotes.Model.Notes;
import com.abhijith.nanodegree.geonotes.Model.NotesAdapter;
import com.abhijith.nanodegree.geonotes.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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

    private ArrayList<Notes> notesList = new ArrayList<>();
    FirebaseFirestore firestoreDB;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notes_list, container, false);

        ButterKnife.bind(this, rootView);

        firestoreDB = FirebaseFirestore.getInstance();
        userID = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        getNotes();

        return rootView;
    }

    private void getNotes() {
        getMyDocumentFromCollection(userID);
    }

    private void getMyDocumentFromCollection(String userID) {
        firestoreDB.collection("notes")
                .whereEqualTo("email", userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            Notes note = doc.toObject(Notes.class);
                            // note.setId(doc.getId());
                            notesList.add(note);
                        }
                        initializeNotesList();

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void initializeNotesList() {
        rvNotes.setHasFixedSize(true);
        rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNotes.setAdapter(new NotesAdapter(getContext(), notesList));
    }
}
