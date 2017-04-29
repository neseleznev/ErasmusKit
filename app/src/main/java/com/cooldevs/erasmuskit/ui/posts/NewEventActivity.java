package com.cooldevs.erasmuskit.ui.posts;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cooldevs.erasmuskit.R;
import com.cooldevs.erasmuskit.ui.posts.model.Event;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class NewEventActivity extends AppCompatActivity {

    private static final String TAG = "NewEventActivity";
    private static final int PLACE_PICKER_REQUEST = 1;

    private TextView dateSelector;
    private TextView placeSelector;

    private String placeID;

    private static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.new_event_toolbar_title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dateSelector = (TextView) findViewById(R.id.event_date_selector);
        dateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(dateSelector);
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        placeSelector = (TextView) findViewById(R.id.event_place_selector);
        placeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(NewEventActivity.this), PLACE_PICKER_REQUEST);
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
                Place place = PlacePicker.getPlace(this, data);
                placeSelector.setText(place.getName());

                // Save the place ID and name
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

                final EditText evTitleEditText =
                        (EditText) findViewById(R.id.event_title);
                final EditText evContentEditText =
                        (EditText) findViewById(R.id.event_content);
                String eventTitle = evTitleEditText.getText().toString();
                String eventContent = evContentEditText.getText().toString();

                if (!TextUtils.isEmpty(eventTitle) && !TextUtils.isEmpty(eventContent)
                        && placeID != null && calendar != null) {

                    String cityKey = getIntent().getStringExtra("cityKey");
                    String placeName = placeSelector.getText().toString();
                    long timestamp = System.currentTimeMillis();
                    long eventTimestamp = calendar.getTimeInMillis();

                    // Add event to Firebase Database
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("posts").child("events");
                    ref.push().setValue(new Event(eventTitle, eventContent,
                            cityKey, timestamp, placeID, placeName, eventTimestamp, null));

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


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private TextView tv;

        public DatePickerFragment() {

        }

        public DatePickerFragment(TextView tv) {
            this.tv = tv;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            String dateText = String.format(Locale.US, "%d-%02d-%02d", year, month + 1, day);
            tv.setText(dateText);
        }
    }

}
