package com.cooldevs.erasmuskit;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    private SectionsAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Section> sections;
    private ArrayList<String> citiesList;

    private DatabaseReference usersRef;
    private ValueEventListener usersEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get screen dimensions (width) for the RecyclerView arrangement
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numRows = ((int) dpWidth) / 520 + 1;

        // Current user
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
            throw new IllegalStateException("User should not be null in this activity!");

        final String userKey = firebaseUser.getEmail().replace(".", "");
        boolean mProfile = getIntent().getStringExtra("userName").equals(firebaseUser.getDisplayName());
        final String toolbarTitle;

        // Initialize sections
        sections = new ArrayList<>();


        // -------------THIS IS OUR PROFILE ---------------
        if (mProfile) {
            toolbarTitle = firebaseUser.getDisplayName();

            // Get user information from Firebase Database
            getUserInformation(userKey);

            // Get the list of available cities from Firebase Database
            getCitiesList();
        }

        // ------------- SOMEONE ELSE'S PROFILE ---------------
        else {
            String userName = getIntent().getStringExtra("userName");
            String userNationality = getIntent().getStringExtra("userNationality");
            String userStudyField = getIntent().getStringExtra("userStudyField");
            String userHostCity = getIntent().getStringExtra("userHostCity");
            String userType = getIntent().getStringExtra("userType");

            toolbarTitle = userName;

            sections.add(new Section(R.drawable.nationality_black_24dp, userNationality));
            sections.add(new Section(R.drawable.ic_school_black_24dp, userStudyField));
            sections.add(new Section(R.drawable.ic_location_city_black_24dp, userHostCity));
            sections.add(new Section(R.drawable.ic_people_black_24dp, userType));
        }

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_activity_toolbar);
        setSupportActionBar(toolbar);

        // Finish activity from status bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setting the title in the toolbar (actually in the "collapsing toolbar layout")
        CollapsingToolbarLayout ctLayout = (CollapsingToolbarLayout) findViewById(R.id.profile_ctlayout);
        ctLayout.setTitle(toolbarTitle);

        recyclerView = (RecyclerView) findViewById(R.id.profile_recView);
        adapter = new SectionsAdapter(sections);
        if (mProfile)
            setAdapterListener(userKey);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, numRows));
    }


    private void setAdapterListener(final String userKey) {
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> items;
                final int clickedElement = recyclerView.getChildAdapterPosition(view);
                int[] strId = new int[]{R.array.nationalities, R.array.study_fields};
                final String[] userParameter = new String[]{"nationality", "studyField", "hostCity", "userType"};

                String dialogTitle = getResources().getStringArray(R.array.dialog_titles)[recyclerView.getChildAdapterPosition(view)];

                switch (clickedElement) {
                    case 2:
                        items = citiesList;
                        break;
                    case 3:
                        items = new ArrayList<>();
                        for (User.UserType userType : User.UserType.values())
                            items.add(userType.getUserType());

                        break;
                    default:
                        items = Arrays.asList(getResources().getStringArray(strId[clickedElement]));
                }


                new MaterialDialog.Builder(ProfileActivity.this)
                        .title(dialogTitle)
                        .items(items)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userKey);
                                userRef.child(userParameter[clickedElement]).setValue(text.toString());

                                sections.get(clickedElement).setTitle(text.toString());
                                adapter.notifyItemChanged(clickedElement);
                            }
                        })
                        .show();
            }
        });
    }


    private void getUserInformation(String userKey) {
        usersRef = FirebaseDatabase.getInstance().getReference("users").child(userKey);
        usersEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User mUser = dataSnapshot.getValue(User.class);

                // Initializing recyclerView
                sections.clear();
                sections.add(new Section(R.drawable.nationality_black_24dp, mUser.getNationality() == null ? getString(R.string.user_field_default_text) : mUser.getNationality()));
                sections.add(new Section(R.drawable.ic_school_black_24dp, mUser.getStudyField() == null ? getString(R.string.user_field_default_text) : mUser.getStudyField()));
                sections.add(new Section(R.drawable.ic_location_city_black_24dp, mUser.getHostCity() == null ? getString(R.string.user_field_default_text) : mUser.getHostCity()));
                sections.add(new Section(R.drawable.ic_account_settings, mUser.getUserType() == null ? getString(R.string.user_field_default_text) : mUser.getUserType()));

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "ValueEventListener:onCancelled method. Something went wrong");
            }
        };
    }

    private void getCitiesList() {
        Query citiesRef = FirebaseDatabase.getInstance().getReference("cities").orderByChild("name");
        citiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                citiesList = new ArrayList<>();

                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                    String cityName = citySnapshot.child("name").getValue(String.class);
                    citiesList.add(cityName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "ValueEventListener:onCancelled method. Something went wrong");
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (usersRef != null)
            usersRef.addValueEventListener(usersEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (usersRef != null && usersEventListener != null)
            usersRef.removeEventListener(usersEventListener);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
