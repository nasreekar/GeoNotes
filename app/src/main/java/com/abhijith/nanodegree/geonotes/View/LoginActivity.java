package com.abhijith.nanodegree.geonotes.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abhijith.nanodegree.geonotes.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    //Google Login Request Code
    private int RC_SIGN_IN = 7;

    //Google Sign In Client
    private GoogleSignInClient mGoogleApiClient;

    //Firebase Auth
    private FirebaseAuth firebaseAuth;

    @BindView(R.id.sign_in_button)
    SignInButton signIn;

    @BindView(R.id.btn_login)
    Button loginBtn;

    @BindView(R.id.btn_register)
    Button register;

    @BindView(R.id.btn_forgotPassword)
    Button forgotPassword;

    @BindView(R.id.et_email)
    EditText etEmail;

    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.WEB_CLIENT_ID))
                .requestEmail()
                .build();

        mGoogleApiClient = GoogleSignIn.getClient(this, gso);

        signIn.setOnClickListener(view -> signIn());

        register.setOnClickListener(view -> startActivity(new Intent(this, RegisterActivity.class)));

        loginBtn.setOnClickListener(view -> loginUserAccount());

        forgotPassword.setOnClickListener(view -> passwordReset());

    }

    private void passwordReset() {
        String emailaddress = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(emailaddress)) {
            Toast.makeText(LoginActivity.this, getString(R.string.email_entry_message), Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth.sendPasswordResetEmail(emailaddress)
                    .addOnCompleteListener(task -> {

                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                etEmail.setError(getString(R.string.error_invalid_email));
                                etEmail.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                etEmail.setError(getString(R.string.error_user_exists));
                                etEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        } else {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(LoginActivity.this, getString(R.string.message_email_check), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void loginUserAccount() {

        hideKeyboard();

        String email, password;
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.message_login_success), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);

                            sendToMainActivity();

                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_login_fail), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.error_missing_details_login), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                authWithGoogle(account);
            }
        }
    }

    private void authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                sendToMainActivity();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_firebase_auth), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void signIn() {
        Intent signInIntent = mGoogleApiClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMainActivity();
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
