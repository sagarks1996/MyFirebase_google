package com.example.myfirebase;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity  {


    SignInButton signInButton;
    TextView statustextview;
    public static GoogleSignInClient mgoogleSignInClient;
    public static GoogleSignInOptions googleSignInOptions;

    private FirebaseAuth mAuth;

//    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        signInButton = findViewById(R.id.sign_in_button);
        statustextview = findViewById(R.id.textview);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        googleSignInOptions  = new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();




        mgoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);



    }

    private void signIn(){
        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.d("googlesignin", "Google sign in success");
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"Google sign in failed",Toast.LENGTH_SHORT).show();

                //Log.w(TAG, "Google sign in failed", e);

            }
        }
    }


    public void onStart() {

        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {


        if (currentUser != null){


            Intent intent = new Intent(MainActivity.this,SecondActivity.class);
            String email = currentUser.getEmail();
            intent.putExtra("email",email);
            String name = currentUser.getDisplayName();
            intent.putExtra("name",name);
            String dp = String.valueOf(currentUser.getPhotoUrl());
            intent.putExtra("dp",dp);


            startActivity(intent);
        }


    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){

        //Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();

        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        }else {
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }


}