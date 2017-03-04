package com.cooldevs.erasmuskit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_city);

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

                    Toast.makeText(NewCityActivity.this, "City successfully added", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
