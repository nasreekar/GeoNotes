package com.abhijith.nanodegree.geonotes.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abhijith.nanodegree.geonotes.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_reg_email)
    EditText regEmail;

    @BindView(R.id.et_reg_password)
    EditText regPassword;

    @BindView(R.id.btn_signUp)
    Button signup;

    @BindView(R.id.btn_alreadyMember)
    Button alreadyMember;

    FirebaseAuth firebaseAuth;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> registerNewUser());

        alreadyMember.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void registerNewUser() {
        progressBar.setVisibility(View.VISIBLE);

        String email, password;
        email = regEmail.getText().toString();
        password = regPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        navigateToMainActivity();

                    } else {
                        Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
