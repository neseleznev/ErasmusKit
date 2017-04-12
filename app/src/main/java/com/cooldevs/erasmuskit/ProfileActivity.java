package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "CityObjectsListActivity";

    private boolean isFavorite = false;
    private String userName;
    private String userNationality;
    private String userStudyField;
    private String userHostCity;
    private String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userName=getIntent().getStringExtra("userName");
        userNationality=getIntent().getStringExtra("userNationality");
        userStudyField=getIntent().getStringExtra("userStudyField");
        userHostCity=getIntent().getStringExtra("userHostCity");
        userType=getIntent().getStringExtra("userType");

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_activity_toolbar);
        setSupportActionBar(toolbar);

        // Finish activity from status bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setting the title in the toolbar (actually in the "collapsing toolbar layout")
        CollapsingToolbarLayout ctLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_ctlayout);
        Log.d(TAG,"user: "+userName);
        ctLayout.setTitle(userName);

        // Initializing recyclerView
        ArrayList<ProfileSection> sections = new ArrayList<>();
        sections.add(new ProfileSection(R.drawable.nationality_black_24dp,userNationality));
        sections.add(new ProfileSection(R.drawable.ic_school_black_24dp,userStudyField));
        sections.add(new ProfileSection(R.drawable.ic_location_city_black_24dp,userHostCity));
        sections.add(new ProfileSection(R.drawable.ic_people_black_24dp,userType));

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.profile_recView);
        recyclerView.setHasFixedSize(true);
        ProfileSectionAdapter adapter = new ProfileSectionAdapter(sections);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 1));
        /*data = firebase.database().ref('indexCards').orderByChild('word').equalTo('noun')*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                return true;

            case R.id.my_profile:
                Intent intent = new Intent(this, UserProfileActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
