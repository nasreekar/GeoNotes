package com.abhijith.nanodegree.geonotes.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.abhijith.nanodegree.geonotes.R;
import com.abhijith.nanodegree.geonotes.Utils.Constants;
import com.abhijith.nanodegree.geonotes.Utils.InternetCheck;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileFragment extends Fragment {

    @BindView(R.id.btn_logout)
    Button logout;

    @BindView(R.id.iv_stackoverflow)
    ImageView stackoverflow;

    @BindView(R.id.iv_github)
    ImageView github;

    @BindView(R.id.iv_linkedin)
    ImageView linkedIn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, rootView);

        logout.setOnClickListener(view -> logout());

        stackoverflow.setOnClickListener(view -> new InternetCheck(internet -> {
            if (internet) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.STACKOVERFLOW));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }));

        github.setOnClickListener(view -> new InternetCheck(internet -> {
            if (internet) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.GITHUB));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }));

        linkedIn.setOnClickListener(view -> new InternetCheck(internet -> {
            if (internet) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.LINKEDIN));
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }));

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
