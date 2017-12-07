package com.mcs17.goldenage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.util.logging.Logger.global;

/**
 * Created by twindo-mac2 on 27/11/17.
 */

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.email) EditText email;
    @BindView(R.id.password) EditText password;
    @BindView(R.id.progressBar) ProgressBar progressBar;

    private FirebaseAuth auth;
    private SharedPreferences sharedPref;

    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_login)
    public void login(View view) {
//        Toast.makeText(getApplicationContext(), "You have entered: " + email.getText().toString(),
//                Toast.LENGTH_SHORT).show();

        String uemail = email.getText().toString();
        final String upassword = password.getText().toString();

        if (TextUtils.isEmpty(uemail)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(upassword)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(uemail, upassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (upassword.length() < 6) {
                                password.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            onAuthSuccess(task.getResult().getUser());
                            /*
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            */
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user){
        //global.userID = user.getUid();
        //sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("firebasekey", user.getUid());
        editor.commit();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_reset_password)
    public void forgotPassword(View view) {
        Toast.makeText(getApplicationContext(), "You have entered: " + email.getText().toString(),
                Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btn_signup)
    public void onButtonClick(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }
}
