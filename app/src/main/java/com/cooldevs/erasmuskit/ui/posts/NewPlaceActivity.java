package com.cooldevs.erasmuskit.ui.posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cooldevs.erasmuskit.R;
import com.cooldevs.erasmuskit.ui.posts.model.Place;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPlaceActivity extends AppCompatActivity {

    private static final String TAG = "NewPlaceActivity";
    private static final int PLACE_PICKER_REQUEST = 1;

    private String placeID;
    private boolean isPlaceRated = false;

    private TextView placeSelector;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_place);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.new_place_toolbar_title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ratingBar = (RatingBar) findViewById(R.id.place_rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "onRatingChanged");

                isPlaceRated = true;
            }
        });

        placeSelector = (TextView) findViewById(R.id.place_selector);
        placeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(NewPlaceActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e(TAG, "There was a problem with Google Places API");
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(this, data);
                placeSelector.setText(place.getName());

                // Save the place ID
                placeID = place.getId();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_post_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                final EditText placeContentEditText =
                        (EditText) findViewById(R.id.place_content);

                String placeContent = placeContentEditText.getText().toString();
                String placeTitle = placeSelector.getText().toString();

                if (!TextUtils.isEmpty(placeContent) && placeID != null && isPlaceRated) {

                    String cityKey = getIntent().getStringExtra("cityKey");
                    long timestamp = System.currentTimeMillis();

                    // Add event to Firebase Database
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts").child("places");
                    ref.push().setValue(new Place(placeTitle, placeContent, cityKey, timestamp, ratingBar.getRating(), placeID));

                    Toast.makeText(this, R.string.post_saved, Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, R.string.uncomplete_data, Toast.LENGTH_LONG).show();

                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
