package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsersActivity extends AppCompatActivity {




    private static final String TAG = "UsersActivity";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // Name, email address, and profile photo Url
    String name = user.getDisplayName();
    String email = user.getEmail();
    Uri photoUrl = user.getPhotoUrl();

    // The user's ID, unique to the Firebase project. Do NOT use this value to
    // authenticate with your backend server, if you have one. Use
    // FirebaseUser.getToken() instead.
    String uid = user.getUid();

    private Button config_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        TextView user_name_tv=(TextView)findViewById(R.id.user_name);
        user_name_tv.setText(name);

        TextView user_email_tv=(TextView)findViewById(R.id.user_email);
        user_email_tv.setText(email);

        config_btn=(Button)findViewById(R.id.configue_profile_btn);
        config_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){

                Intent intent = new Intent(UsersActivity.this, ProfileConfigActivity.class);
                startActivity(intent);

            }

        });


    }


}
