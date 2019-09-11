package com.abhijith.nanodegree.geonotes.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.abhijith.nanodegree.geonotes.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileFragment extends Fragment {

    @BindView(R.id.btn_logout)
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);

        logout.setOnClickListener(view -> logout());

        return rootView;
    }

    private void logout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
