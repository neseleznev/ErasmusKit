package com.cooldevs.erasmuskit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CitiesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "CitiesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        // Login / logout session flow (if user is null we go back to LoginActivity)
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null)  {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(CitiesActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        // Initial loading with SwipeRefreshLayout
        final SwipeRefreshLayout refLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ref_layout);
        refLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refLayout.setRefreshing(true);
        refLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refLayout.setRefreshing(false);
            }
        });

        // Initialize the views: recyclerView and its adapter for managing the list of cities
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cities_recView);
        final ArrayList<City> cities = new ArrayList<>();
        final CitiesAdapter adapter = new CitiesAdapter(cities, this);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "Click on the element " + recyclerView.getChildAdapterPosition(view));
                Intent intent = new Intent(CitiesActivity.this, CityActivity.class);
                intent.putExtra("city_name", cities.get(recyclerView.getChildAdapterPosition(view)).getName());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Get the array of cities from Firebase Database (and sort them by name)
        Query citiesRef = FirebaseDatabase.getInstance().getReference("cities").orderByChild("name");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childEventListener:onChildAdded, key: " + dataSnapshot.getKey());
                City city = dataSnapshot.getValue(City.class);
                city.setKey(dataSnapshot.getKey());

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

            }
        };
        citiesRef.addChildEventListener(childEventListener);

        // Floating Action Button onClick listener
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_city_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CitiesActivity.this, NewCityActivity.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);
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
                Intent intent = new Intent(this, UsersActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
