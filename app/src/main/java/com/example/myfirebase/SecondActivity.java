package com.example.myfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class SecondActivity extends AppCompatActivity {

    TextView name,email;
    Button signout;
    ImageView imageView;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        name = findViewById(R.id.nametext);
        email = findViewById(R.id.emailtext);
        imageView = findViewById(R.id.imageview);
        signout = findViewById(R.id.signoutbutton);

        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String name1 = intent.getStringExtra("name");
        String email1 = intent.getStringExtra("email");
        String dp = intent.getStringExtra("dp");
        Uri image = Uri.parse(dp);


        Picasso.get().load(image).into(imageView);

        name.setText(name1);
        email.setText(email1);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                MainActivity.mgoogleSignInClient.signOut()
                        .addOnCompleteListener(SecondActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(),"Log out Successfully",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
        });

    }
}
