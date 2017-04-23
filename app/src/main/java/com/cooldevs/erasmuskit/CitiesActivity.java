package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.cooldevs.erasmuskit.Utils.hideSoftKeyboard;

public class CitiesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ArrayList<City> cities;
    private Query citiesRef;
    private ChildEventListener childEventListener;

    private CitiesAdapter adapter;
    private CoordinatorLayout.LayoutParams params;
    private SwipeRefreshLayout refLayout;
    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout.Behavior behavior;

    private static final String TAG = "CitiesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        // Get screen dimensions (width) for the RecyclerView arrangement
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numRows = (int) Math.ceil(dpWidth / 520f); // FIXME 520? Maybe 420 or 666?

        // Login / logout session flow (if user is null we go back to WelcomeActivity)
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(CitiesActivity.this, WelcomeActivity.class));
                    finish();
                }
            }
        };

        // Initial loading with SwipeRefreshLayout
        refLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ref_layout);
        refLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refLayout.setRefreshing(true);
        refLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refLayout.setRefreshing(false);
            }
        });

        // Get behavior
        params = (CoordinatorLayout.LayoutParams) refLayout.getLayoutParams();
        behavior = params.getBehavior();


        // Initialize the views: recyclerView and its adapter for managing the list of cities
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cities_recView);
        cities = new ArrayList<>();
        adapter = new CitiesAdapter(cities, this);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "Click on the element " + recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(CitiesActivity.this, CityActivity.class);
                intent.putExtra("cityName",
                        cities.get(recyclerView.getChildAdapterPosition(view)).getName());
                intent.putExtra("cityKey",
                        cities.get(recyclerView.getChildAdapterPosition(view)).getKey());
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numRows));

        // Get the array of cities from Firebase Database (and sort them by name)
        citiesRef = FirebaseDatabase.getInstance().getReference("cities").orderByChild("name");
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childEventListener:onChildAdded, key: " + dataSnapshot.getKey());

                City city = dataSnapshot.getValue(City.class);
                city.setKey(dataSnapshot.getKey());

                /*
                // Add to the right position (alphabetical order). OPTIMIZATION PROBLEM!!!
                int pos = 0;
                for (City listedCity : cities) {
                    if (city.getName().compareToIgnoreCase(listedCity.getName()) < 0)
                        break;
                    pos++;
                }
                cities.add(pos, city);
                */

                cities.add(city);
                adapter.notifyDataSetChanged();

                refLayout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "childEventListener:onChildRemoved");
                String key = dataSnapshot.getKey();

                for (int i = 0; i < cities.size(); i++) {
                    City city = cities.get(i);
                    if (city.getKey().equals(key)) {
                        cities.remove(city);
                        adapter.notifyItemRemoved(i);
                        adapter.notifyItemRangeChanged(i, cities.size());

                        return;
                    }
                }

                throw new IllegalStateException("Removed child not found in local array.");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "ChildEventListener:onCancelled");
            }
        };

        // Floating Action Button onClick listener
        floatingActionButton = (FloatingActionButton) findViewById(R.id.add_city_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(CitiesActivity.this)
                        .title(R.string.new_city_dialog_title)
                        .customView(R.layout.dialog_new_city, false)
                        .positiveText(R.string.new_city_positive_btn)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View view = dialog.getCustomView();

                                final EditText cityNameEditText =
                                        (EditText) view.findViewById(R.id.newcity_name);
                                final EditText cityCountryEditText =
                                        (EditText) view.findViewById(R.id.newcity_country);
                                String newCityName = cityNameEditText.getText().toString();
                                String newCityCountry = cityCountryEditText.getText().toString();

                                if (!TextUtils.isEmpty(newCityName)
                                        && !TextUtils.isEmpty(newCityCountry)) {

                                    // Add city to Firebase Database
                                    DatabaseReference ref = FirebaseDatabase.getInstance()
                                            .getReference("cities");
                                    ref.push().setValue(new City(newCityName, newCityCountry));

                                    Toast.makeText(
                                            CitiesActivity.this,
                                            R.string.add_city_toast,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });

        View parentView = findViewById(R.id.cities_parent);
        setupUI(parentView);
    }

    private void setUserPermissions() {
        if (mAuth.getCurrentUser() != null && mAuth.getCurrentUser().getEmail() != null) {
            String userKey = mAuth.getCurrentUser().getEmail().replace(".", "");
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("users").child(userKey);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    int visibility = user.getUserType().equals(User.UserType.ADMINISTRATOR.getUserType())
                            ? View.VISIBLE
                            : View.GONE;
                    setViewsVisibility(visibility);

                    cities.clear();
                    citiesRef.addChildEventListener(childEventListener);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "userRef onCancelled");
                }
            });
        }
    }

    private void setViewsVisibility(int visibility) {
        adapter.setIconVisibility(visibility);
        floatingActionButton.setVisibility(visibility);

        params.setBehavior(visibility == View.VISIBLE ? behavior : null);
        refLayout.requestLayout();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        setUserPermissions();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

        if (citiesRef != null && childEventListener != null)
            citiesRef.removeEventListener(childEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cities_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();

        final ArrayList<City> _cities = new ArrayList<>();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _cities.clear();
                for (City city: cities) {
                    _cities.add(city);
                }
            }
        });

        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewDetachedFromWindow(View arg0) {
                // search was detached/closed
                cities.clear();
                for (City city: _cities) {
                    cities.add(city);
                }
            }

            @Override
            public void onViewAttachedToWindow(View arg0) {
                // search was opened
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String substring) {
                cities.clear();

                // Fill with saved values
                for (City city : _cities) {
                    if (city.getName().toLowerCase().contains(substring.toLowerCase())
                            || city.getCountry().toLowerCase().contains(substring.toLowerCase())) {
                        cities.add(city);
                    }
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    /**
     * http://stackoverflow.com/a/11656129/820410
     */
    public void setupUI(View view) {
        if(!(view instanceof SearchView)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(CitiesActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
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

            case R.id.action_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("userName", mAuth.getCurrentUser().getDisplayName());
                startActivity(intent);
                return true;

            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
