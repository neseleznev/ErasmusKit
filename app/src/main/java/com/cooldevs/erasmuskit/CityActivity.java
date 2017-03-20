package com.cooldevs.erasmuskit;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CityActivity extends AppCompatActivity {

    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.city_activity_toolbar);
        setSupportActionBar(toolbar);

        // Finish activity from status bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.new_city_toolbar);
        }

        final EditText cityNameEditText = (EditText) findViewById(R.id.newcity_name);
        final EditText cityCountryEditText = (EditText) findViewById(R.id.newcity_country);

        Button saveCityBtn = (Button) findViewById(R.id.newcity_save_btn);
        saveCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCityName = cityNameEditText.getText().toString();
                String newCityCountry = cityCountryEditText.getText().toString();

                if (!TextUtils.isEmpty(newCityName) && !TextUtils.isEmpty(newCityCountry)) {

                    // Add city to Firebase
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cities");
                    ref.push().setValue(new City(newCityName, newCityCountry));

                    Toast.makeText(NewCityActivity.this, R.string.add_city_toast, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
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
