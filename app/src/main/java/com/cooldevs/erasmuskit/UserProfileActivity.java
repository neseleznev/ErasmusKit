package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";

    private FirebaseAuth mAuth;

    FirebaseUser thisUser = FirebaseAuth.getInstance().getCurrentUser();
    // Name, email address, and profile photo Url
    String name = thisUser.getDisplayName();
    String email = thisUser.getEmail();
    Uri photoUrl = thisUser.getPhotoUrl();
    // The user's ID, unique to the Firebase project. Do NOT use this value to
    // authenticate with your backend server, if you have one. Use
    // FirebaseUser.getToken() instead.
    String uid = thisUser.getUid();
    String userKey=email.replace(".","");

    User myUser = new User();
    ArrayList<User> users=new ArrayList<> ();

    private Button config_btn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getUser();




    }

    public void getUser(){

        Query usersRef = FirebaseDatabase.getInstance().getReference("users").child(userKey);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<User, Object> data = (HashMap<User, Object>) dataSnapshot.getValue();

                //myUser= (User) dataSnapshot.getValue();

                Log.d(TAG,"HOLA MUNDOOOOOOOOOOOOOOOOOOzzzzO data size: "+data);

                if(data == null){
                    Log.d(TAG,"users is empty!");
                    myUser.setUserName(name);
                    myUser.setNationality("");
                    myUser.setStudyField("");
                    myUser.setHostCity("");
                    myUser.setUserType("");
                }else{

                    myUser.setUserName(data.get("userName").toString());
                    myUser.setNationality(data.get("nationality").toString());
                    myUser.setStudyField(data.get("studyField").toString());
                    myUser.setHostCity(data.get("hostCity").toString());
                    myUser.setUserType(data.get("userType").toString());

                    Log.d(TAG,"data "+data );
                }

                // Set toolbar
                Toolbar toolbar = (Toolbar) findViewById(R.id.user_profile_activity_toolbar);
                setSupportActionBar(toolbar);

                // Finish activity from status bar
                if(getSupportActionBar() != null) {
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }


                // Setting the title in the toolbar (actually in the "collapsing toolbar layout")
                CollapsingToolbarLayout ctLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_ctlayout);
                ctLayout.setTitle(myUser.getUserName());


                // Initializing recyclerView
                ArrayList<ProfileSection> sections = new ArrayList<>();
                sections.add(new ProfileSection(R.drawable.nationality_black_24dp,myUser.getNationality()));
                sections.add(new ProfileSection(R.drawable.ic_school_black_24dp,myUser.getStudyField()));
                sections.add(new ProfileSection(R.drawable.ic_location_city_black_24dp,myUser.getHostCity()));
                sections.add(new ProfileSection(R.drawable.ic_people_black_24dp,myUser.getUserType()));

                final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.user_profile_recView);
                recyclerView.setHasFixedSize(true);
                ProfileSectionAdapter adapter = new ProfileSectionAdapter(sections);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new GridLayoutManager(UserProfileActivity.this, 1));

                // Floating Action Button onClick listener
                FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.profile_config_btn);
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(UserProfileActivity.this, ProfileConfigActivity.class);
                        intent.putExtra("userName",myUser.getUserName());
                        intent.putExtra("userNationality",myUser.getNationality());
                        intent.putExtra("userStudyField",myUser.getStudyField());
                        intent.putExtra("userHostCity",myUser.getHostCity());
                        intent.putExtra("userType",myUser.getUserType());
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return;

    }


}
