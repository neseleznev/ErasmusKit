package com.cooldevs.erasmuskit;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;


public class ProfileConfigActivity extends AppCompatActivity {

    private static final String TAG = "ProfileConfigActivity";


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // Name, email address, and profile photo Url
    String userName = user.getDisplayName();
    String userEmail = user.getEmail();
    Spinner citySpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_config);

        TextView user_name_tv=(TextView)findViewById(R.id.user_name);
        user_name_tv.setText(userName);

        final Spinner nationalitySpinner = (Spinner) findViewById(R.id.nationality_spinner);
        final Spinner studiesSpinner= (Spinner) findViewById(R.id.study_field_spinner);
        final Spinner userTypeSpinner= (Spinner) findViewById(R.id.user_type_spinner);

        //Filling the spinner with the cities
        Query citiesRef = FirebaseDatabase.getInstance().getReference("cities").orderByChild("name");
        citiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> cityList=new ArrayList<String>();

                for(DataSnapshot citySnapshot: dataSnapshot.getChildren()){

                    String city_name = citySnapshot.child("name").getValue(String.class);
                    cityList.add(city_name);
                }

                citySpinner = (Spinner) findViewById(R.id.host_city_spinner);
                ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(ProfileConfigActivity.this, android.R.layout.simple_spinner_item, cityList);
                citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(citiesAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Button save_profile_btn = (Button) findViewById(R.id.profile_save_btn);
        save_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newHostCity= citySpinner.getSelectedItem().toString();
                String newUserName=userName;
                String newUserEmail=userEmail;
                String newNationality=nationalitySpinner.getSelectedItem().toString();
                String newStudies=studiesSpinner.getSelectedItem().toString();
                String newUserType=userTypeSpinner.getSelectedItem().toString();

                String key = null;
                //for shared preferences
                //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",0);//0: MODE_PRIVATE
                //SharedPreferences.Editor editor=pref.edit();

                pref.getString("userKey: ",key);

                Log.d(TAG,"userKey: "+key);

                if(key == null){
                    if (!TextUtils.isEmpty(newNationality)) {

                        Log.d(TAG,"Add user to Firebase");

                        DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference(("users")).push();
                        key = mUserRef.getKey();
                        mUserRef.setValue(new User(newHostCity, newUserName, newUserEmail, newNationality, newStudies, newUserType));

                        pref.edit().putString("userKey",key).apply();;
                        //editor.putString("userKey",key);
                        //editor.commit();

                        Toast.makeText(ProfileConfigActivity.this, R.string.profile_saved_toast, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else{
                    Log.d(TAG,"modify the users profile");

                    DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference(("users")).child(key);
                    mUserRef.setValue(new User(newHostCity, newUserName, newUserEmail, newNationality, newStudies, newUserType));

                    Toast.makeText(ProfileConfigActivity.this, R.string.profile_saved_toast, Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });

    }
}
